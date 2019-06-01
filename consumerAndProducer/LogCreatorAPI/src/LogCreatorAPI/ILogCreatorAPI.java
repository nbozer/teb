package LogCreatorAPI;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public interface ILogCreatorAPI {

	public boolean publishLogCount(int logCount, String cityName, long systemTime);
}
