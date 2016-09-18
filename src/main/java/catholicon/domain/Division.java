package catholicon.domain;

public class Division extends Seasonal {
	
	public static class TeamPosition {
		private String team;
		private int teamId;
		private String played;
		private String matchesWon;
		private String rubbersWon;
		private String rubbersLost;
		private String gamesWon;
		private String gamesLost;
		private String pointsFor;

		

		public TeamPosition(String team, int teamId, String played, String matchesWon, String rubbersWon, String rubbersLost,
				String gamesWon, String gamesLost, String pointsFor) {
			super();
			this.team = team;
			this.teamId = teamId;
			this.played = played;
			this.matchesWon = matchesWon;
			this.rubbersWon = rubbersWon;
			this.rubbersLost = rubbersLost;
			this.gamesWon = gamesWon;
			this.gamesLost = gamesLost;
			this.pointsFor = pointsFor;
		}


		public String getTeam() {
			return team;
		}

		public int getTeamId() {
			return teamId;
		}


		public String getPlayed() {
			return played;
		}

		public String getMatchesWon() {
			return matchesWon;
		}

		public String getRubbersWon() {
			return rubbersWon;
		}

		public String getRubbersLost() {
			return rubbersLost;
		}

		public String getGamesWon() {
			return gamesWon;
		}

		public String getGamesLost() {
			return gamesLost;
		}

		public String getPointsFor() {
			return pointsFor;
		}

		@Override
		public String toString() {
			return "TeamPosition [team=" + team + ", played=" + played + ", matchesWon=" + matchesWon + ", rubbersWon="
					+ rubbersWon + ", rubbersLost=" + rubbersLost + ", gamesWon=" + gamesWon + ", gamesLost="
					+ gamesLost + ", pointsFor=" + pointsFor + "]";
		}

	}

	
	private TeamPosition[] positions;
	private int leagueTypeId;

	
	public Division(TeamPosition[] positions, int season, int leagueTypeId) {
		super(season);
		this.positions = positions;
		this.leagueTypeId = leagueTypeId;
	}

	public TeamPosition[] getPositions() {
		return positions;
	}

	public int getLeagueTypeId() {
		return leagueTypeId;
	} 
}
