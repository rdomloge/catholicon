package catholicon.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import catholicon.dao.SeasonDao;
import catholicon.domain.SeasonsDescriptor;
import catholicon.ex.DaoException;

import javax.servlet.http.HttpServletRequest;

@RestController
public class SeasonController {

	@RequestMapping("/season/list")
	public SeasonsDescriptor getSeasonList(HttpServletRequest req) throws DaoException {
		
		return new SeasonDao().loadSeasonsDescriptor();
	}
}
