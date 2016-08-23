package catholicon.domain;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.web.multipart.support.StringMultipartFileEditor;

public class Match {
	
	private static final String QUOTED_STRING_REGEXP = "\".*\"";
	private static final String QUOTED_DATE_REGEXP = "new Date\\(\"(.*?)\"\\)";
	private static final String SCORE_REGEXT = "\\>(\\d\\s-\\s\\d)\\<";
	private static final Pattern scorePattern = Pattern.compile(SCORE_REGEXT);
	private static final Pattern datePattern = Pattern.compile(QUOTED_DATE_REGEXP);
	
	
	private String awayTarget;
	private String homeTarget;
	private String date;
	private int fixtureStatus;
	private String matchStatus;
	private String scoreText;
	private String scoreExtracted;
	

	/**
	 * matchDate:new Date("04 Feb 2016"), fixtureID:1220, leagueTypeID:13, fixtureStatus:5, 
	 * cardStatus:5, walkoverStatus:0, teamToAction:164, websiteID:1, homeClubID:3, 
	 * awayClubID:7, homeSchoolID:null, awaySchoolID:null, homeTeamID:174, awayTeamID:164, 
	 * homeTeamName:"Andover Open", awayTeamName:"BH Pegasus Open A", enterFullMatchCards:true, 
	 * juniorLeague:false, schoolLeague:false
	 * @param group
	 */
	public Match(String group) {
		System.out.println("Processing "+group);
		String[] parts = splitOnUnquotedCommas(group);
		
		for (String keyValuePair : parts) {
			String[] pair = splitOnUnquotedColons(keyValuePair);
			
			if(pair.length != 2)
				throw new IllegalStateException(pair.length +" > "+keyValuePair);

			System.out.println("Processing "+pair[0]+":"+pair[1]);
			String value = stripSurroundingQuotesIfNec(pair[1]);
			
			if("awayTarget".equals(pair[0].trim())) {
				awayTarget = value;
			}
			else if("homeTarget".equals(pair[0].trim())) {
				homeTarget = value;
			}
			else if("matchDate".equals(pair[0].trim())) {
				String dateString;
				Matcher m = datePattern.matcher(pair[1]);
				if(m.find()) {
					dateString = m.group(1);
				}
				else {
					dateString = pair[1];
				}
				try {
					Date dateObj = new SimpleDateFormat("dd MMM yyyy").parse(dateString);
					date = new SimpleDateFormat("yyyy-MM-dd").format(dateObj);
				} 
				catch (ParseException e) {
					date = dateString;
				}
			}
			else if("fixtureStatus".equals(pair[0].trim())) {
				this.fixtureStatus = Integer.parseInt(pair[1]);
			}
			else if("scoreText".equals(pair[0].trim())) {
				this.scoreText = pair[1];
				extractScore();
			}
			else if("fixtureLink".equals(pair[0].trim())) {
				System.out.println("Discarding "+keyValuePair);
			}
			else {
				System.out.println("Ignoring "+pair[0]+":"+pair[1]);
			}
		}
		System.out.println("===========================================");
	}
	
	public Match(String awayTarget, String homeTarget, String date, int fixtureStatus, String matchStatus,
			String scoreText) {
		super();
		this.awayTarget = awayTarget;
		this.homeTarget = homeTarget;
		this.date = date;
		this.fixtureStatus = fixtureStatus;
		this.matchStatus = matchStatus;
		this.scoreText = scoreText;
	}

	private String[] splitOnUnquotedCommas(String s) {
		List<String> parts = new LinkedList<>();
		StringBuilder buf = new StringBuilder();
		char[] chars = s.toCharArray();
		boolean inSingleQuotes = false;
		boolean inDoubleQuotes = false;
		
		for (char c : chars) {
			switch(c) {
				case ',':
					if(inSingleQuotes || inDoubleQuotes) {
						buf.append(c);
						break;
					}
					parts.add(buf.toString());
					buf.setLength(0);
					break;
				case '"':
					inDoubleQuotes = !inDoubleQuotes;
					buf.append(c);
					break;
				case '\'':
					inSingleQuotes = !inSingleQuotes;
					buf.append(c);
					break;
				default:
					buf.append(c);
			}
		}
		
		if(buf.length() > 0) parts.add(buf.toString());
		
		return parts.toArray(new String[parts.size()]);
	}
	
	private String[] splitOnUnquotedColons(String s) {
		List<String> parts = new LinkedList<>();
		StringBuilder buf = new StringBuilder();
		char[] chars = s.toCharArray();
		boolean inSingleQuotes = false;
		boolean inDoubleQuotes = false;
		
		for (char c : chars) {
			switch(c) {
				case ':':
					if(inSingleQuotes || inDoubleQuotes) {
						buf.append(c);
						break;
					}
					parts.add(buf.toString());
					buf.setLength(0);
					break;
				case '"':
					inDoubleQuotes = !inDoubleQuotes;
					buf.append(c);
					break;
				case '\'':
					inSingleQuotes = !inSingleQuotes;
					buf.append(c);
					break;
				default:
					buf.append(c);
			}
		}
		
		if(buf.length() > 0) parts.add(buf.toString());
		
		return parts.toArray(new String[parts.size()]);
	}
	
	/**
	 * <td nowrap='nowrap' class='borderBottomLR MatchConfirmed' title='Match Result Confirmed'>2 - 7</td>
	 */
	private void extractScore() {
		Matcher matcher = scorePattern.matcher(scoreText);
		if(matcher.find())  {
			this.scoreExtracted = matcher.group(1);
		}
	}
	
	public String getScoreExtracted() {
		return scoreExtracted;
	}

	private static String stripSurroundingQuotesIfNec(String s) {
		if(s.matches(QUOTED_STRING_REGEXP)) {
			return s.substring(1, s.length()-1); 
		}
		return s;
	}

	public String getAwayTarget() {
		return awayTarget;
	}

	public String getHomeTarget() {
		return homeTarget;
	}

	public String getDate() {
		return date;
	}

	public int getFixtureStatus() {
		return fixtureStatus;
	}
	
}
