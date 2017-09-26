package catholicon.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import catholicon.dao.LeagueDao;
import catholicon.dao.MatchDao;
import catholicon.domain.League;
import catholicon.domain.Match;
import catholicon.ex.DaoException;

@RestController
public class LeagueController {
	
	@Autowired
	private MatchDao matchDao;

	@RequestMapping(method=RequestMethod.GET, value="/season/{seasonId}/league/list")
	public List<League> listLeagues(
			@PathVariable("seasonId") int seasonId) throws DaoException {
		
		return new LeagueDao().list(seasonId);
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/recent")
	public List<Match> listRecent() throws DaoException {
		
		return matchDao.getRecentMatches();
	}
}
