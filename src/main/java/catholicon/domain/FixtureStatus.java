package catholicon.domain;

public enum FixtureStatus {
	
	UNKNOWN(0), PLAYED(5);

	private int value;


	FixtureStatus(int value) {
		this.value = value;
	}


	public static FixtureStatus forId(int id) {
		
		if(PLAYED.value == id) {
			return PLAYED;
		}
		
		return UNKNOWN;
	}
}
