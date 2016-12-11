package catholicon.domain;

import java.util.Map;

public class MatchCard {
	
	private Rubber[] rubbers = new Rubber[9];
	private String[] awayPlayers;
	private String[] homePlayers;
	private String homeTeam;
	private String awayTeam;
	private String matchDate;
	private int homeScore; 
	private int awayScore;
	private boolean[] homeTeamWins;
	private boolean teamSize6;
	
	public MatchCard(Map<Integer, Rubber> scoreMap, String[] homePlayers, String[] awayPlayers, 
			String homeTeam, String awayTeam, String matchDate, int homeScore, int awayScore, 
			boolean[] homeTeamWins, boolean teamSize6) {
		
		for (Integer rubberNum : scoreMap.keySet()) {
			rubbers[rubberNum-1] = scoreMap.get(rubberNum);
		}
		this.homePlayers = homePlayers;
		this.awayPlayers = awayPlayers;
		this.homeTeam = homeTeam;
		this.awayTeam = awayTeam;
		this.matchDate = matchDate;
		
		this.homeScore = homeScore;
		this.awayScore = awayScore;
		
		this.homeTeamWins = homeTeamWins;
		this.teamSize6 = teamSize6;
	}

	public Rubber[] getRubbers() {
		return rubbers;
	}

	public String[] getAwayPlayers() {
		return awayPlayers;
	}

	public String[] getHomePlayers() {
		return homePlayers;
	}

	public String getHomeTeam() {
		return homeTeam;
	}

	public String getAwayTeam() {
		return awayTeam;
	}

	public String getMatchDate() {
		return matchDate;
	}

	public int getHomeScore() {
		return homeScore;
	}

	public int getAwayScore() {
		return awayScore;
	}

	public boolean[] getHomeTeamWins() {
		return homeTeamWins;
	}

	public boolean isTeamSize6() {
		return teamSize6;
	}
}
