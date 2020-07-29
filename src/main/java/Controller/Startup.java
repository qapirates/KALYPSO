package Controller;
/*
 *
 * **********************************************************************
 * Developer     :  A Nandy
 * PROJECT       :  Kalypso
 * FILENAME      :  Startup.java
 * REF			:  MAIN
 * **********************************************************************
 */

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import Model.Payload;
import Model.PayloadBuffer;
import singleton.Singletons;

public class Startup {
	static float temperature;
	static float pH;
	static float particles;
	static float o2;
	static float salinity;
	static String dateTimeNow;
	static DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	static int SensorIntervals = 15; // in minutes DEFAULT
	
	
	public static void main(String[] args) {
		if (args.length > 0) {
			if (args[0].contains("--help")) {
				infos();
				return;
			}
			if (args[0].contains("--diagnose")) {
				GPIO.CPUTemperature();
				return;
			}

			if (args[0].contains("--setInterval") && args[1].length() > 0) {
				int val = Integer.parseInt(args[1]);
				if (val > 0 && val <= 24 * 60) {
					SensorIntervals = val;
					System.out.println("Scan Interval: " + SensorIntervals);
				} else {
					System.out.println("Scan Interval is set to default");
				}
			}
		}
		/* -------------------< dependencies >----------------------------------
		 	-------------------------------------------------------------------		*/
			Logger logger = Singletons.startLogger();
			PayloadBuffer payBuff = new PayloadBuffer(logger, SensorIntervals);
			DataHandler handle = new DataHandler(logger);
			GPIO gpio = new GPIO(logger);
			CallHandler httpCallHandler = new CallHandler(logger, handle);
			Payload payload = new Payload();
			handle.SensorIntervals = SensorIntervals;
	   /*  --------------------------------------------------------------------
		  ----------------------------------------------------------------------	*/
		while (true) {
			try {
				handle.sendBufferedDataToTheHost(payBuff, httpCallHandler, payload, gpio);
				
				temperature = (float) gpio.getWaterTemperature();
				System.out.println("---->\nWATER TEMPERATURE (C): "+temperature);
				pH = (float) gpio.getWaterpH();
				System.out.println("WATER pH: "+pH);
				particles = (float) gpio.getWaterTurbidity();
				System.out.println("WATER PARTICLES (mg/L): "+particles);
				o2 = (float) gpio.getWaterO2();
				System.out.println("WATER DISSOLVED OXYGEN (mg/L): "+o2);
				salinity = (float) gpio.getWaterAmmonia();
				System.out.println("WATER Ammonia (mg/L): "+salinity);				
				dateTimeNow = format.format(new Date()).replace(" ", "T");
				
				boolean isSuccess = httpCallHandler.PostMethod(Singletons.POST_AND_GETSettings, payload.
						PayLoad_For_Create(handle.GUID,temperature,pH,particles,o2,salinity,dateTimeNow));

				if (!isSuccess) {
					payBuff.push(temperature,pH,particles,o2,salinity, dateTimeNow);
					gpio.indicateProcessNotOk();
				} 
				else{ 
					gpio.indicateProcessOk();
				}
				
				alert(handle, temperature, pH, particles, o2, salinity);
				
				handle.updateDeviceSettings(payBuff);
				
				Thread.sleep(handle.SensorIntervals / 6 * 1000);
				
			} catch (Exception ex) {
				System.err.println("Encountered problem in main procedure :: ");
				gpio.resetGPIOInternalParams();
				logger.log(Level.SEVERE, "Encountered problem in main procedure :: Stack: " + ex);
				System.gc();
				try {Thread.sleep(handle.SensorIntervals/6 * 1000);
				} catch (InterruptedException e) {}
			}
		}
	}


	private static void alert(DataHandler handle, float temperature, float pH, float particles, float o2, float salinity){
		if(temperature > handle.Temperature_MAX){
			System.out.println("------------------\nALERT AS TEMPERATURE IS HIGHER THAN THE LIMIT!!\n---------------------");
		}
		if(temperature <  handle.Temperature_MIN){
			System.out.println("------------------\nALERT AS TEMPERATURE IS LOWER THAN THE LIMIT!!\n---------------------");
		}
		if(pH > handle.Ph_MAX){
			System.out.println("------------------\nALERT AS pH IS HIGHER THAN THE LIMIT!!\n---------------------");
		}
		if(pH <  handle.Ph_MIN){
			System.out.println("------------------\nALERT AS pH IS LOWER THAN THE LIMIT!!\n---------------------");
		}
		if(particles > handle.Turbidity_MAX){
			System.out.println("------------------\nALERT AS TURBIDITY IS HIGHER THAN THE LIMIT!!\n---------------------");
		}
		if(particles <  handle.Turbidity_MIN){
			System.out.println("------------------\nALERT AS TURBIDITY IS LOWER THAN THE LIMIT!!\n---------------------");
		}
		if(o2 > handle.O2_MAX){
			System.out.println("------------------\nALERT AS DOx IS HIGHER THAN THE LIMIT!!\n---------------------");
		}
		if(o2 <  handle.O2_MIN){
			System.out.println("------------------\nALERT AS DOx IS LOWER THAN THE LIMIT!!\n---------------------");
		}
		if(salinity > handle.Salinity_MAX){
			System.out.println("------------------\nALERT AS AMMONIA LEVEL IS HIGHER THAN THE LIMIT!!\n---------------------");
		}
		if(salinity <  handle.Salinity_MIN){
			System.out.println("------------------\nALERT AS AMMONIA LEVEL IS LOWER THAN THE LIMIT!!\n---------------------");
		}
	}


	private static void infos(){
		System.out.println("--diagnose");
		System.out.println(" Displays some important parameters of the device.");
		System.out.println("--setInterval <val>");
		System.out.println(" This will set the the scanning interval in minutes and also allocate a day-long queue for any sudden n/w connectivity "
				+ "problem. Defatul value is 30 minutes. Max value 1440");
	}
}


