package generator.randomisers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import generator.extenders.IRandomiserFunctionality;
import generator.extenders.RandomiserInstance;
import generator.misc.Utils;

import generator.misc.PDFDiscrete;
import generator.misc.PDFDiscreteInterface;

public class ReferentialPDFDiscreteRandomiser implements IRandomiserFunctionality{

	Logger logger = Logger.getLogger(ReferentialPDFDiscreteRandomiser.class);
	
	int  RefPos ;
	int  KeyPos ;
	int cntPos ;
	int TargetPos ;
	
	// [TM] Performance improvement .
	PDFDiscreteInterface PDFInterface ;
	
	HashMap <String,List<String>>tmpMap = new HashMap<String,List<String>>();
	List <List<String>> valuesList = new ArrayList<List<String>>();
	Vector<String> vItems;
		
	public void setRandomiserInstance(RandomiserInstance ri) {
		
		LinkedHashMap<String, String> hashMap;

        String inputFile, scntPos, sKeyPos, sRefPos,sTargetPos ;
                
        hashMap = ri.getProperties();

        inputFile = (String) hashMap.get("inputFile");
        scntPos = (String) hashMap.get("countPosition");
        sTargetPos  = (String) hashMap.get("targetPosition");
        sRefPos  = (String) hashMap.get("referentialPosition");
        sKeyPos  = (String) hashMap.get("keyPosition");
        
        logger.debug("Params : " +  hashMap.toString() );
        try
        {
            cntPos = Integer.parseInt(scntPos);
            TargetPos = Integer.parseInt(sTargetPos);
            RefPos = Integer.parseInt(sRefPos);
            KeyPos = Integer.parseInt(sKeyPos);
        }
        catch(Exception e)
        {
            logger.warn(ri.getName() +": Error setting the numerical values (1 - init)", e);
            
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
        
        
        List <String> KeyList = new ArrayList<String>();
        List <String> TargetList = new ArrayList<String>();
        List <String> CountList = new ArrayList<String>();
        
		Iterator<String> ittr=vItems.iterator() ;
        while (ittr.hasNext() ) {
        	String line = ittr.next() ;
        	//List <String> valList = new ArrayList<String>();
        	logger.debug("Line : " + line);
        	String[] lineList=line.split("\t") ;
        	
        	String compositeKey =  lineList[KeyPos].replace(" ", "_") ;
        	String targetval = lineList[TargetPos] ;
        	String cntval = lineList[cntPos] ;
        	
        	KeyList.add(compositeKey) ;
        	TargetList.add(targetval) ;
        	CountList.add(cntval) ;
        	
     	
        }
        valuesList.add(KeyList) ;
        valuesList.add(TargetList) ;
        valuesList.add(CountList);
        //[TM] Performance improvement .
        PDFInterface = new PDFDiscreteInterface(valuesList) ;
		
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

	// [TM]Performance improvement .
	// 
	public Object generatefromlist(int pos, long numrecs, List<String> dslist) {

		String matchKey = dslist.get(RefPos).replace(" ", "_") ;
		String ret = PDFInterface.getPDF(matchKey) ;
		
		return ret ;
	}
	
	// [TM] Original generatefromlist
	public Object generatefromlist_old(int pos, long numrecs, List<String> dslist) {
		// TODO Lots of error checking.
		// And a complete rewrite !!
		// This is extremely inefficient 
        List<String> TargetList = new ArrayList<String>();
        List<String> sCountList =  new ArrayList<String>();
         //= new ArrayList<double>();
        
        String matchKey = dslist.get(RefPos).replace(" ", "_") ;
        logger.debug("matchKey : " + matchKey ) ;
       
        /*
        for (int x=0; x<valuesList.size() ; x++) {
        	logger.debug("Vals : " + valuesList.get(x).toString() ) ;
        	logger.debug("Vals : " + valuesList.get(x).size() ) ;
        }
        */
        
		for (int x=0; x<valuesList.get(0).size() ; x++) {
			logger.debug("Matching : " + valuesList.get(0).get(x) + " with " + matchKey);
			if (valuesList.get(0).get(x).equals(matchKey)) {
				logger.debug("Matched : " + valuesList.get(0).get(x) + " with " + matchKey);
				TargetList.add(valuesList.get(1).get(x)) ;
				sCountList.add(valuesList.get(2).get(x)) ;
			}
		}
		double [] CountList = new double[sCountList.size()];
		double total = 0;
		for (int i = 0; i < CountList.length; i++) {
			total +=  Double.parseDouble(sCountList.get(i)) ;

		}
		for (int i = 0; i < CountList.length; i++) {
			CountList[i] = (Double.parseDouble(sCountList.get(i)) / total) ;  

		}
		logger.debug("countlist : " + CountList.toString() );
		PDFDiscrete pdf = new PDFDiscrete(CountList);
		int pointer = pdf.next() ;
		
		return TargetList.get(pointer) ;
		//logger.debug("Vals : " + valuesList.get(x).toString() ) ;
		//logger.debug("Vals : " + valuesList.get(x).size() ) ;
		//logger.debug("Vals : " + valuesList.get(1).toString() ) ;
		//logger.debug("Vals : " + valuesList.get(2).toString() ) ;
		

	}

}
