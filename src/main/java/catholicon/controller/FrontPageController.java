package catholicon.controller;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import catholicon.dao.FrontPageDao;
import catholicon.dao.MatchDao;
import catholicon.domain.Match;
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
	public ResponseEntity<List<UpcomingFixture>> getUpcomingFixtures() throws DaoException {
		
		return ResponseEntity.ok()
				.cacheControl(CacheControl.maxAge(2, TimeUnit.HOURS))
				.body(frontPageDao.getUpcomingFixtures());
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/recent")
	@Cacheable(cacheNames="Recent")
	public ResponseEntity<List<Match>> listRecent() throws DaoException {
		
		return ResponseEntity.ok()
				.cacheControl(CacheControl.maxAge(20, TimeUnit.MINUTES))
				.body(matchDao.getRecentMatches());
	}
}
