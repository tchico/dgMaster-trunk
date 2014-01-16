/*
 * dgMaster: A versatile, open source data generator.
 *(c) 2007 M. Michalakopoulos, mmichalak@gmail.com
 */

package generator.engine.file;
import generator.Schema.SchemaObjectAttributeInterface;
import generator.engine.ProgressUpdateObservable;
import generator.engine.ProgressUpdateObserver;
import generator.extenders.IRandomiserFunctionality;
import generator.extenders.RandomiserInstance;
import generator.masterbuild.MasterBuilder;
import generator.misc.Constants;
import generator.misc.DataFileDefinition;
import generator.misc.DataFileItem;
import generator.misc.EntityEventNode;
import generator.misc.EntityEventNodeInterface;
import generator.misc.RandomiserType;
import generator.misc.StageInstanceInterface;
import generator.misc.Utils;
import generator.randomisers.ConditionalFieldRandomiserWithPercentageDistribution;
import generator.randomisers.MultipleRandomisersWithPercentageDistribution;
import generator.randomisers.RandomiserWithPercentageDistribution;
import generator.randomisers.StageAmountRandomiser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.Vector;

import org.apache.log4j.Logger;


public class Generator implements ProgressUpdateObservable
{
	EntityEventNodeInterface entityEventModel;
    Vector<RandomiserType> vRandomiserTypes;
    Vector<RandomiserInstance> vRandomiserInstances;
    Vector<DataFileDefinition> vDataFileDefinitions;
    DataFileDefinition dataFileDefinition;
    
    Logger logger = Logger.getLogger(Generator.class);
    
    ProgressUpdateObserver observer;
    
    public static int rowRefId; // need this so in memory randomisers know what row is in question
    
    /** Creates a new instance of generateData */
    public Generator()
    {
    }
    
    
//    /**
//     * Loads the randomiser types, randomiser instances and file definitions.
//     * This is used when using the application from the command line.
//     */
//    public void loadfromFiles(String folder)
//    {
//        logger.debug("Loading data from files (RandomiserTypes, randomiser instances, file definitions)");
//        Utils utils = new Utils();
//        
//        //load the randomiser-type definitions from file
//        vRandomiserTypes = utils.loadRandomiserTypes();
//        
//        //load the randomiser-instance definitions from the file
//        vRandomiserInstances = utils.loadRandomiserInstances();
//        
//        //load file definitions
//        File definitionsFiles;
//		try {
//			definitionsFiles = ApplicationContext.findDefinitionsFileInFolder(folder);
//			vDataFileDefinitions = utils.loadDataFileDefinitions(definitionsFiles.getAbsolutePath());
//			logger.debug("Loading data from files (RandomiserTypes, randomiser instances, file definitions) Done.");
//		} catch (FileNotFoundException e) {
//			logger.debug("Loading data from files failed.", e);
//		}
//        
//    }
    
    
    /**
     * Loads the randomiser types, randomiser instances and file definitions.
     * This is used when using the application from withing the GUI.
     */
    public void setEngineData(Vector<RandomiserType> vRT, Vector<RandomiserInstance> vRI, Vector<DataFileDefinition> vDFDs)
    {
        this.vRandomiserTypes = vRT;
        this.vRandomiserInstances = vRI;
        this.vDataFileDefinitions = vDFDs;
    }
    public void setEngineData(Vector<RandomiserType> vRT, Vector<RandomiserInstance> vRI, Vector<DataFileDefinition> vDFDs, EntityEventNodeInterface node)
    {
        this.vRandomiserTypes = vRT;
        this.vRandomiserInstances = vRI;
        this.vDataFileDefinitions = vDFDs;
        this.entityEventModel = node;
    }
    
    
    /**
     * Loads the data file definition object; this contains all the
     * generate text file's details.
     * It takes a DataFileDefinition parameter, so the application
     * should have already loaded that prior to calling this method.
     */
    public void setFileDefinitionOutput(DataFileDefinition dfd)
    {
        this.dataFileDefinition = dfd;
    }
    
    
    
