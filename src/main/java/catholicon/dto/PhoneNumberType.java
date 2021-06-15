package catholicon.dto;

import com.fasterxml.jackson.annotation.JsonValue;


public enum PhoneNumberType {
    HOME("H", "Home"), MOBILE("M", "Mobile");
    
    private String identifier;
    
    private String jsonValue;
    
    private PhoneNumberType(String identifier, String jsonValue) {
        this.identifier = identifier;
        this.jsonValue = jsonValue;
    }

    public static PhoneNumberType forIdentifier(String s) {
        if(HOME.identifier.equalsIgnoreCase(s)) return HOME;
        if(MOBILE.identifier.equalsIgnoreCase(s)) return MOBILE;
        throw new RuntimeException("No idea what this is: "+s);
    }

    @JsonValue
    public String getJsonValue() {
        return jsonValue;
    }

    public String getIdentifier() {
        return identifier;
    }
}
