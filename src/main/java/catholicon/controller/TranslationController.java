package catholicon.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TranslationController {


	@RequestMapping(method=RequestMethod.GET, value="/i18n")
	@Cacheable(cacheNames="Translations")
	public ResponseEntity<Map<String,String>> fetchTranslations(@RequestParam("lang") String lang) {
		
		Map<String, String> map = new HashMap<>();
		
		switch(lang) {
			case "en_GB":
				map.put("FILTER_MATCH_COUNT", "Your filter \'{{term}}\' matched {{count}} result(s)");
				map.put("HOME", "Home");
				map.put("LOGIN", "Login");
				break;
			case "fr_FR":
				map.put("FILTER_MATCH_COUNT", "Votre filtre \'{{term}}\' a donné {{count}} résultats");
				map.put("HOME", "Page d'accueil");
				map.put("LOGIN", "S'identifier");
				break;
		}
		return ResponseEntity.ok().cacheControl(CacheControl.maxAge(7, TimeUnit.DAYS)).body(map);
	}
}
