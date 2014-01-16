package generator.randomisers;

import generator.extenders.IRandomiserFunctionality;
import generator.extenders.RandomiserInstance;
import generator.misc.PDFDiscrete;
import generator.misc.PercentageDistributionGenerator;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import org.apache.log4j.Logger;

public class IPPercentageDistributionRandomiser implements IRandomiserFunctionality{

	    Logger logger = Logger.getLogger(DoubleListRandomiser.class);
	    Random probability, nullGen ; // gen;
	    PDFDiscrete gen ;
	    Vector<String> vItems;
	    Boolean randomValues;
        int[] from,to,current;
        ArrayList<Double> percentages = new ArrayList<Double>();
        ArrayList<Integer> distribution = new ArrayList<Integer>();
        ArrayList<Integer> percentageDistribution = new ArrayList<Integer>();
	    /**
	     * Creates a new instance of DoubleListRandomiser
	     */
	    public IPPercentageDistributionRandomiser()
	    {
	    	from = new int[4];
	    	to = new int[4];
	    	current = new int[4];
	    	
	    }
	    
	    public void setRandomiserInstance(RandomiserInstance ri)
	    {        
	        String fromBase,toBase;

	        double percentField[] = null;
	        LinkedHashMap<String,String> hashMap;
	        
	        hashMap = ri.getProperties();
	        randomValues = Boolean.parseBoolean(hashMap.get("RandomValues"));
	        if(randomValues == null)
	        	randomValues = false;
	        fromBase =  (String) hashMap.get("IPFromRange");
	        toBase = (String) hashMap.get("IPToRange");
	        if(fromBase == null)
	        	fromBase = "0.0.0.0";
	        if(toBase == null)
	        	toBase ="255.255.255.255"; //(String) hashMap.get();
	        
	        String [] fromBaseArray = fromBase.split("\\.");
	        String [] toBaseArray = toBase.split("\\.");
	        
	    	// now have to get the ip range to choose from.
	        for(int i = 0; i < 4; i++){
	        	from[i] = Integer.parseInt(fromBaseArray[i]);
	        	current[i] = from[i];
	        	to[i] = Integer.parseInt(toBaseArray[i]);
	        }
	        
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
	    		newValue = getNewIp();
	    		generation = generation + newValue + "|"; // newValue must be replaced with an IP generation method using the arrays created above
	    	}
	    	percentageDistribution.remove(0);
	    	percentageDistribution.add(amount); // put amount at the end of the array, so the values loop around
	    	
	    	// this should remove the final delimiter from the string
	    	generation = generation.substring(0, generation.lastIndexOf("|")); 
	    	// now we should have our new column row this can be returned
	        return generation;
	    }
	    
	    public String getNewIp(){
	    	if(randomValues == false){
	    	int a =0;// array position
	    	while(a > -1){
	    		if(current[a] < to[a]){ // then increasing it is ok, close loop
	    			current[a]++;
	    			a = -1;
	    		}
	    		else{ // reset this section of the ip, increase the next one by 1
	    			current[a] = from[a];
	    			a++;
	    		}
	    		if(a > 3){ // we have hit the limit of the loop
	    			for(int i = 0; i < 4; i++){
	    			current[i] = from[i];
	    			}
	    			a = -1;
	    		}
	    	}
	    	return ""+current[3]+"."+current[2]+ "." + current[1]+"."+current[0]+"";
	    	}
	    	else{
	    		Random rand = new Random();
	    		return ""+ ((rand.nextInt(to[3] - from[3]))+ from[3])+"."+((rand.nextInt(to[2] - from[2]))+ from[2])+ "." + ((rand.nextInt(to[1] - from[1]))+ from[1])+"."+((rand.nextInt(to[0] - from[0]))+ from[0])+"";
	    	}
	    }
	    
	    public void destroy()
	    {
	    }

		public boolean isListCompatible() {
			return false;
		}

		public Object generatefromlist(int pos, long numrecs, List<String> dslist) {
			//  Auto-generated method stub
			return null;
		}
	    
	}
