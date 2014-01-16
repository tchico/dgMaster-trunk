package generator.misc;

import java.util.HashMap;

public class StageDataContext {
	
	private static HashMap<String,Object> context;
	private static String increment= "NaturalIncrement";
	
	public StageDataContext(){
		
	}
	public static void init(){
		context = new HashMap<String,Object>();
	}
	
	public static void setValue(String name,String type, String value){
		try{
			if(type.equalsIgnoreCase("String"))
				context.put(name,value);
			else if(type.equalsIgnoreCase("Long"))
				context.put(name, Long.parseLong(value));
			else if(type.equalsIgnoreCase("Integer"))
				context.put(name, Integer.parseInt(value));
			else if(type.equalsIgnoreCase("Boolean"))
				context.put(name, Boolean.parseBoolean(value));
			else
				System.out.println("" + type + " cannot be parsed as a type");
		}
		catch(Exception e){
			System.out.println("Error cannot parse value in StageDataContext " + e);
		}
	}
	
	public static Object getValue(String s){
		if(context.containsKey(s))
			return context.get(s);
		else{
			System.out.println("Warning!   	Stage Data Map does not contain "+ s + " ");
			return null;
		}
	}
	
	public static void setValue(String s, Object o){
		context.put(s, o);
	}
	
	
	public static void incrementValue(String s){
		if(context.containsKey(s)){
			Object obj = context.get(s);
			if(context.containsKey(s+increment)){
				
				if(obj instanceof Integer){
					Integer value = (Integer) context.get(s);
					Integer natural =  (Integer) context.get(s+increment);
					context.put(s,value+natural);
				}else if(obj instanceof Long){
					Long value = (Long) context.get(s);
					Long natural =  (Long) context.get(s+increment);
					context.put(s,value+natural);
				}
				else if(obj instanceof String){
					String value = (String) context.get(s);
					String natural =  (String) context.get(s+increment);
					context.put(s,value+natural);
				}
				else{
					System.out.println("Warning: Cannot perform natural increment on " + s);
				}
			}
			else if(obj instanceof Boolean){
				boolean value = (Boolean) context.get(s);
				value = (value == true)? false : true;
				context.put(s,value);
			}
		}
	}
}
