package twitter.trends.provider.connector;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import twitter.trends.ratelimit.RuntimeConfig;

public class TwitterConnector {
	public String connect(String urlString, String HTTPVerb, String contentType, String authorizationHeader,
			Map<String, String> parameters) throws IOException {
		URL url = new URL(urlString);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod(HTTPVerb);
		con.setRequestProperty("Content-Type", contentType);
		con.setRequestProperty("Authorization", authorizationHeader);
		if(parameters !=null && !parameters.isEmpty()) {
		//Map<String, String> parameters = new HashMap<>();
		//parameters.put("grant_type", "client_credentials");
			con.setDoOutput(true);
			DataOutputStream out = new DataOutputStream(con.getOutputStream());
			out.writeBytes(getParamsString(parameters));
			out.flush();
			out.close();
			
		}
		
		con.setConnectTimeout(3000);
		int status = con.getResponseCode();
		if(status == 401) {
			Map<Integer, Integer> unauthorizedRequestMap = RuntimeConfig.getUnauthorizedAccessMap();
			unauthorizedRequestMap.put(401, 1);
		}
		System.out.println("received response with status code:"+status);
		
		BufferedReader in = new BufferedReader(
				  new InputStreamReader(con.getInputStream()));
		
		String inputLine;
		StringBuffer content;
		if(urlString.contains("/twitter-trends/trends")) {
			content = new StringBuffer(20000);
		} else {
			content = new StringBuffer();
		}
		
		while ((inputLine = in.readLine()) != null) {
		    content.append(inputLine);
		}
		in.close();
		con.disconnect();
		return content.toString();
	}
	
	public String getParamsString(Map<String, String> params) 
			throws UnsupportedEncodingException{
		StringBuilder result = new StringBuilder();

		for (Map.Entry<String, String> entry : params.entrySet()) {
			result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
			result.append("=");
			result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
			result.append("&");
		}

		String resultString = result.toString();
		return resultString.length() > 0
				? resultString.substring(0, resultString.length() - 1)
						: resultString;
	}
		
}
