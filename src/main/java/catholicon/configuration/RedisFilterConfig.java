package catholicon.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import catholicon.archive.RestArchive;
import catholicon.filter.archive.RestCachingFilter;

@Configuration
@ComponentScan(basePackages = { "catholicon"}) 
@EnableWebMvc
public class RedisFilterConfig {
	
	@Autowired
	private RestArchive restArchive;
	
	@Bean
    public FilterRegistrationBean cacheFilter() {
        final FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new RestCachingFilter(restArchive));
        registrationBean.addUrlPatterns(
	    		"/seasons",
	    		"/season/*",
	    		"/division",
	    		"/secure/*", 
	    		"/frontpage/upcoming", 
	    		"/matchcard/*", 
	    		"/fixture/*"); 
        registrationBean.setName("restArchiveFilter");
        registrationBean.setOrder(1);
        return registrationBean;
    }
	
//	@Bean
//    public FilterRegistrationBean redisFilter() {
//        final FilterRegistrationBean registrationBean = new FilterRegistrationBean();
//        registrationBean.setFilter(new SeasonArchiveFilter(seasonArchive));
//        registrationBean.addUrlPatterns("/seasons");
//        registrationBean.setName("seasonArchiveFilter");
//        registrationBean.setOrder(1);
//        return registrationBean;
//    }
//	
//	@Bean
//	public FilterRegistrationBean frontPageFilter() {
//		final FilterRegistrationBean registrationBean = new FilterRegistrationBean();
//        registrationBean.setFilter(new FrontPageArchiveFilter(frontPageArchive));
//        registrationBean.addUrlPatterns("/frontpage/upcoming");
//        registrationBean.setName("frontPageArchiveFilter");
//        registrationBean.setOrder(1);
//        return registrationBean;
//	}

}