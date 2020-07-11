package Model;
/*
 *
 * **********************************************************************
 * Developer     :  A Nandy
 * PROJECT       :  Kalipso
 * FILENAME      :  Payload.java
 * REF			 :  JSON Payloads
 * **********************************************************************
 */


public final class Payload {
	
	public String PayLoad_For_Create(float value, String dateTimeNow) throws UnsupportedOperationException {
		if(dateTimeNow == null || dateTimeNow.trim().length()==0) 
			throw new UnsupportedOperationException("PayLoad_For_Create : Date-Time is NULL in payload");
		try {
			
			String payLd = "{\r\n" + 
						"    \r\n" + 
						"    \"Device_Id\":1,\r\n" + 
						"    \"Param_Id\":1,\r\n" + 
						"    \"Input_Value\":" + value + ",\r\n" + 
						"    \"DataEntryTime\":\"" + dateTimeNow + "\"\r\n" +
						" \r\n" + "}";
			return payLd;
		} catch (Exception e) {
			throw new UnsupportedOperationException("PayLoad_For_Create : Payload is NULL");
		}
	}
	
	

}
