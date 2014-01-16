/*
 * Generate an empty string
 */

package generator.randomisers;
import java.util.List;
import org.apache.log4j.Logger;
import generator.extenders.IRandomiserFunctionality;
import generator.extenders.RandomiserInstance;

public class EmptyStringGenerator implements IRandomiserFunctionality
{
    Logger logger = Logger.getLogger(EmptyStringGenerator.class);
    

    
    /** Creates a new instance of RandomTextRandomiser */
    public EmptyStringGenerator()
    {
    }

    public void setRandomiserInstance(RandomiserInstance ri)
    {
    	logger.debug("EmptyStringGenerator set.");
      
    }

    public Object generate()
    {

    	String sNull = "" ;
        return sNull ;
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
