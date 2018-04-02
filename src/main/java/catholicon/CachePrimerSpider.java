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

import catholicon.controller.ClubController;
import catholicon.controller.CommitteeController;
import catholicon.controller.DivisionController;
import catholicon.controller.FixtureDetailsController;
import catholicon.controller.LeagueController;
import catholicon.controller.MatchCardController;
import catholicon.controller.MatchController;
import catholicon.controller.SeasonController;
import catholicon.dao.Loader;
import catholicon.domain.Club;
import catholicon.domain.Division;
import catholicon.domain.Division.TeamPosition;
import catholicon.domain.DivisionDescriptor;
import catholicon.domain.League;
import catholicon.domain.Match;
import catholicon.domain.Season;
import catholicon.filter.ThreadLocalLoaderFilter;

@Component
public class CachePrimerSpider {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CachePrimerSpider.class);
	
	private ScheduledExecutorService exec;
	
	@Value("${CACHE_PRIMER_SPIDER_DISABLED:false}")
	private boolean disableSpider;
	
	@Value("${BASE_URL:http://192.168.0.14}")
	private String BASE;
	
	@Value("${SPIDER_MAX_SEASONS:1}")
	private int maxSeasonsToSpider;
	
	@Autowired
	private SeasonController seasonController;
	
	@Autowired
	private LeagueController leagueController;
	
	@Autowired
	private DivisionController divisionController;
	
	@Autowired
	private MatchController matchController;
	
	@Autowired
	private FixtureDetailsController fixtureDetailsController;
	
	@Autowired
	private MatchCardController matchCardController;
	
	@Autowired
	private ClubController clubController;
	
	@Autowired
	private CommitteeController committeeController;

	@EventListener(ContextRefreshedEvent.class)
	public void startSpider() {
		if( ! disableSpider) {
			LOGGER.info("Starting spider...");
			exec = Executors.newScheduledThreadPool(3);
			
			exec.execute(new ClubsSpider());
			exec.execute(new CommitteeSpider());
			exec.execute(new SeasonSpider());
		}
		else {
			LOGGER.info("Cache primer Spider disabled");
		}
	}
	
	@EventListener(ContextStoppedEvent.class)
	public void stopSpider() {
		exec.shutdown();
	}
	
	abstract class Wrapper implements Runnable {

		@Override
		public final void run() {
			try {
				ThreadLocalLoaderFilter.set(new Loader(BASE));
				_run();
			}
			catch(RuntimeException rex) {
				LOGGER.error("Spider error", rex);
			}
		}

		abstract void _run();
	}
	
	class ClubsSpider extends Wrapper {

		@Override
		void _run() {
			List<Club> clubs = clubController.getClubs(new String[] {"clubId", "clubName"}).getBody();
			for (Club club : clubs) {
				exec.execute(new ClubSpider(club));
			}
		}
	}
	
	class ClubSpider extends Wrapper {
		
		private Club descriptor;

		public ClubSpider(Club descriptor) {
			this.descriptor = descriptor;
		}

		@Override
		void _run() {
			clubController.getClub(descriptor.getClubId());
		}
		
	}
	
	class CommitteeSpider extends Wrapper {

		@Override
		void _run() {
			committeeController.getCommitteeContacts();
		}
		
	}
	
	class SeasonSpider extends Wrapper {
		@Override
		public void _run() {
			LOGGER.debug("Cache primer spider loading seasons");
			
			Season[] seasons = seasonController.getSeasonList().getBody();
			for (int i = 0; i < Math.min(seasons.length, maxSeasonsToSpider); i++) {
				exec.execute(new LeagueSpider(seasons[i]));
			}
		}
	}
	
	class LeagueSpider extends Wrapper {
		private Season season;
		
		public LeagueSpider(Season season) {
			this.season = season;
		}

		@Override
		public void _run() {
			LOGGER.debug("Spider loading leagues for "+season.getSeasonStartYear());
			List<League> leagues = leagueController.listLeagues(season.getApiIdentifier()).getBody();
			
			for (League league : leagues) {
				exec.execute(new DivisionsSpider(league));
			}
		}
	}
	
	class DivisionsSpider extends Wrapper {
		
		private League league;
		
		public DivisionsSpider(League league) {
			this.league = league;
		}

		@Override
		public void _run() {
			LOGGER.debug("Spider loading divisions for league "+league.getLeagueTypeId()+" for "+league.getSeason());
			
			List<DivisionDescriptor> divisionsForLeague = divisionController.getDivisionsForLeague(league.getLeagueTypeId(), league.getSeason()).getBody();
			for (DivisionDescriptor divisionDescriptor : divisionsForLeague) {
				exec.execute(new DivisionSpider(divisionDescriptor));
			}
		}
	}
	
	class DivisionSpider extends Wrapper {
		
		private DivisionDescriptor divisionDescriptor;

		public DivisionSpider(DivisionDescriptor divisionDescriptor) {
			this.divisionDescriptor = divisionDescriptor;
		}

		@Override
		public void _run() {
			String leagueTypeId = ""+divisionDescriptor.getLeagueTypeId();
			int divisionId = divisionDescriptor.getDivisionId();
			int seasonStartYear = divisionDescriptor.getSeason();
			LOGGER.debug("Spider loading division "+divisionId+" for league "+leagueTypeId+" in "+seasonStartYear);
			Division division = divisionController.getDivision(
					leagueTypeId, 
					divisionId, 
					seasonStartYear).getBody();
			TeamPosition[] teamPositions = division.getPositions();
			for (TeamPosition teamPosition : teamPositions) {
				exec.execute(new MatchesSpider(seasonStartYear, teamPosition));
			}
		}
	}
	
	class MatchesSpider extends Wrapper {

		private TeamPosition teamPosition;
		
		private int seasonStartYear;
		
		public MatchesSpider(int seasonStartYear, TeamPosition teamPosition) {
			this.teamPosition = teamPosition;
			this.seasonStartYear = seasonStartYear;
		}

		@Override
		public void _run() {
			LOGGER.debug("Spider loading matches for team "+teamPosition.getTeamId()+" in "+seasonStartYear);
			Match[] matches = matchController.loadMatches(""+teamPosition.getTeamId(), seasonStartYear).getBody();
			for (Match match : matches) {
				if(null != match.getFixtureId()) {
					exec.execute(new FixtureDetailsSpider(match));
				}
				if(match.isPlayed()) {
					exec.execute(new MatchSpider(match));
				}
			}
		}
	}
	
	class FixtureDetailsSpider extends Wrapper {

		private Match match;
		
		public FixtureDetailsSpider(Match match) {
			this.match = match;
		}

		@Override
		public void _run() {
			LOGGER.debug("Spider fixture details "+match.getFixtureId());
			fixtureDetailsController.getFixtureDetails(Integer.parseInt(match.getFixtureId()));
			
		}
		
	}
	
	class MatchSpider extends Wrapper {

		private Match match;
		
		public MatchSpider(Match match) {
			this.match = match;
		}
		
		@Override
		public void _run() {
			LOGGER.debug("Spider loading match "+match.getFixtureId());
			matchCardController.loadMatchCard(match.getFixtureId());
		}
		
	}
}
