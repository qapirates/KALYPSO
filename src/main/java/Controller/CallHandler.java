package Controller;
/*
 *
 * **********************************************************************
 * Developer     :  A Nandy
 * PROJECT       :  Kalipso
 * FILENAME      :  CallHandler.java
 * REF			 :  HTTP/HTTPS
 * **********************************************************************
 */


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.HttpsURLConnection;

public final class CallHandler {
	private Logger logger = null;
	private BufferedReader br = null;
	
	CallHandler(Logger logger){
		this.logger = logger;
	}

	public boolean GetMethod(String url_) {
		// String Token = "Bearer "+ bearerToken;
		// String Token = "Basic "+
		// Base64.getEncoder().encode(("username:password").getBytes());
		HttpsURLConnection con1 = null;
		HttpURLConnection con2 = null;
		StringBuilder response = new StringBuilder();
		int code = 0;
		try {
			URL url = new URL(url_);
			if (url_.contains("https")) {
				con1 = (HttpsURLConnection) url.openConnection();
				con1.setConnectTimeout(15000);
				con1.setReadTimeout(10000);
				con1.setRequestMethod("GET");
				con1.setRequestProperty("Content-Type", "application/json; charset=utf-8");
				con1.setRequestProperty("Cache-Control", "no-cache");
				// con.setRequestProperty("Authorization", Token);
				// con.setRequestProperty("Cookie", cookie);
				con1.connect();

				code = con1.getResponseCode();
				if (con1 != null && code == HttpsURLConnection.HTTP_OK) {
					br = new BufferedReader(new InputStreamReader(con1.getInputStream()));
					String st = "";
					while ((st = br.readLine()) != null) {
						response.append(st);
					}
				} else {
					response.append("{}");
				}
			} else {
				con2 = (HttpURLConnection) url.openConnection();
				con2.setConnectTimeout(15000);
				con2.setReadTimeout(10000);
				con2.setRequestMethod("GET");
				con2.setRequestProperty("Content-Type", "application/json; charset=utf-8");
				con2.setRequestProperty("Cache-Control", "no-cache");
				// con.setRequestProperty("Authorization", Token);
				// con.setRequestProperty("Cookie", cookie);
				con2.connect();

				code = con2.getResponseCode();
				if (con2 != null && code == HttpURLConnection.HTTP_OK) {
					br = new BufferedReader(new InputStreamReader(con2.getInputStream()));
					String st = "";
					while ((st = br.readLine()) != null) {
						response.append(st);
					}
				} else {
					response.append("{}");
				}
			}
		} catch (IOException e) {
			System.err.println("Encountered problem in GET request: ");
			e.printStackTrace();
			logger.log(Level.WARNING, "Encountered problem in GET request: Stack: "+e.toString()+" , Params: "+url_);
		} finally {
			if (con1 != null)
				con1.disconnect();
			if (con2 != null)
				con2.disconnect();
			try{br.close();} catch (IOException e) {}
		}
		
		if(code == 200) return true;
		else return false;
	}

	public boolean PostMethod(String url_, String requestBody) {
		HttpsURLConnection con1 = null;
		HttpURLConnection con2 = null;
		StringBuilder response = new StringBuilder();
		OutputStream os =  null;
		int code = 0;
		
		try {
			
			URL url = new URL(url_);
			if (url_.contains("https")) {
				con1 = (HttpsURLConnection) url.openConnection();
				con1.setConnectTimeout(15000);
				con1.setReadTimeout(10000);
				con1.setRequestMethod("POST");
				con1.setRequestProperty("Content-Type", "application/json; charset=utf-8"); // con1.setRequestProperty("Content-Type",
																							// "text/plain");
				con1.setRequestProperty("Accept", "application/json; charset=utf-8");
				// con.setRequestProperty("Authorization", Token);
				// con.setRequestProperty("Cookie", cookie);
				con1.setDoOutput(true);

				os = con1.getOutputStream();
					byte[] input = requestBody.getBytes("utf-8");
					os.write(input, 0, input.length);
					
				con1.connect();				
				code = con1.getResponseCode();
				
				if (con1 != null && (code == HttpsURLConnection.HTTP_OK || code == HttpsURLConnection.HTTP_CREATED)){
					br = new BufferedReader(new InputStreamReader(con1.getInputStream()));
					String st = "";
					while ((st = br.readLine()) != null) {
						response.append(st);
					}
				} else {
					response.append("{}");
				}
			}
			else {
				con2 = (HttpURLConnection) url.openConnection();
				con2.setConnectTimeout(15000);
				con2.setReadTimeout(10000);
				con2.setRequestMethod("POST");
				con2.setRequestProperty("Content-Type", "application/json; charset=utf-8"); // con1.setRequestProperty("Content-Type",
																							// "text/plain");
				con2.setRequestProperty("Accept", "application/json; charset=utf-8");
				con2.setDoOutput(true);

				os = con2.getOutputStream();
					byte[] input = requestBody.getBytes("utf-8");
					os.write(input, 0, input.length);

				con2.connect();
				code = con2.getResponseCode();

				if (con2 != null && (code == HttpURLConnection.HTTP_OK || code == HttpURLConnection.HTTP_CREATED)) {
					br = new BufferedReader(new InputStreamReader(con2.getInputStream()));
					String st = "";
					while ((st = br.readLine()) != null) {
						response.append(st);
					}
				} else {
					response.append("{}");
				}
			}			
		} catch (IOException e) {
			System.err.println("Encountered problem in POST request: "+e.getMessage());
			logger.log(Level.SEVERE, "Encountered problem in POST request: Stack: "+e.toString()+" , Params: "+url_); 
			response.append("{}");
		} 
		finally {
			try{
				if (con1 != null)
					con1.disconnect();
				if (con2 != null)
					con2.disconnect();
				if (br!=null)	
					br.close();
				if(os!=null)
					os.close();
			} catch (IOException e) {}
		}
		String res = response.toString();
		System.out.println("\n\nRESPONSE: "+res);
		if((code == 201 || code == 200) && !res.equals("{}")) return true;
		else return false;
	}

}// end of class