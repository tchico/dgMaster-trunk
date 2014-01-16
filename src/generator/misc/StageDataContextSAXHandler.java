package generator.misc;

import java.util.Vector;
import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class StageDataContextSAXHandler  extends DefaultHandler {
	
	    private Logger logger = Logger.getLogger(RandomTypeSAXHandler.class);
	    
	    public StageDataContextSAXHandler()
	    {
	    	
	    }
	    
	    public void startDocument() throws SAXException
	    {
	        logger.debug("Document parsing started");
	    }
	    
	    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
	    {
	        String attribName, attribValue,name,type,value;
	        
	        logger.debug("Element found:"+qName);
	        if(qName.equalsIgnoreCase("variable"))
	        {
	        	name = ""; value = ""; type ="";
	            for(int i=0; i<attributes.getLength(); i++)
	            {
	                attribName  = attributes.getQName(i);
	                attribValue = attributes.getValue(i);
	                logger.debug("Attribute name,value:"+attribName + "," + attribValue);        
	                
	                if( attribName.equalsIgnoreCase("name") )
	                    name = attribValue;
	                
	                if( attribName.equalsIgnoreCase("type") )
	                    type = attribValue;
	                    
	                if( attribName.equalsIgnoreCase("value") )
	                    value = attribValue;
	            }
	            StageDataContext.setValue(name, type, value);
	        }
	    }
	    
	    public void endElement (String uri, String localName, String qName) throws SAXException
	    {
	          
	    }   
	    
	    public void getData()
	    {
	    	
	    }   
}
