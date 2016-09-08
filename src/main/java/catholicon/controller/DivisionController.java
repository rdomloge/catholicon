package catholicon.controller;

import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import catholicon.dao.DivisionDao;
import catholicon.domain.Division;
import catholicon.domain.DivisionDescriptor;
import catholicon.ex.DaoException;

@RestController
public class DivisionController {
	
	@RequestMapping("/season/{seasonStartYear}/league/{leagueTypeId}/division/{divisionId}")
	public Division getLeague(@PathVariable("leagueTypeId") String leagueTypeId,
			@PathVariable("divisionId") int divisionId,
			@PathVariable("seasonStartYear") int seasonStartYear) throws DaoException {
		
		return new DivisionDao().load(leagueTypeId, divisionId, seasonStartYear);
	}
	
	@RequestMapping("/season/{seasonStartYear}/league/{leagueTypeId}/divisions")
	public List<DivisionDescriptor> getDivisionsForLeague(
			@PathVariable("leagueTypeId") int leagueTypeId,
			@PathVariable("seasonStartYear") int seasonStartYear) throws DaoException {
		
		return new DivisionDao().getDivisionsForLeague(leagueTypeId, seasonStartYear);
	}
}