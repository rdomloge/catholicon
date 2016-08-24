package catholicon.dao;

import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import catholicon.domain.MatchCard;
import catholicon.domain.Rubber;
import catholicon.ex.DaoException;

public class MatchCardDao {
	
	private static final String urlTemplate = 
			"http://bdbl.org.uk/Live/MatchCard6.asp?FixtureID=%1$s&Juniors=false&Schools=false&Season=0&Website=1";
	
	public MatchCard load(String fixtureId) throws DaoException {
		String url = String.format(urlTemplate, fixtureId);
		
		String page = Loader.load(url);
		
		Document doc = Jsoup.parse(page);
		Elements scores = doc.select("span.Boxed[id^=Score]");
		Map<Integer, Rubber> scoreMap = new HashMap<>();
		
		for(int i=0; i < scores.size(); i++) {
			Element scoreEl = scores.get(i);
			String id = scoreEl.attr("id").substring(5);
			String score = scoreEl.text();
			boolean homeScore = id.startsWith("H");
			int gameNum = Integer.parseInt(id.substring(id.indexOf("_")+1));
			int rubberNum = Integer.parseInt(id.substring(1, id.indexOf("_")));
			if(! scoreMap.containsKey(rubberNum)) {
				scoreMap.put(rubberNum, new Rubber());
			}
			
			Rubber r = scoreMap.get(rubberNum);
			r.setGame(gameNum, homeScore, score);
			
		}
		return new MatchCard(scoreMap);
	}

	
	public static void main(String[] args) throws DaoException {
		MatchCardDao mcd = new MatchCardDao();
		mcd.load("1234");
	}
}
