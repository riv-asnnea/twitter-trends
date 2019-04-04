package twitter.trends.api;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.datastax.driver.mapping.Result;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import twitter.trends.bean.DatabaseProperties;
import twitter.trends.bean.LocationData;
import twitter.trends.bean.TrendsBean;
import twitter.trends.bean.TwitterProperties;
import twitter.trends.db.RetrieveTokenService;
import twitter.trends.httpstatus.ServiceStatus;
import twitter.trends.provider.connector.TwitterConnector;

@RestController
public class TrendsController {
	
	@Autowired
	RetrieveTokenService retrieveTokenService;
	
	@Autowired
	private TwitterProperties twitterProperties;
	
	@Autowired
	private DatabaseProperties databaseProperties;
	
	
	//retrive trends from twitter
	@RequestMapping(value="/trends", method = RequestMethod.GET)
    public ServiceStatus retrieveTrends(@RequestParam(value="locationId", defaultValue="1") int locationId) {
		try {
			String accessToken = getToken();
			
			TwitterConnector connector = new TwitterConnector();
			
			String authHeader = "Bearer " + accessToken;
			String content = connector.connect(twitterProperties.getTrendsUrl()+"?id="+locationId, "GET", "application/json;charset=utf-8",
					authHeader, null);
			
			JsonParser jsonParser = new JsonParser();
			JsonArray arrayFromString = jsonParser.parse(content).getAsJsonArray();
			JsonObject jsonObject = (JsonObject) arrayFromString.get(0);
			
			//Create gson instance
			Gson gson = new Gson();
			TrendsBean trends = gson.fromJson(jsonObject, TrendsBean.class);
			
			return new ServiceStatus("success", "service call successfully executed", trends);
		} catch(Exception e) {
			e.printStackTrace();
			return new ServiceStatus("error", "service call failed", null);
		}
    }
	
	
	//retrive location data from database, to be used to search the trends API
	@RequestMapping(value="/locations", method = RequestMethod.GET)
	@Cacheable(value = "locationData")
    public ServiceStatus retrieveLocations() {
		Session session = null;
		Cluster cluster = null;
		try {
			
			cluster = Cluster.builder().addContactPoint("127.0.0.1").build();
			session = cluster.connect();
			MappingManager manager = new MappingManager(session);
			Mapper<LocationData> mapper = manager.mapper(LocationData.class);
			
			String retrieveAllRowsQuery = "select * from "+ databaseProperties.getKeyspace() + "." +
			databaseProperties.getWoeidTable() +";";
			ResultSet results = session.execute(retrieveAllRowsQuery);
			Result<LocationData> locations = mapper.map(results);
			if(!session.isClosed()) {
				session.close();
			}
			if(!cluster.isClosed()) {
				cluster.close();
			}
			return new ServiceStatus("success", "service call successfully executed", locations.all());
		} catch(Exception e) {
			e.printStackTrace();
			return new ServiceStatus("error", "service call failed", null);
		} finally {
			if(!session.isClosed()) {
				session.close();
			}
			if(!cluster.isClosed()) {
				cluster.close();
			}
		}
    }

	public String getToken() throws Exception {
		return retrieveTokenService.retrieveTokenFromDB();
	}
}
