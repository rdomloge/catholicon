package catholicon.domain;

public class Season {
	
	private int id;

	private int seasonStartYear;
	
	private int seasonEndYear;
	
	private int apiIdentifier;
	

	public Season(int id, int seasonStartYear, int seasonEndYear, boolean current) {
		this.id = id;
		this.seasonStartYear = seasonStartYear;
		this.seasonEndYear = seasonEndYear;
		this.apiIdentifier = current ? 0 : seasonStartYear;
	}

	public Season() {
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

	public int getApiIdentifier() {
		return apiIdentifier;
	}

	@Override
	public String toString() {
		return "Season [id=" + id + ", seasonStartYear=" + seasonStartYear + ", seasonEndYear=" + seasonEndYear
				+ ", apiIdentifier=" + apiIdentifier + "]";
	}
}
