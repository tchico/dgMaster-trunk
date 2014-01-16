package generator.randomisers;

import generator.extenders.IRandomiserFunctionality;
import generator.extenders.RandomiserInstance;
import generator.misc.PercentageDistributionGenerator;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.log4j.Logger;
public class MultipleRandomisersWithPercentageDistribution implements IRandomiserFunctionality{

			    Logger logger = Logger.getLogger(DoubleListRandomiser.class);
		        ArrayList<Double> percentages = new ArrayList<Double>();
		        ArrayList<Integer> distribution = new ArrayList<Integer>();
		        ArrayList<Integer> percentageDistribution = new ArrayList<Integer>();
		        IRandomiserFunctionality targetRandomiser;
		        ArrayList<IRandomiserFunctionality> randomisers = new ArrayList<IRandomiserFunctionality>();
		        Boolean listCompatiable;
			    /**
			     * Creates a new instance of DoubleListRandomiser
			     */
			    public MultipleRandomisersWithPercentageDistribution()
			    {

			    }
			    
			    public void setRandomiserInstance(RandomiserInstance ri)
			    {        
			        LinkedHashMap<String,String> hashMap;
			        
			        hashMap = ri.getProperties();
			      
			        
			        //Now we have to get The percentage distribution
			        int exit = 0;
			        if(hashMap.containsKey("percentage0")){// then we can assume it has a distribution value aswell
		        		exit++;
		        	}
			     
			        for(int i = 0; i < exit; i++ ){
			        	// get the percentage
			        	percentages.add(Double.parseDouble(hashMap.get("percentage"+i)));
			        	// get the distribution
			        	distribution.add(i);// just add the values
			        	
			        	//randomisers.add();
			        	
			        	if(hashMap.containsKey("percentage"+exit)){ // there is another percentage so extend the loop
			        		exit++;
			        	}
			        }
			        //now have the percentages and distribution to use during generation
			        if(exit > 0){
			        	PercentageDistributionGenerator.setValues(percentages, distribution);
			        	percentageDistribution = PercentageDistributionGenerator.getPercentageDistribution();
			        }
			       
			    }
			    
			    public Object generate() // not done, read comments
			    {
			    	
			    	String returnValue = (String) randomisers.get(percentageDistribution.get(0)).generate();
			    	percentageDistribution.add(percentageDistribution.get(0)); // put amount at the end of the array, so the values loop around
			    	percentageDistribution.remove(0);

			        return returnValue;
			    }

			    public void addTargetRandomiser(IRandomiserFunctionality targetRandomiser){
			    	this.randomisers.add(targetRandomiser);
			    	//listCompatiable = targetRandomiser.isListCompatible();
			    }
			    
			    public void destroy()
			    {
			    }

				public boolean isListCompatible() {
					//return the value of whatever randomiser is currently next on the distribution list
					return randomisers.get(percentageDistribution.get(0)).isListCompatible(); // listCompatiable;
				}

				public Object generatefromlist(int pos, long numrecs, List<String> dslist) {
	    			String returnValue = (String) randomisers.get(percentageDistribution.get(0)).generatefromlist(pos,numrecs,dslist);;
			    	percentageDistribution.add(percentageDistribution.get(0)); // put amount at the end of the array, so the values loop around
			    	percentageDistribution.remove(0);

			        return returnValue;
				}
			    
	}


