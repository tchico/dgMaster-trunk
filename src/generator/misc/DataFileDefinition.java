/*
 * dgMaster: A versatile, open source data generator.
 *(c) 2007 M. Michalakopoulos, mmichalak@gmail.com
 */


package generator.misc;
import java.util.Vector;

/**
 *
 * Represents the information used to create a new text output file.
 * It also holds a vector of DataFileItem which define what to output in the
 * text file.
 */
public class DataFileDefinition
{
    private String name;
    private String description;
    private Vector<DataFileItem> outDataItems; //a place holder for DataFileItem
    private String outFilename;
    private String delimiter;
    private long numOfRecs;
    
    
    /** Creates a new instance of FileDefinition */
    public DataFileDefinition()
    {
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Vector<DataFileItem> getOutDataItems()
    {
        return outDataItems;
    }

    public void setOutDataItems(Vector<DataFileItem> vOutData)
    {
        this.outDataItems = vOutData;
    }

    public String getOutFilename()
    {
        return outFilename;
    }

    public void setOutFilename(String outFilename)
    {
        this.outFilename = outFilename;
    }

    public String getDescription()
    {
        return this.description;        
    }
    
    public String getDelimiter()
    {
        return delimiter;
    }

    public void setDelimiter(String delimiter)
    {
        this.delimiter = delimiter;
    }

    public long getNumOfRecs()
    {
        return numOfRecs;
    }

    public void setNumOfRecs(long numOfRecs)
    {
        this.numOfRecs = numOfRecs;
    }
    
    public void setDescription(String description)
    {
        this.description = description;
    }
    
    public String toString()
    {
        return name;        
    }
}
