package catholicon.domain;

import java.util.List;

public class Club {
	
	private int clubId;
	private String clubName;
	private int seasonId;
	private String chairMan;
	private String chairManPhone;
	private String secretary;
	private String secretaryPhone;
	private String matchSec;
	private String matchSecPhone;
	private String treasurer;
	private String treasurerPhone;
	private String chairmanEmail;
	private String secretaryEmail;
	private String matchSecEmail;
	private String treasurerEmail;
	
	private List<Session> clubSessions;
	
	private List<Session> matchSessions;
	
	public List<Session> getClubSessions() {
		return clubSessions;
	}

	public void setClubSessions(List<Session> clubSessions) {
		this.clubSessions = clubSessions;
	}

	public List<Session> getMatchSessions() {
		return matchSessions;
	}

	public void setMatchSessions(List<Session> matchSessions) {
		this.matchSessions = matchSessions;
	}

	public Club(int clubId, String clubName, int seasonId) {
		super();
		this.clubId = clubId;
		this.clubName = clubName;
		this.seasonId = seasonId;
	}
	
	public int getClubId() {
		return clubId;
	}
	
	public String getClubName() {
		return clubName;
	}
	
	public int getSeasonId() {
		return seasonId;
	}

	public void fillOutRoles(String chairMan, String secretary, String matchSec, String treasurer) {
		this.chairMan = chairMan;
		this.secretary = secretary;
		this.matchSec = matchSec;
		this.treasurer = treasurer;
	}
	
	public void fillOutPhoneNumbers(String chairMan, String secretary, String matchSec, String treasurer) {
		this.chairManPhone = chairMan;
		this.secretaryPhone = secretary;
		this.matchSecPhone = matchSec;
		this.treasurerPhone = treasurer;
	}
	
	public void fillOutEmailAddresses(String chairmanEmail, String secretaryEmail, String matchSecEmail,
			String treasurerEmail) {
		
		this.chairmanEmail = chairmanEmail;
		this.secretaryEmail = secretaryEmail;
		this.matchSecEmail = matchSecEmail;
		this.treasurerEmail = treasurerEmail;
	}

	public String getChairmanEmail() {
		return chairmanEmail;
	}

	public String getSecretaryEmail() {
		return secretaryEmail;
	}

	public String getMatchSecEmail() {
		return matchSecEmail;
	}

	public String getTreasurerEmail() {
		return treasurerEmail;
	}

	public String getChairMan() {
		return chairMan;
	}

	public String getSecretary() {
		return secretary;
	}

	public String getMatchSec() {
		return matchSec;
	}

	public String getTreasurer() {
		return treasurer;
	}

	public String getChairManPhone() {
		return chairManPhone;
	}

	public String getSecretaryPhone() {
		return secretaryPhone;
	}

	public String getMatchSecPhone() {
		return matchSecPhone;
	}

	public String getTreasurerPhone() {
		return treasurerPhone;
	}
}