package catholicon.domain;

public class PlayerReport {

	private String position; 
	private String playerName; 
	private String teamName;
	private String division; 
	private String matchesPlayed;
	private String rubbers;
	private String rating;
	private String pointsDiff;
	
	public PlayerReport(String position, String playerName, String teamName, String division, String matchesPlayed,
			String rubbers, String rating, String pointsDiff) {
		super();
		this.position = position;
		this.playerName = playerName;
		this.teamName = teamName;
		this.division = division;
		this.matchesPlayed = matchesPlayed;
		this.rubbers = rubbers;
		this.rating = rating;
		this.pointsDiff = pointsDiff;
	}

	public String getPosition() {
		return position;
	}

	public String getPlayerName() {
		return playerName;
	}

	public String getTeamName() {
		return teamName;
	}

	public String getDivision() {
		return division;
	}

	public String getMatchesPlayed() {
		return matchesPlayed;
	}

	public String getRubbers() {
		return rubbers;
	}

	public String getRating() {
		return rating;
	}

	public String getPointsDiff() {
		return pointsDiff;
	}

	@Override
	public String toString() {
		return "PlayerReport [position=" + position + ", playerName=" + playerName + ", teamName=" + teamName
				+ ", division=" + division + ", matchesPlayed=" + matchesPlayed + ", rubbers=" + rubbers + ", rating="
				+ rating + ", pointsDiff=" + pointsDiff + "]";
	}

}