    /**
     * Loads the data file definition object; this contains all the
     * generate text file's details.
     * Preconditions: vDataFileDefinitions must NOT be null :)
     */
    public boolean setFileDefinitionOutput(String fileDefinition)
    {
        boolean found=false;
        
        logger.debug("Searching definition: " +fileDefinition);
        DataFileDefinition dfd;
        
        for(int i=0; i<vDataFileDefinitions.size(); i++)
        {
            dfd = vDataFileDefinitions.elementAt(i);
            if(dfd.getName().equalsIgnoreCase(fileDefinition))
            {
                dataFileDefinition = dfd;
                found = true;
                break;
            }
        }
        if(dataFileDefinition!=null)
            logger.debug("Searching definition: Done (loaded)");
        else
            logger.warn("Searching definition, failed.");
        
        return found;
    }
    
    
    /**
     * Creates the requested output file. If the file can not created,
     * null is returned, the caller should check this
     */
    private OutputStreamWriter  createOutputFile(String filename)
    {
    	OutputStreamWriter  fileWriter = null;
        
        try
        {
        	File file = new File(filename);
        	if(!file.exists()){
        		file.createNewFile();
        	}
        	
        	
        	
        	FileOutputStream out =  new FileOutputStream(filename);
            fileWriter = new OutputStreamWriter(out,Charset.forName("UTF-8").newEncoder());
        }
        catch(IOException ioe)
        {
            logger.error("Output file not created",ioe);
        }
        return fileWriter;
    }
    
    private OutputStreamWriter  createOutputFile(String filename,String type)
    {
    	OutputStreamWriter  fileWriter = null;
    	List<String> fileContents = new ArrayList<String>();
    	FileOutputStream out = null;
        try
        {
        	File file = new File(filename);
        	if(!file.exists()){
        		file.createNewFile();
        		
        		out =  new FileOutputStream(filename);
        		fileWriter = new OutputStreamWriter(out,Charset.forName("UTF-8").newEncoder());
        	}
        	else if(type.equalsIgnoreCase("event")){ // if its an event  then read the file first so you can append, could be a batch
        		Scanner scanner = new Scanner(new File(dataFileDefinition.getOutFilename()),"UTF-8");
        		
        		
				while (scanner.hasNextLine()){
        			fileContents.add(scanner.nextLine());
        			
        		}
				out =  new FileOutputStream(filename);
        		fileWriter = new OutputStreamWriter(out,Charset.forName("UTF-8").newEncoder());
        		for(String s: fileContents){
        			fileWriter.write(s);
        			fileWriter.write(System.getProperty("line.separator")) ;
        		}
        	}
        	else{
        		out =  new FileOutputStream(filename);
        		fileWriter = new OutputStreamWriter(out,Charset.forName("UTF-8").newEncoder());
        	}
        }
        catch(IOException ioe)
        {
            logger.error("Output file not created",ioe);
        }
        return fileWriter;
    }
    
    /**
     * Returns a RandomiserInstance object, given a string.
     * Preconditions: vRandomiserInstances must NOT be null :)
     */
    private RandomiserInstance getRandomiserInstance(String riName)
    {
        RandomiserInstance ri = null;
        
        boolean found = false;
        int i=0;
        
        logger.debug("Retrieving randomiserInstance object for:"+riName);
        while( i<vRandomiserInstances.size() && !found)
        {
            ri   = vRandomiserInstances.elementAt(i);
            if(ri.getName().equalsIgnoreCase(riName))
                found = true;
            i++;
        }
        logger.debug("Retrieving the randomiserInstance for:"+riName + ". Found:"+found);
        return ri;
    }
    
    
    /**
     * Returns the name of a RandomiserType class,
     * given its name in the application.
     * Preconditions: vRandomiserTypes must NOT be null :)
     */
    private RandomiserType getRandomiserType(String randomiserType)
    {
        RandomiserType rt = null;
        
        boolean found = false;
        int i=0;
        
        logger.debug("Retrieving randomiserInstance object for:"+randomiserType);
        while( i<vRandomiserTypes.size() && !found)
        {
            rt = vRandomiserTypes.elementAt(i);
            if(rt.getName().equalsIgnoreCase(randomiserType))
            {
                found = true;
            }
            i++;
        }
        logger.debug("Retrieving the randomiserType for:"+randomiserType + ". Found:"+found +", class:"+rt.getName());
        return rt;
    }
    
    
    // padLeft, padRight, padCenter are used when aligning data fields.
    private String padLeft(String s, int width)
    {
//        int paramWidth = s.length();
        int pad = width - s.length();
        String temp="", retValue;
        
        for(int i=0; i<pad; i++)
            temp=temp +" ";
        retValue = temp +s;
        
        return retValue;
    }
    
    private String padRight(String s, int width)
    {
//        int paramWidth = s.length();
        int pad = width - s.length();
        String temp="", retValue;
        
        for(int i=0; i<pad; i++)
            temp=temp +" ";
        retValue = s + temp;
        
        return retValue;
    }
    
