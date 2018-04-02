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

import catholicon.dao.ChangeDao;
import catholicon.domain.Change;

@RestController
public class ChangeController {

	@RequestMapping(method=RequestMethod.GET, value="/seasons/{seasonStartYear}/fixtures/{fixtureId}/changes")
	@Cacheable(cacheNames="Changes")
	public ResponseEntity<List<Change>> getChanges(
			@PathVariable("fixtureId") int fixtureId,
			@PathVariable("seasonStartYear") int seasonStartYear) {
		
		return ResponseEntity.ok()
				.cacheControl(CacheControl.maxAge(1, TimeUnit.HOURS))
				.body(new ChangeDao().getChanges(fixtureId, seasonStartYear));
	}
	
}
