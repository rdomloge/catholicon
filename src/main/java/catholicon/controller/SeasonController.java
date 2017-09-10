package catholicon.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import catholicon.dao.SeasonDao;
import catholicon.domain.SeasonsDescriptor;
import catholicon.ex.DaoException;

@RestController
public class SeasonController {

	@RequestMapping(method=RequestMethod.GET, value="/season/list")
	public SeasonsDescriptor getSeasonList() throws DaoException {
		
		return new SeasonDao().loadSeasonsDescriptor();
	}
}
