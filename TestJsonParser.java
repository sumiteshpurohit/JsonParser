package JSON;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TestJsonParser {

	/**
	 * @param args
	 * @throws FormatException 
	 * @throws InvalidArrayException 
	 * @throws InvalidValueException 
	 * @throws InvalidStringException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws InvalidStringException, InvalidValueException, InvalidArrayException, FormatException, IOException {
		// TODO Auto-generated method stub
		JsonParser jp = new JsonParser("testschema.json");
		JsonObject jo = jp.parse();
		if(jo!=null){
			System.out.println(jo.get("properties"));
			
			String gets="type";
			Object gen = jo.get(gets);
			
			switch(gen.getClass().toString()){
			case "class java.lang.Boolean":
				boolean b=(Boolean) gen;
				break;
			case "class java.lang.Float":
				float f=(Float) gen;
				break;
			case "class java.lang.Integer":
				int i = (Integer) gen;
				break;
			case "class java.lang.String":
				String s = (String) gen;
				break;
			case "class java.util.HashMap":
				Map<String, Object> m = (HashMap<String, Object>) gen;
				break;	
			case "class java.util.ArrayList":
				ArrayList<Object> al = (ArrayList<Object>) gen;
				break;	
			}
		}
	}

}
