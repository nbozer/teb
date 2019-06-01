package LogCreatorService;

import java.util.concurrent.atomic.AtomicReference;

import org.osgi.service.component.annotations.*;

import LogCreatorAPI.ILogCreatorAPI;

@Component(name="LogCreatorService", immediate = true)
public class LogCreatorService {

	public static AtomicReference<ILogCreatorAPI> refLogCreatorApi = new AtomicReference<>();
    ScheduledExecutor executorService5Sec = null;
    ScheduledExecutor executorService3Sec = null;
	public LogCreatorService() {
		super();
		System.out.println("LogCreatorService.LogCreatorService()");
		if(executorService5Sec == null) {
			this.executorService5Sec = new ScheduledExecutor(5);
		}else {
			executorService5Sec.startAsync();
		}
	
		if(executorService3Sec == null) {
			this.executorService3Sec = new ScheduledExecutor(3);
		}else {
			executorService3Sec.startAsync();
		}
		
	}

	@Activate
	public void activate() {
		System.out.println("LogCreatorService.activate()");

		if(executorService5Sec ==null) {
			this.executorService5Sec = new ScheduledExecutor(5);
		}else {
			executorService5Sec.startAsync();
		}
	
		if(executorService3Sec == null) {
			this.executorService3Sec = new ScheduledExecutor(1);
		}else {
			executorService3Sec.startAsync();
		}
		
	}
	
	@Deactivate
	public void deactivate() {
		System.out.println("LogCreatorService.deactivate()");
	}
	
	@Reference(policy = ReferencePolicy.DYNAMIC, cardinality=ReferenceCardinality.MANDATORY, unbind = "unsetKafkaProducerService")
	public void setKafkaProducerService(final ILogCreatorAPI logCreatorApi) {
		System.out.println("LogCreatorService.setKafkaProducerService()");
		
		if(refLogCreatorApi.compareAndSet(null, logCreatorApi)) {
			System.out.println("LogCreatorService.setKafkaProducerService().AtomicSet Edildi.");
		}
		
	}
	
	public void unsetKafkaProducerService(final ILogCreatorAPI logCreatorApi) {
		System.out.println("LogCreatorService.unsetKafkaProducerService()");
	}
	
	
	
	
}
