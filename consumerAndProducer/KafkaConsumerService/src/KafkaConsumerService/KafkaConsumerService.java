package KafkaConsumerService;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.TopicPartition;
import org.osgi.service.component.annotations.*;




@Component(name="KafkaConsumerService", immediate = true)
public class KafkaConsumerService {

//	  ScheduledExecutor executorService5sec = null;// write 5 saniyede bir yapildigindan read islemide 5 saniyede bir yaparsak 
	  												//son 5 tane kafka datasini aldigimizda butun sehirleri elde etmis oluruz.
	ArrayList<RecordLog> consumerRecordLogList ;
	public static  Consumer<Long, String> consumer = null;
	public static long readRecordOldCount = 0;
	public final static long readIntervalCount = 5;
	 long currentLogSystemTime=0;
	 boolean isSameTimeLogs = true;
	boolean isActivate = false;
	boolean isConstructed = false;
	
	
	  public  KafkaConsumerService() {
//			if(executorService5sec == null) {
//				this.executorService5sec = new ScheduledExecutor(5);
//			}else {
//				executorService5sec.startAsync();
//			}		
		  isConstructed =true;
		  if(isActivate) {
			  runConsumer();
		  }
	}
	  
	@Activate
	public void activate() {
		System.out.println("KafkaConsumerService.activate()");
//		if(executorService5sec == null) {
//			this.executorService5sec = new ScheduledExecutor(5);
//		}else {
//			executorService5sec.startAsync();
//		}
			isActivate =true;
		  if(isActivate) {
			  runConsumer();
		  }
	}

	@Deactivate
	public void deactivate() {
		System.out.println("KafkaConsumerService.deactivate()");
	}
	
	  
	
