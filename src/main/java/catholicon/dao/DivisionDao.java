package catholicon.dao;

import java.util.LinkedList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import catholicon.domain.DivisionDescriptor;
import catholicon.ex.DaoException;
import catholicon.filter.ThreadLocalLoaderFilter;

@Component
public class DivisionDao {
	
	public static final String url = 
			"/Leagues.asp?LeagueTypeID=%1$s&CompetitionStyle=0&Season=%2$s&Juniors=false&Schools=false&Website=1";
	
	public static String leagueUrl = 
			"/Division.asp?LeagueTypeID=%1$s&Division=%2$s&Season=%3$s&Juniors=false&Schools=false&Website=1";
	
	public List<DivisionDescriptor> getDivisionsForLeague(int leagueTypeId, int seasonStartYear) throws DaoException {
		
		String page = ThreadLocalLoaderFilter.getLoader().load(String.format(url, leagueTypeId, seasonStartYear));
		List<DivisionDescriptor> divisions = new LinkedList<>();
		
		Document doc = Jsoup.parse(page);
		Elements divs = doc.select("#Divs option");
		if(divs.size() == 0) {
			throw new DaoException("No divisions found for "+leagueTypeId+ " in season "+seasonStartYear);
		}
		for (Element option : divs) {
			String label = option.text();
			int id = Integer.parseInt(option.attr("value"));
			if(0 == id) continue;
			divisions.add(new DivisionDescriptor(label, id, leagueTypeId, seasonStartYear));
		}
		
		return divisions;
	}
}
