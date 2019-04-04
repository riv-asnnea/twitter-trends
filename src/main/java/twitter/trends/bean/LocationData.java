package twitter.trends.bean;

import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

@Table(keyspace = "twitter_trends_keyspace", name = "woeid",
readConsistency = "QUORUM",
writeConsistency = "QUORUM",
caseSensitiveKeyspace = false,
caseSensitiveTable = false)
public class LocationData {
	
	@PartitionKey(0)
	private int woeid;
	
	@PartitionKey(1)
	private String name;
	
	private String country;	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public int getWoeid() {
		return woeid;
	}
	public void setWoeid(int woeid) {
		this.woeid = woeid;
	}
}
