package twitter.trends.bean;

import java.util.List;

public class TrendsBean {
	private List<Trend> trends;
    private String as_of;
    private String created_at;
    private List<Location> locations;
	public List<Trend> getTrends() {
		return trends;
	}
	public void setTrends(List<Trend> trends) {
		this.trends = trends;
	}
	public String getAs_of() {
		return as_of;
	}
	public void setAs_of(String as_of) {
		this.as_of = as_of;
	}
	public String getCreated_at() {
		return created_at;
	}
	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}
	public List<Location> getLocations() {
		return locations;
	}
	public void setLocations(List<Location> locations) {
		this.locations = locations;
	}
    
}
