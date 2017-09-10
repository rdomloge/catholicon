package catholicon.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;

import org.joda.time.DateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController
@RequestMapping("/user")
public class UserController {

    private final Map<String, List<String>> userDb = new HashMap<>();

    public UserController() {
        userDb.put("tom", Arrays.asList("user"));
        userDb.put("sally", Arrays.asList("user", "admin"));
    }

    @RequestMapping(value="login", method = RequestMethod.POST)
    public ResponseEntity<LoginResponse> login(@RequestBody final UserLogin login)
        throws ServletException {
        if (login.name == null || !userDb.containsKey(login.name)) {
            return new ResponseEntity<LoginResponse>(HttpStatus.FORBIDDEN);
        }
        
        Date expires = new DateTime().plusMinutes(1).toDate();
        
        LoginResponse lr = new LoginResponse(
        		Jwts.builder()
        			.setSubject(login.name)
        			.claim("roles", userDb.get(login.name))
        			.setIssuedAt(new Date())
        			.setExpiration(expires)
        			.signWith(SignatureAlgorithm.HS256, "secretkey")
        			.compact());
        
        return new ResponseEntity<LoginResponse>(lr, HttpStatus.OK);
    }
    
    @SuppressWarnings("unused")
    private static class UserLogin {
        public String name;
        public String password;
    }

    @SuppressWarnings("unused")
    private static class LoginResponse {
        public String token;

        public LoginResponse(final String token) {
            this.token = token;
        }
    }
}
