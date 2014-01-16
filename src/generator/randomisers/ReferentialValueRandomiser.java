package generator.randomisers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import generator.extenders.IRandomiserFunctionality;
import generator.extenders.RandomiserInstance;
/*
 *  Create a value based on one or more previously generated values.
 *  
 * */
public class ReferentialValueRandomiser implements IRandomiserFunctionality{

	Logger logger = Logger.getLogger(IRandomiserFunctionality.class);
	
	List<String>  RefPos = new ArrayList<String>() ;
	String seperator = " " ;
	String prefix = "" ;
	int rangesNum = 0 ;
	
	HashMap <String,List<String>>tmpMap = new HashMap<String,List<String>>();
	
	Vector<String> vItems;
	
	public void setRandomiserInstance(RandomiserInstance ri) {
		
		LinkedHashMap<String,String> hashMap;

		String sItem ;
        hashMap = ri.getProperties();
        
        if (hashMap.get("prefix") != null){
        	prefix = (String) hashMap.get("prefix");
        }
        if (hashMap.get("seperator") != null){
        	seperator = (String) hashMap.get("seperator");
        }
        rangesNum  = Integer.parseInt((String) hashMap.get("rangesNum"));
        
        for(int i=1; i<=rangesNum; i++)
        {
            try
            {
                sItem = (String) hashMap.get("RefPos"+ (i-1) );
            	//sItem = (String) hashMap.get("RefPos0" );
                this.RefPos.add(sItem);
                logger.info("Added : " + sItem + " at " + i);

            }
            catch(Exception e)
            {
                logger.warn(ri.getName() +": Error setting values (2 - Loop, index="+i+")", e);
            }
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
		
		String returnValue ="" ;
		returnValue = prefix + returnValue ;
		for(int i=1; i<=rangesNum; i++)
        {
            try
            {
            	            	
            	returnValue =  returnValue + dslist.get(Integer.parseInt(this.RefPos.get(i-1))) ;
            	if (i != rangesNum ) {
            		returnValue = returnValue + seperator ;
            	}
            	

            }
            catch(Exception e)
            {
                logger.warn(returnValue +": Error setting values in string.", e);
            }
        }                		
		
		return returnValue;
	}

}
