package catholicon.controller;

import java.util.Arrays;
import java.util.List;

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
	
//	@RequestMapping(method=RequestMethod.GET, value="/clubs")
//	public List<Club> getClubs() {
//		
//		return new ClubDao().getClubs(0);
//	}

	@RequestMapping(method=RequestMethod.GET, value="/clubs")
	public List<Club> getClubs(@RequestParam String[] fetch) {
		
		if("[clubId, clubName]".equalsIgnoreCase(Arrays.toString(fetch))) {
			return new ClubDao().getClubIds(0);	
		}
		
		throw new UnsupportedParametersException("Do not understand fetch parameter(HINT: only 'clubId,clubName' supported): "+Arrays.toString(fetch));
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/clubs/{clubId}")
	public Club getClub(@PathVariable("clubId") int clubId) {
		
		return new ClubDao().getClub(0, clubId);
	}
}
