package catholicon.domain;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import catholicon.parser.ParserUtil;

public class Match {
	
	private static final String QUOTED_STRING_REGEXP = "\".*\"";
	
	private static final String SCORE_REGEXP = "\\>(\\d\\s-\\s\\d)\\<";
	private static final String FIXTURE_REGEXP = "openMatchCard\\((.*?),";
	private static final Pattern scorePattern = Pattern.compile(SCORE_REGEXP);
	private static final Pattern fixturePattern = Pattern.compile(FIXTURE_REGEXP);
	
	
	
	private String awayTarget;
	private String homeTarget;
	private String date;
	private int fixtureStatus;
	private String fixtureText;
	private String fixtureId;
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
		String[] parts = ParserUtil.splitOnUnquotedCommas(group);
		
		for (String keyValuePair : parts) {
			String[] pair = ParserUtil.splitOnUnquotedColons(keyValuePair);
			
			if(pair.length != 2)
				throw new IllegalStateException(pair.length +" > "+keyValuePair);

			String value = stripSurroundingQuotesIfNec(pair[1]);
			
			if("awayTarget".equals(pair[0].trim())) {
				awayTarget = value;
			}
			else if("homeTarget".equals(pair[0].trim())) {
				homeTarget = value;
			}
			else if("matchDate".equals(pair[0].trim())) {
				date = ParserUtil.parseDate(pair[1]);
			}
			else if("fixtureStatus".equals(pair[0].trim())) {
				this.fixtureStatus = Integer.parseInt(pair[1]);
			}
			else if("fixtureLink".equals(pair[0].trim())) {
				fixtureText = pair[1];
				extractFixtureId();
			}
			else if("scoreText".equals(pair[0].trim())) {
				this.scoreText = pair[1];
				extractScore();
			}
			else if("fixtureLink".equals(pair[0].trim())) {
			}
			else {
			}
		}
	}
	
	/**
	 * 
	 * 
	 * '<td nowrap="nowrap" onclick="openMatchCard(1319,false,false);" class="FakeLink" style="cursor:pointer" id="Card203_202"><img src="BdblImages/ArrowDown.bmp" alt="" />&nbsp;Tue, 1 Dec 2015</td>'
	 * 
	 * 
	 * @param string
	 */
	private void extractFixtureId() {
		Matcher m = fixturePattern.matcher(fixtureText);
		if(m.find()) {
			fixtureId = m.group(1);
		}
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

	public String getFixtureId() {
		return fixtureId;
	}
	
	public boolean isPlayed() {
		return 5 == getFixtureStatus();
	}
	
	public boolean isUnPlayed() {
		return 3 == getFixtureStatus();
	}
	
	public boolean isUnConfirmed() {
		return 4 == getFixtureStatus();
	}
}
