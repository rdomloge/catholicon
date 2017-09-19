package catholicon.archive;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import catholicon.domain.Season;

@Repository
public class SeasonArchive {
	
	private static final String KEY = "SEASON";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SeasonArchive.class);
	
	private RedisTemplate<String, Object> redisTemplate;
    private HashOperations<String,Integer,Season> hashOps;
    
    @Autowired
    private ObjectMapper mapper;
    
    @Autowired
    public SeasonArchive(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
 
    @PostConstruct
    private void init() {
        hashOps = redisTemplate.opsForHash();
    }
    
    public void save(Season s) {
    	hashOps.put(KEY, s.getId(), s);
    }
    
    public Season findSeason(Integer id) {
    	return hashOps.get(KEY, id);
    }
    
    public Map<Integer,Season> findAll() {
    	return hashOps.entries(KEY);
    }
    
    public void saveAll(String json) {
    	try {
			Season[] seasons = mapper.readValue(json, Season[].class);
			for (Season season : seasons) {
				save(season);
			}
		} 
    	catch (IOException e) {
    		LOGGER.error("Could not read: "+json, e);
		}
    }
    
    public String getAll() throws JsonProcessingException {
    	
    	Map<Integer, Season> all = findAll();
    	Season[] sorted = all.values().toArray(new Season[all.size()]);
    	Arrays.sort(sorted, new Comparator<Season>(){
			@Override
			public int compare(Season s1, Season s2) {
				return s1.getId() - s2.getId();
			}});
    	return mapper.writeValueAsString(sorted);
    }
}
