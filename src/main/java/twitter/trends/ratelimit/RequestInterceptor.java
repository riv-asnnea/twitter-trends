package twitter.trends.ratelimit;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class RequestInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle
	(HttpServletRequest request, HttpServletResponse response, Object handler) 
			throws Exception {
		Map<String, Integer> pathMap = RuntimeConfig.getRateLimitMap();
		String trendsPath = "/twitter-trends/trends";
		String newTokenPath = "/twitter-trends/token/new";
		if(request.getRequestURI().equals(trendsPath)) {
			if(pathMap.get(trendsPath) != null) {
				// limit trends API to 5 requests per minute
				if(pathMap.get(trendsPath) <= 4) {
					pathMap.put(trendsPath, pathMap.get(trendsPath) + 1);
					response.addHeader("X-RateLimit-Limit", "5/60s");
					response.addIntHeader("X-RateLimit-Remaining", 5 - pathMap.get(trendsPath));
				} else {
			        response.sendError(429, "rate limit exceeded");
			        return false;
				}
			} else {
				pathMap.put(trendsPath, 1);
				response.addHeader("X-RateLimit-Limit", "5/60s");
				response.addIntHeader("X-RateLimit-Remaining", 4);
			}
		} else if(request.getRequestURI().equals(newTokenPath)) {
			// limit new token API to the case when we receive a 401
			Map<Integer, Integer> unauthorizedRequestMap = RuntimeConfig.getUnauthorizedAccessMap();
			if(!unauthorizedRequestMap.containsKey(401)) {
				response.sendError(403, "a new token can only be requested when a previous "
						+ "request failed with an unauthorized status");
		        return false;
			}
		}
		return true;
	}
}
