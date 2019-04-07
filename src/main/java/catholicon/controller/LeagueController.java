package catholicon.controller;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
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
	public ResponseEntity<List<League>> listLeagues(
			@PathVariable("seasonId") int seasonId) throws DaoException {
		return ResponseEntity.ok()
				.cacheControl(CacheControl.maxAge(1, TimeUnit.DAYS))
				.body(new LeagueDao().list(seasonId));
	}
	
}
