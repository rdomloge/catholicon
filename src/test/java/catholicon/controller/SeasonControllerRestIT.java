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
			.baseUri("htp://localhost:8080/catholicon")
			.get("/seasons").then().body("id", equalTo(0));
	}

}
