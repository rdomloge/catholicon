package catholicon;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import catholicon.controller.MatchCardController;
import catholicon.dao.ChangeDao;
import catholicon.dao.DivisionDao;
import catholicon.dao.LeagueDao;
import catholicon.dao.Loader;
import catholicon.dao.MatchCardDao;
import catholicon.dao.MatchDao;
import catholicon.domain.Change;
import catholicon.domain.Change.ActionCode;
import catholicon.domain.Division;
import catholicon.domain.Division.TeamPosition;
import catholicon.domain.DivisionDescriptor;
import catholicon.domain.League;
import catholicon.domain.Match;
import catholicon.ex.DaoException;
import catholicon.filter.ThreadLocalLoaderFilter;

@Component
public class RecentMatchResultsSpider {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RecentMatchResultsSpider.class);
	
	private static final long HOURLY = 1000 * 60 * 60;
	
	private static final DateFormat matchDateFormat = new SimpleDateFormat("yyyy'-'MM'-'dd");
	
	@Value("${BASE_URL:http://192.168.0.14}")
	private String BASE;
	
	@Value("${RECENT_MATCH_RESULT_SPIDER_DISABLED:false}")
	private boolean disableSpider;
	
	private Loader loader;
	private MatchDao matchDao = new MatchDao();
	private DivisionDao divisionDao = new DivisionDao();
	private LeagueDao leagueDao = new LeagueDao();
	
	@Autowired
	private MatchCardController matchCardController;
	
	private Set<Match> recentMatches = new HashSet<>();
	private List<Match> sortedRecentMatches = new LinkedList<>();
	
	private Date cutOff;
	
	private boolean lastSpiderFailed;
	

	@Scheduled(fixedDelay = HOURLY, initialDelay = 0)
	public void spiderLatestResults() {
		if(disableSpider) return;
		LOGGER.info("Updating latest fixtures");
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		
		loader = new Loader(BASE);
		GregorianCalendar gc = new GregorianCalendar();
		gc.add(Calendar.DAY_OF_YEAR, -7);
		cutOff = gc.getTime();
		recentMatches.clear();
		sortedRecentMatches.clear();
		ThreadLocalLoaderFilter.set(loader);
		try {
			new LeagueSpider().run();
			lastSpiderFailed = false;
			sortedRecentMatches.addAll(recentMatches);
			Collections.sort(sortedRecentMatches, new Comparator<Match>(){
				@Override
				public int compare(Match m1, Match m2) {
					return m2.getDate().compareTo(m1.getDate());
				}});
			
			stopWatch.stop();
			LOGGER.debug("Spider complete. Took "+stopWatch.getTotalTimeSeconds()+" seconds - found "+recentMatches.size());
			for (Match match : sortedRecentMatches) {
				LOGGER.debug("[RECENT]({}) {} v {} on {}: {}",
						match.getFixtureId(),
						match.getHomeTeam().getName(),
						match.getAwayTeam().getName(),
						match.getDate(),
						match.getScoreExtracted());
			}
		}
		catch(DaoException dex) {
			LOGGER.error("Spider failed (previous state was "+lastSpiderFailed+")", dex);
			lastSpiderFailed = true;
		}
	}
	
	public List<Match> getRecentMatches() {
		if(lastSpiderFailed) throw new DaoException("Spider failed");
		return sortedRecentMatches;
	}

	class LeagueSpider implements Runnable {
		
		@Override
		public void run() {
			
			List<League> leagues = leagueDao.list(0);
			for (League league : leagues) {
				new DivisionSpider(league.getLeagueTypeId()).run();
			}
		}
	}
	
	class DivisionSpider extends Wrapper {
		
		private int leagueTypeId;

		public DivisionSpider(int leagueTypeId) {
			this.leagueTypeId = leagueTypeId;
		}

		@Override
		protected void _run() {
			List<DivisionDescriptor> divisionsForLeague = divisionDao.getDivisionsForLeague(leagueTypeId, 0);
			for (DivisionDescriptor divisionDescriptor : divisionsForLeague) {
				Division division = divisionDao.load(""+leagueTypeId, divisionDescriptor.getDivisionId(), 0);
				TeamPosition[] teamPositions = division.getPositions();
				for (TeamPosition teamPosition : teamPositions) {
					MatchSpider matchSpider = new MatchSpider(teamPosition.getTeamId());
					matchSpider.run();
				}
			}
		}
	}
	
	class MatchSpider extends Wrapper {

		private int team;

		public MatchSpider(int team) {
			this.team = team;
		}

		@Override
		protected void _run() {
			
			Match[] matchs = matchDao.load(0, ""+team);
			for (Match match : matchs) {
				if(recentMatches.contains(match)) continue;
				if(match.isPlayed() || match.isUnConfirmed()) {
					String dateStr = null;
					List<Change> changeHistory = new ChangeDao().getChanges(Integer.parseInt(match.getFixtureId()), 0);
					for (Change change : changeHistory) {
						if(change.getActionCode().equals(ActionCode.ENTERED)) {
							dateStr = change.getChangeDate();
						}
					}
					if(null == dateStr) {
						dateStr = match.getDate();
						LOGGER.warn("No changes for match {} - using match date {}", 
								match.getFixtureId(),
								match.getDate());
					}
					try {
						Date date = matchDateFormat.parse(dateStr);
						if(date.after(cutOff)) {
							recentMatches.add(match);
							LOGGER.debug("Match {} on {} is recent: {}",
									match.getFixtureId(),match.getDate(),dateStr);
							matchCardController.loadMatchCard(match.getFixtureId()); // cache a recent match card result, for good measure
						}
						else {
							LOGGER.debug("Match {} on {} is too old", match.getFixtureId(), dateStr);
						}
					} 
					catch (ParseException e) {
						LOGGER.error("Could not parse "+dateStr, e);
						continue;
					}
				}
			}
		}
	}
	
	abstract class Wrapper implements Runnable {

		@Override
		public final void run() {
			try {
				ThreadLocalLoaderFilter.set(loader);
				_run();
			}
			catch(RuntimeException rex) {
				LOGGER.error("Spider error", rex);
			}
		}
		
		protected abstract void _run();
		
	}
}
