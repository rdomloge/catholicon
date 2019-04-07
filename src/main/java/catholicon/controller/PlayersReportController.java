package catholicon.controller;

import java.util.concurrent.TimeUnit;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import catholicon.dao.PlayerReportDao;
import catholicon.domain.PlayerReport;
import catholicon.ex.DaoException;

@RestController
public class PlayersReportController {

	
	@RequestMapping(method=RequestMethod.GET, value="/season/{season}/league/{league}/report")
	@Cacheable(cacheNames="PlayerReports")
	public ResponseEntity<PlayerReport[]> getBestPlayerReport(
			@PathVariable("season") int season,
			@PathVariable("league") int league) throws DaoException {
		
		return ResponseEntity.ok()
				.cacheControl(CacheControl.maxAge(1, TimeUnit.DAYS))
				.body(new PlayerReportDao().loadPlayerReport(""+season, ""+league));
	}
}
