package catholicon.domain;

public class Game {
	
	private String homeScore;
	private String awayScore;
	private int gameNum;



	public void setHomeScore(String homeScore) {
		this.homeScore = homeScore;
	}

	public void setAwayScore(String awayScore) {
		this.awayScore = awayScore;
	}

	public void setGameNum(int gameNum) {
		this.gameNum = gameNum;
	}

	public String getHomeScore() {
		return homeScore;
	}

	public String getAwayScore() {
		return awayScore;
	}

	public int getGameNum() {
		return gameNum;
	}

	@Override
	public String toString() {
		return "Result [homeScore=" + homeScore + ", awayScore=" + awayScore + ", gameNum=" + gameNum + "]";
	}

}
