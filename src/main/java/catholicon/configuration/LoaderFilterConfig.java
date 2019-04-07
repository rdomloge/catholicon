package catholicon.configuration;

import javax.servlet.Filter;

import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import catholicon.filter.JwtFilter;
import catholicon.filter.ThreadLocalLoaderFilter;

@Configuration
public class LoaderFilterConfig extends WebMvcAutoConfiguration {
	
	@Bean
    public FilterRegistrationBean jwtFilter() {
        final FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new JwtFilter());
        registrationBean.addUrlPatterns("/api/*");

        return registrationBean;
    }

	@Bean
	public FilterRegistrationBean getLoaderRegistration() { 
		FilterRegistrationBean registration = new FilterRegistrationBean();
	    registration.setFilter(getLoaderFilter());
	    registration.addUrlPatterns(
	    		"/seasons",
	    		"/season/*",
	    		"/secure/*", 
	    		"/frontpage/upcoming", 
	    		"/matchcard/*", 
	    		"/fixture/*",
	    		"/clubs/*",
	    		"/committee",
	    		"/newsitems");    
	    registration.setName("loaderFilter");
	    registration.setOrder(3);
	    return registration;
	}
	
	@Bean(name="ThreadLocalLoader")
	public Filter getLoaderFilter() {
		return new ThreadLocalLoaderFilter();
	}
}
