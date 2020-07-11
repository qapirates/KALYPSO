/*
*
* **********************************************************************
* Developer     :  A Nandy
* PROJECT       :  Kalipso
* FILENAME      :  PayloadBuffer.java
* REF			:  An emergency n/w fault buffer for holding sensor data 
* **********************************************************************
*/


package Model;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.LinkedList;
import java.util.Queue;

public class PayloadBuffer {
	private Logger _logger = null;
	public Queue<DoubleQueue> buffer;
	private int lengthOfBuffer = 0;
	
	public DoubleQueue param=null;
	public PayloadBuffer(Logger logger, int SensorIntervals) {
		this._logger = logger;
		try {
			lengthOfBuffer = (int)(24 * 60) / SensorIntervals;  // A day long buffer
			buffer = new LinkedList<>();
		} catch (Exception e) {
			System.err.println("Encountered problem. Can not allocate buffer. System exits with error"); e.printStackTrace();
			_logger.log(Level.SEVERE, "Encountered problem. Can not allocate buffer. System exits with error : Stack: "+e.getMessage());
			System.exit(1);
		}
	}
	
	public void push(float waterTemperature, String dateTimeStamp) {
		if(buffer.size() == lengthOfBuffer) buffer.clear();
		buffer.add(new DoubleQueue(waterTemperature, dateTimeStamp));
		_logger.log(Level.INFO, "Value is pushed into the buffer -- Water Temperature: "+waterTemperature+", Time Stamp: " +dateTimeStamp);
	}
	
	
	//local class
	public class DoubleQueue{
		private float waterTemperature;
		private String dateTimeStamp;
		
		DoubleQueue(float waterTemperature, String dateTimeStamp){
			this.waterTemperature = waterTemperature;
			this.dateTimeStamp = dateTimeStamp;
		}
		
		public float getwaterTemperature(){
			return waterTemperature;
		}
		public String getdateTimeStamp(){
			return dateTimeStamp;
		}
	}
}
