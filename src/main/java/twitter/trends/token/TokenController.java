package twitter.trends.token;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.datastax.oss.driver.api.core.CqlSession;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import twitter.trends.bean.DatabaseProperties;
import twitter.trends.bean.TwitterProperties;
import twitter.trends.db.CassandraConnector;
import twitter.trends.httpstatus.ServiceStatus;
import twitter.trends.provider.connector.TwitterConnector;

@RestController
public class TokenController {
    
	@Autowired
	private TwitterProperties twitterProperties;
	
	@Autowired
	private DatabaseProperties databaseProperties;
	
	@RequestMapping(value="/token/new", method = RequestMethod.GET)
	@CacheEvict(value = "accessTokenCache", allEntries = true)
    public ServiceStatus saveToken() {
		String accessToken = null;
		try {
			accessToken = getTokenFromTwitter();
		} catch(Exception e) {
			e.printStackTrace();
			return new ServiceStatus("error", "error while retrieving access token", null);
		}
		CqlSession session = CassandraConnector.getSession(databaseProperties.getKeyspace());
		String saveTokenQuery = "update access_token set access_token = '"+ accessToken +
				"' where appid = '"+ databaseProperties.getAppid() + "';";
		try {
			session.execute(saveTokenQuery);
			return new ServiceStatus("success");
		} catch (Exception e) {
			return new ServiceStatus("error", "error while writing token to DB", null);
		}        
    }
	
	public String getTokenFromTwitter() throws Exception {
		
		TwitterConnector connector = new TwitterConnector();
		String authHeader = "Basic " + returnBase64EncodedValue(twitterProperties.getClientId() + ":" + twitterProperties.getClientSecret());
		Map<String, String> parameters = new HashMap<>();
		parameters.put("grant_type", "client_credentials");
		String content = connector.connect(twitterProperties.getTokenUrl(), "POST", "application/x-www-form-urlencoded",
				authHeader, parameters);
		JsonParser jsonParser = new JsonParser();
		JsonElement jsonTree = jsonParser.parse(content);
		System.out.println("content returned:"+content);
		if(jsonTree.isJsonObject()) {
		    JsonObject jsonObject = jsonTree.getAsJsonObject();
		    JsonElement token = jsonObject.get("access_token");
		    if(!token.isJsonNull()) {
		    	return token.getAsString();
		    } else {
		    	throw new Exception("The token is not present.");
		    }
		} else {
			throw new Exception("Could not retrieve token from Twitter.");
		}
	}
	
	public String returnBase64EncodedValue(String rawString) {
		if(rawString!= null)
			return Base64.getEncoder().encodeToString(rawString.getBytes());
		else return null;
	}
}