    // [*] may not exactly pad the correct amount of spaces
    private String padCenter(String s, int width)
    {
//        int paramWidth = s.length();
        int pad = (width - s.length())/2; //[*] tricky integer division :P
        String temp="", retValue;
        
        for(int i=0; i<pad; i++)
            temp=temp +" ";
        retValue = temp + s + temp;
        if(retValue.length()<width)
            retValue=" "+retValue;
        else if(retValue.length()>width)
            retValue=retValue.substring(0, retValue.length()-1);
        return retValue;
    }
    
    
    
    
    public void generate()
    {
        long start = System.currentTimeMillis();
        Vector<DataFileItem> vDataItems;
        OutputStreamWriter fileWriter;
        int i;
        long numOfRecs;
        boolean error, cancelled;
        String temp, outLine, riName, enclChar;
        IRandomiserFunctionality aGenerator[];
        Utils utils;
        RandomiserInstance ri;
        RandomiserType rt;
        Object objValue;
        StringBuffer sb;
        
        TreeMap<Integer,String> ordering;
        TreeMap<String,String> unOrderingOutput;
        Vector<DataFileItem> vDataItemsClone = new Vector<DataFileItem>();
        
        
        fileWriter = createOutputFile( dataFileDefinition.getOutFilename() );
        if(fileWriter==null)
        {
            logger.error("Error while creating output file:" + dataFileDefinition.getOutFilename());
            observer.datageGenError("Error while creating the output file.");
            return;
        }
        
        
        //load the generator objects together with the appropriate randomiser instances as set by the user
        vDataItems   = dataFileDefinition.getOutDataItems(); //user requested data
        aGenerator   = new IRandomiserFunctionality[vDataItems.size()]; //the generators
        utils        = new Utils();
       
        
        //create ordering map, because its a tree map it will order them naturally
        
        ordering = new TreeMap<Integer,String>();
        unOrderingOutput =  new TreeMap<String,String>();
        int maxOrder = -1;
        int uniqueNum = 0;
    
        for(int k = 0; k < vDataItems.size();k++){ // find the highest ordering number
        	vDataItemsClone.add(new DataFileItem(vDataItems.elementAt(k)));//clone
        	if (maxOrder < vDataItemsClone.elementAt(k).getOrder())
        		maxOrder= vDataItemsClone.elementAt(k).getOrder();
        	
        	//prevent duplicate names, otherwise the randomname will override each other
        	vDataItemsClone.elementAt(k).setRandomiserInstanceName(vDataItems.elementAt(k).getRandomiserInstanceName()+""+uniqueNum);
        	uniqueNum++;
        }
        
        for(int k = 0; k < vDataItemsClone.size();k++){
        	int value =  vDataItemsClone.elementAt(k).getOrder();
        	if(value == -1){// default value
        		value = ++maxOrder; 
        		//maxOrder++;
        	}
        	ordering.put(value,vDataItemsClone.elementAt(k).getRandomiserInstanceName());
        	unOrderingOutput.put(vDataItemsClone.elementAt(k).getRandomiserInstanceName(), "");
        }
       
        
        
        //now we can notify the observer about the number of steps
        notifyInit();
        notifyMaxProgressValue(vDataItems.size());
        try
        {
            for(int j=0; j<vDataItems.size(); j++)
            {
                DataFileItem dataItem = vDataItems.elementAt(j);
                riName = dataItem.getRandomiserInstanceName();
                logger.debug("Loading class for :"+ riName);
                
                //inform user
                cancelled = notifyProgrssUpdate("Loading initialiser for:"+ riName, j+1);
                
                //create the randomiser instance out of the name
                ri = getRandomiserInstance(riName);
                
                //get the randomiser type out of the RI
                rt = getRandomiserType( ri.getRandomiserType() );
                
                if(rt.getGenerator().equalsIgnoreCase("generator.randomisers.RandomiserWithPercentageDistribution")){
                	//then need to use targetRandomiser value
                	String target = ri.getProperties().get("targetRandomiser"); // randomiser we want, so initalise this and give it to this randomiser
                	RandomiserInstance ri2 = getRandomiserInstance(target);
                	RandomiserType rt2 = getRandomiserType( ri2.getRandomiserType() );
                	IRandomiserFunctionality  targetRandomiser = (IRandomiserFunctionality) utils.createObject(rt2.getGenerator());
                	
                	//load and store the generator, set its RI, now it is ready to use
	                aGenerator[j] = (IRandomiserFunctionality) utils.createObject(rt.getGenerator());
	                aGenerator[j].setRandomiserInstance(ri);
	                logger.debug("Loading class for :"+ riName  +". Done");
	                
	                ((RandomiserWithPercentageDistribution) aGenerator[j]).setTargetRandomiser(targetRandomiser);
                }
                else{
	                //load and store the generator, set its RI, now it is ready to use
	                aGenerator[j] = (IRandomiserFunctionality) utils.createObject(rt.getGenerator());
	                aGenerator[j].setRandomiserInstance(ri);
	                logger.debug("Loading class for :"+ riName  +". Done");
                }
            }
        }
        catch(Exception e)
        {
            logger.error("Error while initialising a generator",e);
            observer.datageGenError("Error while initialising a generator:"+e);
            return;
        }
        
        //now generate the data, notify the observer about the number of records
        numOfRecs = dataFileDefinition.getNumOfRecs();        
        notifyMaxProgressValue((int)numOfRecs);
        
        i=0; error=false; cancelled = false;
        while( i<numOfRecs && !error && !cancelled)
        {
            try
            {
                //generates a line
                outLine="";
                sb = new StringBuffer();
                List<String> sbList = new ArrayList<String>();
                for(int j=0; j<vDataItemsClone.size(); j++)
                {
                    //retrieve format parameters for this generator
                    DataFileItem dataItem = vDataItemsClone.elementAt(j);
                    
                    // [TM] 
                    if(aGenerator[j].isListCompatible())
                    {
                    	logger.debug("Class " + aGenerator[j].toString() +" is list compatible " );
                    	objValue = aGenerator[j].generatefromlist(i,numOfRecs,sbList) ;
                    }
                    else
                    {
                    	logger.debug("Class " + aGenerator[j].toString() +" is not list compatible " );	
                    	 objValue = aGenerator[j].generate();
                    }
                    //
                   
                    if(objValue==null)
                        objValue="";
                    temp = objValue.toString();
                    
                    logger.debug("Class " + aGenerator[j].toString() +" :  generated  " + temp);
                    
                    enclChar = dataItem.getEncloseChar();
                    if(temp.length()<dataItem.getWidth())
                    {
                        if(dataItem.getAlignment()==Constants.ALIGN_LEFT)
                            temp = padRight(temp,dataItem.getWidth());
                        else if(dataItem.getAlignment()==Constants.ALIGN_RIGHT)
                            temp = padLeft(temp,dataItem.getWidth());
                        else
                            temp = padCenter(temp,dataItem.getWidth());
                    }
                    
                   
                    
                    sb.append(enclChar);
                    sb.append(temp);                   
                    sb.append(enclChar);
                    //sb.append(dataFileDefinition.getDelimiter());
                    unOrderingOutput.put(dataItem.getRandomiserInstanceName(), sb.toString());
                    sb = new StringBuffer();
                    
                    sbList.add(temp) ;
                }
                
                Collection<String> orderedValues = ordering.values();
                for(String s : orderedValues){
                	sb.append(unOrderingOutput.get(s));
                	sb.append(dataFileDefinition.getDelimiter());
                }
                
                
                //inform the observer
                cancelled = notifyProgrssUpdate("Generating ("+ (i+1) + "/" +numOfRecs+")", i+1 );
                
                //remove the last delimiter[*] what if there is none?
                sb.deleteCharAt( sb.lastIndexOf(dataFileDefinition.getDelimiter()));
                outLine = sb.toString();
                fileWriter.write(outLine);
                //fileWriter.newLine();
                fileWriter.write(System.getProperty("line.separator")) ;
                logger.debug(outLine);
                for(int n=0; n<sbList.size(); n++) {
                	logger.debug("Position : " + n +" :: Value : " + sbList.get(n));
                }
                
                logger.debug("DS Position : " + i +" :: Total Records : " + numOfRecs);
                i++;
            }
            catch(IOException ioe)
            {
                logger.warn("Exception error while writing data, will exit after loops:"+i, ioe);
                error=true;
            }
        }//end while
        
        try
        {
            fileWriter.close();
        }
        catch(IOException ioe)
        {
            logger.warn("Exception error while closing file:", ioe);
            observer.datageGenError("Error during a file operation!");
            return;
        }
        
        long stop = System.currentTimeMillis();
        logger.info("Time in millis:" + (stop-start) );
        if(error)
        {
            observer.datageGenError("There were errors during the data generation progress, possibly a file write error.");
            return;
        }
        //notify observer, we are done
        notifyEnd();
    }
    
