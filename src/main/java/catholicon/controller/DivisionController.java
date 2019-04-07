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

import catholicon.dao.DivisionDao;
import catholicon.domain.Division;
import catholicon.domain.DivisionDescriptor;
import catholicon.ex.DaoException;

@RestController
public class DivisionController {
	
	@RequestMapping(method=RequestMethod.GET, value="/season/{seasonStartYear}/league/{leagueTypeId}/division/{divisionId}")
	@Cacheable(cacheNames="Division")
	public ResponseEntity<Division> getDivision(
			@PathVariable("leagueTypeId") String leagueTypeId,
			@PathVariable("divisionId") int divisionId,
			@PathVariable("seasonStartYear") int seasonStartYear) throws DaoException {
		
		return ResponseEntity.ok()
				.cacheControl(CacheControl.maxAge(1, TimeUnit.HOURS))
				.body(new DivisionDao().load(leagueTypeId, divisionId, seasonStartYear));
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/season/{seasonStartYear}/league/{leagueTypeId}/divisions")
	@Cacheable(cacheNames="DivisionDescriptor")
	public ResponseEntity<List<DivisionDescriptor>> getDivisionsForLeague(
			@PathVariable("leagueTypeId") int leagueTypeId,
			@PathVariable("seasonStartYear") int seasonStartYear) throws DaoException {
		
		return ResponseEntity.ok()
				.cacheControl(CacheControl.maxAge(1, TimeUnit.DAYS))
				.body(new DivisionDao().getDivisionsForLeague(leagueTypeId, seasonStartYear));
	}
}
