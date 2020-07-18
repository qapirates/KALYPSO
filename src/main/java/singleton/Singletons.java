package singleton;
/*
*
* **********************************************************************
* Developer     :  A Nandy
* PROJECT       :  Kalypso
* FILENAME      :  Singletones.java
* REF			:  Singleton / Constants
* **********************************************************************
*/



import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public interface Singletons {
	
	final int MyDeviceId = 1;
	final String MACid = "B8-27-EB-83-6A-F5";
	final String tmpSense_DS18B20 = "28-030597943779"; 	//sensor actual id
	final String POST_AND_GETSettings = "https://piratesbay-chipper-roan-rs.eu-gb.mybluemix.net/api/SensorDataSet";
	
	static Logger startLogger() {

	    DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-HH-mm-ss");
	    Date date = new Date();
	    String fileName = "KalypsoLOG_"+dateFormat.format(date);

	    Logger LOGGER = Logger.getLogger(fileName);
	    LOGGER.setUseParentHandlers(false);
	    FileHandler fh;  
	    try {  
	    	if(System.getProperty("os.name").toLowerCase().contains("win")){
	    		
	    		File dir = new File(".//logs");
		    	if (!dir.exists()) dir.mkdirs(); 
		        fh = new FileHandler(".//logs//"+fileName+".log");  
	    	}
	    	else{
	    		File dir = new File("//home//pi//java_exec//logs");
		    	if (!dir.exists()) dir.mkdirs(); 
		        fh = new FileHandler("//home//pi//java_exec//logs//"+fileName+".log");  
	    	}
	    	
	        LOGGER.addHandler(fh);
	        SimpleFormatter formatter = new SimpleFormatter();  
	        fh.setFormatter(formatter);  
	        
	    } catch (IOException | SecurityException e) {  
	    	System.err.println("Encountered problem in Logger creation. System exits with error :-------------");
	        e.printStackTrace();  
	        System.exit(1);
	    }
		return LOGGER;
	}
	
}
