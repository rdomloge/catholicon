package catholicon.domain;

public class League {

	private String label;
	
	private int leagueTypeId;

	public League(String label, int leagueTypeId) {
		super();
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