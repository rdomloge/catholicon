package catholicon.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import catholicon.dao.SeasonDao;
import catholicon.domain.Season;
import catholicon.ex.DaoException;

@RestController
public class SeasonController {

	@RequestMapping("/season/list")
	public List<Season> getSeasonList() throws DaoException {
		
		return new SeasonDao().loadSeasons();
	}
}
