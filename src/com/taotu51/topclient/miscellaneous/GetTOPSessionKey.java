package com.taotu51.topclient.miscellaneous;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetTOPSessionKey {
	String url;
	String appKey;
	public GetTOPSessionKey(String url, String appKey) {
		this.url = url;
		this.appKey = appKey;
	}
	public String getSessionKey() throws Exception {
		URL urlobj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) urlobj.openConnection(); 
		con.setRequestMethod("GET");
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
 
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		System.out.println(inputLine);

		return null;
	}
}
