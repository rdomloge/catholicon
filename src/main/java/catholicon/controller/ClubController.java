package catholicon.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import catholicon.dao.ClubDao;
import catholicon.domain.Club;

@RestController
public class ClubController {
	
	@RequestMapping(method=RequestMethod.GET, value="/clubs")
	public List<Club> getClubs() {
		
		return new ClubDao().getClubs(0);
	}

}
