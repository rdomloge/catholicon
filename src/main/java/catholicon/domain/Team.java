package catholicon.domain;

public class Team {
	private String name;
	private int id;
	private int score;

	public Team(String name, int id, int score) {
		super();
		this.name = name;
		this.id = id;
		this.score = score;
	}

	public Team() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}
}