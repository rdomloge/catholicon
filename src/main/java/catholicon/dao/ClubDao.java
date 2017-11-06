package catholicon.dao;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import catholicon.domain.Club;
import catholicon.domain.PhoneNumber;
import catholicon.domain.PhoneNumber.Type;
import catholicon.domain.Session;
import catholicon.ex.DaoException;
import catholicon.filter.ThreadLocalLoaderFilter;

public class ClubDao {
	
	private static String seedUrl = "/Live/ClubInfo.asp?Season=0&website=1";

	private static String clubUrl = "/Live/ClubInfo.asp?Club=%1$s&Season=%2$s&Juniors=false&Schools=false&Website=1";
	
	
	public List<Club> getClubIds(int seasonId) {
		String seedPage = ThreadLocalLoaderFilter.getLoader().load(seedUrl);
		Document doc = Jsoup.parse(seedPage);
		List<Club> clubList = new LinkedList<>();
		
		Elements clubs = doc.select("select[id$=ClubList] option");
		
		for(int i=0; i < clubs.size(); i++) {
			Element clubEl = clubs.get(i);
			int clubId = Integer.parseInt(clubEl.attr("value"));
			String clubName = clubEl.ownText().replaceAll("Badminton Club", "");
			Club club = new Club(clubId, clubName, seasonId);
			clubList.add(club);
		}
		return clubList;
	}
	
	public Club getClub(int seasonId, int clubId) {
		Club club = new Club(clubId, seasonId);
		fillOutClub(club);
		return club;
	}
	
	private static String getClubName(Document doc) {
		// var clubName = "Aldermaston Badminton Club";
		String script = doc.getElementsByTag("script").get(6).html();
		int firstQuote = script.indexOf('"')+1;
		return script.substring(firstQuote, script.indexOf('"', firstQuote)).replaceAll("Badminton Club", "");		
	}
	
	private void fillOutClub(Club club, Document doc) {
		club.setClubName(getClubName(doc));
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
		PhoneNumber[] chairmanPhoneNumbers = parsePhoneNumbers(phoneNumbers.get(0).ownText().trim());
		PhoneNumber[] secretaryPhoneNumbers = parsePhoneNumbers(phoneNumbers.get(1).ownText().trim());
		PhoneNumber[] matchSecPhoneNumbers = parsePhoneNumbers(phoneNumbers.get(2).ownText().trim());
		PhoneNumber[] treasurerPhoneNumbers = parsePhoneNumbers(phoneNumbers.get(3).ownText().trim());
		club.fillOutPhoneNumbers(chairmanPhoneNumbers, secretaryPhoneNumbers, matchSecPhoneNumbers, treasurerPhoneNumbers);
	}
	
	private PhoneNumber[] parsePhoneNumbers(String s) {
		List<PhoneNumber> numbers = new LinkedList<>();
		String regex = "[ 0-9]+\\([HM]\\)";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(s);
		if(m.find()) {
			String entry = m.group();
			int bracket = entry.indexOf('(');
			String number = entry.substring(0, bracket-1);
			String type = entry.substring(bracket+1, entry.indexOf(')'));
			numbers.add(new PhoneNumber(Type.forIdentifier(type), number));
		}
		
		return numbers.toArray(new PhoneNumber[numbers.size()]);
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
