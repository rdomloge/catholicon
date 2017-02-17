package catholicon.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DummyController {

	@RequestMapping("/api/dummy")
	public String getSecuredThing() {
		return "this is a secured API";
	}
}
