package KafkaConsumerService;

import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.TopicPartition;

import com.google.common.util.concurrent.AbstractScheduledService;

public class ScheduledExecutor extends AbstractScheduledService 
{
	public static  Consumer<Long, String> consumer = null;
	public long recordCurrentCount = 0;
	public long recordOldCount = 0;
	long time;
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
		System.out.println("ScheduledExecutor.runOneIteration()");
		
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
	        public void run() {
	        	//runConsumer();
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


//   static void runConsumer() {
//	   try {
//		   if(consumer == null) 
//			      consumer = ConsumerCreator.createConsumer();		   
//	       int noMessageFound = 0;
//	       while (true) {
//	       	  TopicPartition tp = new TopicPartition(IKafkaConstants.TOPIC_NAME, IKafkaConstants.KAFKA_PARTITION);
//	       //  ConsumerRecords<Long, String> consumerRecords = consumer.poll(1000);
//	       	  consumer.poll(0);
//	       	  consumer.seekToEnd(Collections.singletonList(tp));
//	       	  long lastIndx = consumer.position(tp);
//	       	  consumer.seek(tp, lastIndx-1);
//	       	  ConsumerRecords<Long, String> consumerRecords = consumer.poll(4*60);// 240 is the time in milliseconds consumer will wait if no record is found at broker.
//	         if (consumerRecords.count() == 0) {
//	             noMessageFound++;
//	             if (noMessageFound > IKafkaConstants.MAX_NO_MESSAGE_FOUND_COUNT)
//	               // If no message found count is reached to threshold exit loop.  
//	               break;
//	             else
//	                 continue;
//	         }
//	         //print each record. 
//	         consumerRecords.forEach(record -> {
//	             System.out.println("Record Key " + record.key());
//	             System.out.println("Record value " + record.value());
//	             System.out.println("Record partition " + record.partition());
//	             System.out.println("Record offset " + record.offset());
//	          });
//	         // commits the offset of record to broker. 
//	          consumer.commitAsync();
//	       }
//	   consumer.close();
//	} catch (Exception e) {
//		// TODO: handle exception
//		e.printStackTrace();
//	}
//	  
//   }	 
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