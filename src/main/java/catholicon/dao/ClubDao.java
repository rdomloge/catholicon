package catholicon.dao;

import java.util.LinkedList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import catholicon.domain.Club;
import catholicon.domain.Session;
import catholicon.ex.DaoException;
import catholicon.filter.ThreadLocalLoaderFilter;

public class ClubDao {
	
	private static String seedUrl = "/Live/ClubInfo.asp?Season=0&website=1";

	private static String clubUrl = "/Live/ClubInfo.asp?Club=%1$s&Season=%2$s&Juniors=false&Schools=false&Website=1";
//	<td colspan="2"><select onchange='changeClub(this);' id="ClubList"><option value="1">Aldermaston Badminton Club</option><option value="2" selected>Alresford Badminton Club</option><option value="3">Andover Badminton Club</option><option value="4">Andover Sports Badminton Club</option><option value="6">Beechdown Badminton Club</option><option value="7">BH Pegasus Badminton Club</option><option value="16">Challengers Badminton Club</option><option value="9">Hurst Badminton Club</option><option value="11">Phoenix Badminton Club</option><option value="12">St Marys Badminton Club</option><option value="13">Viking Badminton Club</option><option value="14">Waverley Badminton Club</option><option value="15">Whitchurch Badminton Club</option></select></td>
	
	public List<Club> getClubs(int seasonId) {
		String seedPage = ThreadLocalLoaderFilter.getLoader().load(seedUrl);
		List<Club> clubList = new LinkedList<>();
		
		Document doc = Jsoup.parse(seedPage);
		Elements clubs = doc.select("select[id$=ClubList] option");
		if(clubs.size() < 1) throw new DaoException("Could not find the club list select");
		for(int i=0; i < clubs.size(); i++) {
			Element clubEl = clubs.get(i);
			int clubId = Integer.parseInt(clubEl.attr("value"));
			String clubName = clubEl.ownText();
			Club club = new Club(clubId, clubName, seasonId);
			clubList.add(club);
			if(i == 0) fillOutClub(club, doc);
			else fillOutClub(club);
		}
		
		return clubList;
	}
	
	private void fillOutClub(Club club, Document doc) {
		String chairMan = parseRole(doc, "#ChairmanID + span");
		String secretary = parseRole(doc, "#SecretaryID + span");
		String matchSec = parseRole(doc, "#MatchSecID + span");
		String treasurer = parseRole(doc, "#TreasurerID + span");
		club.fillOutRoles(chairMan, secretary, matchSec, treasurer);
		
		fillOutPhoneNumbers(doc, club);
		fillOutEmailAddrs(doc, club);
		fillOutClubSessions(doc, club);
	}
	
	private static String parseRole(Document doc, String selector) {
		Elements elements = doc.select(selector);
		if(elements.size() != 1) throw new DaoException("Could not find role element");
		return elements.first().ownText();
	}
	
	private void fillOutPhoneNumbers(Document doc, Club club) {
		Elements phoneNumbers = 
				doc.select("#ChairmanID").first().parent().parent().parent().select("tr").get(2).select("td");
		String chairmanPhone = phoneNumbers.get(0).ownText();
		String secretaryPhone = phoneNumbers.get(1).ownText();
		String matchSecPhone = phoneNumbers.get(2).ownText();
		String treasurerPhone = phoneNumbers.get(3).ownText();
		club.fillOutPhoneNumbers(chairmanPhone, secretaryPhone, matchSecPhone, treasurerPhone);
	}
	
	private void fillOutEmailAddrs(Document doc, Club club) {
		/**
		 * This doesn't work because the email address is generated
		 */
//		Elements emails = 
//				doc.select("#ChairmanID").first().parent().parent().parent().select("tr").get(3).select("td");
//		String chairmanEmail = emails.get(0).ownText(); //<td><a href="Click to create email" onclick="GenerateMailHref(this, 'StephenGaunt');">Stephen Gaunt</a></td>
//		String secretaryEmail = emails.get(1).ownText();
//		String matchSecEmail = emails.get(2).ownText();
//		String treasurerEmail = emails.get(3).ownText();
//		club.fillOutEmailAddresses(chairmanEmail, secretaryEmail, matchSecEmail, treasurerEmail);
	}
	
	private void fillOutClubSessions(Document doc, Club club) {
		Elements clubSessionsTable = 
				doc.select("h4:contains(CLUB SESSIONS) + table");
		Elements rows = clubSessionsTable.select("tr td.TableColHeading[colspan=4]");
		
		LinkedList<Session> clubSessions = new LinkedList<>();
		
		for(int i=0; i < rows.size(); i++) {
			Element location = rows.get(i).parent().nextElementSibling().child(0); //<td rowspan="2"><b>Aldermaston Recreational Society</b><br>Aldermaston<br>Berkshire RG7 4PR</td>
			String locationName;
			String locationAddr;
			if("As Above".equalsIgnoreCase(location.ownText())) {
				if(clubSessions.size() < 1) throw new DaoException("'As above' but no previous");
				Session above = clubSessions.getLast();
				locationName = above.getLocationName();
				locationAddr = above.getLocationAddr();
			}
			else {
				locationName = location.child(0).ownText();
				locationAddr = location.ownText();
			}
			Element sessionSpecifics = location.parent().nextElementSibling().child(1); //<td>Tuesdays<br>1</td>
			String days = sessionSpecifics.childNode(0).toString();
			String numCourts = sessionSpecifics.childNode(2).toString();
			Element timing = location.parent().nextElementSibling().child(3); //<td>20:00<br>23:00</td>
			String start = timing.childNode(0).toString();
			String end = timing.childNode(2).toString();
			clubSessions.add(new Session(locationName, locationAddr, days, numCourts, start, end));
		}
	}
	
	private void fillOutClub(Club club) {
		String clubPage = ThreadLocalLoaderFilter.getLoader().load(String.format(clubUrl, club.getClubId(), club.getSeasonId()));
		fillOutClub(club, Jsoup.parse(clubPage));
	}
}
