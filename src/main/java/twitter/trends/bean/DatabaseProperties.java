package twitter.trends.bean;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("db")
public class DatabaseProperties {
	private String appid;
	private String tokenTable;
	private String woeidTable;
	private String keyspace;
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public String getTokenTable() {
		return tokenTable;
	}
	public void setTokenTable(String tokenTable) {
		this.tokenTable = tokenTable;
	}
	public String getKeyspace() {
		return keyspace;
	}
	public void setKeyspace(String keyspace) {
		this.keyspace = keyspace;
	}
	public String getWoeidTable() {
		return woeidTable;
	}
	public void setWoeidTable(String woeidTable) {
		this.woeidTable = woeidTable;
	}

}