	  void runConsumer() {
		   try {
			   if(consumer == null) 
				      consumer = ConsumerCreator.createConsumer();
		       int noMessageFound = 0;
		     
		       while (true) {
		    	   this.currentLogSystemTime = 0;
		       	  TopicPartition tp = new TopicPartition(IKafkaConstants.TOPIC_NAME, IKafkaConstants.KAFKA_PARTITION);
		       	  consumer.poll(0);
		       	  consumer.seekToEnd(Collections.singletonList(tp));
		       	  long lastIndx = consumer.position(tp);
		       	  
		       	  if((readRecordOldCount <= 0)) {
		       		  // ilk acilista Kafkadan veri okumayacak. Daha sonraki loop'ta okumaya baslyacak.
		       		readRecordOldCount = lastIndx;
		       		continue;
		       	  }
		       	  long readLogCount = lastIndx - readRecordOldCount;//Kafka'ya yeni gelen log sayisi
		       	  if(readLogCount < readIntervalCount)
		       		  continue; // 5 sehir icin logunda minumum 5 olmasi gerekiyor.Cunku publish edilirken 5 sehir birden publish ediliyor.

		       	  long readOffset = readRecordOldCount ;// oknmasi gereken 5 adet log'un baslangic offset degeri.(Kafka loglari icindeki sirasi)
		       	  consumer.seek(tp, readOffset);//butun yeni gelen loglar okunuyor.
		       	 ConsumerRecords<Long, String> consumerRecords = consumer.poll(10000);// 240 is the time in milliseconds consumer will wait if no record is found at broker.
		       	  if (consumerRecords.count() == 0) {
		             noMessageFound++;
		                 continue;
		       	  }else
		        	 System.out.println("ConsumerRecords Count: "+consumerRecords.count() );
		         
		       	  this.consumerRecordLogList = new ArrayList<>();		
		       	 
		         consumerRecords.records(tp).stream()
		         .filter(p->(p.offset()>=readRecordOldCount) && (p.offset()<readRecordOldCount+readIntervalCount)).
		         peek(p->System.out.println("p offset  " + p.offset())).forEach(filteredRecord->{
		        	 try {
		        			//String publishStr = "cityName:" + cityName+","+"logCount:"+logCount+","+"systemTime:"+systemTime;
			        	 String value = filteredRecord.value().trim();
			        	 String[] splittedValueByComma = value.split(",",3);
			        	 String[] splittedValueByColon1 = splittedValueByComma[0].split(":",2);
			        	 String[] splittedValueByColon2 = splittedValueByComma[1].split(":",2);
			        	 
			        	 String[] splittedValueByColon3 = splittedValueByComma[2].split(":",2);
			        	 String systeTimeString = splittedValueByColon3[1];
			        	 this.currentLogSystemTime = Long.valueOf(systeTimeString);
			        	 RecordLog recordlog = new RecordLog();
			        	 recordlog.setLog(filteredRecord.value());
			        	 recordlog.setSystemTime(currentLogSystemTime);
			        	 recordlog.setCityName(splittedValueByColon1[1]);
			        	 recordlog.setLogCount(splittedValueByColon2[1]);
			        	 
			        	 this.consumerRecordLogList.add(recordlog);
			        	 
			        
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
		         		        	 
		         });
		         try {
			    	// boolean isSameTimeLogs = consumerRecordLogList.stream().allMatch(p-> p.getSystemTime() == currentLogSystemTime );
			    	 
		        	
		        	 consumerRecordLogList.stream().forEach(p->{
		        		 if(p.getSystemTime() != currentLogSystemTime )
		        			 isSameTimeLogs = false;
		        	 });
		        	 
		        	 
		        	 if(isSameTimeLogs) {
			        	 
			        	 System.out.println("KafkaConsumerService.runConsumer()********************************************"); 	 
			        	 System.out.println("CurrenLogSystemTime  " + currentLogSystemTime);
			             System.out.println("consumerRecordLogList Count  " + consumerRecordLogList.stream().count() );
			            consumerRecordLogList.stream().forEach(p->{
			            	 System.out.println("Log:  " + p.getLog());
			            	 System.out.println("SystemTime:  " + p.getSystemTime());
			            	 writeLogToFile(p.getLog());
			             });
			             
			        	 System.out.println("KafkaConsumerService.runConsumer()********************************************");
			        	 readRecordOldCount +=(readIntervalCount);//old record count increment
		        	 }else {
		        		 readRecordOldCount += 1; //bir sonraki logdan itibaren bakilacak.
		        	 }	
				} catch (Exception e2) {
					// TODO: handle exception
					e2.printStackTrace();
				}
	
		         // commits the offset of record to broker. 
		          consumer.commitAsync();
		          
		         //print each record. 
//		         consumerRecords.forEach(record -> {
//		        	 
//		        	 
//		        	 System.out.println("KafkaConsumerService.runConsumer()********************************************");
//		             System.out.println("Record Key " + record.key());
//		             System.out.println("Record value " + record.value());
//		             System.out.println("Record partition " + record.partition());
//		             System.out.println("Record offset " + record.offset());	             
//		        	 System.out.println("KafkaConsumerService.runConsumer()********************************************");
//		        	 consumerRecordList.add(record);
//		        	 
//		          });
//		         
//		         // commits the offset of record to broker. 
//		          consumer.commitAsync();
		       }
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			
		}finally {
			 consumer.close();
		}
		  
	   }	

		public static void writeLogToFile(String txt)  {
			

			try {
				
			//	System.out.println("writeLogToFile WRITING LINE:"+txt);
				String logPath = getLogPath();
				System.out.println("logPath :" +logPath);
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
		
		public static String getLogPath()  {
			try {
//				 String tempPath = new File(".").getAbsolutePath();
//			     String realPath = "";
//			     
//			     if ((tempPath != null) && (tempPath.length() > 0)) {
//			    	 realPath = tempPath.substring(0, tempPath.length() - 1);//delete .
//			      }
				 String realPath = "/tmp";
			     return realPath+"/jspLog.txt";
				
			}catch(Exception e)
			{
				return "";
			}
		
		}
}
class RecordLog{
	public long systemTime;
	public String log;
	public String cityName;
	public String logCount;
	
	public long getSystemTime() {
		return systemTime;
	}
	public void setSystemTime(long systemTime) {
		this.systemTime = systemTime;
	}
	public String getLog() {
		return log;
	}
	public void setLog(String log) {
		this.log = log;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getLogCount() {
		return logCount;
	}
	public void setLogCount(String logCount) {
		this.logCount = logCount;
	}
	
}