    public void registerObserver(ProgressUpdateObserver observer)
    {
        this.observer = observer;
    }
    
    public void unregisterObserver()
    {
        this.observer = null;
    }
    
    public void notifyInit()
    {
        if(observer==null)
            return;
        observer.dataGenStarted();
    }
    
    public void notifyMaxProgressValue(int max)
    {
        if(observer==null)
            return;        
        observer.dataGenMaxProgressValue(max);
    }
    
    
    public boolean notifyProgrssUpdate(String msg, int progress)
    {
        if(observer==null)
            return false;
        
        return observer.dataGenProgressContinue(msg, progress);
    }
    
    public void notifyEnd()
    {
        if(observer==null)
            return;
        
        observer.dataGenEnd();
        observer=null;
    }
    
    public void datageGenError(String msg)
    {
        if(observer==null)
            return;
        
        observer.datageGenError(msg);
        observer.dataGenEnd();
    }
    
    public void generate(String type)
    {
    	StringBuffer currentFileContents = new StringBuffer();
    	
        long start = System.currentTimeMillis();
        Vector<DataFileItem> vDataItems;
        OutputStreamWriter fileWriter = null;
        int i;
        long numOfRecs;
        boolean error, cancelled;
        String temp, outLine, riName, enclChar;
        IRandomiserFunctionality aGenerator[];
        IRandomiserFunctionality stageAmountRandomiser;
        DataFileItem stageAmountDataItem = null;
        Utils utils;
        RandomiserInstance ri;
        RandomiserType rt;
        Object objValue;
        StringBuffer sb;
        boolean hasStageAmountRandomiser = false;
        
        TreeMap<Integer,String> ordering;
        TreeMap<String,String> unOrderingOutput;
        Vector<DataFileItem> vDataItemsClone = new Vector<DataFileItem>();
        
       
    	
        fileWriter = createOutputFile( dataFileDefinition.getOutFilename(),type );
        if(fileWriter==null)
        {
            logger.error("Error while creating output file:" + dataFileDefinition.getOutFilename());
            observer.datageGenError("Error while creating the output file.");
            return;
        }
        
        
        //load the generator objects together with the appropriate randomiser instances as set by the user
        vDataItems   = dataFileDefinition.getOutDataItems(); //user requested data
        aGenerator   = new IRandomiserFunctionality[vDataItems.size()]; //the generators
        utils        = new Utils();
       
        
        //create ordering map, because its a tree map it will order them naturally
        
        ordering = new TreeMap<Integer,String>();
        unOrderingOutput =  new TreeMap<String,String>();
        int maxOrder = -1;
        int uniqueNum = 0;
    
        for(int k = 0; k < vDataItems.size();k++){ // find the highest ordering number
        	
        	// check for an order 2, remove it
        	if(vDataItems.elementAt(k).getOrder() == -2){
        		hasStageAmountRandomiser = true;
        		stageAmountDataItem = vDataItems.elementAt(k);
        		vDataItems.remove(k);
        		k--;
        		//set the stageamountrandomiser
        	}
        	else{
	        	vDataItemsClone.add(new DataFileItem(vDataItems.elementAt(k)));//clone
	        	if (maxOrder < vDataItemsClone.elementAt(k).getOrder())
	        		maxOrder= vDataItemsClone.elementAt(k).getOrder();
	        	
	        	//prevent duplicate names, otherwise the randomname will override each other
	        	vDataItemsClone.elementAt(k).setRandomiserInstanceName(vDataItems.elementAt(k).getRandomiserInstanceName()+""+uniqueNum);
	        	uniqueNum++;
        	}
        }
        
        for(int k = 0; k < vDataItemsClone.size();k++){
        	int value =  vDataItemsClone.elementAt(k).getOrder();
        	if(value == -1){// default value
        		value = ++maxOrder; 
        		//maxOrder++;
        	}
        	ordering.put(value,vDataItemsClone.elementAt(k).getRandomiserInstanceName());
        	unOrderingOutput.put(vDataItemsClone.elementAt(k).getRandomiserInstanceName(), "");
        }
       
        
        
        //now we can notify the observer about the number of steps
        notifyInit();
        notifyMaxProgressValue(vDataItems.size());
        try
        {
            for(int j=0; j<vDataItems.size(); j++)
            {
                DataFileItem dataItem = vDataItems.elementAt(j);
                riName = dataItem.getRandomiserInstanceName();
                logger.debug("Loading class for :"+ riName);
                
                //inform user
                cancelled = notifyProgrssUpdate("Loading initialiser for:"+ riName, j+1);
                aGenerator[j] = initaliseRandomiser(riName);
            }
        }
        catch(Exception e)
        {
            logger.error("Error while initialising a generator",e);
            observer.datageGenError("Error while initialising a generator:"+e);
            return;
        }
        
        error = false;
        
        
        // check for an order 2 randomiser, initialise it, run it
        if(hasStageAmountRandomiser && (MasterBuilder.currentDay < 2)){
        	// clear the current stage instances from here down!
        	clearStageInstances(MasterBuilder.currentNode);
        	
        	String linkageNode = null;
         	String [] pdValues = null;
         	ArrayList<Double> per = new ArrayList<Double>(); // percentage
         	int perCount = 0;
         	
         	//are there any linkages!!
         	for(SchemaObjectAttributeInterface a :MasterBuilder.currentNode.getStageDataContext().getAttributes()){
    				if(a.getType().equalsIgnoreCase("linkage")){
    					linkageNode = a.getValue("node"); // get the entity/event it references
    					pdValues = a.getValue("dis").split("\\|"); // need to split this up and get the dis values!!
    					for(String s: pdValues){
    						per.add(Double.parseDouble(s));
    						perCount++;
    					}
    				}
         	}
         	
          	riName = stageAmountDataItem.getRandomiserInstanceName();
        	stageAmountRandomiser = initaliseRandomiser(riName);// init the randomiser
        	EntityEventNodeInterface keyStageNode = ((EntityEventNode) MasterBuilder.headNode.getNode(((StageAmountRandomiser) stageAmountRandomiser).getKeyStageName()));
            
        	for(int k = 0; k < MasterBuilder.headNode.getNode(((StageAmountRandomiser) stageAmountRandomiser).getKeyStageName()).getStageInstancesCount(); k++){
        		this.rowRefId = k; // the stage instance in the key stage that we are getting the amount from
        		
        		Integer amount = Integer.parseInt((String) stageAmountRandomiser.generate()); // amount of this stage we need to make for this particular keystage instance

        		if(MasterBuilder.currentNode.getParent().getName().equalsIgnoreCase(keyStageNode.getName())){ // parent is the keystage, so map directly
        			
        			((EntityEventNode) MasterBuilder.currentNode).build(k,amount,MasterBuilder.currentNode.getParent().getStageInstance(k));
        		}else{ // parent is not the keystage, so need to map them// is there a linkage?
        			if(linkageNode != null){ // then we have linkage values to work with
        				List<StageInstanceInterface> childrenToConnectTo = keyStageNode.findLinkageStage(linkageNode, k);
        	         	int count = 0;
        	         	for(Double p:per){ // for every distribution
        	         		int pp = p.intValue();
        	         		
        	         		Double tempAmount = ((amount.doubleValue()/100)*pp); //// this value distribtuion needs to be altered, not accurate enough
        	         		if(tempAmount < 1){
        	         			tempAmount = 1.0;
        	         		}
        	         		if(perCount == count+1){ // last dis, so split the values over the remaining values
        	         			
        	         			tempAmount = tempAmount/(childrenToConnectTo.size()-count);
        	         			if(tempAmount < 1){
            	         			tempAmount = 1.0;
            	         		}
        	         			while(count < childrenToConnectTo.size()){
        	         				((EntityEventNode) MasterBuilder.currentNode).build(childrenToConnectTo.get(count).getId(),tempAmount.intValue(),childrenToConnectTo.get(count)); 
        	         				count++;
        	         			}
        	         		}
        	         		else{
        	         			((EntityEventNode) MasterBuilder.currentNode).build(childrenToConnectTo.get(count).getId(),tempAmount.intValue(),childrenToConnectTo.get(count)); 
        	         			count++;
        	         		}
        	         		
        	         	}
        	         	
        			}	// public static void setValues(ArrayList<Double> percentage, ArrayList<Integer> dis){
        			else{ // no linkage node,distribute in order
        				List<StageInstanceInterface> childrenToConnectTo = keyStageNode.findLinkageStage(MasterBuilder.currentNode.getParent().getName(), k);
	        	        Double tempAmount = amount.doubleValue()/ childrenToConnectTo.size();
	        	        Double leftOver = 0.0;
	         			for(StageInstanceInterface si:childrenToConnectTo){
	         				// make a double, add up left overs
	         				((EntityEventNode) MasterBuilder.currentNode).build(si.getId(),(new Double(tempAmount.doubleValue()+leftOver.doubleValue())).intValue(),si); 
	         				leftOver = (tempAmount+leftOver) % 1.0;
	         			}
        			}
        		}
        	}
        }
        	
        //now generate the data, notify the observer about the number of records
        numOfRecs = this.entityEventModel.getStageInstancesCount();//dataFileDefinition.getNumOfRecs();        
        notifyMaxProgressValue((int)numOfRecs);
        
        i=0; error=false; cancelled = false;
        while( i<numOfRecs && !error && !cancelled)
        {
        	rowRefId = i; // has to be static so that the randomiser can access it, need this so can tell the field in the in memory model
            try
            {
                //generates a line
                outLine="";
                sb = new StringBuffer();
                List<String> sbList = new ArrayList<String>();
                for(int j=0; j<vDataItemsClone.size(); j++)
                {
                    //retrieve format parameters for this generator
                    DataFileItem dataItem = vDataItemsClone.elementAt(j);
                    
                    // [TM] 
                    if(aGenerator[j].isListCompatible())
                    {
                    	logger.debug("Class " + aGenerator[j].toString() +" is list compatible " );
                    	objValue = aGenerator[j].generatefromlist(i,numOfRecs,sbList) ;
                    }
                    else
                    {
                    	logger.debug("Class " + aGenerator[j].toString() +" is not list compatible " );	
                    	 objValue = aGenerator[j].generate();
                    }
                    //
                   
                    if(objValue==null)
                        objValue="";
                    temp = objValue.toString();
                    
                    logger.debug("Class " + aGenerator[j].toString() +" :  generated  " + temp);
                    
                    enclChar = dataItem.getEncloseChar();
                    if(temp.length()<dataItem.getWidth())
                    {
                        if(dataItem.getAlignment()==Constants.ALIGN_LEFT)
                            temp = padRight(temp,dataItem.getWidth());
                        else if(dataItem.getAlignment()==Constants.ALIGN_RIGHT)
                            temp = padLeft(temp,dataItem.getWidth());
                        else
                            temp = padCenter(temp,dataItem.getWidth());
                    }
                    
                   
                    
                    sb.append(enclChar);
                    sb.append(temp);                   
                    sb.append(enclChar);
                    //sb.append(dataFileDefinition.getDelimiter());
                    unOrderingOutput.put(dataItem.getRandomiserInstanceName(), sb.toString());
                    sb = new StringBuffer();
                    
                    sbList.add(temp) ;
                }
                
            	// if this is an entity, may have to clean the values array each time, otherwise data will jack up
                if(type.equalsIgnoreCase("event")){
                	entityEventModel.cleanValuesOfInstance(i);
            	}
                
                Collection<String> orderedValues = ordering.values();
                int indexOfColumn = 0;
                for(String s : orderedValues){
                	sb.append(unOrderingOutput.get(s));
                	sb.append(dataFileDefinition.getDelimiter());

                	//update the node in the tree
                	if(!type.equalsIgnoreCase("Event")){
                		String columnName = entityEventModel.getColumns().get(indexOfColumn++);
                		entityEventModel.putValueToStageInstance(i, columnName, unOrderingOutput.get(s));
                	}
                }
                	
                
                
                //inform the observer
                cancelled = notifyProgrssUpdate("Generating ("+ (i+1) + "/" +numOfRecs+")", i+1 );
                
                //remove the last delimiter[*] what if there is none?
                sb.deleteCharAt( sb.lastIndexOf(dataFileDefinition.getDelimiter()));
                outLine = sb.toString();
                fileWriter.write(outLine);
                fileWriter.write(System.getProperty("line.separator")) ;
                logger.debug(outLine);
                for(int n=0; n<sbList.size(); n++) {
                	logger.debug("Position : " + n +" :: Value : " + sbList.get(n));
                }
                
                logger.debug("DS Position : " + i +" :: Total Records : " + numOfRecs);
                i++;
            }
            catch(IOException ioe)
            {
                logger.warn("Exception error while writing data, will exit after loops:"+i, ioe);
                error=true;
            }
        }//end while
        
        
        try
        {
            fileWriter.close();
        }
        catch(IOException ioe)
        {
            logger.warn("Exception error while closing file:", ioe);
            observer.datageGenError("Error during a file operation!");
            return;
        }
        
        long stop = System.currentTimeMillis();
        logger.info("Time in millis:" + (stop-start) );
        if(error)
        {
            observer.datageGenError("There were errors during the data generation progress, possibly a file write error.");
            return;
        }
        //notify observer, we are done
        notifyEnd();
    }
    
