
package generator.randomisers;

import generator.misc.Utils;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;
import java.util.Vector;
import org.apache.log4j.Logger;
import generator.extenders.IRandomiserFunctionality;
import generator.extenders.RandomiserInstance;

import generator.misc.PDFDiscrete;

public class DoubleListRandomiser implements IRandomiserFunctionality
{
    Logger logger = Logger.getLogger(DoubleListRandomiser.class);
    Random probability, nullGen ; // gen;
    PDFDiscrete gen ;
    Vector<String> vItems;
    boolean fileSelected;
    double[] limits;
    int nulls;
    
    /**
     * Creates a new instance of DoubleListRandomiser
     */
    public DoubleListRandomiser()
    {
    }
    
    public void setRandomiserInstance(RandomiserInstance ri)
    {        
        String inputSource, sRangesNum, sItem, sPercentField, sNulls;
        int rangesNum ;
        double percentField[] = null;
        LinkedHashMap<String,String> hashMap;
        
        hashMap = ri.getProperties();
        inputSource = (String) hashMap.get("inputSource");
        if(inputSource.toLowerCase().equalsIgnoreCase("list"))
        {
            sRangesNum  = (String) hashMap.get("rangesNum");
            //System.out.println(sRangesNum) ;
            sNulls = (String) hashMap.get("nullField");
            try
            {
                rangesNum = Integer.parseInt(sRangesNum);
                nulls = Integer.parseInt(sNulls);
                percentField = new double[rangesNum];
                //limits = new int[rangesNum+1];//space reserved for first pos.
                limits = new double[rangesNum+1];//
            }
            catch(Exception e)
            {
                logger.warn(ri.getName() +": Error setting the numerical values (1 - init)", e);
                nulls=0; rangesNum=0; percentField=null;
            }
            
            limits[0] = 0;
            vItems = new Vector<String>();
            for(int i=1; i<=rangesNum; i++)
            {
                try
                {
                    sItem = (String) hashMap.get("itemField"+ (i-1) );
                    vItems.add(sItem);
                    sPercentField = (String) hashMap.get("percentField"+ (i-1) );
                    //System.out.println(sPercentField) ;
                    //System.out.println(sItem) ;
                    //System.out.println(Double.parseDouble(sPercentField)) ;
                    percentField[i-1] = Double.parseDouble(sPercentField);
                    //if(percentField[i-1]==-1)
                    //    percentField[i-1] = 100 / rangesNum;
                    //limits[i] = limits[i-1] + percentField[i-1];
                }
                catch(Exception e)
                {
                    logger.warn(ri.getName() +": Error setting the numerical values (2 - Loop, index="+i+")", e);
                }
            }
            fileSelected = false;
        }
        else
        {
            Utils utils = new Utils();
            String inputFile = (String) hashMap.get("inputFile");
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
            fileSelected = true;
        }
        probability = new Random();
        nullGen     = new Random();
        //gen         = new Random();

        gen = new PDFDiscrete(percentField) ;
    }
    
    public Object generate()
    {
        //check the nulls first

        int idxGen = 0;
        
        nullGen.nextInt(100);
        /*
        if(prob<nulls)
            return null;
        
        if(fileSelected==false)
        {
            prob = probability.nextInt(100);
            for(int i=1; i<limits.length; i++)
            {
                if(prob>=limits[i-1] && prob<limits[i])
                {
                    idxGen=i-1;
                    break;
                }
            }
        }
        else
        {
            //idxGen = gen.nextInt( vItems.size() );
        	
            idxGen = gen.next() ;
            System.out.println("Hello : " + idxGen) ;
        }
        */
        idxGen = gen.next() ;
        
        return vItems.elementAt(idxGen);
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