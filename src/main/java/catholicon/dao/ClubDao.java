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
		fillOutMatchSessions(doc, club);
	}
	
	private static String parseRole(Document doc, String selector) {
		Elements elements = doc.select(selector);
		if(elements.size() != 1) throw new DaoException("Could not find role element");
		return elements.first().ownText();
	}
	
	private void fillOutPhoneNumbers(Document doc, Club club) {
		Elements phoneNumbers = 
				doc.select("#ChairmanID").first().parent().parent().parent().select("tr").get(2).select("td");
		String chairmanPhone = phoneNumbers.get(0).ownText().trim();
		String secretaryPhone = phoneNumbers.get(1).ownText().trim();
		String matchSecPhone = phoneNumbers.get(2).ownText().trim();
		String treasurerPhone = phoneNumbers.get(3).ownText().trim();
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
	
	private void fillOutMatchSessions(Document doc, Club club) {
		Elements rows = 
				doc.select("h4:contains(MATCH SESSIONS) + table tr");
		
		club.setMatchSessions(parseSessions(rows));
	}
	
	private List<Session> parseSessions(Elements rows) {
		LinkedList<Session> sessions = new LinkedList<>();
		
		String locationName = null;
		String locationAddr = null;
		String days = null;
		String numCourts = null;
		String start = null;
		String end = null;
		
		for(int i=0; i < rows.size(); i++) {
			Element row = rows.get(i);
			if(row.select("tr td.TableColHeading").size() > 0) continue; // heading row
			
			if(row.select("td[rowspan]").size() > 0) {				// venue row
				locationName = row.select("td[rowspan] b").text();
				locationAddr = row.select("td[rowspan]").text();				
			}
			
			if(row.select("td[align]").size() > 0) {				// session details row
				if(row.select("td:containsOwn(As Above)").size() > 0) {
					locationName = sessions.getLast().getLocationName();
					locationAddr = sessions.getLast().getLocationAddr();
				}
				Elements sessionCells = row.select("td[align] + td");
				days = sessionCells.get(0).childNode(0).toString();
				numCourts = sessionCells.get(0).childNode(2).toString();
				start = sessionCells.get(1).childNode(0).toString();
				end = sessionCells.get(1).childNode(2).toString();
				
				sessions.add(new Session(locationName, locationAddr, days, numCourts, start, end));
				locationName = null;
				locationAddr = null;
				days = null;
				numCourts = null;
				start = null;
				end = null;
			}
		}
		
		return sessions;
	}
	
	private void fillOutClubSessions(Document doc, Club club) {
		Elements rows = 
				doc.select("h4:contains(CLUB SESSIONS) + table tr");
		
		club.setClubSessions(parseSessions(rows));
	}
	
	private void fillOutClub(Club club) {
		String clubPage = ThreadLocalLoaderFilter.getLoader().load(String.format(clubUrl, club.getClubId(), club.getSeasonId()));
		fillOutClub(club, Jsoup.parse(clubPage));
	}
}
