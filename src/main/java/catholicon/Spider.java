package catholicon;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import catholicon.archive.RestArchive;
import catholicon.dao.DivisionDao;
import catholicon.dao.LeagueDao;
import catholicon.dao.Loader;
import catholicon.dao.MatchDao;
import catholicon.dao.SeasonDao;
import catholicon.domain.Division;
import catholicon.domain.Division.TeamPosition;
import catholicon.domain.DivisionDescriptor;
import catholicon.domain.League;
import catholicon.domain.Match;
import catholicon.domain.Season;
import catholicon.filter.ThreadLocalLoaderFilter;

@Component
public class Spider {

	private static final Logger LOGGER = LoggerFactory.getLogger(Spider.class);
	
	private ScheduledExecutorService exec;
	
	private SeasonDao seasonDao = new SeasonDao();
	private LeagueDao leagueDao = new LeagueDao();
	private DivisionDao divisionDao = new DivisionDao();
	private MatchDao matchDao = new MatchDao();
	
	@Autowired
	private RestArchive restArchive;
	
	@Value("${BASE_URL:http://192.168.0.14}")
	private String BASE;
	
	@Value("${SPIDER_MAX_SEASONS:1}")
	private int maxSeasonsToSpider;
	
	@Value("${SPIDER_BASE_URL:http://localhost:8080}")
	private String spiderBase;
	
	@Value("${SPIDER_ENABLED:true}")
	private boolean runSpider;
	
	private Loader loader;
	
	private Loader spiderLoader;
	
	@EventListener(ContextRefreshedEvent.class)
	public void startSpider() {
		if(runSpider) {
			LOGGER.info("Starting spider...");
			exec = Executors.newSingleThreadScheduledExecutor();
			loader = new Loader(BASE);
			spiderLoader = new Loader(spiderBase);
			
			exec.execute(new Wrapper(new SeasonSpider()));
		}
		else {
			LOGGER.info("Spider disabled");
		}
	}
	
	@EventListener(ContextStoppedEvent.class)
	public void stopSpider() {
		exec.shutdown();
	}

	class Wrapper implements Runnable {

		private Runnable target;
		
		public Wrapper(Runnable target) {
			this.target = target;
		}

		@Override
		public void run() {
			try {
				ThreadLocalLoaderFilter.set(loader);
				target.run();
			}
			catch(RuntimeException rex) {
				LOGGER.error("Spider error", rex);
			}
		}
		
	}
	
	class SeasonSpider implements Runnable {
		@Override
		public void run() {
			LOGGER.debug("Spider loading seasons (max="+maxSeasonsToSpider+")");
			if( ! restArchive.exists("/catholicon/seasons")) {
				spiderLoader.load("/catholicon/seasons");
			}
			Season[] seasons = seasonDao.loadSeasons();
			seasons[0] = new Season(seasons[0].getId(), 0, seasons[0].getSeasonEndYear());
			for (int i = 0; i < Math.min(seasons.length, maxSeasonsToSpider); i++) {
				exec.execute(new Wrapper(new LeagueSpider(seasons[i])));
			}
		}
	}
	
	class LeagueSpider implements Runnable {
		private Season season;
		
		public LeagueSpider(Season season) {
			this.season = season;
		}

		@Override
		public void run() {
			int seasonStartYear = season.getSeasonStartYear();
			LOGGER.debug("Spider loading leagues for "+seasonStartYear);
			String url = String.format("/catholicon/season/%d/league/list", seasonStartYear);
			if( ! restArchive.exists(url)) {
				spiderLoader.load(url);
			}
			
			List<League> leagues = leagueDao.list(seasonStartYear);
			for (League league : leagues) {
				exec.execute(new Wrapper(new DivisionsSpider(league)));
			}
		}
	}
	
	class DivisionsSpider implements Runnable {
		
		private League league;
		
		public DivisionsSpider(League league) {
			this.league = league;
		}

