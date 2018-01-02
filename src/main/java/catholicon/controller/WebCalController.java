package catholicon.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

import javax.servlet.http.HttpServletResponse;

import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.TimeZone;
import net.fortuna.ical4j.model.TimeZoneRegistry;
import net.fortuna.ical4j.model.TimeZoneRegistryFactory;
import net.fortuna.ical4j.model.ValidationException;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.component.VTimeZone;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Uid;
import net.fortuna.ical4j.model.property.Version;
import net.fortuna.ical4j.util.UidGenerator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import catholicon.dao.MatchDao;
import catholicon.domain.Match;
import catholicon.ex.DaoException;

@RestController
public class WebCalController {

	private static final String TZ_EUROPE_LONDON = "Europe/London";

	@Autowired
	private MatchDao matchDao;

	@RequestMapping(method = RequestMethod.GET, value = "/season/{seasonStartYear}/matches/{team}/webcal")
	@Cacheable(cacheNames = "MatchWebCals")
	public void getWebCal(HttpServletResponse response,
			@PathVariable("seasonStartYear") int seasonStartYear,
			@PathVariable("team") String team) throws DaoException, ParseException, IOException {

		Match[] matches = matchDao.load(seasonStartYear, team);

		System.out.printf("getWebCal[team=%s, season=%d] : matches=%d\n\n",
				team, seasonStartYear, matches.length);

		createWebCal(response, seasonStartYear, team, matches);
		
		response.flushBuffer();
	}
	
	
	private void createWebCal(HttpServletResponse response, 
			int seasonStartYear, String team, Match[] matches) throws ParseException, IOException {

		OutputStream out = response.getOutputStream();
		response.setContentType("text/calendar");

		net.fortuna.ical4j.model.Calendar icsCalendar = new net.fortuna.ical4j.model.Calendar();
		icsCalendar.getProperties().add(
				new ProdId("-//Events Calendar//iCal4j 1.0//EN"));
		icsCalendar.getProperties().add(CalScale.GREGORIAN);
		icsCalendar.getProperties().add(Version.VERSION_2_0);

		for (Match match : matches) {
			VEvent event = createEvent(match);
			icsCalendar.getComponents().add(event);
		}

		CalendarOutputter outputter = new CalendarOutputter();

		try {
			outputter.output(icsCalendar, out);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ValidationException e) {
			e.printStackTrace();
		}

	}

	private VEvent createEvent(Match match) throws ParseException,
			SocketException {
		TimeZoneRegistry registry = TimeZoneRegistryFactory.getInstance()
				.createRegistry();
		TimeZone timezone = registry.getTimeZone(TZ_EUROPE_LONDON);
		//VTimeZone tz = timezone.getVTimeZone();

		java.util.Date matchDate = new SimpleDateFormat("yyyy-MM-dd")
				.parse(match.getDate());
		java.util.Calendar matchCal = java.util.Calendar.getInstance();
		matchCal.setTime(matchDate);

		String eventName = String.format("Match: %s v %s", match.getHomeTeam()
				.getName(), match.getAwayTeam().getName());

		DateTime startDate = createStartDate(matchCal);
		DateTime endDate = createEndDate(matchCal);

		VEvent meeting = new VEvent(startDate, endDate, eventName);

		//meeting.getProperties().add(tz.getTimeZoneId());

		UidGenerator ug = new UidGenerator("uidGen");
		Uid uid = ug.generateUid();
		meeting.getProperties().add(uid);

		return meeting;
	}

	private DateTime createStartDate(java.util.Calendar matchCal) {
		java.util.Calendar date = new GregorianCalendar();
		date.set(java.util.Calendar.MONTH,
				matchCal.get(java.util.Calendar.MONTH));
		date.set(java.util.Calendar.DAY_OF_MONTH,
				matchCal.get(java.util.Calendar.DATE));
		date.set(java.util.Calendar.YEAR, matchCal.get(java.util.Calendar.YEAR));
		date.set(java.util.Calendar.HOUR_OF_DAY, 19);
		date.set(java.util.Calendar.MINUTE, 30);
		date.set(java.util.Calendar.SECOND, 0);

		return new DateTime(date.getTime());
	}

	private DateTime createEndDate(java.util.Calendar matchCal) {
		java.util.Calendar date = new GregorianCalendar();
		date.set(java.util.Calendar.MONTH,
				matchCal.get(java.util.Calendar.MONTH));
		date.set(java.util.Calendar.DAY_OF_MONTH,
				matchCal.get(java.util.Calendar.DATE));
		date.set(java.util.Calendar.YEAR, matchCal.get(java.util.Calendar.YEAR));
		date.set(java.util.Calendar.HOUR_OF_DAY, 22);
		date.set(java.util.Calendar.MINUTE, 0);
		date.set(java.util.Calendar.SECOND, 0);

		return new DateTime(date.getTime());
	}
}
