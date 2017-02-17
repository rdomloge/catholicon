package catholicon.configuration;

import javax.servlet.Filter;

import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import catholicon.filter.JwtFilter;
import catholicon.filter.ThreadLocalLoaderFilter;

@Configuration
@ComponentScan(basePackages = { "catholicon"}) 
@EnableWebMvc
public class WebMvcConfig extends WebMvcAutoConfiguration {
	
	@Bean
    public FilterRegistrationBean jwtFilter() {
        final FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new JwtFilter());
        registrationBean.addUrlPatterns("/api/*");

        return registrationBean;
    }

	public FilterRegistrationBean getLoaderRegistration() { 
		FilterRegistrationBean registration = new FilterRegistrationBean();
	    registration.setFilter(getLoaderFilter());
	    registration.addUrlPatterns("/season/*","/secure/*", "/frontpage/upcoming", "/matchcard/*", "/fixture/*");    
	    registration.setName("someFilter");
	    registration.setOrder(1);
	    return registration;
	}
	
	@Bean(name="ThreadLocalLoader")
	public Filter getLoaderFilter() {
		return new ThreadLocalLoaderFilter();
	}
}
