package catholicon.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import catholicon.dao.MatchDao;
import catholicon.domain.Match;
import catholicon.ex.DaoException;

@RestController
public class MatchController {
	
	
	@RequestMapping("/matches/{team}/list")
	public Match[] loadMatches(@PathVariable("team") String team) 
			throws DaoException {
		
		return new MatchDao().load(team);
	}
}
