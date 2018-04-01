package catholicon.configuration;

import javax.servlet.Filter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.filter.ShallowEtagHeaderFilter;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.RedirectViewControllerRegistration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableScheduling
@ComponentScan(basePackages = {"catholicon", "catholicon.dao"})
public class CatholiconConfiguration extends WebMvcConfigurerAdapter {

	public static final int CACHE_TIME_ONE_HOUR = 60*60;
	public static final int CACHE_TIME_ONE_DAY = CACHE_TIME_ONE_HOUR * 24;
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/static/html/**").addResourceLocations("/static/html/").setCachePeriod(CACHE_TIME_ONE_DAY);
		registry.addResourceHandler("/static/script/**").addResourceLocations("/static/script/").setCachePeriod(CACHE_TIME_ONE_DAY);
		registry.addResourceHandler("/static/style/**").addResourceLocations("/static/style/").setCachePeriod(CACHE_TIME_ONE_DAY);
		registry.addResourceHandler("/static/img/**").addResourceLocations("/static/img/").setCachePeriod(CACHE_TIME_ONE_DAY);
		registry.addResourceHandler("/static/font/**").addResourceLocations("/static/font/").setCachePeriod(CACHE_TIME_ONE_DAY);
	}
	
	@Bean
    public Filter shallowEtagHeaderFilter() {
        return new ShallowEtagHeaderFilter();
    }
	
	// equivalent for <mvc:default-servlet-handler/> tag
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }
 
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        RedirectViewControllerRegistration redirectViewControllerRegistration = 
        		registry.addRedirectViewController("/", "/static/html/index.html");
        redirectViewControllerRegistration.setContextRelative(true);
        redirectViewControllerRegistration.setStatusCode(HttpStatus.FOUND);
    }
}