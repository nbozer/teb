package LogCreatorService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import com.google.common.util.concurrent.AbstractScheduledService;


public class ScheduledExecutor extends AbstractScheduledService 
{
	
	
	long time;
	boolean isReadToFile = false;
	 ConcurrentHashMap<String, Integer> hmLogsSendToKafka = new ConcurrentHashMap<>();
	 
	 public final String[] cities = {
			 "Istanbul",
			 "Tokyo",
			 "Beijing",
			 "London",
			 "Moskow"
	 };
	 
	public  ScheduledExecutor(long periodicTime) {
		this.time = periodicTime;			
		isReadToFile = ((this.time == 5) ? true : false );
		performOnInitialize();
	
	}
	
	public void performOnInitialize() {
		initializeHmLogsSendToKafka();

	}
	
	public void initializeHmLogsSendToKafka() {
		for (String city : cities) {
			hmLogsSendToKafka.put(city, 0);
		}
	}
	
	
@Override
   protected void runOneIteration() 
   {
	
	try {
		 //System.out.println("Executing.... Time :" + time + "currentTime :" + LocalDateTime.now() );
	      if(isReadToFile) {
	    	  //reading 5sec periodic
	    	  ArrayList readedLogs = ApplicationData.readLogToFile();
	    	  ArrayList logsSendToKafka = new ArrayList();
	    	 
	    	  
	    	  LocalDateTime currentTime = LocalDateTime.now();
	 
	    	  for(int i=0;i< readedLogs.size();i++)
	    	  {
	    		  String logValue = (String) readedLogs.get(i);
	    		  String[] logSplitted = logValue.split("\t");
	    		  String cityName = "";
	   
	    		  cityName = logSplitted[2];
	    		  //String[] timeSplitted = logSplitted[0].split(" ");
	    		  LocalDateTime logTime = LocalDateTime.parse(logSplitted[0]);
	    		  
	    		  LocalDateTime from = logTime;
	    	      LocalDateTime to = LocalDateTime.now();

	    	        LocalDateTime fromTemp = LocalDateTime.from(from);
	    	        long years = fromTemp.until(to, ChronoUnit.YEARS);
	    	        fromTemp = fromTemp.plusYears(years);

	    	        long months = fromTemp.until(to, ChronoUnit.MONTHS);
	    	        fromTemp = fromTemp.plusMonths(months);

	    	        long days = fromTemp.until(to, ChronoUnit.DAYS);
	    	        fromTemp = fromTemp.plusDays(days);

	    	        long hours = fromTemp.until(to, ChronoUnit.HOURS);
	    	        fromTemp = fromTemp.plusHours(hours);

	    	        long minutes = fromTemp.until(to, ChronoUnit.MINUTES);
	    	        fromTemp = fromTemp.plusMinutes(minutes);

	    	        long seconds = fromTemp.until(to, ChronoUnit.SECONDS);
	    	        fromTemp = fromTemp.plusSeconds(seconds);

	    	        long millis = fromTemp.until(to, ChronoUnit.MILLIS);
	    	        
	    	        if(years == 0 && months == 0 && days == 0 && hours == 0 && minutes == 0 && seconds <= 5 && seconds>0)
	    	        {
	    	        	logsSendToKafka.add(readedLogs.get(i));
	    	        	int logCount = hmLogsSendToKafka.get(cityName) + 1;
	    	        	hmLogsSendToKafka.put(cityName, (logCount));// +1
	    	        }
	    	       
	    	  }
	    	  sendToKafka();
	    		    	 
	    	System.out.println("ScheduledExecutor.runOneIteration() --- 5");
	    	  
	      }
	      else {
	    	  //writing 1sec periodic
	    	  String log = ApplicationData.createLog();
	    	 ApplicationData.writeLogToFile(log);
		    //	System.out.println("ScheduledExecutor.runOneIteration() --- 3");

	      }
	} catch (Exception e) {
		// TODO: handle exception
		e.printStackTrace();

	}
     
   }

   private void sendToKafka() {	
	    javax.swing.SwingUtilities.invokeLater(new Runnable() {
	        public void run() {
	        	ArrayList<String> kafkaList = new ArrayList<>();
	        	long systemTime = System.currentTimeMillis();
	        	for (String city : cities) {
	  	          LogCreatorService.refLogCreatorApi.get().publishLogCount(hmLogsSendToKafka.get(city),city,systemTime);				
	        	}

	        	
	          initializeHmLogsSendToKafka();
	        }
	    });
	   
   }

@Override
   protected Scheduler scheduler()
   {
        return Scheduler.newFixedRateSchedule(0, getTime(), TimeUnit.SECONDS);
   }

   @Override
   protected void startUp()
   {
       System.out.println("StartUp Activity....");
   }


   @Override
   protected void shutDown()
   {
       System.out.println("Shutdown Activity...");
   }

   public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}
	
 /*  public static void main(String[] args) throws InterruptedException
   {
       ScheduledExecutor se = new ScheduledExecutor();
       se.startAsync();
       Thread.sleep(15000);
       se.stopAsync();
   }*/
}