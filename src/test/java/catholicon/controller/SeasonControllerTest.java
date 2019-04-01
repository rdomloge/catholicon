package catholicon.controller;

import static org.junit.Assert.*;
import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

import org.junit.Test;

public class SeasonControllerTest {

	public final void test() {
		get("/lotto").then().body("lotto.lottoId", equalTo(5));
	}

}
