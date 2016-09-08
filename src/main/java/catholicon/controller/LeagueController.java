package catholicon.controller;

import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import catholicon.dao.LeagueDao;
import catholicon.domain.League;
import catholicon.ex.DaoException;

@RestController
public class LeagueController {

	@RequestMapping("/season/{seasonId}/league/list")
	public List<League> listLeagues(@PathVariable("seasonId") int seasonId) throws DaoException {
		
		return new LeagueDao().list(seasonId);
	}
}
