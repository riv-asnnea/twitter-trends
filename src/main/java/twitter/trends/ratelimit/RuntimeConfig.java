package twitter.trends.ratelimit;

import java.util.HashMap;
import java.util.Map;

public class RuntimeConfig {

	private static Map<String, Integer> rateLimitMap = null;
	private static Map<Integer, Integer> unauthorizedAcessMap = null;

	private RuntimeConfig() {
		//Prevent duplicate creation using reflection API

		if (rateLimitMap != null){
			throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
		}
	}

	public static Map<String, Integer> getRateLimitMap() {
		if (rateLimitMap == null){
			rateLimitMap  = new HashMap<String, Integer>();
		}
		return rateLimitMap;
	}
	
	public static Map<Integer, Integer> getUnauthorizedAccessMap() {
		if (unauthorizedAcessMap == null){
			unauthorizedAcessMap  = new HashMap<Integer, Integer>();
		}
		return unauthorizedAcessMap;
	}
}
