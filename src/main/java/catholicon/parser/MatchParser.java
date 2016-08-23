package catholicon.parser;

import java.util.LinkedList;
import java.util.List;

import catholicon.domain.Match;

public class MatchParser {
	
	private List<Match> matches = new LinkedList<>();

	public MatchParser(String data) {
		String[] parts = data.split("\\},\\{");
		
		for (String part : parts) {
			if(part.startsWith("[")) part = part.substring(1);
			part = part.substring(1, part.length());
			Match m = new Match(part);
			matches.add(m);
		}
	}

	public List<Match> getMatches() {
		return matches;
	}

}
