package Controller;

import java.util.logging.Level;
import java.util.logging.Logger;

import Model.Payload;
import Model.PayloadBuffer;
import singleton.Singletons;

/*
*
* **********************************************************************
* Developer     :  A Nandy
* PROJECT       :  Kalypso
* FILENAME      :  Startup.java
* REF			:  Auxiliary controller for main
* **********************************************************************
*/

public final class DataHandler implements Singletons{
	private Logger _logger = null;	
	public String GUID;
	public boolean updated = false;
	
	public int MyDeviceID;
	public int SensorIntervals;
	public DataHandler(Logger logger) {
		this._logger = logger;
		this.GUID = "sdff=Jhgd-dj_747uyh4-djdui54smdu";
	}
	
	public void updateDeviceSettings(PayloadBuffer buff){
		try{
		if(MyDeviceID != Singletons.MyDeviceId || updated == false){
			return;
		}
		buff.reformBuffer(SensorIntervals);	
		
		System.out.println("Device settings are updated\n\n");
		}catch(Exception e){
			System.err.println("Encountered problem in updateDeviceSettings: "+e.getMessage());
			_logger.log(Level.WARNING, "Encountered problem in updateDeviceSettings: Stack: "+e.toString()); 
		}
		
	}

	public void sendBufferedDataToTheHost(PayloadBuffer payBuff, CallHandler httpCallHandler, Payload payload, GPIO gpio) {
		if (payBuff.buffer.isEmpty())
			return;

		while (payBuff.buffer.iterator().hasNext()) {
			payBuff.param = payBuff.buffer.peek();
			if(payBuff.param == null) continue;
			boolean isSuccess = httpCallHandler.PostMethod(Singletons.POST_AND_GETSettings, payload.
					PayLoad_For_Create(GUID, payBuff.param.getwaterTemperature(),payBuff.param.getwaterpH(),payBuff.param.getWaterTurbidity(),payBuff.param.getwaterO2(),payBuff.param.getwaterSalinity(), payBuff.param.getdateTimeStamp()));
			if (!isSuccess) {
				payBuff.param =  null;
				break;
			} else {
				payBuff.buffer.poll();
				System.out.println("VALUE UPLOADED FROM BUFFER");
				_logger.log(Level.INFO, "VALUE UPLOADED FROM BUFFER: -- Water Params: 1:"+payBuff.param.getwaterTemperature()+",2:"+payBuff.param.getwaterpH()+",3:"+ payBuff.param.getWaterTurbidity()+",4:"+ payBuff.param.getwaterO2()+",5:"+ payBuff.param.getwaterSalinity()+", " +payBuff.param.getdateTimeStamp());
				gpio.indicateProcessOk();
				payBuff.param=null;
			}
		}
	}
}
