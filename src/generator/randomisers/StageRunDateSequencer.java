/*
 * dgMaster: A versatile, open source data generator.
 *(c) 2007 M. Michalakopoulos, mmichalak@gmail.com
 */


package generator.randomisers;
import generator.extenders.IStageAwareRandomiserFunctionality;
import generator.extenders.RandomiserInstance;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * To be used in order to create a field that contains the runDate corresponding
 * to the stage that is being generated.
 * @author joaoesteves
 */
public class StageRunDateSequencer implements IStageAwareRandomiserFunctionality
{
    Logger logger = Logger.getLogger(StageRunDateSequencer.class);
    private SimpleDateFormat sdf;
    private Date runDate = new Date();
    
    
    public void setRandomiserInstance(RandomiserInstance ri)
    {
        String sSDF;
        LinkedHashMap<String, String> hashMap;
        
        hashMap = ri.getProperties();
        sSDF = (String) hashMap.get("dateFormat");
        
        try{
            sdf = new SimpleDateFormat(sSDF);
        }
        catch(Exception e){
            logger.warn("Error setting the date format", e);
        }
    }
    
    /**
     * if not defined or set then it defaults to the current date
     */
    public Object generate()
    {
        return sdf.format(runDate);
    }
    
    
    public void destroy() {
    }


	public boolean isListCompatible() {
		return false;
	}


	public Object generatefromlist(int pos, long numrecs, List<String> dslist) {
		return null;
	}


	public Date getRunDate() {
		return runDate;
	}


	public void setRunDate(Date runDate) {
		this.runDate = runDate;
	}
    
    
}
