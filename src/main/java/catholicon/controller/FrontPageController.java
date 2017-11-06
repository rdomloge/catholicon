package catholicon.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
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
	public List<UpcomingFixture> getUpcomingFixtures() throws DaoException {
		
		return frontPageDao.getUpcomingFixtures();
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/recent")
	@Cacheable(cacheNames="Recent")
	public List<Match> listRecent() throws DaoException {
		
		return matchDao.getRecentMatches();
	}
}
