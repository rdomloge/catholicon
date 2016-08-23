package catholicon.dao;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import catholicon.domain.Match;
import catholicon.ex.DaoException;
import catholicon.parser.MatchParser;

public class MatchDao {
	
	private static final String openaurl = 
			"http://bdbl.org.uk/Live/TeamFixtureList.asp?ID=164&Season=0&Juniors=false&Schools=false&Website=1";
	
	private static final String mixedaurl = 
			"http://bdbl.org.uk/Live/TeamFixtureList.asp?ID=203&Season=0&Juniors=false&Schools=false&Website=1";
	
	private static final Pattern allMatchesDataLineRegExp = Pattern.compile("var data = (.*?)];");
	

	public Match[] load(String team) throws DaoException {
		String url = null;
		
		switch(team) {
			case "opena":
				url = openaurl;
				break;
			case "mixeda":
				url = mixedaurl;
				break;
			default:
				throw new IllegalArgumentException("Unknown team: "+team);
		}
		
		String page = Loader.load(url);
		
		Matcher dataLineMatcher = allMatchesDataLineRegExp.matcher(page);
		
		List<Match> matches = new LinkedList<>();
		while(dataLineMatcher.find()) {
			String dataLine = dataLineMatcher.group(1) + ']';
			MatchParser parser = new MatchParser(dataLine);
			matches.addAll(parser.getMatches());
		}
		
		return matches.toArray(new Match[matches.size()]);
	}
}
