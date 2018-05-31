package catholicon.controller;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import catholicon.dao.NewsDao;
import catholicon.domain.NewsItem;

@RestController
public class NewsController {

	@Autowired
	private NewsDao newsDao;

	@RequestMapping(method = RequestMethod.GET, value = "/newsitems")
	@Cacheable(cacheNames = "News")
	public ResponseEntity<List<NewsItem>> loadNews() {
		
		return ResponseEntity.ok()
				.cacheControl(CacheControl.maxAge(7, TimeUnit.DAYS))
				.body(newsDao.list());
	}
}
