package catholicon.controller;

import java.util.concurrent.TimeUnit;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import catholicon.dao.SeasonDao;
import catholicon.domain.Season;
import catholicon.ex.DaoException;

@RestController
public class SeasonController {

	@RequestMapping(method=RequestMethod.GET, value="/seasons", produces = "application/json; charset=UTF-8")
	@Cacheable(cacheNames="Season")
	public ResponseEntity<Season[]> getSeasonList() throws DaoException {
		return ResponseEntity.ok()
				.cacheControl(CacheControl.maxAge(7, TimeUnit.DAYS))
				.body(new SeasonDao().loadSeasons());
	}
}
