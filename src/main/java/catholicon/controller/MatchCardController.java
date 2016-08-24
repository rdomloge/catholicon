package catholicon.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import catholicon.dao.MatchCardDao;
import catholicon.domain.MatchCard;
import catholicon.ex.DaoException;

@RestController
public class MatchCardController {

	
	@RequestMapping("/matchcard/{fixtureid}")
	public MatchCard loadMatchCard(@PathVariable("fixtureid") String fixtureId) throws DaoException {
		
		return new MatchCardDao().load(fixtureId);
	}
}
