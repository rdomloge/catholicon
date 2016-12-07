package catholicon.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import catholicon.dao.FixtureDao;
import catholicon.domain.FixtureDetails;
import catholicon.ex.DaoException;

import javax.servlet.http.HttpServletRequest;

@RestController
public class FixtureDetailsController {
	
	@RequestMapping("/fixture/{fixtureId}")
	public FixtureDetails getFixtureDetails(
			HttpServletRequest req,
			@PathVariable("fixtureId") int fixtureId) throws DaoException {
		
		return new FixtureDao().load(fixtureId);
	}
}
