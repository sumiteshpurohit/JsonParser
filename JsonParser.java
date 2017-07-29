package JSON;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class JsonParser {
	String jsonStr;
	int index;
	
	JsonParser(String filepath) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(filepath));
		String str="";
		try{
		String line;
		while((line = br.readLine()) != null){
			line=line.trim();
			str += line;
		}
		//System.out.println(str);
		
		}finally{
				br.close();
		}
		this.jsonStr=str;
	}
	
	JsonObject parse() throws InvalidStringException, InvalidValueException, InvalidArrayException, FormatException{
		try{
		int ch;
		index=0;
		ch = jsonStr.charAt(index);
        Object o=processValue((char)ch);
        //while(jsonStr.charAt(index)==' '){index++;}
        //System.out.println(index+" : "+ jsonStr.length());
		if(o==null || index != jsonStr.length()){
        	System.out.println("Invalid Json!");
			return null;
        }
        else{
        	System.out.println("Valid Json!");
        	return new JsonObject((Map<String, Object>) o);
        }
		}catch(Exception e){
        	System.out.println("Invalid Json: " + e.getMessage());
			return null;
		}
	}
	
	Object processValue(char ch) throws InvalidStringException, InvalidValueException, InvalidArrayException, FormatException{
		switch(ch){
		case '{':
			return foundObject(ch);
		case '[':
			return foundArray(ch);
		case '\"':
			return foundString(ch);
		case 't':
		case 'f':
			return foundBoolean(ch);
		case 'n':
			return foundNull(ch);
		default:
			if(ch=='-' || (Character.getNumericValue(ch)>=0 && Character.getNumericValue(ch)<=9)){
				return foundNumber(ch);
			}
			else{
				System.out.println("Invalid value!");
				throw new InvalidValueException();
			}
		}
	}
	
	Object foundNull(char ch) throws InvalidValueException{
		String t = "";
		int ndx=index;
		for(int i=0;i<4;i++){
			t+=jsonStr.charAt(ndx);
			ndx++;
		}
		if(t.equals("null")){
			//return ndx;
			index=ndx;
			return "";
		}
		else
		{
			System.out.println("Invalid null at: "+index);
			throw new InvalidValueException();
		}
	}
	
	Object foundBoolean(char ch) throws InvalidValueException{
		String t = "";
		int ndx=index;
		if(ch=='t'){
			for(int i=0;i<4;i++){
				t+=jsonStr.charAt(ndx);
				ndx++;
			}
			if(t.equals("true")){
				index=ndx;
				return new Boolean(true);
			}
			else
			{
				System.out.println("Invalid true at: "+index);
				throw new InvalidValueException();
			}
		}
		else if(ch=='f'){
			for(int i=0;i<5;i++){
				t+=jsonStr.charAt(ndx);
				ndx++;
			}
			if(t.equals("false")){
				index=ndx;
				return new Boolean(false);
			}
			else
			{
				System.out.println("Invalid false at: "+index);
				throw new InvalidValueException();
			}
		}
		System.out.println("Invalid boolean value at: "+index);
		throw new InvalidValueException();
	}
	
	Object foundNumber(char ch) throws InvalidValueException{
		String num="";
		int ndx=index;
		if(ch=='-'){
			num+=ch;
			ndx++;
		}
		try{
	    while(jsonStr.charAt(ndx)!=' ' 
	    		&& Character.getNumericValue(jsonStr.charAt(ndx)) >= 0 
	    		&& Character.getNumericValue(jsonStr.charAt(ndx)) <= 9) { 
	        num += jsonStr.charAt(ndx);
	        ndx++;
	    }
	    boolean fint=true;
	    if(jsonStr.charAt(ndx)=='.'){
	    	num += jsonStr.charAt(ndx);
	        ndx++;
	        while(jsonStr.charAt(ndx)!=' ' 
	        		&& Character.getNumericValue(jsonStr.charAt(ndx)) >= 0 
	        		&& Character.getNumericValue(jsonStr.charAt(ndx)) <= 9) { 
		        num += jsonStr.charAt(ndx);
		        ndx++;
		    }
	        fint=false;
	    }
	    
		    if(fint){
		    	int x=Integer.parseInt(num);
		    	index = ndx;
		    	return new Integer(x);
		    }
		    else{
		    	float x = Float.parseFloat(num);
		    	index = ndx;
		    	return new Float(x);
		    }
	    }catch(NumberFormatException ne){
			System.out.println("Invalid number at: "+index);
		    throw new InvalidValueException();
	    }catch(StringIndexOutOfBoundsException ne){
	    	System.out.println("Expected more data here: "+index);
	    	throw new InvalidValueException();
	    }
	}
	
	Object foundString(char ch) throws InvalidStringException{
		String t = "";
		int ndx=index;
		if(ch != '"') {
			System.out.println("String should start with \".");
			throw new InvalidStringException();
		}
		ndx++;
		try{
		while(true){
			if(jsonStr.charAt(ndx)=='"'){
				ndx++;
				index=ndx;
				//return ndx;
				return t;
			}
			t+=jsonStr.charAt(ndx);
			ndx++;
		}
		}catch(StringIndexOutOfBoundsException ne){
	    	System.out.println("Expected more data here: "+index);
	    	throw new InvalidStringException();
	    }
	}
	
	Object foundArray(char ch) throws InvalidValueException, InvalidArrayException, InvalidStringException, FormatException{
		//String t = "";
		ArrayList<Object> arr = new ArrayList<Object>();
		int ndx=index+1;

		while(jsonStr.charAt(ndx)==' '){ndx++;}
		try{
		if(jsonStr.charAt(ndx)==']'){
			ndx++;
			index=ndx;
			return arr;
		}
		ndx-=1;
		do{
			ndx++;
			while(jsonStr.charAt(ndx)==' '){ndx++;}
			index=ndx;
			Object obj=processValue(jsonStr.charAt(ndx));
			ndx=index;
//			if(obj==null){
//				//return null;
//				//add throw here
//				throw new InvalidArrayException();
//			}
			arr.add(obj);
			while(jsonStr.charAt(ndx)==' '){ndx++;}
			if(jsonStr.charAt(ndx)==']'){
				ndx++;
				index=ndx;
				return arr;
				//return ndx;
			}
			if(jsonStr.charAt(ndx)!=','){
				throw new InvalidArrayException();
			}
		}while(jsonStr.charAt(ndx)==',');
		}catch(StringIndexOutOfBoundsException ne){
	    	System.out.println("Expected more data here: "+index);
	    	throw new InvalidArrayException();
	    }
		return null;
	}
	
	Object foundObject(char ch) throws InvalidArrayException, InvalidValueException, InvalidStringException, FormatException{
		//String t = "";
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		int ndx=index+1;
		
		while(jsonStr.charAt(ndx)==' '){ndx++;}
		try{
		if(jsonStr.charAt(ndx)=='}'){
			ndx++;
			index=ndx;
			return jsonMap;
		}
		ndx-=1;
		do{
			ndx++;
			index=ndx;
			String key=(String)foundString(jsonStr.charAt(ndx));
			ndx=index;
//			if(obj==-1){
//				return ndx;
//			}
			while(jsonStr.charAt(ndx)==' '){ndx++;}
			if(jsonStr.charAt(ndx)!= ':'){
				System.out.println("Expected ':' at "+ ndx);
				throw new FormatException();
			}
			ndx++;
			while(jsonStr.charAt(ndx)==' '){ndx++;}
			index=ndx;
			Object val=processValue(jsonStr.charAt(ndx));
			ndx=index;

			jsonMap.put(key, val);
			
			while(jsonStr.charAt(ndx)==' '){ndx++;}
			if(jsonStr.charAt(ndx)=='}'){
				ndx++;
				index=ndx;
				return jsonMap;
			}
			if(jsonStr.charAt(ndx)!=','){
				throw new InvalidArrayException();
			}
		}while(jsonStr.charAt(ndx)==',');
		}catch(StringIndexOutOfBoundsException ne){
	    	System.out.println("Expected more data here: "+index);
		    throw new InvalidValueException();
	    }
		return null;
	}
}

class FormatException extends Exception {
	public FormatException(){
		super("Input format is invalid. Format is key:value.");
	}
}

class InvalidArrayException extends Exception {
	public InvalidArrayException(){
		super("Input array is not valid.");
	}
}

class InvalidStringException extends Exception {
	public InvalidStringException(){
		super("Input value is not a valid String.");
	}
}

class InvalidValueException extends Exception {
	public InvalidValueException(){
		super("Input value is not a valid Token. Value can be String, number, boolean, array, object, or null only.");
	}
}
