package catholicon.controller;

import static org.junit.Assert.*;
import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

import org.junit.Test;

import io.restassured.config.JsonConfig;
import io.restassured.config.RestAssuredConfig;

public class SeasonControllerRestIT {

	@Test
	public final void test() {
		given()
			.baseUri("http://catholicon-integration-test:9090")
			.get("/seasons")
			.then().body("id", hasItems(0))
			.and()
			.body("apiIdentifier", hasItems(0));
	}

}
