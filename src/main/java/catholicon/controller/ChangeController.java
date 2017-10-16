package catholicon.controller;

import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import catholicon.dao.ChangeDao;
import catholicon.domain.Change;

@RestController
public class ChangeController {

	@RequestMapping(method=RequestMethod.GET, value="/seasons/{seasonStartYear}/fixtures/{fixtureId}/changes")
	public List<Change> getChanges(
			@PathVariable("fixtureId") int fixtureId,
			@PathVariable("seasonStartYear") int seasonStartYear) {
		
		return new ChangeDao().getChanges(fixtureId, seasonStartYear);
	}
	
}
