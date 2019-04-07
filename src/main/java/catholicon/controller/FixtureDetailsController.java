package catholicon.controller;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
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
	public ResponseEntity<FixtureDetails> getFixtureDetails(@PathVariable("fixtureId") int fixtureId) throws DaoException {
		
		return ResponseEntity.ok()
				.cacheControl(CacheControl.maxAge(24, TimeUnit.HOURS))
				.body(fixtureDao.load(fixtureId));
	}
}
