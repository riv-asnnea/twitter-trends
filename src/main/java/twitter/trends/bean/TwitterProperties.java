package twitter.trends.bean;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("twitter")
public class TwitterProperties {
	private String tokenUrl;
	private String clientId;
	private String clientSecret;
	private String trendsUrl;
	
	public String getTrendsUrl() {
		return trendsUrl;
	}
	public void setTrendsUrl(String trendsUrl) {
		this.trendsUrl = trendsUrl;
	}
	public String getTokenUrl() {
		return tokenUrl;
	}
	public void setTokenUrl(String tokenUrl) {
		this.tokenUrl = tokenUrl;
	}
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getClientSecret() {
		return clientSecret;
	}
	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}
	
	
}
