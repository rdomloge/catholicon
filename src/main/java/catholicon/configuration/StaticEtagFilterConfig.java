package catholicon.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import catholicon.filter.StaticEtagFilter;

@Configuration
public class StaticEtagFilterConfig {
	
	@Autowired
	private ApplicationContext context;

	@Bean
    public FilterRegistrationBean cacheFilter() {
        final FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        StaticEtagFilter filter = context.getBean(StaticEtagFilter.class);
        registrationBean.setFilter(filter);
        registrationBean.addUrlPatterns(
        		"/static/*", 
        		"/static/img/*", 
        		"/static/script/*", 
        		"/static/html/*", 
        		"/static/style/*"); 
        
        registrationBean.setName("staticEtagFilter");
        registrationBean.setOrder(1);
        return registrationBean;
    }
}
