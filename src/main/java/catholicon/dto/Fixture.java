package catholicon.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@JsonIgnoreProperties("id")
public class Fixture {
	
	private int externalFixtureId;
	
	private int divisionId;
	
	private String matchDate;
	
	private int homeTeamId;
	
	private int awayTeamId;

	private String homeTeamName;
	
	private String awayTeamName;
	
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "proxy"})
	private Matchcard matchCard;
	
	private int season;
	
	
	public Fixture() {
	}

	public Fixture(int externalFixtureId, int divisionId, String matchDate, int homeTeamId, int awayTeamId,
			String homeTeamName, String awayTeamName, Matchcard matchCard, int season) {
		this.externalFixtureId = externalFixtureId;
		this.divisionId = divisionId;
		this.matchDate = matchDate;
		this.homeTeamId = homeTeamId;
		this.awayTeamId = awayTeamId;
		this.homeTeamName = homeTeamName;
		this.awayTeamName = awayTeamName;
		this.matchCard = matchCard;
		this.season = season;
	}

	public int getExternalFixtureId() {
		return externalFixtureId;
	}

	public void setExternalFixtureId(int newInt) {
		this.externalFixtureId = newInt;
	}
	
	public int getDivisionId() {
		return divisionId;
	}

	public void setDivisionId(int divisionId) {
		this.divisionId = divisionId;
	}

	public String getMatchDate() {
		return matchDate;
	}
	
	public void setMatchDate(String matchDate) {
		this.matchDate = matchDate;
	}
	
	public int getHomeTeamId() {
		return homeTeamId;
	}
	
	public void setHomeTeamId(int homeTeamId) {
		this.homeTeamId = homeTeamId;
	}
	
	public int getAwayTeamId() {
		return awayTeamId;
	}
	
	public void setAwayTeamId(int awayTeamId) {
		this.awayTeamId = awayTeamId;
	}
	
	public Matchcard getMatchCard() {
		return matchCard;
	}

	public void setMatchCard(Matchcard matchCard) {
		this.matchCard = matchCard;
	}

	public int getSeason() {
		return season;
	}

	public void setSeason(int season) {
		this.season = season;
	}

	public String getHomeTeamName() {
		return homeTeamName;
	}

	public void setHomeTeamName(String homeTeamName) {
		this.homeTeamName = homeTeamName;
	}

	public String getAwayTeamName() {
		return awayTeamName;
	}

	public void setAwayTeamName(String awayTeamName) {
		this.awayTeamName = awayTeamName;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this, "id");
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj, "id");
	}

}

