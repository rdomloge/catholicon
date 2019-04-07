package catholicon.dao;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import catholicon.domain.PlayerReport;
import catholicon.ex.DaoException;
import catholicon.filter.ThreadLocalLoaderFilter;

@Component
public class PlayerReportDao {
	
	private static final String url = 
			"/BestPlayer.asp?LeagueTypeID=%1$s&Season=%2$s&Juniors=false&Schools=false&Website=1";
	
	public PlayerReport[] loadPlayerReport(String season, String league) throws DaoException {
		String formattedUrl = String.format(url, league, season);
		String page = ThreadLocalLoaderFilter.getLoader().load(formattedUrl);
		Document doc = Jsoup.parse(page);
		Elements dataRows = doc.select("#DataTable tr");
		Iterator<Element> dataRowItr = dataRows.iterator();
		List<PlayerReport> reports = new LinkedList<>();
		while(dataRowItr.hasNext()) {
			Element e = dataRowItr.next();
			String position = parsePosition(e.select("td:eq(0)").text());
			String playerName = e.select("td:eq(1)").text();
			String teamName = e.select("td:eq(2)").text();
			String division = e.select("td:eq(3)").text();
			String matchesPlayed = e.select("td:eq(4)").text();
			String rubbers = e.select("td:eq(5)").text();
			String rating = parseRating(e.select("td:eq(6)").text());
			String pointsDiff = e.select("td:eq(7)").text();
			PlayerReport report = new PlayerReport(position, playerName, teamName, division, 
					matchesPlayed, rubbers, rating, pointsDiff);
			reports.add(report);
		}
		
		return reports.toArray(new PlayerReport[reports.size()]);
	}
	
	private static String parseRating(String s) {
		if(s.indexOf('%') > 0) {
			return s.replace("%", "").trim();
		}
		
		return s.trim();
	}
	
	private String parsePosition(String s) {
		if (s.indexOf(')') > 0) {
			return s.substring(0, s.indexOf(')')).trim();
		}

		return s.trim();
	}
}
