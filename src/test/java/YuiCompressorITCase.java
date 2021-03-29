import static io.restassured.RestAssured.*;
import org.junit.Test;

public class YuiCompressorITCase {


	@Test
	public final void testYuiCompressorCreatedFiles() {
		String ip = System.getProperty("ip");
		System.out.println("IP: "+ip);
		given()
			.baseUri("http://"+ip+":8080")
			.get("/static/script/libs/angular1.6.9-ALL.js")
			.then().statusCode(200);
	}
}
