package Controller;
/*
 *
 * **********************************************************************
 * Developer     :  A Nandy
 * PROJECT       :  Kalypso
 * FILENAME      :  Startup.java
 * REF			 :  MAIN
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
	static float temperatureValue;
	public static int SensorIntervals = 30; // in minutes DEFAULT

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
			BufferHandler bufhandle = new BufferHandler(logger);
			GPIO gpio = new GPIO(logger);
			CallHandler httpCallHandler = new CallHandler(logger);
			Payload payload = new Payload();
			
	   /*  --------------------------------------------------------------------
		  ----------------------------------------------------------------------	*/
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		while (true) {
			try {

				bufhandle.sendBufferedDataToTheHost(payBuff, httpCallHandler, payload, gpio);
				
				temperatureValue = (float) gpio.getWaterTemperature();
				System.out.println("WATER TEMPERATURE: "+temperatureValue);
				
				String dateTimeNow = format.format(new Date());
				boolean isSuccess = httpCallHandler.PostMethod(Singletons.CREATE, payload.PayLoad_For_Create(temperatureValue, dateTimeNow));

				if (!isSuccess) {
					payBuff.push(temperatureValue, dateTimeNow);
					gpio.indicateProcessNotOk();
				} 
				else{ 
					gpio.indicateProcessOk();
				}
				
				Thread.sleep(5 * 1000);
				
			} catch (Exception ex) {
				System.err.println("Encountered problem in main procedure :: ");
				gpio.resetGPIOInternalParams();
				logger.log(Level.SEVERE, "Encountered problem in main procedure :: Stack: " + ex);
				System.gc();
			}
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

final class BufferHandler {
	private Logger _logger = null;

	BufferHandler(Logger logger) {
		this._logger = logger;
	}

	void sendBufferedDataToTheHost(PayloadBuffer payBuff, CallHandler httpCallHandler, Payload payload, GPIO gpio) {
		if (payBuff.buffer.isEmpty())
			return;

		while (payBuff.buffer.iterator().hasNext()) {
			payBuff.param = payBuff.buffer.peek();
			boolean isSuccess = httpCallHandler.PostMethod(Singletons.CREATE, payload.PayLoad_For_Create(payBuff.param.getwaterTemperature(), payBuff.param.getdateTimeStamp()));
			if (!isSuccess) {
				break;
			} else {
				payBuff.buffer.poll();
				_logger.log(Level.INFO, "VALUE UPLOADED FROM BUFFER: -- Water Temperature: "+payBuff.param.getwaterTemperature()+", Time Stamp: " +payBuff.param.getdateTimeStamp());
				gpio.indicateProcessOk();
			}
		}
	}
}
