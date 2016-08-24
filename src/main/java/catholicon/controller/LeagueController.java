package catholicon.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import catholicon.dao.LeagueDao;
import catholicon.domain.League;
import catholicon.ex.DaoException;

@RestController
public class LeagueController {

	@RequestMapping("/league/{league}")
	public League getLeague(@PathVariable("league") String league) throws DaoException {
		
		return new LeagueDao().load(league);
	}
	
	@RequestMapping("/league/list")
	public String[] listLeagues() throws DaoException {
		
		return new LeagueDao().list();
	}
}
