package catholicon.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import catholicon.dao.FixtureDao;
import catholicon.domain.FixtureDetails;
import catholicon.ex.DaoException;

import javax.servlet.http.HttpServletRequest;

@RestController
public class FixtureDetailsController {
	
	@Autowired
	private FixtureDao fixtureDao;
	
	@RequestMapping(method=RequestMethod.GET, value="/fixture/{fixtureId}")
	public FixtureDetails getFixtureDetails(
			HttpServletRequest req,
			@PathVariable("fixtureId") int fixtureId) throws DaoException {
		
		return fixtureDao.load(fixtureId);
	}
}
