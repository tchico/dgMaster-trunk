package generator.misc;

import generator.Schema.SchemaObject;
import generator.Schema.SchemaObjectAttribute;
import generator.Schema.SchemaObjectInterface;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SchemaSAXHandler  extends DefaultHandler{
    private Logger logger = Logger.getLogger(RandomTypeSAXHandler.class);
    private boolean event,entity;
    private List<SchemaObjectInterface> list;
    private SchemaObjectInterface current;
    
   public SchemaSAXHandler(){
    	list = new ArrayList<SchemaObjectInterface>();
    }
    
    public void startDocument() throws SAXException
    {
        logger.debug("Document parsing started");
    }
    
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
    {
        String attribName, attribValue,name,type,value,link;
        try{
        	
      
        logger.debug("Element found:"+qName);
        if(qName.equalsIgnoreCase("entity"))
        {
        	//create a new entity
        	name = attribValue = attributes.getValue(0);
        	current = new SchemaObject(name,qName);
        	list.add(current);
        	entity = true;
        }
        
        else if(qName.equalsIgnoreCase("event")){
        	//create a new event
        	name = attribValue = attributes.getValue(0);
        	current = new SchemaObject(name,qName);
        	list.add(current);
        	event = true;
        }
        else{
        	if(entity || event){
        		
        		SchemaObjectAttribute att = new SchemaObjectAttribute();
        		att.setType(qName);
        		for(int i=0; i<attributes.getLength(); i++)
                {
                    attribName  = attributes.getQName(i);
                    attribValue = attributes.getValue(i);
                    att.add(attribName,attribValue );
                }
        		current.addAttribute(att);
        	}
        	else{ // must be an event
        		
        	}
        }
        }catch(Exception e){
        	System.out.println(e);
        }
    }
    
    public void endElement (String uri, String localName, String qName) throws SAXException
    {
    	 if(qName.equalsIgnoreCase("entity"))
    		 entity = false;
         else if(qName.equalsIgnoreCase("event"))
         	event = false;
    }   
    
    public List<SchemaObjectInterface> getData()
    {
    	return list;
    }   
}
