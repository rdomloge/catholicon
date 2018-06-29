package catholicon.controller;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import catholicon.dao.FrontPageDao;
import catholicon.dao.MatchDao;
import catholicon.domain.Match;
import catholicon.domain.Team;
import catholicon.domain.UpcomingFixture;
import catholicon.ex.DaoException;

@RestController
public class FrontPageController {
	
	@Autowired
	private FrontPageDao frontPageDao;
	
	@Autowired
	private MatchDao matchDao;

	
	@RequestMapping(method=RequestMethod.GET, value="/frontpage/upcoming")
	@Cacheable(cacheNames="Upcoming")
	public ResponseEntity<List<UpcomingFixture>> getUpcomingFixtures(
			@RequestParam(required=false) String test) 
					throws DaoException {
		
		if(null != test) {
			List<UpcomingFixture> dummyData = new LinkedList<>();
			dummyData.add(new UpcomingFixture("BH Pegasus", "Away team", "2018-05-20", 1234));
			dummyData.add(new UpcomingFixture("Viking", "Away team", "2018-05-25", 1234));
			dummyData.add(new UpcomingFixture("Waverley", "Away team", "2018-05-28", 1234));
			dummyData.add(new UpcomingFixture("Aldermaston", "Challengers", "2018-06-01", 1234));
			dummyData.add(new UpcomingFixture("Hurst", "Waverley", "2018-06-03", 1234));
			dummyData.add(new UpcomingFixture("Challengers", "Aldermaston", "2018-06-04", 1234));
			dummyData.add(new UpcomingFixture("Andover Sports", "Away team", "2018-06-10", 1234));
			dummyData.add(new UpcomingFixture("Andover Sports", "Viking", "2018-06-11", 1232));
			dummyData.add(new UpcomingFixture("Andover Sports", "BH Pegasus", "2018-06-12", 1234));
			
			return ResponseEntity.ok()
					.cacheControl(CacheControl.maxAge(2, TimeUnit.HOURS))
					.body(dummyData);
		}
		
		return ResponseEntity.ok()
				.cacheControl(CacheControl.maxAge(2, TimeUnit.HOURS))
				.body(frontPageDao.getUpcomingFixtures());
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/recent")
	@Cacheable(cacheNames="Recent")
	public ResponseEntity<List<Match>> listRecent(@RequestParam(required=false) String test) throws DaoException {
		
		if(null != test) {
			List<Match> dummyData = new LinkedList<>();
			dummyData.add(new Match("2018-05-20", 1, "Fixture text", "1234", "away win 4-5", "4-5", new Team("Home Team", 1, 4), new Team("Away team", 2, 5)));
			dummyData.add(new Match("2018-05-21", 2, "Fixture text 2", "1235", "philanthropic gerbils", "carrots", new Team("BH Pegasus", 1, 7), new Team("Waverley", 2, 2)));
			
			return ResponseEntity.ok()
					.cacheControl(CacheControl.maxAge(20, TimeUnit.MINUTES))
					.body(dummyData);
		}
		
		return ResponseEntity.ok()
				.cacheControl(CacheControl.maxAge(20, TimeUnit.MINUTES))
				.body(matchDao.getRecentMatches());
	}
}
