package catholicon.domain;

public class Season {
	
	private int id;

	private int seasonStartYear;
	
	private int seasonEndYear;
	

	public Season(int id, int seasonStartYear, int seasonEndYear) {
		this.id = id;
		this.seasonStartYear = seasonStartYear;
		this.seasonEndYear = seasonEndYear;
	}

	public int getId() {
		return id;
	}

	public int getSeasonStartYear() {
		return seasonStartYear;
	}

	public int getSeasonEndYear() {
		return seasonEndYear;
	}

	@Override
	public String toString() {
		return "Season [id=" + id + ", seasonStartYear=" + seasonStartYear + ", seasonEndYear=" + seasonEndYear + "]";
	}


}
