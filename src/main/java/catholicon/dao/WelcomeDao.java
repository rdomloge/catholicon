package catholicon.dao;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import catholicon.domain.Login;
import catholicon.domain.WelcomePageItem;
import catholicon.ex.DaoException;
import catholicon.filter.ThreadLocalLoaderFilter;
import catholicon.parser.ParserUtil;

public class WelcomeDao {
	
	private static final String welcomeUrl = "http://bdbl.org.uk/Live/WelcomePage.asp?Flags=31&Season=0&Juniors=false&Schools=false&Website=1";

	private static final String url = "http://bdbl.org.uk/Live/Login.asp?FromHotlink=&AutoLogin=&UserLogID=&Season=0&Juniors=false&Schools=false&Website=1";
	
	public List<WelcomePageItem> loadWelcomePage() throws DaoException {
		String welcomePage = ThreadLocalLoaderFilter.getLoader().load(welcomeUrl);
		return parseWelcomePage(welcomePage);
	}

	/**
	 * <script language="javascript" type="text/javascript"> var fixtureInfo =
	 * {}; fixtureInfo['215_223'] = {matchDate:new Date("27 Sep 2016"),
	 * fixtureID:1496, leagueTypeID:16, fixtureStatus:5, cardStatus:1,
	 * walkoverStatus:0, teamToAction:223, homeClubID:7, awayClubID:1,
	 * homeSchoolID:null, awaySchoolID:null, homeTeamID:215, awayTeamID:223,
	 * homeTeamName:"BH Pegasus Open A", awayTeamName:"Aldermaston Open",
	 * enterFullMatchCards:true, juniorLeague:false, schoolLeague:false};
	 * </script>
	 * 
	 * @return
	 * 
	 */
	private List<WelcomePageItem> parseWelcomePage(String responsePage) {

		List<WelcomePageItem> list = new LinkedList<>();

		try (Scanner s = new Scanner(responsePage)) {
			while (true && s.hasNext()) {
				String line = s.nextLine();
				if (line.trim().startsWith("fixtureInfo")) {
					list.add(parseFixtureInfo(line));
				}
			}
		}

		return list;
	}

	/**
	 * fixtureInfo['215_223'] = { matchDate:new Date("27 Sep 2016"),
	 * fixtureID:1496, leagueTypeID:16, fixtureStatus:5, cardStatus:1,
	 * walkoverStatus:0, teamToAction:223, homeClubID:7, awayClubID:1,
	 * homeSchoolID:null, awaySchoolID:null, homeTeamID:215, awayTeamID:223,
	 * homeTeamName:"BH Pegasus Open A", awayTeamName:"Aldermaston Open",
	 * enterFullMatchCards:true, juniorLeague:false, schoolLeague:false };
	 */
	private WelcomePageItem parseFixtureInfo(String line) {
		String[] pairs = ParserUtil.splitOnUnquotedCommas(line.substring(line.indexOf('{')+1, line.lastIndexOf('}')));

		Map<String, String> map = ParserUtil.pairsToMap(pairs);
		
		String teamToActionId = map.get("teamToAction");
		String homeTeamId = map.get("homeTeamID");
		String teamToAction = homeTeamId.equals(teamToActionId) ? map.get("homeTeamName") : map.get("awayTeamName");

		WelcomePageItem item = new WelcomePageItem(ParserUtil.parseDate(map.get("matchDate")), map.get("awayTeamName"),
				map.get("homeTeamName"), Integer.parseInt(map.get("fixtureStatus")),
				Integer.parseInt(map.get("cardStatus")), teamToAction);

		return item;
	}
	
	
//	var data =
//	{
//		login:true,
//		IsLoggedIn:true,
//		Username:"RDomloge",
//		DisplayName:"Ramsay Domloge",
//		UserLogID:1744,
//		UserID:24,
//		UserRolesInfo:{'1_28':{WebsiteID:1,UserRoleID:28,AdultClubList:[7],JuniorClubList:[],SchoolList:[],AdultLeagueList:[],JuniorLeagueList:[],SchoolLeagueList:[],AdultTeamList:[215],JuniorTeamList:[],SchoolTeamList:[]}},
//		PlayerID:211,
//		CanLockLogins:false,
//		CanChangePassword:true,
//		SysAdmin:false
//	};

//<td colspan="3"><b>Invalid Username or Password</b></td>
	public Map<String,String> login(Login login) throws AuthenticationException, DaoException {
		String responsePage = ThreadLocalLoaderFilter.getLoader().sendLogin(url, login);
		
		if(responsePage.indexOf("Invalid Username or Password") > 0) {
			throw new AuthenticationException();
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
					return map;					
				}
			}
			
			throw new AuthenticationException();
		}
	}
	
	private Map<String, String> parse(String s) {
		String[] parts = ParserUtil.splitOnUnquotedCommas(s);
		return ParserUtil.pairsToMap(parts);
	}
	
}
