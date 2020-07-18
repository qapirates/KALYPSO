package Model;

import singleton.Singletons;

/*
 *
 * **********************************************************************
 * Developer     :  A Nandy
 * PROJECT       :  Kalypso
 * FILENAME      :  Payload.java
 * REF			:  JSON Payloads
 * **********************************************************************
 */


public final class Payload implements Singletons {
	StringBuilder sb =  null; 
	public String PayLoad_For_Create(String Guid , float temperature, float pH, float turbidity , float o2, float salinity, String dateTimeNow) throws UnsupportedOperationException {
		if(dateTimeNow == null || Guid==null || dateTimeNow.trim().length()==0 || Guid.trim().length()==0) 
			throw new UnsupportedOperationException("PayLoad_For_Create : NULL in payload params");
		try {
		    sb = new StringBuilder();
			sb.append("{"); 
			sb.append("\"DeviceId\": "); sb.append(Singletons.MyDeviceId); 
			sb.append(",\"Guid\":\""); sb.append(Guid);
			sb.append("\",\"MacID\":\""); sb.append(Singletons.MACid);
			sb.append("\",\"dateTime\":\""); sb.append(dateTimeNow); 
			sb.append("\",\"DataSet\": [ {"); 
			sb.append("\"ParameterID\":1,"); 
			sb.append("\"Value\":"); sb.append(temperature);
			sb.append("},{"); 
			sb.append("\"ParameterID\":2,"); 
			sb.append("\"Value\":"); sb.append(pH); 
			sb.append("},{"); 
			sb.append("\"ParameterID\":3,"); 
			sb.append("\"Value\":"); sb.append(turbidity); 
			sb.append("},{"); 
			sb.append("\"ParameterID\":4,"); 
			sb.append("\"Value\":"); sb.append(o2); 
			sb.append("},{");
			sb.append("\"ParameterID\":5,"); 
			sb.append("\"Value\":"); sb.append(salinity); 
			sb.append("} ] }");
			
			return sb.toString();
		} catch (Exception e) {
			throw new UnsupportedOperationException("PayLoad_For_Create : Payload is NULL");
		}
	}
	
	

}
