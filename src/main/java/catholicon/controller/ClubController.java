package catholicon.controller;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import catholicon.dao.ClubDao;
import catholicon.domain.Club;
import catholicon.ex.UnsupportedParametersException;

@RestController
public class ClubController {
	
	@RequestMapping(method=RequestMethod.GET, value="/clubs")
	@Cacheable(cacheNames="Clubs")
	public ResponseEntity<List<Club>> getClubs(@RequestParam String[] fetch) {
		
		if("[clubId, clubName]".equalsIgnoreCase(Arrays.toString(fetch))) {
			return ResponseEntity.ok()
					.cacheControl(CacheControl.maxAge(7, TimeUnit.DAYS))
					.body(new ClubDao().getClubIds(0));	
		}
		
		throw new UnsupportedParametersException("Do not understand fetch parameter(HINT: only 'clubId,clubName' supported): "+Arrays.toString(fetch));
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/clubs/{clubId}")
	@Cacheable(cacheNames="Club")
	public ResponseEntity<Club> getClub(@PathVariable("clubId") int clubId) {
		
		return ResponseEntity.ok()
				.cacheControl(CacheControl.maxAge(7, TimeUnit.DAYS))
				.body(new ClubDao().getClub(0, clubId));
	}
}
