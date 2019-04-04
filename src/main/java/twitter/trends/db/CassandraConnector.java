package twitter.trends.db;

import com.datastax.oss.driver.api.core.CqlSession;


public class CassandraConnector {
	
    private static CqlSession session = null;
    
    private CassandraConnector() {
    	//Prevent duplicate creation using reflection API
    	
        if (session != null){
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }
    }
 
    public static CqlSession getSession(String keyspace) {
    	System.out.println("keyspace while retriving session:" +keyspace);
    	if (session == null){
    		session  = CqlSession.builder().withKeyspace(keyspace).build();
    	}
        return session;
    }
}
