package catholicon.controller;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import catholicon.dao.LeagueDao;
import catholicon.domain.League;
import catholicon.ex.DaoException;

@RestController
public class LeagueController {
	

	@RequestMapping(method=RequestMethod.GET, value="/season/{seasonId}/league/list")
	@Cacheable(cacheNames="Leagues")
	public List<League> listLeagues(
			@PathVariable("seasonId") int seasonId) throws DaoException {
		
		return new LeagueDao().list(seasonId);
	}
	
}
