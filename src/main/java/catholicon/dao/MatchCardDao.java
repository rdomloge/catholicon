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
import catholicon.filter.ThreadLocalLoaderFilter;

public class MatchCardDao {
	
	private static final String urlTemplate = 
			"http://bdbl.org.uk/Live/MatchCard6.asp?FixtureID=%1$s&Juniors=false&Schools=false&Season=0&Website=1";
	private static final String NAME_WITHHELD = "Name withheld";
	
	public MatchCard load(String fixtureId) throws DaoException {
		String url = String.format(urlTemplate, fixtureId);
		
		String page = ThreadLocalLoaderFilter.getLoader().load(url);
		
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
		
		String[] awayPlayers = preFillNamesWithWitheld(new String[6]);
		String[] homePlayers = preFillNamesWithWitheld(new String[6]);
		Elements players = doc.select("span.Boxed[id*=Player]");
		for(int i=0; i < players.size(); i++) {
			Element player = players.get(i);
			String id = player.attr("id");
			String playerType = id.substring(0, id.indexOf("Player"));
			String playerNum = id.substring(id.indexOf("Player")+6);
			if("home".equalsIgnoreCase(playerType)) {
				homePlayers[Integer.parseInt(playerNum)] = player.text();
			}
			else {
				awayPlayers[Integer.parseInt(playerNum)] = player.text();
			}
		}
		
		String homeTeam = doc.select("#homeTeam").first().attr("value");
		String awayTeam = doc.select("#awayTeam").first().attr("value");
		
		String matchDate = doc.select("#matchDate").first().attr("value");
		String score = doc.select("#ScoreBoard").first().text();
		int hyphen = score.indexOf("-");
		int homeScore = Integer.parseInt(score.substring(0, hyphen).trim());
		int awayScore = Integer.parseInt(score.substring(hyphen+1).trim());
		
		Elements results = doc.select("input[id^=Result]");
		boolean[] homeTeamWins = new boolean[9];
		for(int i=0; i < results.size(); i++) {
			String result = results.get(i).attr("value");
			String rubberNumStr = results.get(i).attr("id");
			int rubberNum = Integer.parseInt(rubberNumStr.substring(rubberNumStr.indexOf("Result")+6)) - 1;
			homeTeamWins[rubberNum] = result.toLowerCase().contains("home") ? true : false;
		}
		
		return new MatchCard(scoreMap, homePlayers, awayPlayers, homeTeam, awayTeam, matchDate, 
				homeScore, awayScore, homeTeamWins);
	}
	
	private String[] preFillNamesWithWitheld(String[] empty) {
		for (int i = 0; i < empty.length; i++) {
			empty[i] = NAME_WITHHELD;
		}
		return empty;
	}

}
