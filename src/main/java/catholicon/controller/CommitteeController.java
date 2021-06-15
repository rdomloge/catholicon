package catholicon.controller;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import catholicon.dao.CommitteeDao;
import catholicon.dto.Contact;

@RestController
public class CommitteeController {

	@RequestMapping(method=RequestMethod.GET, value="/committee")
	@Cacheable(cacheNames="Committee")
	public ResponseEntity<List<Contact>> getCommitteeContacts() {
		return ResponseEntity.ok()
				.cacheControl(CacheControl.maxAge(1, TimeUnit.DAYS))
				.body(new CommitteeDao().getCommitteeMembers());
	}
}
