package JSON;

import java.util.Map;

public class JsonObject {
	Map<String, Object> jsonMap;
	
	JsonObject(Map<String, Object> jsonMap){
		this.jsonMap=jsonMap;
	}
	
	public Object get(String key){
		return jsonMap.get(key);
	}
	
}
