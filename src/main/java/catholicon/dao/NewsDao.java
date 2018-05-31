package catholicon.dao;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import catholicon.domain.NewsItem;
import catholicon.filter.ThreadLocalLoaderFilter;

@Component
public class NewsDao {

	public static String listUrl = "/BdblHome.asp?Season=0&website=1";
	
	
	public List<NewsItem> list() {
		
		List<NewsItem> items = new LinkedList<>();
		Loader loader = ThreadLocalLoaderFilter.getLoader();
		String page = loader.load(listUrl);
		
		Document doc = Jsoup.parse(page);
		Elements newsRows = doc.select("div[id^=NewsDiv] table tr");
		for (Element row : newsRows) {
			Elements td = row.select("td");
			if(td.hasAttr("style")) {
				// found a header
				items.add(parse(row));
			}
		}
		
		return items;
	}
	
	//weekly round up w/e 4th May (Added 14 May 2018)
	private static final Pattern addedPattern = Pattern.compile("Added (\\d+ [a-zA-Z]+ \\d+)\\)");
	private static final Pattern titlePattern = Pattern.compile("(.*) \\(Added \\d+ [a-zA-Z]+ \\d+\\)");
	
	
	private NewsItem parse(Element titleRow) {
		
		Elements td = titleRow.select("td");
		
		String titleString = td.select("b").text();
		
		String title = null; 
		Matcher titleMatcher = titlePattern.matcher(titleString);
		if(titleMatcher.find()) {
			title = titleMatcher.group(1);
		}
		
		Matcher addedMatcher = addedPattern.matcher(titleString);
		String added = null;
		if(addedMatcher.find()) {
			added = addedMatcher.group(1);
		}
		
		Element dataRow = titleRow.nextElementSibling();
		String title2 = dataRow.select("h2, h3").text();
		Elements paragraphs = dataRow.select("p");
		
		List<String> paragraphList = new LinkedList<>();
		for (Element element : paragraphs) {
			paragraphList.add(element.text());
		}
		
		NewsItem newsItem = new NewsItem(title, title2, added, paragraphList);
		
		return newsItem;
	}
}
