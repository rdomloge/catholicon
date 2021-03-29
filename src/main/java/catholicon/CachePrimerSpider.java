package catholicon;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import catholicon.controller.ClubController;
import catholicon.controller.CommitteeController;
import catholicon.controller.FixtureDetailsController;
import catholicon.dao.Loader;
import catholicon.domain.Club;
import catholicon.domain.Match;
import catholicon.filter.ThreadLocalLoaderFilter;

@Component
public class CachePrimerSpider {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CachePrimerSpider.class);
	
	private ScheduledExecutorService exec;
	
	@Value("${CACHE_PRIMER_SPIDER_DISABLED:false}")
	private boolean disableSpider;
	
	@Value("${BASE_URL:http://bdbl.org.uk}")
	private String BASE;
	
	@Value("${SPIDER_MAX_SEASONS:1}")
	private int maxSeasonsToSpider;
	
	@Autowired
	private FixtureDetailsController fixtureDetailsController;
	
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
		}
		else {
			LOGGER.info("Cache primer Spider disabled");
		}
	}
	
	@EventListener(ContextClosedEvent.class)
	public void stopSpider() throws InterruptedException {
		
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
	
}
