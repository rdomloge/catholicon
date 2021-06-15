package catholicon.dto;

import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class Season {

	private String id;

	private int seasonStartYear;
	
	private int seasonEndYear;
	
	private int apiIdentifier;

	private List<League> leagues;

	public Season(int seasonStartYear, int seasonEndYear, boolean current, List<League> leagues) {
		this.seasonStartYear = seasonStartYear;
		this.seasonEndYear = seasonEndYear;
		this.apiIdentifier = current ? 0 : seasonStartYear;
		this.leagues = leagues;
	}
	
	public Season() {
		super();
	}

	// public String getId() {
	// 	return id;
	// }

	// public void setId(String id) {
	// 	this.id = id;
	// }

	public int getSeasonStartYear() {
		return seasonStartYear;
	}

	public void setSeasonStartYear(int seasonStartYear) {
		this.seasonStartYear = seasonStartYear;
	}

	public int getSeasonEndYear() {
		return seasonEndYear;
	}

	public void setSeasonEndYear(int seasonEndYear) {
		this.seasonEndYear = seasonEndYear;
	}

	public int getApiIdentifier() {
		return apiIdentifier;
	}

	public void setApiIdentifier(int apiIdentifier) {
		this.apiIdentifier = apiIdentifier;
	}

	public void setLeagues(List<League> leagues) {
		this.leagues = leagues;
	}

	public List<League> getLeagues() {
		return leagues;
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
