package catholicon.dao;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import catholicon.domain.UpcomingFixture;
import catholicon.ex.DaoException;
import catholicon.parser.ParserUtil;

public class FrontPageDao {
	
	private static final String url = "http://bdbl.org.uk/Live/BdblHome.asp?Season=0&website=1";
	
	private static final Pattern p = Pattern.compile("\\{(..*)\\};");

	
	public List<UpcomingFixture> getUpcomingFixtures() throws DaoException {
		String page = Loader.load(url);
		
		Document doc = Jsoup.parse(page);
		Elements upcomingFixturesDiv = doc.select("#UpcomingFixturesDiv");
		Element javaScriptEl = upcomingFixturesDiv.select("center script").first();
		String javaScript = javaScriptEl.html();
		try(Scanner s = new Scanner(javaScript)) {
			List<UpcomingFixture> fixtures = new LinkedList<>();
			while(s.hasNext()) {
				String line = s.nextLine();
				Matcher m = p.matcher(line);
				while(m.find()) {
					String group = m.group();
					String json = group.substring(1, group.length()-2);
					String[] pairs = ParserUtil.splitOnUnquotedCommas(json);
					Map<String, String> map = ParserUtil.pairsToMap(pairs);
					UpcomingFixture fixture = new UpcomingFixture(
									map.get("homeTeamName"), 
									map.get("awayTeamName"), 
									ParserUtil.parseDate(map.get("matchDate")));
					fixtures.add(fixture);
				}
			}
			return fixtures;
		}
	}

	public static void main(String[] args) throws DaoException {
		FrontPageDao dao = new FrontPageDao();
		List<UpcomingFixture> frontPage = dao.getUpcomingFixtures();
		System.out.println(frontPage);
	}
}
