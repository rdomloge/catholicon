package catholicon.domain;

public abstract class Seasonal {

	private int season;

	public Seasonal(int season) {
		super();
		this.season = season;
	}

	
	public int getSeason() {
		return season;
	}
}
