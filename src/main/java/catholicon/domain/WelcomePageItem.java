package catholicon.domain;

public class WelcomePageItem {
	
	private String date;
	
	private String awayTeam;
	
	private String homeTeam;
	
	private FixtureStatus fixtureStatus; 
	
	private CardStatus cardStatus;
	
	private String teamToAction;


	public WelcomePageItem(String date, String awayTeam, String homeTeam, int fixtureStatus, int cardStatus, String teamToAction) {
		super();
		this.date = date;
		this.awayTeam = awayTeam;
		this.homeTeam = homeTeam;
		this.fixtureStatus = FixtureStatus.forId(fixtureStatus);
		this.cardStatus = CardStatus.forId(cardStatus);
		this.teamToAction = teamToAction;
	}

	public String getDate() {
		return date;
	}

	public String getAwayTeam() {
		return awayTeam;
	}

	public String getHomeTeam() {
		return homeTeam;
	}

	public FixtureStatus getFixtureStatus() {
		return fixtureStatus;
	}

	public CardStatus getCardStatus() {
		return cardStatus;
	}

	public String getTeamToAction() {
		return teamToAction;
	}
}
