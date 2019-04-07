package catholicon.dao;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import catholicon.domain.NewsItem;
import catholicon.filter.ThreadLocalLoaderFilter;

@Component
public class NewsArchiveDao {

	public static String listUrl = "/NewsArchive.asp?Season=0&Juniors=false&Schools=false&Website=1";
	
	//weekly round up w/e 4th May (Added 14 May 2018)
	private static final Pattern addedPattern = Pattern.compile("Added (\\d+ [a-zA-Z]+ \\d+)\\)");
	private static final Pattern titlePattern = Pattern.compile("(.*) \\(Added \\d+ [a-zA-Z]+ \\d+\\)");
		
	
	public List<NewsItem> list() {
		List<NewsItem> items = new LinkedList<>();
		Loader loader = ThreadLocalLoaderFilter.getLoader();
		String page = loader.load(listUrl);
		
		Document doc = Jsoup.parse(page);
		Elements newsRows = doc.select("div[id^=OuterDiv] table[width=80%] tr");
		for (Element row : newsRows) {
			Elements td = row.select("td");
			if(td.hasAttr("style")) {
				// found a header
				items.add(parseHeader(row));
			}
		}
		
		return items;
	}
	
	private NewsItem parseHeader(Element titleRow) {
		
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
