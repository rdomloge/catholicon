package catholicon.domain;

public class Change {
	
	public enum ActionCode {
		ENTERED(7), UPDATED(8), CONFIRMED(10), UNKNOWN(1000);
		
		private int bdblCode;

		private ActionCode(int bdblCode) {
			this.bdblCode = bdblCode;
		}

		public int getBdblCode() {
			return bdblCode;
		}
		
		public static ActionCode fromBdbleCode(int code) {
			switch(code) {
				case 7:
					return ENTERED;
				case 8:
					return UPDATED;
				case 10:
					return CONFIRMED;
				default:
					return UNKNOWN;
			}
		}
	}
	
	/*
	 * {matchCardActionsID:7016,
	 * userID:28,
	 * playerID:432,
	 * username:"",
	 * changeDate:new Date("20 Sep 2017 01:36"),
	 * playerName:"Ian Aherne",
	 * actionCode:7,
	 * comments:""},
	 */
	private String playerName;
	
	private String comments;
	
	private String changeDate;
	
	private ActionCode actionCode;

	public Change(String playerName, String comments, String changeDate, int actionCode) {
		super();
		this.playerName = playerName;
		this.comments = comments;
		this.changeDate = changeDate;
		this.actionCode = ActionCode.fromBdbleCode(actionCode);
	}

	public String getPlayerName() {
		return playerName;
	}

	public String getComments() {
		return comments;
	}

	public String getChangeDate() {
		return changeDate;
	}

	public ActionCode getActionCode() {
		return actionCode;
	}

}
