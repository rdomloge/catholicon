package catholicon.domain;

public class DivisionDescriptor extends Seasonal {
	
	private String label;
	
	private int divisionId;
	
	private int leagueTypeId;
	

	public DivisionDescriptor(String label, int divisionId, int leagueTypeId, int season) {
		super(season);
		this.label = label;
		this.divisionId = divisionId;
		this.leagueTypeId = leagueTypeId;
	}

	public String getLabel() {
		return label;
	}

	public int getDivisionId() {
		return divisionId;
	}

	public int getLeagueTypeId() {
		return leagueTypeId;
	}
	
}
