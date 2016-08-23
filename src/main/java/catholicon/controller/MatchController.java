package catholicon.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import catholicon.dao.MatchDao;
import catholicon.dao.LeagueDao;
import catholicon.domain.Match;
import catholicon.domain.League;
import catholicon.ex.DaoException;

@RestController
public class MatchController {
	
	
	@RequestMapping("/league")
	public League getLeague(@RequestParam("league") String league) throws DaoException {
		
		return new LeagueDao().load(league);
	}
	
	@RequestMapping("/matches/list")
	public Match[] loadMatches(@RequestParam("team") String team) 
			throws DaoException {
		
		return new MatchDao().load(team);
	}
}
