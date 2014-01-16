package generator.randomisers;

import generator.extenders.IRandomiserFunctionality;
import generator.extenders.RandomiserInstance;
import generator.misc.StageDataContext;

import java.util.LinkedHashMap;
import java.util.List;

import org.apache.log4j.Logger;

public class GenericStageDataContextRandomiser  implements IRandomiserFunctionality{
Logger logger = Logger.getLogger(IRandomiserFunctionality.class);
	
	String preFix = ""; // keep these 
	String postFix = "";
	String sdcontextReference = "";
	
	
	public void setRandomiserInstance(RandomiserInstance ri) {
		
		LinkedHashMap<String,String> hashMap;
        hashMap = ri.getProperties();
        
        try{
        	sdcontextReference  =(String) hashMap.get("sdcontextReference");
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
		String value = preFix + StageDataContext.getValue(sdcontextReference).toString() + postFix;
		StageDataContext.incrementValue(sdcontextReference);
		return value;
	}

	public void destroy() {

	}

	public boolean isListCompatible() {
		return false;
	}

	public Object generatefromlist(int pos, long numrecs, List<String> dslist) {
		return null;
	}
}
