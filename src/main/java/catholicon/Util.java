package catholicon;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.joda.time.DateTime;

import catholicon.domain.Session;
import catholicon.dto.Club;

public class Util {

    private static final SimpleDateFormat fixtureDateFormat = new SimpleDateFormat("YYYY-mm-DD");
    
    public static Session resolveMatchSession(Fixture f, Club c) throws ParseException {
		Map<Integer, Session> dayToSessionMap = c.getMatchSessions().stream().collect(
			Collectors.toMap(Session::daysAsJodaDayOfWeekInt, Function.identity()));
		
		String matchDate = f.getMatchDate();
		Date d = fixtureDateFormat.parse(matchDate);
		DateTime dateTime = new DateTime(d.getTime());
		int fixtureDayOfWeek = dateTime.getDayOfWeek();

		return dayToSessionMap.get(fixtureDayOfWeek);
	}
}
