package catholicon.domain;

public class Division {
	
	public static class TeamPosition {
		private String team;
		private String played;
		private String matchesWon;
		private String rubbersWon;
		private String rubbersLost;
		private String gamesWon;
		private String gamesLost;
		private String pointsFor;

		

		public TeamPosition(String team, String played, String matchesWon, String rubbersWon, String rubbersLost,
				String gamesWon, String gamesLost, String pointsFor) {
			super();
			this.team = team;
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

	
	public Division(TeamPosition[] positions) {
		super();
		this.positions = positions;
	}

	public TeamPosition[] getPositions() {
		return positions;
	} 
}
