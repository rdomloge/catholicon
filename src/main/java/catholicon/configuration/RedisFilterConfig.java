package catholicon.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import catholicon.archive.SeasonArchive;
import catholicon.filter.ArchiveFilter;

@Configuration
@ComponentScan(basePackages = { "catholicon"}) 
@EnableWebMvc
public class RedisFilterConfig {
	
	@Autowired
	private SeasonArchive seasonArchive;

	@Bean
    public FilterRegistrationBean redisFilter() {
        final FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new ArchiveFilter(seasonArchive));
        registrationBean.addUrlPatterns("/seasons");
        registrationBean.setName("redisFilter");
        registrationBean.setOrder(1);
        return registrationBean;
    }

}