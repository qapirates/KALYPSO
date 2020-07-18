/*
*
* **********************************************************************
* Developer     :  A Nandy
* PROJECT       :  Kalypso
* FILENAME      :  PayloadBuffer.java
* REF			:  An emergency n/w fault tolerant buffer for holding sensor data 
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
			lengthOfBuffer = (24 * 60) / SensorIntervals;  // A day long buffer
			buffer = new LinkedList<>();
		} catch (Exception e) {
			System.err.println("Can not allocate buffer for 1st time. Default buffer will be allocated."); e.printStackTrace();
			_logger.log(Level.SEVERE, "Can not allocate buffer for the first time. Default buffer will be allocated : Stack: "+e.getMessage());
			lengthOfBuffer = 48; // A day long buffer with 30 mins interval
			buffer = null;
			buffer = new LinkedList<>();
		}
	}
	
	public void push(float waterTemperature,float pH,float turbidity,float o2,float salinity, String dateTimeStamp) {
		try{
			if(buffer.size() >= lengthOfBuffer){
				buffer.clear();
				System.out.println("Buffer cleared. legth: "+lengthOfBuffer);
				_logger.log(Level.INFO, "Buffer cleared. legth: "+lengthOfBuffer);
			}
			buffer.add(new DoubleQueue(waterTemperature, pH, turbidity, o2, salinity, dateTimeStamp));
			_logger.log(Level.INFO, "Values pushed into buffer -- Water Params: 1:"+waterTemperature+",2:"+pH+",3:"+ turbidity+",4:"+ o2+",5:"+ salinity+",TimeStamp:" +dateTimeStamp);
			System.out.println("Values pushed into the buffer");
		}catch (Exception e) {
			System.err.println("Memory problemr. Reseting buffer");
			_logger.log(Level.SEVERE, "Memory problem. Reseting buffer: Stack: "+e.getMessage());
			buffer = null;
			System.gc();
			buffer = new LinkedList<>();
		}
	}
	
	public void reformBuffer(int SensorIntervals){
		try {
			int x = (24 * 60) / SensorIntervals;
			if(x <= 0) throw new UnsupportedOperationException("Buffer size calculated from user setting is zero");
			else lengthOfBuffer = x;
		} catch (Exception e) {
			System.err.println("Can not reform buffer length from the user settings"); e.printStackTrace();
			_logger.log(Level.WARNING, "Can not reform buffer length from the user settings: Stack: "+e.getMessage());
		}
		
	}
	
	//local data provider class
	public class DoubleQueue{
		private float waterTemperature;
		private float pH;
		private float turbidity;
		private float o2;
		private float salinity;
		private String dateTimeStamp;
		
		DoubleQueue(float waterTemperature,float pH,float turbidity,float o2,float salinity, String dateTimeStamp){
			this.waterTemperature = waterTemperature;
			this.pH = pH;
			this.turbidity = turbidity;
			this.o2 = o2;
			this.salinity = salinity;
			this.dateTimeStamp = dateTimeStamp;
		}
		
		public float getwaterTemperature(){
			return waterTemperature;
		}
		public float getwaterpH(){
			return pH;
		}
		public float getWaterTurbidity(){
			return turbidity;
		}
		public float getwaterO2(){
			return o2;
		}
		public float getwaterSalinity(){
			return salinity;
		}
		public String getdateTimeStamp(){
			return dateTimeStamp;
		}
	}
}
