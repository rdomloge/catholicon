package catholicon.controller;

import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import catholicon.dao.ClubDao;
import catholicon.domain.Club;

@RestController
public class ClubController {
	
	@RequestMapping(method=RequestMethod.GET, value="/clubs")
	public List<Club> getClubs() {
		
		return new ClubDao().getClubs(0);
	}

	@RequestMapping(method=RequestMethod.GET, value="/clubs")
	public List<Club> getClubs(@RequestParam String fetch) {
		
		if("clubId,clubName".equalsIgnoreCase(fetch)) {
			return new ClubDao().getClubIds(0);	
		}
		
		throw new IllegalArgumentException();
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/clubs/{clubId}")
	public Club getClub(@PathVariable("clubId") int clubId) {
		
		return new ClubDao().getClub(0, clubId);
	}
}
