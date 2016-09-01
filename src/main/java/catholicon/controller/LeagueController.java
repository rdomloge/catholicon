package catholicon.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import catholicon.dao.LeagueDao;
import catholicon.domain.League;
import catholicon.ex.DaoException;

@RestController
public class LeagueController {

	@RequestMapping("/league/list")
	public List<League> listLeagues() throws DaoException {
		
		return new LeagueDao().list();
	}
}
