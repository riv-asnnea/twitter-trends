package twitter.trends.httpstatus;

public class ServiceStatus {
    private String status;
    private String errorMessage;
    private Object resultObject;

	public ServiceStatus(String status) {
        this.status = status;
    }
    
    public ServiceStatus(String status, String errorMessage, Object resultObject) {
    	this.status = status;
        this.errorMessage = errorMessage;
        this.resultObject = resultObject;
    }
     
    public String getStatus() {
		return status;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public Object getResultObject() {
		return resultObject;
	}

	public void setResultObject(Object resultObject) {
		this.resultObject = resultObject;
	}

}
