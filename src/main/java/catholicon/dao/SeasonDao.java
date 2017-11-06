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
	
	
	public Season[] loadSeasons() throws DaoException {
		String page = ThreadLocalLoaderFilter.getLoader().load(url);
		int firstSeason = find(page, firstSeasonPattern);
		int latestSeason = find(page, latestSeasonPattern);
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
