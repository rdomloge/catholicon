package catholicon.parser;

import java.util.LinkedList;
import java.util.List;

public class ParserUtil {

	public static String[] splitOnUnquotedCommas(String s) {
		List<String> parts = new LinkedList<>();
		StringBuilder buf = new StringBuilder();
		char[] chars = s.toCharArray();
		boolean inSingleQuotes = false;
		boolean inDoubleQuotes = false;
		
		for (char c : chars) {
			switch(c) {
				case ',':
					if(inSingleQuotes || inDoubleQuotes) {
						buf.append(c);
						break;
					}
					parts.add(buf.toString());
					buf.setLength(0);
					break;
				case '"':
					inDoubleQuotes = !inDoubleQuotes;
					buf.append(c);
					break;
				case '\'':
					inSingleQuotes = !inSingleQuotes;
					buf.append(c);
					break;
				default:
					buf.append(c);
			}
		}
		
		if(buf.length() > 0) parts.add(buf.toString());
		
		return parts.toArray(new String[parts.size()]);
	}
	
	public static String[] splitOnUnquotedColons(String s) {
		List<String> parts = new LinkedList<>();
		StringBuilder buf = new StringBuilder();
		char[] chars = s.toCharArray();
		boolean inSingleQuotes = false;
		boolean inDoubleQuotes = false;
		
		for (char c : chars) {
			switch(c) {
				case ':':
					if(inSingleQuotes || inDoubleQuotes) {
						buf.append(c);
						break;
					}
					parts.add(buf.toString());
					buf.setLength(0);
					break;
				case '"':
					inDoubleQuotes = !inDoubleQuotes;
					buf.append(c);
					break;
				case '\'':
					inSingleQuotes = !inSingleQuotes;
					buf.append(c);
					break;
				default:
					buf.append(c);
			}
		}
		
		if(buf.length() > 0) parts.add(buf.toString());
		
		return parts.toArray(new String[parts.size()]);
	}
}
