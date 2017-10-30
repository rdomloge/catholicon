package catholicon.domain;

public class Session {

	private String locationName;
	private String locationAddr;
	private String days;
	private String numCourts;
	private String start;
	private String end;
	
	public Session(String locationName, String locationAddr, String days, String numCourts, String start,
			String end) {
		
		this.locationName = locationName;
		this.locationAddr = locationAddr;
		this.days = days;
		this.numCourts = numCourts;
		this.start = start;
		this.end = end;
	}
	
	public String getLocationName() {
		return locationName;
	}
	
	public String getLocationAddr() {
		return locationAddr;
	}
	
	public String getDays() {
		return days;
	}
	
	public String getNumCourts() {
		return numCourts;
	}
	
	public String getStart() {
		return start;
	}
	
	public String getEnd() {
		return end;
	}
}
