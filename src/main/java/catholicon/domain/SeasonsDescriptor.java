package catholicon.domain;

import java.util.List;

public class SeasonsDescriptor {
	
	private List<Season> seasons;
	
	private int currentSeason;

	public SeasonsDescriptor(List<Season> seasons, int currentSeason) {
		super();
		this.seasons = seasons;
		this.currentSeason = currentSeason;
	}

	public List<Season> getSeasons() {
		return seasons;
	}

	public int getCurrentSeason() {
		return currentSeason;
	}
}
