package catholicon.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.domloge.catholiconmsclublibrary.Club;
import com.domloge.catholiconmsclublibrary.Session;
import com.domloge.catholiconmsmatchcardlibrary.Fixture;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import catholicon.Util;
import catholicon.domain.FixtureDetails;
import catholicon.ex.DaoException;

@RestController
public class FixtureDetailsController {
	
	private RestTemplate fixtureTemplate;

	private RestTemplate clubTemplate;

	@Value("${MATCHCARDS_SVC_BASE_URL:http://catholicon-ms-matchcard-service:84}")
	private String MATCHCARD_SVC_BASE_URL;

	@Value("${CLUBS_SVC_BASE_URL:http://catholicon-ms-club-service:85/clubs}")
	private String CLUBS_SVC_BASE_URL;
	

	public FixtureDetailsController(@Autowired RestTemplateBuilder builder) {
        this.fixtureTemplate = builder.build();
		this.clubTemplate = builder.build();
    }
	
	@RequestMapping(method=RequestMethod.GET, value="/fixture/{fixtureId}")
	@Cacheable(cacheNames="Fixtures")
	public ResponseEntity<FixtureDetails> getFixtureDetails(@PathVariable("fixtureId") int fixtureId) throws DaoException, ParseException {
		
		ResponseEntity<Fixture> fixtureResponse = fixtureTemplate.getForEntity(MATCHCARD_SVC_BASE_URL+"/fixtures/{}", Fixture.class, fixtureId);
		Fixture f = fixtureResponse.getBody();
		ResponseEntity<Club> clubResponse = clubTemplate.getForEntity(CLUBS_SVC_BASE_URL, Club.class, f.getHomeTeamId());
		Club c = clubResponse.getBody();

		Session matchSession = Util.resolveMatchSession(f, c);

		FixtureDetails details = new FixtureDetails(
			f.getMatchDate(), 
			f.getMatchDate(), 
			f.getHomeTeamName(), 
			f.getAwayTeamName(), 
			""+matchSession.getNumCourts(),
			matchSession.getLocationName(),
			"league-missing");

		return ResponseEntity.ok()
				.cacheControl(CacheControl.maxAge(24, TimeUnit.HOURS))
				.body(details);
	}

	

	
}
