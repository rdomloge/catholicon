package catholicon.controller;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import catholicon.dao.Loader;
import catholicon.domain.Login;
import catholicon.domain.WelcomePageItem;
import catholicon.ex.DaoException;
import catholicon.parser.ParserUtil;

@RestController
public class LoginController {

	private static final String url = "http://bdbl.org.uk/Live/Login.asp?FromHotlink=&AutoLogin=&UserLogID=&Season=0&Juniors=false&Schools=false&Website=1";
	
	private static final String welcomeUrl = 
			"http://bdbl.org.uk/Live/WelcomePage.asp?Flags=31&Season=0&Juniors=false&Schools=false&Website=1";
	
	
	
	@RequestMapping("/secure/welcome")
	public List<WelcomePageItem> loadWelcomePage() throws DaoException {
		String welcomePage = Loader.load(welcomeUrl);
		return parseWelcomePage(welcomePage);
	}
	
//	var data =
//		{
//			login:true,
//			IsLoggedIn:true,
//			Username:"RDomloge",
//			DisplayName:"Ramsay Domloge",
//			UserLogID:1744,
//			UserID:24,
//			UserRolesInfo:{'1_28':{WebsiteID:1,UserRoleID:28,AdultClubList:[7],JuniorClubList:[],SchoolList:[],AdultLeagueList:[],JuniorLeagueList:[],SchoolLeagueList:[],AdultTeamList:[215],JuniorTeamList:[],SchoolTeamList:[]}},
//			PlayerID:211,
//			CanLockLogins:false,
//			CanChangePassword:true,
//			SysAdmin:false
//		};
	
	//<td colspan="3"><b>Invalid Username or Password</b></td>
	
	@RequestMapping(value = "/secure/doLogin")
	public ResponseEntity<Map<String, String>> doLogin(HttpServletRequest request, @RequestBody Login login) throws DaoException {
		
		String responsePage = Loader.sendLogin(url, login);
		
		if(responsePage.indexOf("Invalid Username or Password") > 0) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("IsLoggedIn", "false");
			return new ResponseEntity<>(map, HttpStatus.OK);
		}
			
		try(Scanner s = new Scanner(responsePage)) {
			StringBuilder buf = new StringBuilder();
			boolean started = false;
			while(true) {
				String line = s.nextLine().trim();
				if( ! started) {
					if(line.contains("var data =")) {
						started= true;
					}
					continue;
				}
				
				if(line.contains("};")) {
					break;
				}
				else if(line.trim().equals('{')) {
					continue;
				}
				buf.append(line);
			}
			//{login:true,IsLoggedIn:true,Username:"rdomloge",DisplayName:"Ramsay Domloge",UserLogID:1771,
			//UserID:24,UserRolesInfo:{'1_28':{WebsiteID:1,UserRoleID:28,AdultClubList:[7],JuniorClubList:[],
			//SchoolList:[],AdultLeagueList:[],JuniorLeagueList:[],SchoolLeagueList:[],AdultTeamList:[215],
			//JuniorTeamList:[],SchoolTeamList:[]}},PlayerID:211,CanLockLogins:false,CanChangePassword:true,//
			//SysAdmin:false
			Map<String, String> map = parse(buf.toString());
			if(map.containsKey("IsLoggedIn")) {
				boolean isLoggedIn = Boolean.parseBoolean(map.get("IsLoggedIn"));
				if(isLoggedIn) {
					return new ResponseEntity<Map<String,String>>(
							map, HttpStatus.OK);					
				}
			}
			
			return new ResponseEntity<Map<String,String>>(HttpStatus.FORBIDDEN);
		}
	}
	
	private Map<String, String> parse(String s) {
		String[] parts = ParserUtil.splitOnUnquotedCommas(s);
		return ParserUtil.pairsToMap(parts);
	}
	
	/**
		<script language="javascript" type="text/javascript">
			var fixtureInfo = {};
			fixtureInfo['215_223'] = {matchDate:new Date("27 Sep 2016"), fixtureID:1496, leagueTypeID:16, fixtureStatus:5, cardStatus:1, walkoverStatus:0, teamToAction:223, homeClubID:7, awayClubID:1, homeSchoolID:null, awaySchoolID:null, homeTeamID:215, awayTeamID:223, homeTeamName:"BH Pegasus Open A", awayTeamName:"Aldermaston Open", enterFullMatchCards:true, juniorLeague:false, schoolLeague:false};
		</script>
	 * @return 
	 * 
	 */
	private List<WelcomePageItem> parseWelcomePage(String responsePage) {
		
		List<WelcomePageItem> list = new LinkedList<>();
		
		try(Scanner s = new Scanner(responsePage)) {
			while(true && s.hasNext()) {
				String line = s.nextLine();
				if(line.trim().startsWith("fixtureInfo")) {
					list.add(parseFixtureInfo(line));
				}
			}
		}
		
		return list;
	}
	
	/**
	 * fixtureInfo['215_223'] = {
	 * 		matchDate:new Date("27 Sep 2016"), 
	 * 		fixtureID:1496, 
	 * 		leagueTypeID:16, 
	 * 		fixtureStatus:5, 
	 * 		cardStatus:1, 
	 * 		walkoverStatus:0, 
	 * 		teamToAction:223, 
	 * 		homeClubID:7, 
	 * 		awayClubID:1, 
	 * 		homeSchoolID:null, 
	 * 		awaySchoolID:null, 
	 * 		homeTeamID:215, 
	 * 		awayTeamID:223, 
	 * 		homeTeamName:"BH Pegasus Open A", 
	 * 		awayTeamName:"Aldermaston Open", 
	 * 		enterFullMatchCards:true, 
	 * 		juniorLeague:false, 
	 * 		schoolLeague:false
	 * 	};
	 */
	private WelcomePageItem parseFixtureInfo(String line) {
		String[] pairs = 
				ParserUtil.splitOnUnquotedCommas(line.substring(line.indexOf('{'), line.lastIndexOf('}')));
		
		Map<String, String> map = 
				ParserUtil.pairsToMap(pairs);

		WelcomePageItem item = new WelcomePageItem(
				map.get("matchDate"), 
				map.get("awayTeamName"), 
				map.get("homeTeamName"),
				Integer.parseInt(map.get("fixtureStatus")),
				Integer.parseInt(map.get("cardStatus")));
		
		return item;
	}
}
