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
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.HttpsURLConnection;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public final class CallHandler {
	private Logger logger = null;
	private BufferedReader br = null;
	private DataHandler handler = null;
	
	CallHandler(Logger logger, DataHandler handler){
		this.logger = logger;
		this.handler = handler;
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
		mapNewSettings(res);
		if((code == 201 || code == 200) && !res.equals("{}")){
			System.out.println("POSTed to the host");
			return true;
		}
		else{
			System.out.println("Can not POST to the host");
			return false;
		}
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

	private void mapNewSettings(String response){
		try{
		    handler.updated = false;
		Object obj=JSONValue.parse(response);  
	    JSONObject jsonObject = (JSONObject) obj;  
	    String guid = (String) jsonObject.get("newGuid");  //store new token
	    if(guid!=null && guid.length()>0) handler.GUID =  guid;
	    
	    JSONArray thresHoldSets = (JSONArray) jsonObject.get("thresHoldSets");
	    @SuppressWarnings("unchecked")
		Iterator<JSONObject> it = thresHoldSets.iterator();
	    float[] max = new float[5];;
	    float[] min= new float[5];
	    int i =0;
        while (it.hasNext()) {
        	JSONObject ob = it.next();
            max[i] = Float.parseFloat(ob.get("red_Threshold_High").toString());
            min[i] = Float.parseFloat(ob.get("red_Threshold_Low").toString());
            i++;
        }

	    int deviceID = Integer.parseInt(jsonObject.get("deviceID").toString());
	    int interval = Integer.parseInt(jsonObject.get("interval").toString());
	    float Temperature_MAX = max[0];
	    float Temperature_MIN = min[0];
	    float Ph_MAX = max[1];
	    float Ph_MIN = min[1];
	    float Turbidity_MAX = max[2];
	    float Turbidity_MIN = min[2];
	    float O2_MAX = max[3];
	    float O2_MIN = min[3];
	    float Salinity_MAX = max[4];
	    float Salinity_MIN = min[4];
	    
	        
	    if(handler.SensorIntervals != interval) handler.updated = true;
	    else if(handler.Temperature_MAX != Temperature_MAX) handler.updated = true;
	    else if(handler.Temperature_MIN != Temperature_MIN) handler.updated = true;
	    else if(handler.Ph_MAX != Ph_MAX) handler.updated = true;
	    else if(handler.Ph_MIN != Ph_MIN) handler.updated = true;
	    else if(handler.Turbidity_MAX != Turbidity_MAX) handler.updated = true;
	    else if(handler.Turbidity_MIN != Turbidity_MIN) handler.updated = true;
	    else if(handler.O2_MAX != O2_MAX) handler.updated = true;
	    else if(handler.O2_MIN != O2_MIN) handler.updated = true;
	    else if(handler.Salinity_MAX != Salinity_MAX) handler.updated = true;
	    else if(handler.Salinity_MIN != Salinity_MIN) handler.updated = true;
	    
	    handler.MyDeviceID = deviceID;
		if (handler.updated) {
			handler.SensorIntervals = interval;

			handler.Temperature_MAX = Temperature_MAX;
			handler.Temperature_MIN = Temperature_MIN;
			
			handler.Ph_MAX = Ph_MAX;
			handler.Ph_MIN = Ph_MIN;
			
			handler.Turbidity_MAX = Turbidity_MAX;
			handler.Turbidity_MIN = Turbidity_MIN;
			
			handler.O2_MAX = O2_MAX;
			handler.O2_MIN = O2_MIN;
			
			handler.Salinity_MAX = Salinity_MAX;
			handler.Salinity_MIN = Salinity_MIN;
			
			System.out.println("Device settings are stored from response");
		}
		}catch(Exception e){
			System.err.println("Encountered problem in mapNewSettings: "+e.getMessage()+" ,Param: "+response);
			logger.log(Level.WARNING, "Encountered problem in mapNewSettings: Stack: "+e.toString()+" , Param: "+response); 
		}
	    
	}
	




}// end of class
