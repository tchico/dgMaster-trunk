package generator.randomisers;

import generator.extenders.IRandomiserFunctionality;
import generator.extenders.RandomiserInstance;
import generator.misc.PercentageDistributionGenerator;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.log4j.Logger;

public class RandomiserWithPercentageDistribution implements IRandomiserFunctionality{

		    Logger logger = Logger.getLogger(DoubleListRandomiser.class);
		    Boolean randomValues;
	        ArrayList<Double> percentages = new ArrayList<Double>();
	        ArrayList<Integer> distribution = new ArrayList<Integer>();
	        ArrayList<Integer> percentageDistribution = new ArrayList<Integer>();
	        IRandomiserFunctionality targetRandomiser;
	        Boolean listCompatiable;
		    /**
		     * Creates a new instance of DoubleListRandomiser
		     */
		    public RandomiserWithPercentageDistribution()
		    {

		    }
		    
		    public void setRandomiserInstance(RandomiserInstance ri)
		    {        
		        LinkedHashMap<String,String> hashMap;
		        
		        hashMap = ri.getProperties();
		        randomValues = Boolean.parseBoolean(hashMap.get("RandomValues"));
		        if(randomValues == null)
		        	randomValues = false;
		      
		        
		        //Now we have to get The percentage distribution
		        int exit = 0;
		        if(hashMap.containsKey("percentage0")){// then we can assume it has a distribution value aswell
	        		exit++;
	        	}
		     
		        for(int i = 0; i < exit; i++ ){
		        	// get the percentage
		        	percentages.add(Double.parseDouble(hashMap.get("percentage"+i)));
		        	// get the distribution
		        	distribution.add(Integer.parseInt(hashMap.get("distribution"+i)));
		        	
		        	if(hashMap.containsKey("percentage"+exit)){ // there is another percentage so extend the loop
		        		exit++;
		        	}
		        }
		        //now have the percentages and distribution to use during generation
		        if(exit > 0){
		        	PercentageDistributionGenerator.setValues(percentages, distribution);
		        	percentageDistribution = PercentageDistributionGenerator.getPercentageDistribution();
		        }
		        	
		        	//workOutDistribution();
		       
		    }
		    
		    public Object generate() // not done, read comments
		    {
		    	
		    	// use the percentageDistribution to see how many items must be created
		    	
		    	int amount = percentageDistribution.get(0);
		    	String generation = "";
		    	String newValue = "";
		    	for(int i = 0; i < amount; i++){
		    			newValue = (String) this.targetRandomiser.generate();
		    		generation = generation + newValue + "|"; // newValue must be replaced with an IP generation method using the arrays created above
		    	}
		    	percentageDistribution.remove(0);
		    	percentageDistribution.add(amount); // put amount at the end of the array, so the values loop around
		    	
		    	// this should remove the final delimiter from the string
		    	generation = generation.substring(0, generation.lastIndexOf("|")); 
		    	// now we should have our new column row this can be returned
		        return generation;
		    }

		    public void setTargetRandomiser(IRandomiserFunctionality targetRandomiser){
		    	this.targetRandomiser = targetRandomiser;
		    	listCompatiable = targetRandomiser.isListCompatible();
		    }
		    
		    public void destroy()
		    {
		    }

			public boolean isListCompatible() {
				return listCompatiable;
			}

			public Object generatefromlist(int pos, long numrecs, List<String> dslist) {
				int amount = percentageDistribution.get(0);
		    	String generation = "";
		    	String newValue = "";
		    	for(int i = 0; i < amount; i++){
		    			newValue = (String) this.targetRandomiser.generatefromlist(pos,numrecs,dslist);
		    		generation = generation + newValue + "|"; // newValue must be replaced with an IP generation method using the arrays created above
		    	}
		    	percentageDistribution.remove(0);
		    	percentageDistribution.add(amount); // put amount at the end of the array, so the values loop around
		    	
		    	// this should remove the final delimiter from the string
		    	generation = generation.substring(0, generation.lastIndexOf("|")); 
		    	// now we should have our new column row this can be returned
		        return generation;
			}
		    
}

