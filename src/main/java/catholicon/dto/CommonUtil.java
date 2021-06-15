package catholicon.dto;

import java.util.List;

import org.springframework.util.StringUtils;

public class CommonUtil {
    
    /**
     * Returns a copy of the string, with leading and trailing whitespace
     * omitted.
     * <p>
     * If this <code>String</code> object represents an empty character
     * sequence, or the first and last characters of character sequence
     * represented by this <code>String</code> object both have codes
     * greater than <code>'&#92;u0020'</code> (the space character), then a
     * reference to this <code>String</code> object is returned.
     * <p>
     * Otherwise, if there is no character with a code greater than
     * <code>'&#92;u0020'</code> in the string, then a new
     * <code>String</code> object representing an empty string is created
     * and returned.
     * <p>
     * Otherwise, let <i>k</i> be the index of the first character in the
     * string whose code is greater than <code>'&#92;u0020'</code>, and let
     * <i>m</i> be the index of the last character in the string whose code
     * is greater than <code>'&#92;u0020'</code>. A new <code>String</code>
     * object is created, representing the substring of this string that
     * begins with the character at index <i>k</i> and ends with the
     * character at index <i>m</i>-that is, the result of
     * <code>this.substring(<i>k</i>,&nbsp;<i>m</i>+1)</code>.
     * <p>
     * This method may be used to trim whitespace (as defined above) from
     * the beginning and end of a string.
     *
     * @return  A copy of this string with leading and trailing white
     *          space removed, or this string if it has no leading or
     *          trailing white space.
     */
    // This was necessary, I seem to recall, because some non-standard whitespace characters are in use on the BDBL website and 
    // java.lang.String.trim() not trimming it.
    public static String trim(String s) {
    	char[] value = s.toCharArray();
        int len = value.length;
        int st = 0;
        char[] val = value;    /* avoid getfield opcode */

        while ((st < len) && (val[st] <= ' ' || val[st] == (char)160)) {
            st++;
        }
        while ((st < len) && (val[len - 1] <= ' ')) {
            len--;
        }
        return ((st > 0) || (len < value.length)) ? new String(value, st, len - st) : s;
    }

    public static String nullIfEmpty(String s) {
		if(null == s || StringUtils.isEmpty(trim(s))) return null;
		return s;
	}

    public static boolean compare(List<?> l1, List<?> l2) {
        if(null == l1 && null == l2) return true;
        if(null == l1 && null != l2) return false;
        if(null != l1 && null == l2) return false;

        if(l1.size() == l2.size()) {
            for (int i = 0; i < l1.size(); i++) {
                if( ! l1.get(i).equals(l2.get(i))) return false;
            }
        }
        return true;
    }
}

