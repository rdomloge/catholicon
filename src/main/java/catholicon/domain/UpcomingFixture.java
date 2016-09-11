package catholicon.domain;

public class UpcomingFixture {
	
	private String homeTeamName;
	
	private String awayTeamName;
	
	private String fixtureDate;

	
	public UpcomingFixture(String homeTeamName, String awayTeamName, String fixtureDate) {
		super();
		this.homeTeamName = homeTeamName;
		this.awayTeamName = awayTeamName;
		this.fixtureDate = fixtureDate;
	}

	public String getHomeTeamName() {
		return homeTeamName;
	}

	public String getAwayTeamName() {
		return awayTeamName;
	}

	public String getFixtureDate() {
		return fixtureDate;
	}

	@Override
	public String toString() {
		return "UpcomingFixture [homeTeamName=" + homeTeamName + ", awayTeamName=" + awayTeamName + ", fixtureDate="
				+ fixtureDate + "]";
	}
}
