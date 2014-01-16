package generator.misc;

import generator.Schema.SchemaObjectAttributeInterface;
import generator.masterbuild.MasterBuilder;

import java.util.ArrayList;

public class PercentageDistributionGenerator {
	
	static ArrayList<Double> percentages = new ArrayList<Double>();
    static ArrayList<Integer> distribution = new ArrayList<Integer>();
    static ArrayList<Integer> percentageDistribution = new ArrayList<Integer>();
     
     public static void setValues(ArrayList<Double> percentage, ArrayList<Integer> dis){
    	 percentages = percentage;
    	 distribution = dis;
    	 percentageDistribution = new ArrayList<Integer>();
     }
     
     private static void doubleValues(){
    	 for(int i =0 ; i < percentages.size();i++){
    		 percentages.set(i,percentages.get(i)*2.0);
    	 }
     }
     
     public static ArrayList<Integer> getPercentageDistribution(){
    	 // check if any of the randomisers is less that 1, if so keep doubling until it equals one, so turn a .5 into 1, or 0.05 into 1
    	 for(int i =0 ; i < percentages.size();i++){
    		 if(percentages.get(i) < 1){
    			 doubleValues(); // double all of the values
    			 i =-1;
    		 }
    	 }
    	 
    	 int divideBy = evenDistribution(3);
	    	if(divideBy == -1){ // cannot be divided by 2,3,5 / which is bad, distribution will not be 100% accurate
	    		divideBy = 2; // default
	    		// divide up the values until one = 1, then turn the rest into a arraylist with one entry for each number 2 = 2 entries
	    	}
	    	
 		int j = 0;
 		boolean target = false;
 		for(Double d:percentages){
 			if(d < 2){ // hopefully its a nice even one
 				target = true;// something is  less that one, we can begin
 			}
 		}
 		while(target == false){
 			j = 0;
	    		for(Double d:percentages){
	    			percentages.set(j, d/divideBy);
	    			j++;
	    		}
	    		for(Double d:percentages){
	    			if(d < 2){ // hopefully its a nice even one
	    				target = true;// something is now less that one, we can begin
	    			}
	    		}
	    		divideBy = evenDistribution(3);
		    	if(divideBy == -1){ // cannot be divided by 2,3,5 / which is bad, distribution will not be 100% accurate
		    		divideBy = 2; // default
		    		// divide up the values until one = 1, then turn the rest into a arraylist with one entry for each number 2 = 2 entries
		    	}
 		}
 		
 		
 		// something should now be equal to 1 or less than 2 anyway, so we can distribute in low numbers
 		// Ensure this has been tested.
			for(int i = 0; i < 2; i++){ // Iterate over twice in case the divider is not even, better chance of getting a more even spread of values because moduless is carried over
				j = 0;
	    		for(Double d : percentages){
	    			double amountToCreate = (d - (d % 1));
	    			// now create that amount
	    			for(int k = 0; k < amountToCreate; k++){
	    				percentageDistribution.add(distribution.get(j)); // add this distrubtion amount to the array however many times
	    			}
	    			
	    			percentages.set(j, d + (d % 1)); // replace the value with the old value plus the left over values
	    			j++;
	    		}
			}
			ArrayList<Integer> temp = new  ArrayList<Integer>();
			for(int i = 0; i < percentageDistribution.size();i++){
				temp.add(percentageDistribution.get(i));
			}
			return temp; // return a copy so the static values can be reused
     }
     
     private static int evenDistribution(int i){
	    	boolean possible;
    		possible = true;
    		for(double d: percentages){
    			if(d % i != 0){ // cannot be divided evenly
    				possible = false;
    			}
    		}
    		if(possible){ // if possible is still true then all can be divided evenly by 
    			return i; // were done
    		}else if(i == 3){
    			evenDistribution(5);
    		}
	    	return -1;
	    }
     
     public static ArrayList<Integer> getLinkageDistribution(ArrayList<Double> percentage,String linkage){
		return null;
    	
    	
    	 
     }
}
