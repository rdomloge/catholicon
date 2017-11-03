package catholicon.domain;

import java.util.List;

import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import catholicon.parser.ParserUtil;

@JsonInclude(Include.NON_NULL)
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

	public void setClubName(String clubName) {
		this.clubName = clubName;
	}

	public Club(int clubId, String clubName, int seasonId) {
		this.clubId = clubId;
		this.clubName = clubName;
		this.seasonId = seasonId;
	}
	
	public Club(int clubId, int seasonId) {
		this.clubId = clubId;
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
		this.chairMan = nullIfEmpty(chairMan);
		this.secretary = nullIfEmpty(secretary);
		this.matchSec = nullIfEmpty(matchSec);
		this.treasurer = nullIfEmpty(treasurer);
	}
	
	public void fillOutPhoneNumbers(String chairManPhone, String secretaryPhone, String matchSecPhone, String treasurerPhone) {
		this.chairManPhone = nullIfEmpty(chairManPhone);
		this.secretaryPhone = nullIfEmpty(secretaryPhone);
		this.matchSecPhone = nullIfEmpty(matchSecPhone);
		this.treasurerPhone = nullIfEmpty(treasurerPhone);
	}
	
	public void fillOutEmailAddresses(String chairmanEmail, String secretaryEmail, String matchSecEmail,
			String treasurerEmail) {
		
		this.chairmanEmail = nullIfEmpty(chairmanEmail);
		this.secretaryEmail = nullIfEmpty(secretaryEmail);
		this.matchSecEmail = nullIfEmpty(matchSecEmail);
		this.treasurerEmail = nullIfEmpty(treasurerEmail);
	}
	
	private static String nullIfEmpty(String s) {
		if(null == s || StringUtils.isEmpty(ParserUtil.trim(s))) return null;
		return s;
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
