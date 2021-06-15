package catholicon.dto;

import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;


public class League {

	private int id;
	
	private int leagueTypeId;
	
	private int season;
	
	private String label;
	
	private List<Division> divisions;
	
	public League() {
		
	}

	public League(String label, int leagueTypeId, int season, List<Division> divisions) {
		this.season = season;
		this.label = label;
		this.leagueTypeId = leagueTypeId;
		this.divisions = divisions;
	}

	public int getId() {
		return id;
	}

	public String getLabel() {
		return label;
	}

	public int getLeagueTypeId() {
		return leagueTypeId;
	}
	
	public int getSeason() {
		return season;
	}

	public List<Division> getDivisions() {
		return divisions;
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
