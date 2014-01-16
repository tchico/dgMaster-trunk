package generator.randomisers;

import generator.extenders.IRandomiserFunctionality;
import generator.extenders.RandomiserInstance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

public class PartialDuplicateFieldRandomiser implements IRandomiserFunctionality{
Logger logger = Logger.getLogger(IRandomiserFunctionality.class);
	
	int startPos = 0;
	int endPos = 0;
	int columnNum = 0;
	String preFix = "";
	String postFix = "";
	//File filePath = null;
	
	
	public void setRandomiserInstance(RandomiserInstance ri) {
		
		LinkedHashMap<String,String> hashMap;

		String sItem ;
        hashMap = ri.getProperties();
        try{
        	columnNum  = Integer.parseInt((String) hashMap.get("columnNum"));
        	startPos  = Integer.parseInt((String) hashMap.get("startPos"));
        	endPos  = Integer.parseInt((String) hashMap.get("endPos"));
        	 if (hashMap.get("preFix") != null){
        		 preFix = hashMap.get("preFix");
        	 }
        	 if (hashMap.get("postFix") != null){
        		 postFix = hashMap.get("postFix");
        	 }
        }
        catch(Exception e)
        {
            logger.warn(ri.getName() +": Error setting values (2 - ParseInt)", e);
        }
                     
        
		
	}

	public Object generate() {
		// TODO implement generation
		return null;
	}

	public void destroy() {
		//  Auto-generated method stub
		
	}

	public boolean isListCompatible() {
		return true;
	}

	public Object generatefromlist(int pos, long numrecs, List<String> dslist) {
		
		String temp = "";
		String returnValue ="" ;
		returnValue = preFix + returnValue ;
		          	
		temp = dslist.get(columnNum);
		returnValue += temp.substring(startPos,endPos);
		returnValue += postFix;
             		
		
		return returnValue;
	}

}
