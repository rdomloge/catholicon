package catholicon.domain;

public enum CardStatus {

	UNKNOWN(0), AWAITING_CONFIRMATION(1);
	
	private int value;
	
	CardStatus(int value) {
		this.value = value;
	}
	
	public static CardStatus forId(int id) {
		if(id == AWAITING_CONFIRMATION.value) 
			return AWAITING_CONFIRMATION;
		
		return UNKNOWN;
	}
}
