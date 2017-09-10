package catholicon.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import catholicon.dao.AuthenticationException;
import catholicon.dao.WelcomeDao;
import catholicon.domain.Login;
import catholicon.domain.WelcomePageItem;
import catholicon.ex.DaoException;

@RestController
public class LoginController {

	
	@RequestMapping(method=RequestMethod.GET, value="/secure/welcome")
	public List<WelcomePageItem> loadWelcomePage() throws DaoException {
		
		return new WelcomeDao().loadWelcomePage();
		
//		List<WelcomePageItem> list = new WelcomeDao().loadWelcomePage(getLoader(req));
//		WelcomePageItem item1 = new WelcomePageItem("2nd Nov 2016", "Away team A", "Home team B", 0, 0);
//		list.add(item1);
//		WelcomePageItem item2 = new WelcomePageItem("3rd Nov 2016", "Away team A2", "Home team B2", 2, 2);
//		list.add(item2);
//		return list;
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/secure/doLogin")
	public ResponseEntity<Map<String, String>> doLogin(@RequestBody Login login) throws DaoException {
		
		try {
			Map<String, String> map = new WelcomeDao().login(login);
			return new ResponseEntity<Map<String,String>>(map, HttpStatus.OK);
		} 
		catch (AuthenticationException e) {
			return new ResponseEntity<Map<String,String>>(HttpStatus.FORBIDDEN);
		}
		
	}
}
