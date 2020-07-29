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
	public float Temperature_MAX;
	public float Temperature_MIN;
	public float Ph_MAX;
	public float Ph_MIN;
	public float Turbidity_MAX;
	public float Turbidity_MIN;
	public float O2_MAX;
	public float O2_MIN;
	public float Salinity_MAX;
	public float Salinity_MIN;
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
		
		System.err.println("Device settings are updated--");
		System.out.println("SensorIntervals: "+SensorIntervals);
		System.out.println("Temperature_MAX: "+Temperature_MAX);
		System.out.println("Temperature_MIN: "+Temperature_MIN);
		System.out.println("Ph_MAX: "+Ph_MAX);
		System.out.println("Ph_MIN: "+Ph_MIN);
		System.out.println("Turbidity_MAX: "+Turbidity_MAX);
		System.out.println("Turbidity_MIN: "+Turbidity_MIN);
		System.out.println("O2_MAX: "+O2_MAX);
		System.out.println("O2_MIN: "+O2_MIN);
		System.out.println("Ammonia_MAX: "+Salinity_MAX);
		System.out.println("Ammonia_MIN: "+Salinity_MIN);
		System.out.println("\n\n");
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
