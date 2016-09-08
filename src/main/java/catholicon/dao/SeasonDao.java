package catholicon.dao;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import catholicon.domain.Season;
import catholicon.ex.DaoException;

public class SeasonDao {
	
	private static final String url = "http://bdbl.org.uk/Live/Main.asp";
	
	private static final Pattern firstSeasonPattern = Pattern.compile("var firstSeason = (.*?);");
	private static final Pattern latestSeasonPattern = Pattern.compile("var latestSeason = (.*?);");
	
	public List<Season> loadSeasons() throws DaoException {
//		http://bdbl.org.uk/Live/Main.asp?Website=1&Browser=Google%20Chrome&Version=52.0.2743.116&OS=Mac%20OS%20X%2010.10.5&Engine=Web%20Kit&EngineVersion=537.36

//		var firstSeason = 2012;
		
//		var season = 0;
//		var currSeason = 2016;
//		var currSeasonEnd = 2017;
//		var latestSeason = 2016;
//		var nextSeason = 2016;
		
		String page = Loader.load(url);
		int firstSeason = find(page, firstSeasonPattern);
		int latestSeason = find(page, latestSeasonPattern);
		List<Season> seasons = new LinkedList<>();
		
		for(int i=0; i <= latestSeason-firstSeason; i++) {
			int seasonStartYear = latestSeason - i;
			int seasonEndYear = seasonStartYear + 1;
			Season s = new Season(i, seasonStartYear, seasonEndYear);
			seasons.add(s);
		}
		
		return seasons;
	}
	
	public static void main(String[] args) throws DaoException {
		SeasonDao sd = new SeasonDao();
		sd.loadSeasons();
	}
	
	private int find(String page, Pattern p) {
		Matcher m = p.matcher(page);
		if(m.find()) {
			String season = m.group(1);
			return Integer.parseInt(season);	
		}
		throw new RuntimeException("Could not find season for "+p.pattern());
	}
}
