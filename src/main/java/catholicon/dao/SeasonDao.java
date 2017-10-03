package catholicon.dao;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import catholicon.domain.Season;
import catholicon.ex.DaoException;
import catholicon.filter.ThreadLocalLoaderFilter;

@Component
public class SeasonDao {
	
	private static final String url = "/Live/Main.asp";
	
	private static final Pattern firstSeasonPattern = Pattern.compile("var firstSeason = (.*?);");
	private static final Pattern latestSeasonPattern = Pattern.compile("var latestSeason = (.*?);");
	private static final Pattern currentSeasonPattern = Pattern.compile("var currSeason = (.*?);");
	
	
	public Season[] loadSeasons() throws DaoException {
//		http://bdbl.org.uk/Live/Main.asp?Website=1&Browser=Google%20Chrome&Version=52.0.2743.116&OS=Mac%20OS%20X%2010.10.5&Engine=Web%20Kit&EngineVersion=537.36

//		var firstSeason = 2012;
		
//		var season = 0;
//		var currSeason = 2016;
//		var currSeasonEnd = 2017;
//		var latestSeason = 2016;
//		var nextSeason = 2016;
		
		
		String page = ThreadLocalLoaderFilter.getLoader().load(url);
		int firstSeason = find(page, firstSeasonPattern);
		int latestSeason = find(page, latestSeasonPattern);
		int currentSeason = find(page, currentSeasonPattern);
		List<Season> seasons = new LinkedList<>();
		
		for(int i=0; i <= latestSeason-firstSeason; i++) {
			int seasonStartYear = latestSeason - i;
			int seasonEndYear = seasonStartYear + 1;
			Season s = new Season(i, seasonStartYear, seasonEndYear, latestSeason == seasonStartYear);
			seasons.add(s);
		}
		
		return seasons.toArray(new Season[seasons.size()]);
	}

	private int find(String page, Pattern p) throws DaoException {
		Matcher m = p.matcher(page);
		if(m.find()) {
			String season = m.group(1);
			return Integer.parseInt(season);	
		}
		throw new DaoException("Could not find season for "+p.pattern());
	}
}
