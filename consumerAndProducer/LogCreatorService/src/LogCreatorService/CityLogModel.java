package LogCreatorService;


import java.time.LocalDateTime;

public class CityLogModel {

	
	LocalDateTime timeStamp;
	String logLevel;
	String logServerCityName;
	String logDetail;
	
	
	public LocalDateTime getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(LocalDateTime timeStamp) {
		this.timeStamp = timeStamp;
	}
	public String getLogLevel() {
		return logLevel;
	}
	public void setLogLevel(String logLevel) {
		this.logLevel = logLevel;
	}
	public String getLogServerCityName() {
		return logServerCityName;
	}
	public void setLogServerCityName(String logServerCityName) {
		this.logServerCityName = logServerCityName;
	}
	public String getLogDetail() {
		return logDetail;
	}
	public void setLogDetail(String logDetail) {
		this.logDetail = logDetail;
	}
	
	
	
}
