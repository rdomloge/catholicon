package catholicon.domain;

public class Rubber {
	
	private Game firstGame;
	
	private Game secondGame;
	
	private Game finalGame;


	public void setGame(int gameNum, boolean homeScore, String score) {
		Game g = null;
		switch(gameNum) {
			case 1:
				if(null == firstGame) firstGame = new Game();
				g = firstGame;
				break;
			case 2:
				if(null == secondGame) secondGame = new Game();
				g = secondGame;
				break;
			case 3:
				if(null == finalGame) finalGame = new Game();
				g = finalGame;
				break;
			default:
				throw new IllegalArgumentException("Invalid gameNum param: "+gameNum);
		}
		
		g.setGameNum(gameNum);
		
		if(homeScore) g.setHomeScore(score);
		else g.setAwayScore(score);
	}

	public Game getFirstGame() {
		return firstGame;
	}

	public Game getSecondGame() {
		return secondGame;
	}

	public Game getFinalGame() {
		return finalGame;
	}

	@Override
	public String toString() {
		return "Rubber [firstGame=" + firstGame + ", secondGame=" + secondGame + ", finalGame=" + finalGame + "]";
	}

}
