package catholicon.parser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParserUtil {
	
	private static final String QUOTED_DATE_REGEXP = "new Date\\((.*?)\\)";
	private static final Pattern datePattern = Pattern.compile(QUOTED_DATE_REGEXP);
	private static final SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy");
	
	public static String parseDate(String s) {
		Matcher m = datePattern.matcher(s);
		if(m.find()) {
			Date dateObj;
			String dateString = m.group(1);
			try {
				dateObj = format.parse(dateString);
				return new SimpleDateFormat("yyyy-MM-dd").format(dateObj);	
			} 
			catch(NumberFormatException nfex) {
				throw new RuntimeException("Could not extract date from '"+dateString+"'", nfex);
			}
			catch (ParseException e) {
				return dateString;
			}
		}
		throw new RuntimeException("Could not parse "+s);
	}

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
	
	public static Map<String, String> pairsToMap(String[] pairs) {
		Map<String, String> map = new HashMap<>();
		for (String pair : pairs) {
			String[] kvp = ParserUtil.splitOnUnquotedColons(pair);
			map.put(kvp[0].trim(), kvp[1].trim());
		}
		return map;
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
					if(inSingleQuotes) buf.append(c);
					inDoubleQuotes = !inDoubleQuotes;
					
					break;
				case '\'':
					if(inDoubleQuotes) buf.append(c);
					inSingleQuotes = !inSingleQuotes;
					
					break;
				default:
					buf.append(c);
			}
		}
		
		if(buf.length() > 0) parts.add(buf.toString());
		
		return parts.toArray(new String[parts.size()]);
	}
}
