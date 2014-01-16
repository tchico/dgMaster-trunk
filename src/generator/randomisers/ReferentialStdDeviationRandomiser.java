
package generator.randomisers;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Vector;


import cern.jet.random.Normal;


import org.apache.log4j.Logger;

import generator.extenders.IRandomiserFunctionality;
import generator.extenders.RandomiserInstance;
import generator.misc.Utils;


public class ReferentialStdDeviationRandomiser implements IRandomiserFunctionality{

	
	Logger logger = Logger.getLogger(ReferentialStdDeviationRandomiser.class);
	
	List<String>  RefPos = new ArrayList<String>() ;
	List<String>  KeyPos = new ArrayList<String>();
	int amavgPos ;
	int amstdPos ;
	int rangesNum ;
	
	HashMap <String,List<String>>tmpMap = new HashMap<String,List<String>>();
	
	Vector<String> vItems;
		
	public void setRandomiserInstance(RandomiserInstance ri) {
		
		LinkedHashMap<String, String> hashMap;
        //int rangesNum = 0;
        String  sItem ;
        String inputFile,sRangesNum, samavgPos,samstdPos;
               
        hashMap = ri.getProperties();

        inputFile = (String) hashMap.get("inputFile");
        sRangesNum  = (String) hashMap.get("rangesNum");
        samavgPos = (String) hashMap.get("amavgPos");
        samstdPos = (String) hashMap.get("amstdPos");
        
        logger.debug("Params : " + sRangesNum + " " + samavgPos + " " + samstdPos );
        try
        {
            rangesNum = Integer.parseInt(sRangesNum);
            amavgPos = Integer.parseInt(samavgPos);
            amstdPos = Integer.parseInt(samstdPos);
        }
        catch(Exception e)
        {
            logger.warn(ri.getName() +": Error setting the numerical values (1 - init)", e);
            
        }
        
        logger.debug("HashMap : " + hashMap.toString());
        
        for(int i=1; i<=rangesNum; i++)
        {
            try
            {
                sItem = (String) hashMap.get("RefPos"+ (i-1) );
            	//sItem = (String) hashMap.get("RefPos0" );
            	// RefPos0
                logger.debug("sItem : " + sItem + " "  + sItem.getClass() );
                this.RefPos.add(sItem);
                logger.debug("Added : " + sItem + " at " + i);
                sItem = (String) hashMap.get("KeyPos"+ (i-1) );
                this.KeyPos.add(sItem);
                logger.debug("Added : " + sItem + " at " + i);
            }
            catch(Exception e)
            {
                logger.warn(ri.getName() +": Error setting values (2 - Loop, index="+i+")", e);
            }
        }        
      
        // Read the stats file .
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
        
        String compositeKey;
		Iterator<String> ittr=vItems.iterator() ;
        //List <String> valList = new ArrayList<String>();
		while (ittr.hasNext() ) {
        	 compositeKey = "" ;
        	 String line = ittr.next() ;
        	 List <String> valList = new ArrayList<String>();
        	// if (valList != null ) { 
        	//	 valList.clear() ;
        	 //}
        	logger.debug("Line : " + line);
        	//logger.debug("Separator " + sepString) ;
        	
        	// TODO: [TM] fix the separator.
        	String[] lineList=line.split("\t") ;
        	
        	logger.debug("lineList " + lineList[0] );
        	
            for(int i=0; i<rangesNum; i++) {
            	compositeKey = compositeKey + lineList[Integer.parseInt(KeyPos.get(i))].replace(" ", "_") ;
            	logger.debug("Key , Pos : " + compositeKey + "," + i) ;
            }
            valList.add(lineList[amavgPos]) ;
            valList.add(lineList[amstdPos]) ;
        	tmpMap.put(compositeKey,valList) ;
        }        


		
	}

	public Object generate() {
		// Should never get called.
		/*double am ;
		
		logger.debug("Input Values " + amavg + " " + amstd) ;
		am = Normal.staticNextDouble(amavg, Math.sqrt(amstd));
		logger.debug("Output " + am ) ;		
		am = Math.round(am * amtdivide)/amtdivide ;
		
	    return(am);
	    */
		return null ;
	}

	public void destroy() {
		//  Auto-generated method stub
		
	}

	public boolean isListCompatible() {
		return true;
	}

	public Object generatefromlist(int pos, long numrecs, List<String> dslist) {

		double amavg = 0 ;
		double amstd = 0 ;
		double amtdivide= 100 ;
		
		List <String> statlist ;
		String samavgPos ;
		String samstdPos ;
		String compositeKey = "" ;
		
		logger.debug("Begin") ;
        for(int i=0; i<rangesNum; i++) {   	
        	compositeKey = compositeKey + dslist.get(Integer.parseInt(RefPos.get(i))).replace(" ", "_") ;
        	logger.debug("Key , Pos : " + compositeKey + "," + i) ;
        }
        logger.debug("Next") ;
        logger.debug("compositeKey " + compositeKey) ;
        logger.debug("tmpMap " + tmpMap.toString()) ;
        
        if (this.tmpMap.containsKey(compositeKey)) {
        	logger.debug("compositeKey Values" + this.tmpMap.get(compositeKey).toString()) ;
        }
        else {
        	logger.error("Cannot find values for " + compositeKey + " in " + tmpMap.toString()) ;
        	return null  ;
        }
        statlist = this.tmpMap.get(compositeKey);
        
        logger.debug("statlist " + statlist.toString()) ;
           
        samavgPos = (String) statlist.get(0) ;
        samstdPos = (String) statlist.get(1) ;
        
        
        
        try
        {
        	amavg = Double.parseDouble(samavgPos) ;
        	amstd = Double.parseDouble(samstdPos) ;
        }
        catch(Exception e)
        {
            logger.error("Could not cast input values to doubles : ",e);
        }        
        
		double am ;
		
		logger.debug("Input Values " + amavg + " " + amstd) ;
		am = Normal.staticNextDouble(amavg, Math.sqrt(amstd));
		logger.debug("Output " + am ) ;		
		am = Math.round(am * amtdivide)/amtdivide ;
		
		return am ;
	}

}


