package generator.randomisers;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import generator.extenders.IRandomiserFunctionality;
import generator.extenders.RandomiserInstance;
import generator.misc.Utils;

public class ReferentialListRandomiser implements IRandomiserFunctionality{

	
	Logger logger = Logger.getLogger(ReferentialListRandomiser.class);
	int RefPos ;
	int keyPos ;
	int targetPos ;
	String sepString ;
	Vector<String> vItems;
	HashMap <String,String>tmpMap = new HashMap<String,String>();
	
	public void setRandomiserInstance(RandomiserInstance ri) {
		
		LinkedHashMap<String,String> hashMap;
        String sRefPos ;
        String sTargetPos ;
        String skeyPos ;
        
        
        hashMap = ri.getProperties();
        sRefPos  = (String) hashMap.get("referentialPosition");
        sTargetPos  = (String) hashMap.get("targetPosition");
        sepString  = (String) hashMap.get("seperator");
        skeyPos  = (String) hashMap.get("keyPosition");
        String inputFile = (String) hashMap.get("inputFile");
        
        
        try
        {
        	RefPos    = Integer.parseInt(sRefPos);
        	targetPos = Integer.parseInt(sTargetPos);
        	keyPos    = Integer.parseInt(skeyPos) ;

        }
        catch(Exception e)
        {
            logger.warn("Error setting the numerical values", e);

        }
        Utils utils = new Utils();
        
        try
        {
            vItems = utils.readFile(inputFile);
        }
        catch(Exception e)
        {
            logger.error(ri.getName() +": could not locate file:"+inputFile,e);
        }
        if(vItems.size()==0)
        {
            vItems.add("ERROR");
            logger.warn(ri.getName() +": Vector size is 0:");
        }

		
		Iterator<String> ittr=vItems.iterator() ;
        while (ittr.hasNext() ) {
        	// TODO: [TM] Separator needs fixing .
        	String[] lineList=ittr.next().split("\\|") ;
        	
        	tmpMap.put(lineList[keyPos],lineList[targetPos]) ;
        }

	}

	public Object generate() {
		// shouldn't get called
		return null;
	}

	public void destroy() {
		//  Auto-generated method stub
		
	}

	public boolean isListCompatible() {
		return true;
	}

	public Object generatefromlist(int pos, long numrecs, List<String> dslist) {
		// 
		String matchtarget = dslist.get(RefPos);
		String matched = "" ;

        matched =  tmpMap.get(matchtarget)	 ;
		return matched;
	}

}
