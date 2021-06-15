package catholicon.dto;


public class PhoneNumber {
	
	private PhoneNumberType type;
	
	private String number;

	public PhoneNumber() {
	}

	public PhoneNumber(PhoneNumberType type, String number) {
		super();
		this.type = type;
		this.number = number;
	}

	public PhoneNumberType getType() {
		return type;
	}

	public String getNumber() {
		return number;
	}

	public void setType(PhoneNumberType type) {
		this.type = type;
	}

	public void setNumber(String number) {
		this.number = number;
	}
}
