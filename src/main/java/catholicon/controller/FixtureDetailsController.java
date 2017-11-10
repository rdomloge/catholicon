package catholicon.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import catholicon.dao.FixtureDao;
import catholicon.domain.FixtureDetails;
import catholicon.ex.DaoException;

@RestController
public class FixtureDetailsController {
	
	@Autowired
	private FixtureDao fixtureDao;
	
	@RequestMapping(method=RequestMethod.GET, value="/fixture/{fixtureId}")
	@Cacheable(cacheNames="Fixtures")
	public FixtureDetails getFixtureDetails(@PathVariable("fixtureId") int fixtureId) throws DaoException {
		
		return fixtureDao.load(fixtureId);
	}
}
