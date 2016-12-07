package catholicon.dao;

import java.util.LinkedList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import catholicon.domain.Division;
import catholicon.domain.Division.TeamPosition;
import catholicon.domain.DivisionDescriptor;
import catholicon.ex.DaoException;
import catholicon.filter.ThreadLocalLoaderFilter;

public class DivisionDao {
	
	private static final String url = 
			"http://bdbl.org.uk/Live/Leagues.asp?LeagueTypeID=%1$s&CompetitionStyle=0&Season=%2$s&Juniors=false&Schools=false&Website=1";
	
	private static String leagueUrl = 
			"http://bdbl.org.uk/Live/Division.asp?LeagueTypeID=%1$s&Division=%2$s&Season=%3$s&Juniors=false&Schools=false&Website=1";
	
	public List<DivisionDescriptor> getDivisionsForLeague(int leagueTypeId, int seasonStartYear) throws DaoException {
		
		String page = ThreadLocalLoaderFilter.getLoader().load(String.format(url, leagueTypeId, seasonStartYear));
		List<DivisionDescriptor> divisions = new LinkedList<>();
		
		Document doc = Jsoup.parse(page);
		Elements divs = doc.select("#Divs option");
		for (Element option : divs) {
			String label = option.text();
			int id = Integer.parseInt(option.attr("value"));
			if(0 == id) continue;
			divisions.add(new DivisionDescriptor(label, id, leagueTypeId, seasonStartYear));
		}
		
		return divisions;
	}

	public Division load(String leagueTypeId, int divisionId, int seasonStartYear) throws DaoException {
		
		String page = ThreadLocalLoaderFilter.getLoader().load(String.format(leagueUrl, leagueTypeId, divisionId, seasonStartYear));
		
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
			// showTeamFixtures(null,242);
			String teamIdAttr = cells.select("[onclick]").attr("onclick");
			int teamId = Integer.parseInt(
					teamIdAttr.substring(
							teamIdAttr.indexOf(',')+1, 
							teamIdAttr.indexOf(')')
							));
			teamPositions.add(new TeamPosition(teamName, teamId, played, matchesWon, rubbersWon, rubbersLost, gamesWon, gamesLost, pointsFor));
		}
		
		return new Division(teamPositions.toArray(new TeamPosition[teamPositions.size()]), seasonStartYear, Integer.parseInt(leagueTypeId));
	}
}
