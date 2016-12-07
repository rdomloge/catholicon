package catholicon.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import catholicon.dao.Loader;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"catholicon", "catholicon.dao"})
public class CatholiconConfiguration extends WebMvcConfigurerAdapter {

	public static final int CACHE_TIME_ONE_HOUR = 1000*60*60;
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/static/html/**").addResourceLocations("/static/html/").setCachePeriod(CACHE_TIME_ONE_HOUR);
		registry.addResourceHandler("/static/script/**").addResourceLocations("/static/script/").setCachePeriod(CACHE_TIME_ONE_HOUR);
		registry.addResourceHandler("/static/style/**").addResourceLocations("/static/style/").setCachePeriod(CACHE_TIME_ONE_HOUR);
	}
	
	// equivalent for <mvc:default-servlet-handler/> tag
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }
    
    @Bean
	public ThreadLocal<Loader> loader() {
		return new ThreadLocal<Loader>() {
			@Override
			protected Loader initialValue() {
				return new Loader();
			}
			
		};
	}
}