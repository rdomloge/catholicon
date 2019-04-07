package catholicon.domain;

import com.fasterxml.jackson.annotation.JsonValue;

import catholicon.ex.DaoException;

public class PhoneNumber {
	
	public static enum Type {
		HOME("H", "Home"), MOBILE("M", "Mobile");
		
		private String identifier;
		
		private String jsonValue;
		
		private Type(String identifier, String jsonValue) {
			this.identifier = identifier;
			this.jsonValue = jsonValue;
		}

		public static Type forIdentifier(String s) {
			if(HOME.identifier.equalsIgnoreCase(s)) return HOME;
			if(MOBILE.identifier.equalsIgnoreCase(s)) return MOBILE;
			throw new DaoException("No idea what this is: "+s);
		}

		@JsonValue
		public String getJsonValue() {
			return jsonValue;
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
