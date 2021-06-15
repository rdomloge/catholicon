package catholicon.dto;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import org.joda.time.DateTimeConstants;


public class Session {

	private String locationName;
	private String locationAddr;
	private String days;
	private int numCourts;
	private String start;
	private String end;


	public Session() {
	}

	public Session(String locationName, String locationAddr, String days, int numCourts, String start, String end) {
		this.locationName = locationName;
		this.locationAddr = locationAddr;
		this.days = days;
		this.numCourts = numCourts;
		this.start = start;
		this.end = end;
	}

	public int daysAsJodaDayOfWeekInt() {
		if("Mondays".equalsIgnoreCase(days)) return DateTimeConstants.MONDAY;
		if("Tuesdays".equalsIgnoreCase(days)) return DateTimeConstants.TUESDAY;
		if("Wednesdays".equalsIgnoreCase(days)) return DateTimeConstants.WEDNESDAY;
		if("Thursdays".equalsIgnoreCase(days)) return DateTimeConstants.THURSDAY;
		if("Fridays".equalsIgnoreCase(days)) return DateTimeConstants.FRIDAY;
		if("Saturdays".equalsIgnoreCase(days)) return DateTimeConstants.SATURDAY;
		if("Sundays".equalsIgnoreCase(days)) return DateTimeConstants.SUNDAY;
		throw new RuntimeException("Could not map "+days);
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public String getLocationAddr() {
		return locationAddr;
	}

	public void setLocationAddr(String locationAddr) {
		this.locationAddr = locationAddr;
	}

	public String getDays() {
		return days;
	}

	public void setDays(String days) {
		this.days = days;
	}

	public int getNumCourts() {
		return numCourts;
	}

	public void setNumCourts(int numCourts) {
		this.numCourts = numCourts;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
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

