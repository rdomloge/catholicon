package catholicon.domain;

import catholicon.ex.DaoException;

public class PhoneNumber {
	
	public static enum Type {
		HOME("H"), MOBILE("M");
		
		private String identifier;
		
		private Type(String identifier) {
			this.identifier = identifier;
		}

		public static Type forIdentifier(String s) {
			if(HOME.identifier.equalsIgnoreCase(s)) return HOME;
			if(MOBILE.identifier.equalsIgnoreCase(s)) return MOBILE;
			throw new DaoException("No idea what this is: "+s);
		}
	}

	
	private Type type;
	
	private String number;

	public PhoneNumber(Type type, String number) {
		super();
		this.type = type;
		this.number = number;
	}

	public Type getType() {
		return type;
	}

	public String getNumber() {
		return number;
	}
}
