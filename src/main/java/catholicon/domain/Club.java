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
	private PhoneNumber[] chairManPhone;
	private String secretary;
	private PhoneNumber[] secretaryPhone;
	private String matchSec;
	private PhoneNumber[] matchSecPhone;
	private String treasurer;
	private PhoneNumber[] treasurerPhone;
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
		this.chairMan = ParserUtil.nullIfEmpty(chairMan);
		this.secretary = ParserUtil.nullIfEmpty(secretary);
		this.matchSec = ParserUtil.nullIfEmpty(matchSec);
		this.treasurer = ParserUtil.nullIfEmpty(treasurer);
	}
	
	public void fillOutPhoneNumbers(PhoneNumber[] chairManPhone, PhoneNumber[] secretaryPhone, PhoneNumber[] matchSecPhone, PhoneNumber[] treasurerPhone) {
		if(chairManPhone.length > 0) this.chairManPhone = chairManPhone;
		if(secretaryPhone.length > 0) this.secretaryPhone = secretaryPhone;
		if(matchSecPhone.length > 0) this.matchSecPhone = matchSecPhone;
		if(treasurerPhone.length > 0) this.treasurerPhone = treasurerPhone;
	}
	
	public void fillOutEmailAddresses(String chairmanEmail, String secretaryEmail, String matchSecEmail,
			String treasurerEmail) {
		
		this.chairmanEmail = ParserUtil.nullIfEmpty(chairmanEmail);
		this.secretaryEmail = ParserUtil.nullIfEmpty(secretaryEmail);
		this.matchSecEmail = ParserUtil.nullIfEmpty(matchSecEmail);
		this.treasurerEmail = ParserUtil.nullIfEmpty(treasurerEmail);
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

	public PhoneNumber[] getChairManPhone() {
		return chairManPhone;
	}

	public PhoneNumber[] getSecretaryPhone() {
		return secretaryPhone;
	}

	public PhoneNumber[] getMatchSecPhone() {
		return matchSecPhone;
	}

	public PhoneNumber[] getTreasurerPhone() {
		return treasurerPhone;
	}
}
