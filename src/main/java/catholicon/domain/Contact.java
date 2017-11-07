package catholicon.domain;

public class Contact {
	
	private String name;
	
	private String email;
	
	private ContactRole committeeRole;
	
	private PhoneNumber[] contactNumbers;
	
	public Contact(String name, String email, ContactRole committeeRole, PhoneNumber[] contactNumbers) {
		super();
		this.name = name;
		this.email = email;
		this.committeeRole = committeeRole;
		this.contactNumbers = contactNumbers;
	}

	public Contact(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public ContactRole getCommitteeRole() {
		return committeeRole;
	}

	public PhoneNumber[] getContactNumbers() {
		return contactNumbers;
	}

	public String getEmail() {
		return email;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setContactNumbers(PhoneNumber[] contactNumbers) {
		this.contactNumbers = contactNumbers;
	}
	
}
