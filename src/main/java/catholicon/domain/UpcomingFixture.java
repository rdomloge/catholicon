package catholicon.domain;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class UpcomingFixture {
	
	private String homeTeamName;
	
	private String awayTeamName;
	
	private String fixtureDate;
	
	private static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

	
	public UpcomingFixture(String homeTeamName, String awayTeamName, String fixtureDate) {
		super();
		this.homeTeamName = homeTeamName;
		this.awayTeamName = awayTeamName;
		this.fixtureDate = fixtureDate;
	}

	public String getHomeTeamName() {
		return homeTeamName;
	}

	public String getAwayTeamName() {
		return awayTeamName;
	}

	public String getFixtureDate() {
		return fixtureDate;
	}
	
	public String getTtg() {
		try {
			long ttg = format.parse(fixtureDate).getTime() - System.currentTimeMillis();
			if(ttg <= 0) return "0";
			if(ttg < 86400000) return "1";
			return String.valueOf(ttg);
		} 
		catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String toString() {
		return "UpcomingFixture [homeTeamName=" + homeTeamName + ", awayTeamName=" + awayTeamName + ", fixtureDate="
				+ fixtureDate + "]";
	}
}
