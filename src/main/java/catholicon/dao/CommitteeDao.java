package catholicon.dao;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import catholicon.domain.Contact;
import catholicon.domain.ContactRole;
import catholicon.domain.PhoneNumber;
import catholicon.domain.PhoneNumber.Type;
import catholicon.ex.DaoException;
import catholicon.filter.ThreadLocalLoaderFilter;
import catholicon.parser.EmailParser;
import catholicon.parser.ParserUtil;

public class CommitteeDao {
	
	private static String url = "/Committee.asp?Season=0&website=1";
	
//	var committeeList = [{title:"Chairman",showLargeFont:false,subTitle:"",playerObj:{playerIndex:0, playerID:124, hashName:"ajefferies", name:"Andrea&nbsp;Jefferies",clubName:"Beechdown Badminton Club", address:"", homeTel:"", workTel:"", mobileTel:"07758 457541", email:"AndreaJefferies", email2:""},memberList:null},{title:"Secretary",showLargeFont:false,subTitle:"",playerObj:{playerIndex:1, playerID:401, hashName:"khume", name:"Karen&nbsp;Hume",clubName:"Beechdown Badminton Club", address:"", homeTel:"", workTel:"", mobileTel:"", email:"KarenHume", email2:""},memberList:null},{title:"Website Administrator",showLargeFont:false,subTitle:"",playerObj:{playerIndex:2, playerID:21, hashName:"cperkins", name:"Cathryn&nbsp;Perkins",clubName:"Beechdown Badminton Club", address:"", homeTel:"", workTel:"", mobileTel:"07973 258637", email:"CathrynPerkins", email2:""},memberList:null},{title:"Treasurer",showLargeFont:false,subTitle:"",playerObj:{playerIndex:3, playerID:33, hashName:"mbayliss", name:"Mark&nbsp;Bayliss",clubName:"Beechdown Badminton Club", address:"", homeTel:"", workTel:"", mobileTel:"", email:"", email2:""},memberList:null},{title:"Fixtures Secretary",showLargeFont:false,subTitle:"",playerObj:{playerIndex:4, playerID:3, hashName:"pbevell", name:"Peter&nbsp;Bevell",clubName:"Phoenix Badminton Club", address:"", homeTel:"01256 211789", workTel:"", mobileTel:"07795 965131", email:"PeterBevell", email2:""},memberList:null},{title:"Tournaments",showLargeFont:false,subTitle:"",playerObj:{playerIndex:5, playerID:397, hashName:"hmitchell", name:"Horace&nbsp;Mitchell",clubName:"Viking Badminton Club", address:"", homeTel:"", workTel:"", mobileTel:"", email:"HoraceMitchell", email2:""},memberList:null},{title:"Junior Development",showLargeFont:false,subTitle:"",playerObj:{playerIndex:6, playerID:408, hashName:"poxford", name:"Philip&nbsp;Oxford",clubName:"Viking Badminton Club", address:"", homeTel:"", workTel:"", mobileTel:"07796 126980", email:"PhilipOxford", email2:""},memberList:null},{title:"Committee Member",showLargeFont:false,subTitle:"",playerObj:{playerIndex:7, playerID:268, hashName:"slambe", name:"Stephen&nbsp;Lambe",clubName:"Hurst Badminton Club", address:"", homeTel:"", workTel:"", mobileTel:"07803 888592", email:"StephenLambe", email2:""},memberList:null},{title:"Committee Member",showLargeFont:false,subTitle:"",playerObj:{playerIndex:8, playerID:208, hashName:"mdavey", name:"Martin&nbsp;Davey",clubName:"BH Pegasus Badminton Club", address:"", homeTel:"", workTel:"", mobileTel:"07432 558179", email:"MartinDavey", email2:""},memberList:null},{title:"Committee Member",showLargeFont:false,subTitle:"",playerObj:{playerIndex:9, playerID:468, hashName:"paskew", name:"Peter&nbsp;Askew",clubName:"Whitchurch Badminton Club", address:"", homeTel:"", workTel:"", mobileTel:"", email:"PeterAskew", email2:""},memberList:null}];

	
	private Pattern committeePattern = Pattern.compile("(?s)var committeeList =\\s+\\[(.*)\\];");
	
	public List<Contact> getCommitteeMembers() {
		List<Contact> list = new LinkedList<>();
		
		String page = ThreadLocalLoaderFilter.getLoader().load(url);
		Matcher m = committeePattern.matcher(page);
		
		Map<String, String> emails = EmailParser.parseEmails(page);
		
		if(m.find()) {
			String line = m.group(1);
			String[] jsonObjects = ParserUtil.splitArray2(line);
			for (String json : jsonObjects) {
				Map<String, String> map = ParserUtil.convertJsonToMap('['+json+']');
				String title = map.get("title");
				ContactRole role = ContactRole.forPageText(title);
				String email = emails.get(map.get("email"));
				List<PhoneNumber> numbers = new LinkedList<>();
				String mobile = map.get("mobileTel");
				if(null != ParserUtil.nullIfEmpty(mobile)) {
					numbers.add(new PhoneNumber(Type.MOBILE, mobile));
				}
				String name = map.get("name").replace("&nbsp;", " ");
				String clubName = map.get("clubName");
				Contact c = new Contact(name, email, role, numbers.toArray(new PhoneNumber[numbers.size()]), clubName);
				list.add(c);
			}
		}
		else {
			throw new DaoException("Could not find any committee members");
		}
		
		return list;
	}

}
