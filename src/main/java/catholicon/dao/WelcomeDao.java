package catholicon.dao;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.springframework.stereotype.Component;

import catholicon.domain.WelcomePageItem;
import catholicon.ex.DaoException;
import catholicon.filter.ThreadLocalLoaderFilter;
import catholicon.parser.ParserUtil;

@Component
public class WelcomeDao {
	
	private static final String welcomeUrl = "/WelcomePage.asp?Flags=31&Season=0&Juniors=false&Schools=false&Website=1";

	private static final String url = "/Login.asp?FromHotlink=&AutoLogin=&UserLogID=&Season=0&Juniors=false&Schools=false&Website=1";
	
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
}
