package generator.randomisers;


import generator.extenders.IRandomiserFunctionality;
import generator.extenders.RandomiserInstance;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.log4j.Logger;

public class ConditionalFieldRandomiserWithPercentageDistribution implements IRandomiserFunctionality{

    Logger logger = Logger.getLogger(DoubleListRandomiser.class);
    ArrayList<String> fieldValues = new ArrayList<String>();
    IRandomiserFunctionality fieldDataRandomiser;
    
    ArrayList<IRandomiserFunctionality> randomisers = new ArrayList<IRandomiserFunctionality>(); // targeted randomisers
    Boolean listCompatiable;
    /**
     * Creates a new instance of DoubleListRandomiser
     */
    public ConditionalFieldRandomiserWithPercentageDistribution()
    {

    }
    
    public void setRandomiserInstance(RandomiserInstance ri)
    {        
        LinkedHashMap<String,String> hashMap;
        
        hashMap = ri.getProperties();
      
        
        int exit = 0;
        if(hashMap.containsKey("fieldValue0")){
    		exit++;
    	}
        
        for(int i = 0; i < exit; i++ ){
        	fieldValues.add(hashMap.get("fieldValue"+i));
        	if(hashMap.containsKey("fieldValue"+exit)){
        		exit++;
        	}
        }
        if(exit > 0){
        	
        }
       
    }
    
    public Object generate() // cannot use, cannot know pror
    {
    	return null;
    }

    public void addTargetRandomiser(IRandomiserFunctionality targetRandomiser){
    	this.randomisers.add(targetRandomiser);
    }
    
    public void destroy()
    {
    }

	public boolean isListCompatible() {
		return true;
	}

	public Object generatefromlist(int pos, long numrecs, List<String> dslist) {
		String value = "";
		if(fieldDataRandomiser.isListCompatible()){
			 value = (String) fieldDataRandomiser.generatefromlist(pos,numrecs,dslist);
		}
		else{ // getter is not list compatiable
			 value = (String) fieldDataRandomiser.generate();
		}
		
		if(randomisers.get(fieldValues.indexOf(value)).isListCompatible()){
			value = (String) randomisers.get(fieldValues.indexOf(value)).generatefromlist(pos, numrecs, dslist);
		}
		else{
			value = (String) randomisers.get(fieldValues.indexOf(value)).generate();
		}
		
        return value;
    
	}
    
	public void setFieldDataRandomiser(IRandomiserFunctionality fieldDataRandomiser){
		this.fieldDataRandomiser = fieldDataRandomiser;
	}
}



