package catholicon.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import catholicon.parser.ParserUtil;

@JsonInclude(Include.NON_NULL)
public class Club {
	
	private int clubId;
	private String clubName;
	private int seasonId;
	private Contact chairMan;
	private Contact secretary;
	private Contact matchSec;
	private Contact treasurer;
	
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
		this.chairMan = null != ParserUtil.nullIfEmpty(chairMan) ? new Contact(chairMan) : null;
		this.secretary = null != ParserUtil.nullIfEmpty(secretary) ? new Contact(secretary) : null;
		this.matchSec = null != ParserUtil.nullIfEmpty(matchSec) ? new Contact(matchSec) : null;
		this.treasurer = null != ParserUtil.nullIfEmpty(treasurer) ? new Contact(treasurer) : null;
	}
	
	public void fillOutPhoneNumbers(PhoneNumber[] chairManPhone, PhoneNumber[] secretaryPhone, PhoneNumber[] matchSecPhone, PhoneNumber[] treasurerPhone) {
		if(chairManPhone.length > 0) chairMan.setContactNumbers(chairManPhone);
		if(secretaryPhone.length > 0) secretary.setContactNumbers(secretaryPhone);
		if(matchSecPhone.length > 0) matchSec.setContactNumbers(matchSecPhone);
		if(treasurerPhone.length > 0) treasurer.setContactNumbers(treasurerPhone);
	}
	
	public void fillOutEmailAddresses(String chairmanEmail, String secretaryEmail, String matchSecEmail,
			String treasurerEmail) {
		
		if(null != chairMan) {
			chairMan.setEmail(null != ParserUtil.nullIfEmpty(chairmanEmail) ? chairmanEmail : null);
		}
		if(null != secretary) {
			secretary.setEmail(null != ParserUtil.nullIfEmpty(secretaryEmail) ? secretaryEmail : null);
		}
		if(null != matchSec) {
			matchSec.setEmail(null != ParserUtil.nullIfEmpty(matchSecEmail) ? matchSecEmail : null);
		}
		if(null != treasurer) {
			treasurer.setEmail(null != ParserUtil.nullIfEmpty(treasurerEmail) ? treasurerEmail : null);
		}
	}

	public Contact getChairMan() {
		return chairMan;
	}

	public Contact getSecretary() {
		return secretary;
	}

	public Contact getMatchSec() {
		return matchSec;
	}

	public Contact getTreasurer() {
		return treasurer;
	}
	
}
