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
import org.springframework.stereotype.Component;

import catholicon.domain.UpcomingFixture;
import catholicon.ex.DaoException;
import catholicon.filter.ThreadLocalLoaderFilter;
import catholicon.parser.ParserUtil;

@Component
public class FrontPageDao {
	
	private static final String url = "/BdblHome.asp?Season=0&website=1";
	
	private static final Pattern p = Pattern.compile("\\{(..*)\\};");

	
	public List<UpcomingFixture> getUpcomingFixtures() throws DaoException {
		String page = ThreadLocalLoaderFilter.getLoader().load(url);
		
		Document doc = Jsoup.parse(page);
		Elements upcomingFixturesDiv = doc.select("#UpcomingFixturesDiv");
		if(null == upcomingFixturesDiv) {
			throw new DaoException("Page does not have #UpcomingFixturesDiv");
		}
		Element javaScriptEl = upcomingFixturesDiv.select("center script").first();
		if(null == javaScriptEl) {
			throw new DaoException("Page missing 'center script'");
		}
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
									ParserUtil.parseDate(map.get("matchDate")),
									Integer.parseInt(map.get("fixtureID")));
					fixtures.add(fixture);
				}
			}
			return fixtures;
		}
	}

}
