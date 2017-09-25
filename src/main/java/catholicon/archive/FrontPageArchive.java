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

import catholicon.domain.UpcomingFixture;

@Repository
public class FrontPageArchive {
	
	private static final String KEY = "FRONT_PAGE";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FrontPageArchive.class);
	
	private RedisTemplate<String, Object> redisTemplate;
    private HashOperations<String,Integer,UpcomingFixture> hashOps;
    
    @Autowired
    private ObjectMapper mapper;
    
    @Autowired
    public FrontPageArchive(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
 
    @PostConstruct
    private void init() {
        hashOps = redisTemplate.opsForHash();
    }

    public void save(UpcomingFixture f) {
    	hashOps.put(KEY, f.getFixtureId(), f);
    }
    
    public UpcomingFixture findUpcomingFixture(Integer id) {
    	return hashOps.get(KEY, id);
    }
    
    public Map<Integer,UpcomingFixture> findAll() {
    	return hashOps.entries(KEY);
    }
    
    public void saveAll(String json) {
    	try {
    		UpcomingFixture[] fixtures = mapper.readValue(json, UpcomingFixture[].class);
			for (UpcomingFixture fixture : fixtures) {
				save(fixture);
			}
		} 
    	catch (IOException e) {
    		LOGGER.error("Could not read: "+json, e);
		}
    }
    
    public String getAll() throws JsonProcessingException {
    	
    	Map<Integer, UpcomingFixture> all = findAll();
    	UpcomingFixture[] sorted = all.values().toArray(new UpcomingFixture[all.size()]);
    	Arrays.sort(sorted, new Comparator<UpcomingFixture>(){
			@Override
			public int compare(UpcomingFixture f1, UpcomingFixture f2) {
				return f1.getFixtureId() - f2.getFixtureId();
			}});
    	return mapper.writeValueAsString(sorted);
    }
}