    /**
     * extracted the creation of randomisers due to some randomsiers pointing at others, randomiser creation needs to be a method
     * @param riName the name of the randomiser to create
     * @return
     */
    public IRandomiserFunctionality initaliseRandomiser(String riName){
    	RandomiserInstance ri = getRandomiserInstance(riName);
    	IRandomiserFunctionality val = null;
    	Utils utils = new Utils(); 
        //get the randomiser type out of the RI
    	RandomiserType rt = getRandomiserType( ri.getRandomiserType() );
        
        if(rt.getGenerator().equalsIgnoreCase("generator.randomisers.RandomiserWithPercentageDistribution")){
        	//then need to use targetRandomiser value
        	String target = ri.getProperties().get("targetRandomiser"); // randomiser we want, so initalise this and give it to this randomiser
        	IRandomiserFunctionality  targetRandomiser =  initaliseRandomiser(target);
        	
        	//load and store the generator, set its RI, now it is ready to use
        	val = (IRandomiserFunctionality) utils.createObject(rt.getGenerator());
        	val.setRandomiserInstance(ri);
             logger.debug("Loading class for :"+ riName  +". Done");
            
            ((RandomiserWithPercentageDistribution) val).setTargetRandomiser(targetRandomiser);
            return val;
            
        }else if(rt.getGenerator().equalsIgnoreCase("generator.randomisers.MultipleRandomisersWithPercentageDistribution")|| rt.getGenerator().equalsIgnoreCase("generator.randomisers.StageAmountRandomiser")){ // multiple distribution for multiple randomisers
        	
        	val = (IRandomiserFunctionality) utils.createObject(rt.getGenerator());
        	val.setRandomiserInstance(ri);
             logger.debug("Loading class for :"+ riName  +". Done");
             
        	if(ri.getProperties().containsKey("targetRandomiser0")){ // add all the specified randomisers to the list using 
        		int exit = 1; //used to break the loop 
        		for(int k =0; k < exit; k++){
        			String target = ri.getProperties().get("targetRandomiser"+k);
                	IRandomiserFunctionality  targetRandomiser =  initaliseRandomiser(target);
                	if(rt.getGenerator().equalsIgnoreCase("generator.randomisers.MultipleRandomisersWithPercentageDistribution"))
                		((MultipleRandomisersWithPercentageDistribution) val).addTargetRandomiser(targetRandomiser);
                	else
                		((StageAmountRandomiser) val).addTargetRandomiser(targetRandomiser);
        			if(ri.getProperties().containsKey("targetRandomiser"+(k+1))){ //if there is another then extend the loop
        				exit++;
        			}
        		}
        		return val;
        		
        	}
        	else{ // no inital randomiser, not initalised correctly
        		logger.debug("No initail target randomiser, the randomiser has not been setup correctly");
        	}
        }
        else if(rt.getGenerator().equalsIgnoreCase("generator.randomisers.ConditionalFieldRandomiserWithPercentageDistribution")){
        	val = (IRandomiserFunctionality) utils.createObject(rt.getGenerator());
        	val.setRandomiserInstance(ri);
             logger.debug("Loading class for :"+ riName  +". Done");
             

             if(ri.getProperties().containsKey("fieldDataRandomiser")){ //set the field getter randomiser
     			String target = ri.getProperties().get("fieldDataRandomiser");
             	IRandomiserFunctionality  targetRandomiser =  initaliseRandomiser(target);
             	
             	 ((ConditionalFieldRandomiserWithPercentageDistribution) val).setFieldDataRandomiser(targetRandomiser);
             	 
             	if(ri.getProperties().containsKey("targetRandomiser0")){ // now load all of the target randomisers
            		int exit = 1; //used to break the loop 
            		for(int k =0; k < exit; k++){
            			target = ri.getProperties().get("targetRandomiser"+k);
                    	targetRandomiser =  initaliseRandomiser(target);
                    	
                    	 ((ConditionalFieldRandomiserWithPercentageDistribution) val).addTargetRandomiser(targetRandomiser);
            			if(ri.getProperties().containsKey("targetRandomiser"+(k+1))){ //if there is another then extend the loop
            				exit++;
            			}
            		}
            	}
            	else{ // no inital randomiser, not initalised correctly
            		logger.debug("No initial target randomiser, the randomiser has not been setup correctly");
            	}
             	return val; 
         	 }
             else{
            	 logger.debug("No initial target randomiser, the randomiser has not been setup correctly");
             }
         		
        	return null;
        }
      
        else{
            //load and store the generator, set its RI, now it is ready to use
        	val = (IRandomiserFunctionality) utils.createObject(rt.getGenerator());
        	val.setRandomiserInstance(ri);
            logger.debug("Loading class for :"+ riName  +". Done");
            return val;
        }
        
		return null;
    	
    }
    
    public void clearStageInstances(EntityEventNodeInterface  node){
    	((EntityEventNode) node).clearStageInstances();
    	for(EntityEventNodeInterface n: node.getChildren()){
    		clearStageInstances(n);
    	}
    }
}
