package catholicon.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
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
	public Match[] loadMatches(@PathVariable("team") String team,
			@PathVariable("seasonStartYear") int seasonStartYear)
			throws DaoException {

		Match[] matches = matchDao.load(seasonStartYear, team);

		return matches;
	}
}
