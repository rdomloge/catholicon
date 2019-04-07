package catholicon.domain;

import java.util.List;

public class NewsItem {
	
	private String primaryTitle;
	
	private String secondaryTitle;
	
	private String added;
	
	private List<String> paragraphs;

	public NewsItem(String primaryTitle, String secondaryTitle, String added, List<String> paragraphs) {
		this.primaryTitle = primaryTitle;
		this.secondaryTitle = secondaryTitle;
		this.added = added;
		this.paragraphs = paragraphs;
	}

	public String getPrimaryTitle() {
		return primaryTitle;
	}

	public String getSecondaryTitle() {
		return secondaryTitle;
	}

	public String getAdded() {
		return added;
	}

	public List<String> getParagraphs() {
		return paragraphs;
	}

}
