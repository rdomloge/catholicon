package catholicon.controller;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import catholicon.dao.CommitteeDao;
import catholicon.domain.Contact;

@RestController
public class CommitteeController {

	@RequestMapping(method=RequestMethod.GET, value="/committee")
	@Cacheable(cacheNames="Committee")
	public List<Contact> getCommitteeContacts() {
		
		return new CommitteeDao().getCommitteeMembers();	
	}
}
