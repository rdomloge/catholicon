package catholicon.controller;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import catholicon.dao.MatchDao;
import catholicon.domain.Match;
import catholicon.ex.DaoException;

@RestController
public class MatchController {

	@Autowired
	private MatchDao matchDao;

	@RequestMapping(method = RequestMethod.GET, value = "/season/{seasonStartYear}/matches/{team}/list")
	@Cacheable(cacheNames = "Matches")
	public ResponseEntity<Match[]> loadMatches(@PathVariable("team") String team,
			@PathVariable("seasonStartYear") int seasonStartYear)
			throws DaoException {

		return ResponseEntity.ok()
				.cacheControl(CacheControl.maxAge(1, TimeUnit.HOURS))
				.body(matchDao.load(seasonStartYear, team));
	}
}