		@Override
		public void run() {
			LOGGER.debug("Spider loading divisions for league "+league.getLeagueTypeId()+" for "+league.getSeason());
			int leagueTypeId = league.getLeagueTypeId();
			int seasonStartYear = league.getSeason();
			String url = String.format("/season/%s/league/%s/divisions", seasonStartYear, leagueTypeId);
			if( ! restArchive.exists(url)) {
				spiderLoader.load(url);
			}
			List<DivisionDescriptor> divisionsForLeague = divisionDao.getDivisionsForLeague(leagueTypeId, seasonStartYear);
			for (DivisionDescriptor divisionDescriptor : divisionsForLeague) {
				exec.execute(new Wrapper(new DivisionSpider(divisionDescriptor)));
			}
		}
	}
	
	class DivisionSpider implements Runnable {
		
		private DivisionDescriptor divisionDescriptor;

		public DivisionSpider(DivisionDescriptor divisionDescriptor) {
			this.divisionDescriptor = divisionDescriptor;
		}

		@Override
		public void run() {
			String leagueTypeId = ""+divisionDescriptor.getLeagueTypeId();
			int divisionId = divisionDescriptor.getDivisionId();
			int seasonStartYear = divisionDescriptor.getSeason();
			LOGGER.debug("Spider loading division "+divisionId+" for league "+leagueTypeId+" in "+seasonStartYear);
			String url = String.format("/season/%s/league/%s/division/%s", seasonStartYear, leagueTypeId, divisionId);
			if( ! restArchive.exists(url)) {
				spiderLoader.load(url);
			}
			Division division = divisionDao.load(
					leagueTypeId, 
					divisionId, 
					seasonStartYear);
			TeamPosition[] teamPositions = division.getPositions();
			for (TeamPosition teamPosition : teamPositions) {
				exec.execute(new Wrapper(new MatchesSpider(seasonStartYear, teamPosition)));
			}
		}
		
	}
	
	class MatchesSpider implements Runnable {

		private TeamPosition teamPosition;
		
		private int seasonStartYear;
		
		public MatchesSpider(int seasonStartYear, TeamPosition teamPosition) {
			this.teamPosition = teamPosition;
			this.seasonStartYear = seasonStartYear;
		}

		@Override
		public void run() {
			LOGGER.debug("Spider loading matches for team "+teamPosition.getTeamId()+" in "+seasonStartYear);
			String url = String.format("/season/%s/matches/%s/list", seasonStartYear, teamPosition.getTeamId());
			if( ! restArchive.exists(url)) {
				spiderLoader.load(url);	
			}
			Match[] matches = matchDao.load(seasonStartYear, ""+teamPosition.getTeamId());
			for (Match match : matches) {
				if(null != match.getFixtureId()) {
					exec.execute(new Wrapper(new FixtureDetailsSpider(match)));
				}
				if(match.isPlayed()) {
					exec.execute(new Wrapper(new MatchSpider(match)));
				}
			}
		}
	}
	
	class FixtureDetailsSpider implements Runnable {

		private Match match;
		
		public FixtureDetailsSpider(Match match) {
			this.match = match;
		}

		@Override
		public void run() {
			LOGGER.debug("Spider fixture details "+match.getFixtureId());
			String url = String.format("/fixture/%s", match.getFixtureId());
			if( ! restArchive.exists(url)) {
				spiderLoader.load(url);	
			}
		}
		
	}
	
	class MatchSpider implements Runnable {

		private Match match;
		
		public MatchSpider(Match match) {
			this.match = match;
		}
		
		@Override
		public void run() {
			LOGGER.debug("Spider loading match "+match.getFixtureId());
			String url = String.format("/matchcard/%s", match.getFixtureId());
			if( ! restArchive.exists(url)) {
				spiderLoader.load(url);	
			}
//			matchCardDao.load(match.getFixtureId()); // not really beneficial...
		}
		
	}
}