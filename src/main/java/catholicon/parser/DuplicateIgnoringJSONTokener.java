package catholicon.parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class DuplicateIgnoringJSONTokener extends JSONTokener {

	public DuplicateIgnoringJSONTokener(String s) {
		super(s);
	}

	/**
     * Get the next value. The value can be a Boolean, Double, Integer,
     * JSONArray, JSONObject, Long, or String, or the JSONObject.NULL object.
     * @throws JSONException If syntax error.
     *
     * @return An object.
     */
	@Override
    public Object nextValue() throws JSONException {
        char c = this.nextClean();
        String string;

        switch (c) {
            case '"':
            case '\'':
                return this.nextString(c);
            case '{':
                this.back();
//                return new JSONObject(this);
                return new JSONObjectIgnoreDuplicates(this);
            case '[':
                this.back();
                return new JSONArray(this);
        }

        /*
         * Handle unquoted text. This could be the values true, false, or
         * null, or it can be a number. An implementation (such as this one)
         * is allowed to also accept non-standard forms.
         *
         * Accumulate characters until we reach the end of the text or a
         * formatting character.
         */

        StringBuilder sb = new StringBuilder();
        while (c >= ' ' && ",:]}/\\\"[{;=#".indexOf(c) < 0) {
            sb.append(c);
            c = this.next();
        }
        this.back();

        string = sb.toString().trim();
        if ("".equals(string)) {
            throw this.syntaxError("Missing value");
        }
        return JSONObject.stringToValue(string);
    }
}
