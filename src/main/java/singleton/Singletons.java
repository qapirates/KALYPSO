package singleton;
/*
*
* **********************************************************************
* Developer     :  A Nandy
* PROJECT       :  Kalipso
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
	//sensor actual id
	final String tmpSense_DS18B20 = "28-030597943779";
	final String CREATE = "https://piratesbay-chipper-roan-rs.eu-gb.mybluemix.net/api/ActualData";
	//final String GET_A_VALUE = "https://piratesbay-chipper-roan-rs.eu-gb.mybluemix.net/api/values/6";
	
	static Logger startLogger() {

	    DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-HH-mm-ss");
	    Date date = new Date();
	    String fileName = "KalipsoLOG_"+dateFormat.format(date);

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
