package twitter.trends;

import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.datastax.oss.driver.api.core.CqlSession;

import twitter.trends.bean.DatabaseProperties;
import twitter.trends.bean.TwitterProperties;
import twitter.trends.db.CassandraConnector;

@SpringBootApplication
@EnableConfigurationProperties({DatabaseProperties.class, TwitterProperties.class})
@EnableCaching
@EnableScheduling
public class SpringBootTomcatApplication extends SpringBootServletInitializer {
	
	@Autowired
	private DatabaseProperties databaseProperties;
	
	public static void main(String[] args) {
        SpringApplication.run(SpringBootTomcatApplication.class, args);
    }
	
    @PreDestroy
    public void stop() {
    	CqlSession session = CassandraConnector.getSession(databaseProperties.getKeyspace());
    	session.close();
    	System.out.println("---------------closed cql session---------------");
    }
}
