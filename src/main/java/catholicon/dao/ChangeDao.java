package catholicon.dao;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import catholicon.domain.Change;
import catholicon.filter.ThreadLocalLoaderFilter;
import catholicon.parser.ParserUtil;

public class ChangeDao {

	private static final String url = "/Live/MatchCardHistories.asp?FixtureID=%1$s&Juniors=false&Schools=false&Season=%2$s&Website=1";
	
	private static final Pattern p = Pattern.compile("(?s)var matchHistoryList =\\s+\\[\\s+(.*)\\];");
	/*
	 	var matchHistoryList =
		[
			{matchCardActionsID:7016,userID:28,playerID:432,username:"",changeDate:new Date("20 Sep 2017 01:36"),playerName:"Ian Aherne",actionCode:7,comments:""},
			{matchCardActionsID:7017,userID:114,playerID:3,username:"",changeDate:new Date("21 Sep 2017 09:04"),playerName:"Peter Bevell",actionCode:8,comments:"scores entered incorrectly"},
			{matchCardActionsID:7018,userID:154,playerID:10000232,username:"",changeDate:new Date("21 Sep 2017 22:09"),playerName:"Rob Clayburn",actionCode:10,comments:""}
		];
	 */
	
	
	public List<Change> getChanges(int fixtureId, int seasonStartYear) {
		String page = ThreadLocalLoaderFilter.getLoader().load(String.format(url, fixtureId, seasonStartYear));
		Matcher m = p.matcher(page);
		List<Change> changes = new LinkedList<>();
		if(m.find()) {
			String[] jsonObjects = ParserUtil.splitArray(m.group(1));
			for (String jsonObject : jsonObjects) {
				Map<String, String> map = ParserUtil.convertJsonToMap(jsonObject);
				changes.add(
						new Change(
								map.get("playerName"), 
								map.get("comments"), 
								ParserUtil.parseDate(map.get("changeDate")), 
								Integer.parseInt(map.get("actionCode"))));
			}
		}
		return changes;
	}
}
