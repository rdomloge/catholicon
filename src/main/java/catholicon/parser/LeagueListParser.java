package catholicon.parser;

import java.util.LinkedList;
import java.util.List;

import catholicon.domain.League;

public class LeagueListParser {
	
	private List<League> leagues = new LinkedList<>();

	public LeagueListParser(String data, int season) {
		String[] parts = data.split("\\},\\{");
		
		for (String part : parts) {
			if(part.startsWith("[")) part = part.substring(1);
			if(part.startsWith("{")) part = part.substring(1);
			String[] props = ParserUtil.splitOnUnquotedCommas(part);
			String label = null;
			int leagueTypeId = -1;
			for (String propertyKeyPair : props) {
				String[] splitProps = ParserUtil.splitOnUnquotedColons(propertyKeyPair);
				if("label".equalsIgnoreCase(splitProps[0])) {
					label = splitProps[1].replace("\"", "");
				}
				else if("leagueTypeId".equalsIgnoreCase(splitProps[0])) {
					leagueTypeId = Integer.parseInt(splitProps[1]);
				}
			}
			leagues.add(new League(label, leagueTypeId, season));
		}
	}

	public List<League> getLeagues() {
		return leagues;
	}

}
