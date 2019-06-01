package LogCreatorService;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;

public class ApplicationData {

	public static boolean lockTxt;
	
	public enum CITY_NAME{
		Istanbul(0),
		Tokyo(1),
		Moskow(2),
		London(3),
		Beijing(4);
		
		private int cityCode;

		CITY_NAME(int cityCode) {
		    this.cityCode = cityCode;
		}

		public int getCITY_NAME() {
		    return this.cityCode;
		}
	}

	public enum LOG_LEVEL{
		INFO(0),
		WARN(1),
		FATAL(2),
		DEBUG(3),
		ERROR(4);
		
		private int logLevel;

		LOG_LEVEL(int logLevel) {
		    this.logLevel = logLevel;
		}

		public int getLOG_LEVEL() {
		    return this.logLevel;
		}
	}
	
	public static String createLog() {
		CityLogModel cityLogModel = new CityLogModel();
		cityLogModel.setTimeStamp(LocalDateTime.now());
		cityLogModel.setLogLevel( LOG_LEVEL.values()[(new Random().nextInt(LOG_LEVEL.values().length))].toString());
		cityLogModel.setLogServerCityName( CITY_NAME.values()[(new Random().nextInt(CITY_NAME.values().length))].toString());
		cityLogModel.setLogDetail("Hello-from-"+cityLogModel.getLogServerCityName());
		String createdLog =  cityLogModel.getTimeStamp() + "\t" + cityLogModel.getLogLevel() 
								+ "\t" + cityLogModel.getLogServerCityName() + "\t"+cityLogModel.getLogDetail();
		return createdLog;
	}

	public static ArrayList readLogToFile()
	{
		ArrayList logs = new ArrayList();
	       synchronized(ApplicationData.class){
	    	   BufferedReader reader = null;
	   		try {
	   			String logPath = getLogPath();
	   			if(logPath != "")
	   			{
	   				File f = new File(logPath);
	   				if(f.exists() && !f.isDirectory()) { 
	   					reader = new BufferedReader(new FileReader(
	   							logPath));
	   				}	if(reader != null)
	   				{
	   					String line = reader.readLine();
	   					while (line != null) {
	   						//System.out.println("readLogToFile READING LINE:"+line);
	   						// read next line
	   						line = reader.readLine();
	   						if(line != null) {
	   							logs.add(line);
	   						}
	   						
	   						
	   					}
	   					reader.close();
	   				}
	   				
	   			}
	   					
	   	
	   		} catch (IOException e) {
	   			e.printStackTrace();
	   		}
	   		return logs;
	       }
	           
	           
		
		
	}
	public static void writeLogToFile(String txt)  {
		
		synchronized(ApplicationData.class){
			try {
				
			//	System.out.println("writeLogToFile WRITING LINE:"+txt);
				String logPath = getLogPath();
				File file = new File(logPath);

		        if (!file.exists()) {
		            file.createNewFile();
		        }

		        FileWriter fileWriter = new FileWriter(file, true);
		        BufferedWriter bWriter = new BufferedWriter(fileWriter);
		        bWriter.write(txt);
		        bWriter.newLine();
		        bWriter.close();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}
	
	public static String getLogPath()  {
		try {
			 String tempPath = new File(".").getAbsolutePath();
		     String realPath = "";
		     
		     if ((tempPath != null) && (tempPath.length() > 0)) {
		    	 realPath = tempPath.substring(0, tempPath.length() - 1);//delete .
		      }
		     return realPath+"resources/createdLog.txt";
			
		}catch(Exception e)
		{
			return "";
		}
	
	}
}
