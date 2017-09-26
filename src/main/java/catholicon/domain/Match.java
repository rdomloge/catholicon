package catholicon.domain;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import catholicon.parser.ParserUtil;

public class Match {
	
	private static final String QUOTED_STRING_REGEXP = "\".*\"";
	
	private static final Pattern scorePattern = Pattern.compile("\\>(\\d\\s-\\s\\d)\\<");
	private static final Pattern fixturePattern1 = Pattern.compile("openMatchCard\\((.*?),");
	private static final Pattern fixturePattern2 = Pattern.compile("editFixture\\((true|false){1},(.*?),");
	
	private String awayTeamName;
	private int awayTeamId;
	private String homeTeamName;
	private int homeTeamId;
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
				awayTeamName = value;
			}
			else if("homeTarget".equals(pair[0].trim())) {
				homeTeamName = value;
			}
			else if("matchDate".equals(pair[0].trim())) {
				date = ParserUtil.parseDate(value);
			}
			else if("fixtureStatus".equals(pair[0].trim())) {
				this.fixtureStatus = Integer.parseInt(value);
			}
			else if("fixtureLink".equals(pair[0].trim())) {
				fixtureText = pair[1];
				extractFixtureId();
			}
			else if("scoreText".equals(pair[0].trim())) {
				this.scoreText = pair[1];
				extractScore();
			}
			else if("awayTeamID".equals(pair[0].trim())) {
				this.awayTeamId = Integer.parseInt(pair[1]);
			}
			else if("homeTeamID".equals(pair[0].trim())) {
				this.homeTeamId = Integer.parseInt(pair[1]);
			}
			else {
			}
		}
	}
	
	/**
	 * 
	 * 
	 * '<td nowrap="nowrap" onclick="openMatchCard(1319,false,false);" class="FakeLink" style="cursor:pointer" id="Card203_202"><img src="BdblImages/ArrowDown.bmp" alt="" />&nbsp;Tue, 1 Dec 2015</td>'
	 * '<td nowrap="nowrap" onclick="editFixture(false,1517,223,215,1,false,false);" class="FakeLink" style="cursor:pointer" id="Card223_215"><img src="BdblImages/ArrowDown.bmp" alt="" />&nbsp;Thu, 2 Feb 2017</td>'
	 * 
	 * @param string
	 */
	private void extractFixtureId() {
		Matcher m = fixturePattern1.matcher(fixtureText);
		if(m.find()) {
			fixtureId = m.group(1);
		}
		else {
			Matcher m2 = fixturePattern2.matcher(fixtureText);
			if(m2.find()) {
				fixtureId = m2.group(2);
			}
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

	public String getAwayTeamName() {
		return awayTeamName;
	}

	public int getAwayTeamId() {
		return awayTeamId;
	}

	public String getHomeTeamName() {
		return homeTeamName;
	}

	public int getHomeTeamId() {
		return homeTeamId;
	}

	@Override
	public String toString() {
		return "Match [awayTeamName=" + awayTeamName + ", awayTeamId=" + awayTeamId + ", homeTeamName=" + homeTeamName
				+ ", homeTeamId=" + homeTeamId + ", date=" + date + ", fixtureStatus=" + fixtureStatus + ", fixtureId="
				+ fixtureId + ", scoreExtracted=" + scoreExtracted + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + awayTeamId;
		result = prime * result + ((awayTeamName == null) ? 0 : awayTeamName.hashCode());
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((fixtureId == null) ? 0 : fixtureId.hashCode());
		result = prime * result + fixtureStatus;
		result = prime * result + ((fixtureText == null) ? 0 : fixtureText.hashCode());
		result = prime * result + homeTeamId;
		result = prime * result + ((homeTeamName == null) ? 0 : homeTeamName.hashCode());
		result = prime * result + ((scoreExtracted == null) ? 0 : scoreExtracted.hashCode());
		result = prime * result + ((scoreText == null) ? 0 : scoreText.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Match other = (Match) obj;
		if (awayTeamId != other.awayTeamId)
			return false;
		if (awayTeamName == null) {
			if (other.awayTeamName != null)
				return false;
		} else if (!awayTeamName.equals(other.awayTeamName))
			return false;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (fixtureId == null) {
			if (other.fixtureId != null)
				return false;
		} else if (!fixtureId.equals(other.fixtureId))
			return false;
		if (fixtureStatus != other.fixtureStatus)
			return false;
		if (fixtureText == null) {
			if (other.fixtureText != null)
				return false;
		} else if (!fixtureText.equals(other.fixtureText))
			return false;
		if (homeTeamId != other.homeTeamId)
			return false;
		if (homeTeamName == null) {
			if (other.homeTeamName != null)
				return false;
		} else if (!homeTeamName.equals(other.homeTeamName))
			return false;
		if (scoreExtracted == null) {
			if (other.scoreExtracted != null)
				return false;
		} else if (!scoreExtracted.equals(other.scoreExtracted))
			return false;
		if (scoreText == null) {
			if (other.scoreText != null)
				return false;
		} else if (!scoreText.equals(other.scoreText))
			return false;
		return true;
	}
}
