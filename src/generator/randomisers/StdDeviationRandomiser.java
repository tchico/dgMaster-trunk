package generator.randomisers;


import java.util.LinkedHashMap;
import java.util.List;
import java.util.Vector;


import cern.jet.random.Normal;


import org.apache.log4j.Logger;

import generator.extenders.IRandomiserFunctionality;
import generator.extenders.RandomiserInstance;
import generator.panels.PanelStdDeviationRandomiser;

/**
 * Double generator
 * @see PanelStdDeviationRandomiser
 *
 */
public class StdDeviationRandomiser implements IRandomiserFunctionality{

	
	Logger logger = Logger.getLogger(StdDeviationRandomiser.class);
	
	double amavg ;
	double amstd ;
	double amtdivide= 100 ;
	
	Vector<String> vItems;
	
	
	public void setRandomiserInstance(RandomiserInstance ri) {
		LinkedHashMap<String, String> hashMap;
        String samavg ;
        String samstd ;
       
        
        hashMap = ri.getProperties();
        samavg  = (String) hashMap.get("average");
        samstd  = (String) hashMap.get("stddeviation");
        
        try
        {
        	amavg = Double.parseDouble(samavg) ;
        	amstd = Double.parseDouble(samstd);

        }
        catch(Exception e)
        {
            logger.warn("Error setting the numerical values ", e);
            logger.warn("Setting all vals to zero . ");
        	//amavg = 1.0 ;
        	//amstd = 1.0 ;

        }

		
	}

	public Object generate() {
		double am ;
		
		logger.debug("Input Values " + amavg + " " + amstd) ;
		
		am = Normal.staticNextDouble(amavg, Math.sqrt(amstd));
		logger.debug("Output " + am ) ;
		
		am = Math.round(am * amtdivide)/amtdivide ;
		
		//Math.round(value * 100000) / 100000
		
		//am = Math.round(am /amtdivide );
			
		// Johan's original . 
		//amount     = makeAmount(amavg, Math.sqrt(amstd));		
	    //protected double makeAmount(double avg, double std)
	    //{
	     //   double am;

	       // am = Normal.staticNextDouble(avg, std);
	        //if (am <= 0) am = 1.0;
	        //else
	        //am = Math.round(am / this.AMOUNT_DIVIDE);

	        //return(am);
	    //}
		
		
	    return(am);
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


