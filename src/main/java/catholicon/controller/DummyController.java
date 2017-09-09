package catholicon.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.Claims;

@RestController
@RequestMapping("/api")
public class DummyController {
	
	public static class ThingToReturn {
		private String field;

		public ThingToReturn(String field) {
			super();
			this.field = field;
		}

		public String getField() {
			return field;
		}
	}

	@RequestMapping("/claims")
	public Claims getClaims(HttpServletRequest hsr) {
		
		Claims claims = (Claims) hsr.getAttribute("claims");
//		return (List<String>) claims.get("roles");
		return claims;
	}
}
