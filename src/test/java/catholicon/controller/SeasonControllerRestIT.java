package catholicon.controller;

import static org.junit.Assert.*;
import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

import org.junit.Test;

public class SeasonControllerRestIT {

	@Test
	public final void testSeasons() {
		String ip = System.getProperty("ip");
		System.out.println("IP: "+ip);
		given()
			.baseUri("http://"+ip+":8080")
			.get("/seasons")
			.then().body("id", hasItems(0))
			.and()
			.body("apiIdentifier", hasItems(0));
	}

}
