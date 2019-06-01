package KafkaProducerService;



import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.osgi.service.component.annotations.*;


import LogCreatorAPI.ILogCreatorAPI;

@Component(name="KafkaProducerService", immediate = true)
public class KafkaProducerService implements ILogCreatorAPI {

	public static Producer<Long, String> producer = null ;
	 public final String[] cities = {
			 "Istanbul",
			 "Tokyo",
			 "Beijing",
			 "London",
			 "Moskow"
	 };
	 
	public KafkaProducerService() {
		super();
		System.out.println("KafkaProducerService.KafkaProducerService()");
	}

	@Activate
	public void activate() {
		System.out.println("KafkaProducerService.activate()");
	}

	@Deactivate
	public void deactivate() {
		System.out.println("KafkaProducerService.deactivate()");
	}

	@Override
	public boolean publishLogCount(int logCount, String cityName, long systemTime) {
		// TODO Auto-generated method stub
		
		System.out.println("KafkaProducerService.publishLogCount()");
		System.out.println(logCount +" --" + cityName);
		
	    javax.swing.SwingUtilities.invokeLater(new Runnable() {
	        public void run() {
	          publish(logCount,cityName,systemTime);
	        }
	    });
		
		return true;
	}

	
    public  void publish(int logCount, String cityName, long systemTime) {
    	try {
    		
    		if(producer == null)
    			producer = ProducerCreator.createProducer();

        	String publishStr = "cityName:" + cityName+","+"logCount:"+logCount+","+"systemTime:"+systemTime;
           ProducerRecord<Long, String> record = new ProducerRecord<Long, String>(IKafkaConstants.TOPIC_NAME, publishStr);
           RecordMetadata metadata =   producer.send(record).get();
//        	System.out.println("KafkaProducerService.publish().--Record Published: " + publishStr);
//        	System.out.println("KafkaProducerService.publish()--metadata partition : " + metadata.partition() + 
//        														"metadata offset" + metadata.offset());
    		
		}  catch (Exception e) {
            System.out.println("Error in sending record");
            System.out.println(e);
         }
    	
    	
    }
    
//   public  byte [] serializeAvro(SpecificRecordBase message) {
//	   BinaryEncoder encoder = null;
//
//	   ByteArrayOutputStream stream = new ByteArrayOutputStream();
//	   encoder = EncoderFactory.get().binaryEncoder(stream, encoder);
//	   
//	   try {
//		datumWriter.write(message, encoder);
//		   encoder.flush();
//
//	} catch (IOException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}
//	return stream.toByteArray();
//    	
//    }
}
