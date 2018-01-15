package catholicon.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import catholicon.dao.FixtureDao;
import catholicon.dao.MatchDao;
import catholicon.domain.FixtureDetails;
import catholicon.domain.Match;
import catholicon.ex.DaoException;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.ValidationException;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.Description;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.StreetAddress;
import net.fortuna.ical4j.model.property.Uid;
import net.fortuna.ical4j.model.property.Version;

@RestController
public class WebCalController {

	@Autowired
	private MatchDao matchDao;

	@Autowired
	private FixtureDao fixtureDao;

	@RequestMapping(method = RequestMethod.GET, value = "/season/{seasonStartYear}/matches/{team}/webcal")
	@Cacheable(cacheNames = "MatchWebCals")
	public void getWebCal(HttpServletResponse response,
			@PathVariable("seasonStartYear") int seasonStartYear,
			@PathVariable("team") String team) throws DaoException,
			ParseException, IOException, ValidationException {

		createWebCal(response, seasonStartYear, team);

		response.flushBuffer();
	}

	private void createWebCal(HttpServletResponse response,
			int seasonStartYear, String team) throws ParseException,
			IOException, ValidationException {

		Match[] matches = matchDao.load(seasonStartYear, team);

		OutputStream out = response.getOutputStream();
		response.setContentType("text/calendar");

		net.fortuna.ical4j.model.Calendar icsCalendar = new net.fortuna.ical4j.model.Calendar();
		icsCalendar.getProperties().add(
				new ProdId("-//Events Calendar//iCal4j 1.0//EN"));
		icsCalendar.getProperties().add(CalScale.GREGORIAN);
		icsCalendar.getProperties().add(Version.VERSION_2_0);

		for (Match match : matches) {
			icsCalendar.getComponents().add(createEvent(match));
		}

		CalendarOutputter outputter = new CalendarOutputter();
		outputter.output(icsCalendar, out);

	}

	private VEvent createEvent(Match match) throws ParseException, SocketException {
		String eventName = String.format("Match: %s v %s", match.getHomeTeam()
				.getName(), match.getAwayTeam().getName());

		FixtureDetails fixture = fixtureDao.load(Integer.parseInt(match.getFixtureId()));

		DateTime startDate = createDate(match.getDate(), fixture.getMatchTime());
		DateTime endDate = createDate(match.getDate(), "22:00");

		VEvent meeting = new VEvent(startDate, endDate, eventName);

		meeting.getProperties().add(new Uid(match.getDate()+'_'+match.getHomeTeam().getName()+'_'+match.getAwayTeam().getName()));
		
		String description = String.format(
				"BDBL Badminton %s Match\nHome Team: %s\nAway Team: %s",
				fixture.getLeague(),
				match.getHomeTeam().getName(),
				match.getAwayTeam().getName());

		String address = fixture.getVenue();

		meeting.getProperties().add(new Description(description));
		meeting.getProperties().add(new StreetAddress(address));

		return meeting;
	}

	private DateTime createDate(String matchDate, String matchTime) throws ParseException {
		new SimpleDateFormat("yyyy-MM-dd").parse(matchDate);

		java.util.Calendar date = new GregorianCalendar();
		date.set(java.util.Calendar.YEAR, Integer.parseInt(matchDate.substring(0, 4)));
		date.set(java.util.Calendar.MONTH, 
				Integer.parseInt(matchDate.substring(5, 7)) - 1);
		date.set(java.util.Calendar.DAY_OF_MONTH, Integer.parseInt(matchDate.substring(8, 10)));
		date.set(java.util.Calendar.HOUR_OF_DAY, Integer.parseInt(matchTime.substring(0, 2)));
		date.set(java.util.Calendar.MINUTE, Integer.parseInt(matchTime.substring(3, 5)));
		date.set(java.util.Calendar.SECOND, 0);

		return new DateTime(date.getTime());
	}
}
