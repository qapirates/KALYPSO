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
	public String PayLoad_For_Create(String Guid , float temperature, float pH, float turbidity , float o2, float salinity, String dateTimeNow) throws UnsupportedOperationException {
		if(dateTimeNow == null || Guid==null || dateTimeNow.trim().length()==0 || Guid.trim().length()==0) 
			throw new UnsupportedOperationException("PayLoad_For_Create : NULL in payload params");
		try {
			
			String payLd = "{\r\n" + 
					"    \"DeviceId\": "+ Singletons.MyDeviceId +",\r\n" + 
					"    \"Guid\":\"" + Guid  +"\",\r\n" + 
					"    \"MacID\":\""+ Singletons.MACid +"\",\r\n" + 
					"    \"dateTime\":\""+ dateTimeNow +"\",\r\n" + 
					"    \"DataSet\": [\r\n" + 
					"        {\r\n" + 
					"            \"ParameterID\":1,\r\n" + 
					"            \"Value\":"+ temperature +"\r\n" + 
					"        },\r\n" + 
					"        {\r\n" + 
					"            \"ParameterID\":2,\r\n" + 
					"            \"Value\":"+ pH +"\r\n" + 
					"        },\r\n" + 
					"        {\r\n" + 
					"            \"ParameterID\":3,\r\n" + 
					"            \"Value\":"+ turbidity +"\r\n" + 
					"        },\r\n" + 
					"        {\r\n" + 
					"            \"ParameterID\":4,\r\n" + 
					"            \"Value\":"+ o2 +"\r\n" + 
					"        },\r\n" + 
					"        {\r\n" + 
					"            \"ParameterID\":5,\r\n" + 
					"            \"Value\":"+ salinity +"\r\n" + 
					"        }\r\n" + 
					"    ]\r\n" + 
					" \r\n" + 
					"}";
			return payLd;
		} catch (Exception e) {
			throw new UnsupportedOperationException("PayLoad_For_Create : Payload is NULL");
		}
	}
	
	

}
