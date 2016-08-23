package catholicon.dao;

import java.util.LinkedList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import catholicon.domain.League;
import catholicon.domain.League.TeamPosition;
import catholicon.ex.DaoException;

public class LeagueDao {
	
	private static String open1url = 
			"http://bdbl.org.uk/Live/Division.asp?LeagueTypeID=13&Division=26&Season=0&Juniors=false&Schools=false&Website=1";
	
	private static final String mixed2Url = 
			"http://bdbl.org.uk/Live/Division.asp?LeagueTypeID=15&Division=32&Season=0&Juniors=false&Schools=false&Website=1";

	private static final String open2url = 
			"http://bdbl.org.uk/Live/Division.asp?LeagueTypeID=13&Division=27&Season=0&Juniors=false&Schools=false&Website=1";

	private static final String open3url = 
			"http://bdbl.org.uk/Live/Division.asp?LeagueTypeID=13&Division=28&Season=0&Juniors=false&Schools=false&Website=1";

	private static final String mixed1Url = 
			"http://bdbl.org.uk/Live/Division.asp?LeagueTypeID=15&Division=31&Season=0&Juniors=false&Schools=false&Website=1";

	public League load(String div) throws DaoException {
		
		String url = null;
		
		switch(div) {
			case "open1":
				url = open1url;
				break;
			case "open2":
				url = open2url;
				break;
			case "open3":
				url = open3url;
				break;
			case "mixed1":
				url = mixed1Url;
				break;
			case "mixed2":
				url = mixed2Url;
				break;
		}
		
		String page = Loader.load(url);
		
		Document doc = Jsoup.parse(page);
		
		List<TeamPosition> teamPositions = new LinkedList<>();
		
		Elements mainTable = doc.select("table[id$=MainTable]");
		Elements rows = mainTable.select("tr:has(td[class$=LeagueCell])");
		for(int i=0; i < rows.size(); i++) {
			Element teamPosition = rows.get(i);
			Elements cells = teamPosition.select("td[class$=LeagueCell]");
			String teamName = cells.select("td[id^=Team]").text();
			String played = cells.select("td[id^=Played]").text();
			String matchesWon = cells.select("td[id^=MatchesWon]").text();
			String rubbersWon = cells.select("td[id^=RubbersWon]").text();
			String rubbersLost = cells.select("td[id^=RubbersLost]").text();
			String gamesWon = cells.select("td[id^=GamesWon]").text();
			String gamesLost = cells.select("td[id^=GamesLost]").text();
			String pointsFor = cells.select("td[id^=PointsFor]").text();
			teamPositions.add(new TeamPosition(teamName, played, matchesWon, rubbersWon, rubbersLost, gamesWon, gamesLost, pointsFor));
		}
		
		return new League(teamPositions.toArray(new TeamPosition[teamPositions.size()]));
	}
}
