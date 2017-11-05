package catholicon.controller;

import org.springframework.cache.annotation.Cacheable;
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
	public PlayerReport[] getBestPlayerReport(
			@PathVariable("season") int season,
			@PathVariable("league") int league) throws DaoException {
		
		return new PlayerReportDao().loadPlayerReport(""+season, ""+league);
	}
}
