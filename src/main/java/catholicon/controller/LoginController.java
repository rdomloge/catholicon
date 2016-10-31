package catholicon.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import catholicon.dao.Loader;
import catholicon.domain.Login;
import catholicon.ex.DaoException;

@RestController
public class LoginController {

	private static final String url = "http://bdbl.org.uk/Live/Login.asp?FromHotlink=&AutoLogin=&UserLogID=&Season=0&Juniors=false&Schools=false&Website=1";
	/*
	    Username:RDomloge
		Password:Badmuthafucka0
		BrowserName:Google Chrome
		BrowserVersion:54.0.2840.71
		OperatingSystem:Mac OS X 10.10.5
		BrowserEngine:Web Kit
		BrowserEngineVersion:537.36
	 */
	@RequestMapping(value = "/secure/doLogin")
	public void doLogin(HttpServletRequest request, @RequestBody Login login) throws DaoException {
//		HttpSession session = request.getSession();
		String responsePage = Loader.sendLogin(url, login);
		System.out.println(responsePage);
	}
}
