package catholicon.dao;

import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import catholicon.domain.FixtureDetails;
import catholicon.ex.DaoException;

public class FixtureDao {

	private static final String url = 
			"http://bdbl.org.uk/Live/EditFixture.asp?Edit=false&HomeTeamID=228&AwayTeamID=215&FixtureID=%1$s&FixtureIndex=1&Juniors=false&Schools=false&Season=0&Website=1";
	
	public FixtureDetails load(int fixtureId) throws DaoException {
		String page = Loader.load(String.format(url, fixtureId));
		
		Document doc = Jsoup.parse(page);
		Map<String, String> map = parseRows(doc.select("table tr table tr"));
		return new FixtureDetails(
				map.get("Match Date:"), //matchDate
				map.get("Match Time:"), //matchTime
				map.get("Home Team:"), //homeTeam
				map.get("Away Team:"), //awayTeam
				Integer.parseInt(map.get("No. of Courts")), //numberOfCourts
				map.get("Venue"), //venue
				map.get("League:")); //league
	}

	private Map<String, String> parseRows(Elements rows) {
		Map<String, String> map = new HashMap<>();
		for (Element row : rows) {
			if(row.children().size() < 2) continue;
			String key = row.child(0).text();
			String val = row.child(1).text();
			map.put(key, val);
		}
		return map;
	}
}
