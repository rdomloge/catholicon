package catholicon.configuration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableCaching
@EnableScheduling
@ComponentScan(basePackages = {"catholicon", "catholicon.dao"})
public class ApplicationMain {

	public static void main(String[] args) {
        SpringApplication.run(ApplicationMain.class, args);
    }
}
