package catholicon.dao;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import catholicon.domain.Match;
import catholicon.ex.DaoException;
import catholicon.filter.ThreadLocalLoaderFilter;

public class MatchDao {
	
	private static final String url = 
			"http://bdbl.org.uk/Live/TeamFixtureList.asp?ID=%1$s&Season=%2$s&Juniors=false&Schools=false&Website=1";
	
	private static final Pattern allMatchesDataLineRegExp = Pattern.compile("var data = (.*?)];");
	
	

	public Match[] load(int seasonStartYear, String team) throws DaoException {
		
		String page = ThreadLocalLoaderFilter.getLoader().load(String.format(url, team, seasonStartYear));
		
		Matcher dataLineMatcher = allMatchesDataLineRegExp.matcher(page);
		
		List<Match> matches = new LinkedList<>();
		
		while(dataLineMatcher.find()) {
			String dataLine = dataLineMatcher.group(1) + ']';
			String[] parts = dataLine.split("\\},\\{");
			for (String part : parts) {
				if(part.startsWith("[")) part = part.substring(1);
				part = part.substring(1, part.length());
				Match m = new Match(part);
				matches.add(m);
			}
		}
		
		return matches.toArray(new Match[matches.size()]);
	}
}
