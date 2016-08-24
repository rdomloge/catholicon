package catholicon.domain;

import java.util.Map;

public class MatchCard {
	
	private Rubber[] rubbers = new Rubber[9];
	
	public MatchCard(Map<Integer, Rubber> scoreMap) {
		for (Integer rubberNum : scoreMap.keySet()) {
			rubbers[rubberNum-1] = scoreMap.get(rubberNum);
		}
	}

	public Rubber[] getRubbers() {
		return rubbers;
	}

}
