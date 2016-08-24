package catholicon.parser;

import java.util.LinkedList;
import java.util.List;

public class LeagueListParser {
	
	private List<String> leagues = new LinkedList<>();

	public LeagueListParser(String data) {
		String[] parts = data.split("\\},\\{");
		
		for (String part : parts) {
			if(part.startsWith("[")) part = part.substring(1);
			if(part.startsWith("{")) part = part.substring(1);
			String[] props = ParserUtil.splitOnUnquotedCommas(part);
			for (String propertyKeyPair : props) {
				String[] splitProps = ParserUtil.splitOnUnquotedColons(propertyKeyPair);
				if("label".equalsIgnoreCase(splitProps[0])) {
					leagues.add(splitProps[1].replace("\"", ""));
				}
			}
		}
	}

	public List<? extends String> getLeagues() {
		return leagues;
	}

}
