package catholicon.dao;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import catholicon.domain.League;
import catholicon.ex.DaoException;
import catholicon.parser.LeagueListParser;

public class LeagueDao {
	
	private static String listUrl = "http://bdbl.org.uk/Live/Leagues.asp?Season=%1$s&website=1";
	
	private static final Pattern allLeagueRegExp = Pattern.compile("var leagueMenu = (.*?)];");
	
	
	//var leagueMenu = [{label:"Ladies 4",leagueTypeID:14,action:changeLeague},{label:"Mixed",leagueTypeID:15,action:changeLeague},{label:"Open",leagueTypeID:13,action:changeLeague}];
	public List<League> list(int seasonStartYear) throws DaoException {
		String page = Loader.load(String.format(listUrl, seasonStartYear));
		Matcher m = allLeagueRegExp.matcher(page);
		if(m.find()) {
			String group = m.group(1) + ']';
			LeagueListParser parser = new LeagueListParser(group);
			return parser.getLeagues();
		}
		
		throw new RuntimeException("Could not find league names data");
	}
}
