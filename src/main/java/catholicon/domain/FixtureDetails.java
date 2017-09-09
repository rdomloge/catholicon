package catholicon.domain;

public class FixtureDetails {
	/*
	 * {=, 
		League:=Mixed, 
		Match Date:=Tue, 18 Oct 2016 , 
		No. of Courts=2, 
		Venue=Basingstoke Sports Centre, Festival Place Basingstoke Hants RG21 7LE, 
		Division:=Division 1, 
		Away Team:=Beechdown Mixed A, 
		Match Time:=19:40 , 
		Home Team:=BH Pegasus Mixed}
	 */
	
	private String matchDate;
	private String matchTime;
	private String homeTeam;
	private String awayTeam;
	private String numberOfCourts;
	private String venue;
	private String league;


	public FixtureDetails(String matchDate, String matchTime, String homeTeam, String awayTeam, String numberOfCourts,
			String venue, String league) {
		super();
		this.matchDate = matchDate;
		this.matchTime = matchTime;
		this.homeTeam = homeTeam;
		this.awayTeam = awayTeam;
		this.numberOfCourts = numberOfCourts;
		this.venue = venue;
		this.league = league;
	}

	public String getMatchDate() {
		return matchDate;
	}

	public String getMatchTime() {
		return matchTime;
	}

	public String getHomeTeam() {
		return homeTeam;
	}

	public String getAwayTeam() {
		return awayTeam;
	}

	public String getNumberOfCourts() {
		return numberOfCourts;
	}

	public String getVenue() {
		return venue;
	}

	public String getLeague() {
		return league;
	}

	@Override
	public String toString() {
		return "FixtureDetails [matchDate=" + matchDate + ", matchTime=" + matchTime + ", homeTeam=" + homeTeam
				+ ", awayTeam=" + awayTeam + ", numberOfCourts=" + numberOfCourts + ", venue=" + venue + ", league="
				+ league + "]";
	}
}
