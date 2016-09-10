package catholicon.domain;

public class League extends Seasonal {

	private String label;
	
	private int leagueTypeId;

	public League(String label, int leagueTypeId, int season) {
		super(season);
		this.label = label;
		this.leagueTypeId = leagueTypeId;
	}

	public String getLabel() {
		return label;
	}

	public int getLeagueTypeId() {
		return leagueTypeId;
	}
	
	
}
