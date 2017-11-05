package catholicon.controller;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import catholicon.dao.SeasonDao;
import catholicon.domain.Season;
import catholicon.ex.DaoException;

@RestController
public class SeasonController {

	@RequestMapping(method=RequestMethod.GET, value="/seasons")
	@Cacheable(cacheNames="Season")
	public Season[] getSeasonList() throws DaoException {
		
		return new SeasonDao().loadSeasons();
	}
}
