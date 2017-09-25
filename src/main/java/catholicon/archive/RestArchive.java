package catholicon.archive;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RestArchive {

	
	private RedisTemplate<String, Object> redisTemplate;
    private HashOperations<String,Integer,String> hashOps;
    
    @Autowired
    public RestArchive(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
 
    @PostConstruct
    private void init() {
        hashOps = redisTemplate.opsForHash();
    }
    
    public void save(String url, String json) {
    	hashOps.put(url, 1, json);
    }
    
    public String find(String url) {
    	return hashOps.get(url, 1);
    }
    
    public boolean exists(String url) {
    	return hashOps.hasKey(url, 1);
    }
}
