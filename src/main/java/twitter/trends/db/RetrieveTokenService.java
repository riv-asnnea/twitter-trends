package twitter.trends.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;

import twitter.trends.bean.DatabaseProperties;

@Service
public class RetrieveTokenService {
	
	@Autowired
	private DatabaseProperties databaseProperties;
	
	@Cacheable(value = "accessTokenCache", key = "1")
	public String retrieveTokenFromDB() throws Exception {
		CqlSession session = CassandraConnector.getSession(databaseProperties.getKeyspace());
		String retrieveTokenQuery = "select access_token from "+ databaseProperties.getTokenTable() +
				" where appid = '"+ databaseProperties.getAppid() + "';";
		ResultSet set = session.execute(retrieveTokenQuery);
		Row row = set.one();
		String accessToken = row.getString(0);
		if(accessToken != null && !accessToken.isEmpty()) {
			return accessToken;
		} else {
			throw new Exception("The access token is not available");
		}
	}
}
