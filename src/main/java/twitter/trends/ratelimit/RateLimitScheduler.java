package twitter.trends.ratelimit;

import java.util.Map;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class RateLimitScheduler {
	@Scheduled(fixedRate = 60000)
	public void scheduleTaskWithFixedRate() {
		Map<String, Integer> pathMap = RuntimeConfig.getRateLimitMap();
		String trendsPath = "/twitter-trends/trends";
		if(pathMap != null && pathMap.containsKey(trendsPath)) {
			pathMap.put(trendsPath, 0);
		}
	}


}
