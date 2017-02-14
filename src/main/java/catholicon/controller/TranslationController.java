package catholicon.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TranslationController {


	@RequestMapping("/i18n")
	public Map<String, String> fetchTranslations(@RequestParam("lang") String lang) {
		
		Map<String, String> map = new HashMap<>();
		
		switch(lang) {
			case "en":
				map.put("FILTER_MATCH_COUNT", "Your filter \'{{term}}\' matched {{count}} result(s)");
				map.put("HOME", "Home");
				map.put("LOGIN", "Login");
				break;
			case "fr":
				map.put("FILTER_MATCH_COUNT", "Votre filtre \'{{term}}\' a donné {{count}} résultats");
				map.put("HOME", "Page d'accueil");
				map.put("LOGIN", "S'identifier");
				break;
		}
		
		return map;
	}
}
