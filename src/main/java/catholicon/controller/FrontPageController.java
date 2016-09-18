package catholicon.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import catholicon.dao.FrontPageDao;
import catholicon.domain.UpcomingFixture;
import catholicon.ex.DaoException;

@RestController
public class FrontPageController {

	
	@RequestMapping("/frontpage/upcoming")
	public List<UpcomingFixture> getUpcomingFixtures() throws DaoException {
		return new FrontPageDao().getUpcomingFixtures();
	}
}
