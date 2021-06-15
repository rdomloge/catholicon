package catholicon.dto;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;



public class Club {
    
    private int clubId;

    @NotNull
    @Pattern(regexp = "[a-zA-Z ]{3,}") // any case, spaces, at least 3 characters
	private String clubName;

    private int seasonId;
    
    private Contact chairMan;
	private Contact secretary;
	private Contact matchSec;
	private Contact treasurer;
	
    private List<Team> teams;
	private List<Session> clubSessions;
	private List<Session> matchSessions;

    public Club() {
    }

    public Club(int clubId, @NotNull @Pattern(regexp = "[a-zA-Z ]{3,}") String clubName, int seasonId,
            Contact chairMan, Contact secretary, Contact matchSec, Contact treasurer, List<Session> clubSessions,
            List<Session> matchSessions, List<Team> teams) {
        this.clubId = clubId;
        this.clubName = clubName;
        this.seasonId = seasonId;
        this.chairMan = chairMan;
        this.secretary = secretary;
        this.matchSec = matchSec;
        this.treasurer = treasurer;
        this.clubSessions = clubSessions;
        this.matchSessions = matchSessions;
        this.teams = teams;
    }

    public void fillOutRoles(String chairMan, String secretary, String matchSec, String treasurer) {
		this.chairMan = null != CommonUtil.nullIfEmpty(chairMan) ? new Contact(chairMan) : null;
		this.secretary = null != CommonUtil.nullIfEmpty(secretary) ? new Contact(secretary) : null;
		this.matchSec = null != CommonUtil.nullIfEmpty(matchSec) ? new Contact(matchSec) : null;
		this.treasurer = null != CommonUtil.nullIfEmpty(treasurer) ? new Contact(treasurer) : null;
	}
	
	public void fillOutPhoneNumbers(PhoneNumber[] chairManPhone, PhoneNumber[] secretaryPhone, PhoneNumber[] matchSecPhone, PhoneNumber[] treasurerPhone) {
		if(chairManPhone.length > 0) chairMan.setContactNumbers(Arrays.asList(chairManPhone));
		if(secretaryPhone.length > 0) secretary.setContactNumbers(Arrays.asList(secretaryPhone));
		if(matchSecPhone.length > 0) matchSec.setContactNumbers(Arrays.asList(matchSecPhone));
		if(treasurerPhone.length > 0) treasurer.setContactNumbers(Arrays.asList(treasurerPhone));
	}

    public void linkTeam(Team t) {
        if(null == teams) teams = new LinkedList<>();
        this.teams.add(t);
    }
	
	public void fillOutEmailAddresses(String chairmanEmail, String secretaryEmail, String matchSecEmail,
			String treasurerEmail) {
		
		if(null != chairMan) {
			chairMan.setEmail(null != CommonUtil.nullIfEmpty(chairmanEmail) ? chairmanEmail : null);
		}
		if(null != secretary) {
			secretary.setEmail(null != CommonUtil.nullIfEmpty(secretaryEmail) ? secretaryEmail : null);
		}
		if(null != matchSec) {
			matchSec.setEmail(null != CommonUtil.nullIfEmpty(matchSecEmail) ? matchSecEmail : null);
		}
		if(null != treasurer) {
			treasurer.setEmail(null != CommonUtil.nullIfEmpty(treasurerEmail) ? treasurerEmail : null);
		}
	}

    public int getSeasonId() {
        return seasonId;
    }

    public void setSeasonId(int seasonId) {
        this.seasonId = seasonId;
    }

    public int getClubId() {
        return clubId;
    }
    
    public void setClubId(int clubId) {
        this.clubId = clubId;
    }
    
    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public Contact getChairMan() {
        return chairMan;
    }

    public void setChairMan(Contact chairMan) {
        this.chairMan = chairMan;
    }

    public Contact getSecretary() {
        return secretary;
    }

    public void setSecretary(Contact secretary) {
        this.secretary = secretary;
    }

    public Contact getMatchSec() {
        return matchSec;
    }

    public void setMatchSec(Contact matchSec) {
        this.matchSec = matchSec;
    }

    public Contact getTreasurer() {
        return treasurer;
    }

    public void setTreasurer(Contact treasurer) {
        this.treasurer = treasurer;
    }

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

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    @Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this, new String[]{"id"});
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj, new String[]{"id"});
	}

    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SIMPLE_STYLE);
    }
}
