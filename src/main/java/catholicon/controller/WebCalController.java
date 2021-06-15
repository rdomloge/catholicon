package catholicon.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

import catholicon.Util;
import catholicon.dto.Club;
import catholicon.dto.Session;
import catholicon.ex.DaoException;

import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.ValidationException;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Uid;
import net.fortuna.ical4j.model.property.Version;

public class WebCalController {

	private RestTemplate fixtureTemplate;

	private RestTemplate seasonTemplate;

	private RestTemplate clubTemplate;

	@Value("${MATCHCARDS_SVC_BASE_URL:http://catholicon-ms-matchcard-service:84}")
	private String MATCHCARD_SVC_BASE_URL;

	@Value("${SEASONS_SVC_BASE_URL:http://catholicon-ms-seasons-service:81}")
	private String SEASONS_SVC_BASE_URL;

	@Value("${CLUBS_SVC_BASE_URL:http://catholicon-ms-club-service:85/clubs}")
	private String CLUBS_SVC_BASE_URL;
	

	public WebCalController(@Autowired RestTemplateBuilder builder) {
        this.fixtureTemplate = builder.build();
		this.seasonTemplate = builder.build();
		this.clubTemplate = builder.build();
    }


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

		// Fixture[] matches = matchDao.load(seasonStartYear, team);
		Fixture[] matches = fixtureTemplate.getForEntity(MATCHCARD_SVC_BASE_URL
			+ "/search/findByHomeTeamIdOrAwayTeamIdAndSeason?season={}&homeTeamId={}&awayTeamId={}", 
			Fixture[].class, seasonStartYear, team, team).getBody();

		OutputStream out = response.getOutputStream();
		response.setContentType("text/calendar");

		net.fortuna.ical4j.model.Calendar icsCalendar = new net.fortuna.ical4j.model.Calendar();
		icsCalendar.getProperties().add(
				new ProdId("-//Events Calendar//iCal4j 1.0//EN"));
		icsCalendar.getProperties().add(CalScale.GREGORIAN);
		icsCalendar.getProperties().add(Version.VERSION_2_0);

		for (Fixture match : matches) {
			icsCalendar.getComponents().add(createEvent(match));
		}

		CalendarOutputter outputter = new CalendarOutputter();
		outputter.output(icsCalendar, out);

	}

	private VEvent createEvent(Fixture fixture) throws ParseException, SocketException {
		
		String eventName = String.format("Match: %s v %s", fixture.getHomeTeamName(), fixture.getAwayTeamName());

		// FixtureDetails fixture = fixtureDao.load(fixture.getExternalFixtureId());
		ResponseEntity<Club> clubResponse = clubTemplate.getForEntity(CLUBS_SVC_BASE_URL, Club.class, f.getHomeTeamId());
		Club c = clubResponse.getBody();
		Session matchSession = Util.resolveMatchSession(fixture, c);

		DateTime startDate = createDate(fixture.getMatchDate(), matchSession.getStart());
		DateTime endDate = createDate(fixture.getMatchDate(), "22:00");

		VEvent meeting = new VEvent(startDate, endDate, eventName);

		meeting.getProperties().add(new Uid(fixture.getMatchDate()+'_'+fixture.getHomeTeamName()+'_'+fixture.getAwayTeamName()));
		
		LinkedHashMap<String,String> seasons = seasonTemplate
			.getForObject(SEASONS_SVC_BASE_URL + "/seasons?sort=seasonStartYear,desc", LinkedHashMap.class);
		

		// String description = String.format(
		// 		"BDBL Badminton %s Match\nHome Team: %s\nAway Team: %s",
		// 		fixture.getLeague(),
		// 		fixture.getHomeTeamName(),
		// 		fixture.getAwayTeamName());

		// String address = fixture.getVenue();

		// meeting.getProperties().add(new Description(description));
		// meeting.getProperties().add(new Location(address));

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
