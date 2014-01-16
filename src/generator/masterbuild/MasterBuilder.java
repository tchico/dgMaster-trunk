package generator.masterbuild;

import generator.Schema.SchemaObjectInterface;
import generator.extenders.RandomiserInstance;
import generator.misc.ApplicationContext;
import generator.misc.DataFileDefinition;
import generator.misc.DataFileItem;
import generator.misc.EntityEventNode;
import generator.misc.EntityEventNodeInterface;
import generator.misc.SchemaSAXHandler;
import generator.misc.StageDataContext;
import generator.stagebuild.StageBuilderConfig;
import generator.stagebuild.StageBuilderContext;
import generator.stagebuild.StageBuilderInterface;
import generator.stagebuild.impl.AbstractBuilder;
import generator.stagebuild.impl.StageBuilderConfigurationException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.Vector;

import javax.xml.parsers.SAXParserFactory;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;



public class MasterBuilder {

	public static final String STAGING_BASED_STAGES = "stage";
	public static final String TIME_BASE_STAGES = "time";
	

    
    /**
     * 
     * @param args
     */
    public static void main(String []args)
    {
    	String propsFile = null;
    	if (args.length == 1){
    		propsFile = args[0];
        }
        else{
            die("Please specify application.properties file.");
        }
    	
        try{
        	MasterBuilder app = new MasterBuilder(propsFile);

        	if  ( app.getBuildOrder().compareTo(TIME_BASE_STAGES) == 0 ) {
        		app.constructStages(); // once the ratios are worked out, we will know how many things need to be created, can then call the generate
            	app.timeorderBuild() ;
        		
            }
            else if ( app.getBuildOrder().compareTo(STAGING_BASED_STAGES) == 0 ) {
            	app.constructStages(); 
				app.stageorderBuild() ;
			}
            else {
            	System.out.println("Please specify the order to build.");
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
            die(ex.getMessage());
        }
    }
	
	/**
     * Fatal error - message and exit program
     * @param errmsg
     */
    public final static void die(String errmsg) {
    	System.out.println(errmsg) ;
    	System.exit(1) ;
    }

    /* ***********************************************************************/
    
	private Logger logger = Logger.getLogger(MasterBuilder.class);
	private Properties prop = new Properties();
	private int numStages ;
	private String sharedDir ;
	private String baseDir ; 
	private String stageBaseDir ;
	private String log4jConfig ;
	private String systemDefinitions ;
	private File sharedDirFh ;
	private File baseDirFh ; 
	private File stageBaseDirFh ;
	private File systemDefinitionsFh ;
	private Date startDate ;
	private String keyStage;
	private String entityEventModelFileDir;
	private int keyStageAbsoluteNumber;
	private int numDays ;
	private String buildOrder = TIME_BASE_STAGES ;
	private HashMap<Integer,StageBuilderInterface> stages = new HashMap<Integer, StageBuilderInterface>();
	private List<SchemaObjectInterface> entityEventMap;
	public static EntityEventNodeInterface headNode,currentNode;
	public static int currentDay;
	
	/**
	 * 
	 * @param settingsPath path to the settings file
	 */
	public MasterBuilder(String settingsPath){
		if(prop.size() == 0){
			readSettings(settingsPath);
		}
		
		try {
			init();
		} catch (PropertyNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param settingspath  properties file path
	 */
	private void readSettings(String settingspath) 
    {                       
        try {
			prop.load(new FileInputStream(settingspath));
			/*Logger not initialised yet.*/
			System.out.println("INFO   :  Loading application properties .");
			for (String key : prop.keySet().toArray(new String[0])){
				System.out.println(key + " = " + prop.getProperty(key));
			}	
			System.out.println("INFO   :  Properties loaded .");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try{
			StageDataContext.init();
			ApplicationContext.getInstance().loadStageDataContext(settingspath.substring(0, settingspath.lastIndexOf("/"))+"/"+readProperty("dgmaster.masterbuild.default.stagedatacontextdefinition"));
		
		}
		catch(Exception e){
			System.out.println("Error trying to load Master Stage Data Values");
		}
        
		try{// my test run
			loadStageDataContextData(settingspath);
		}
		catch(Exception e){
			
		}
		
    }
	
    
    /**
     * initialisations
     * @throws PropertyNotFoundException
     */
    private void init() throws PropertyNotFoundException
    {
    	
    	this.currentDay =0;
    	// Is the basedir set and readable ?
    	baseDir = readProperty(BuilderProps.DGMASTER_MASTERBUILD_BASEDIR);
    	baseDirFh = new File(baseDir) ;
    	if ( !baseDirFh.canRead() ) {
    		die("Cannot read from basedir .");
    	}
    	
    	
    	// Initialise logger properties.
    	log4jConfig = baseDir + "/conf/" + readProperty(BuilderProps.DGMASTER_MASTERBUILD_LOG4J_CONFIG) ;
    	
    	DOMConfigurator.configure(log4jConfig);
    	logger.info("Initialised logger from properties file " + log4jConfig);
    	
    	//Is the stagebasedir set and readable 
    	stageBaseDir = readProperty(BuilderProps.DGMASTER_MASTERBUILD_STAGEBASEDIR);
    	
    	stageBaseDir = baseDir + "/" + stageBaseDir ; 
    	stageBaseDirFh = new File(stageBaseDir) ;
    	if ( !stageBaseDirFh.canRead() ) {
    		die("Cannot read from stagebasedir .");
    	}    	
    	logger.info("stagebasedir is " + stageBaseDir);
    	
    	//Is the sharededir set and readable
    	sharedDir = readProperty(BuilderProps.DGMASTER_MASTERBUILD_SHAREDEDIR);
    	sharedDir = baseDir + "/" + sharedDir ; 
    	sharedDirFh = new File(sharedDir) ;
    	if ( !sharedDirFh.canRead() ) {
    		die("Cannot read from sharededir .");
    	}      	
    	logger.info("sharededir is " + sharedDir);
    	
    	//Is the systemdefinitions set and readable
    	systemDefinitions = readProperty(BuilderProps.DGMASTER_MASTERBUILD_DEFAULT_SYSTEMDEFINITIONS);
    	systemDefinitions = baseDir + "/conf/" + systemDefinitions ; 
    	systemDefinitionsFh = new File(systemDefinitions) ;
    	if ( !systemDefinitionsFh.canRead() ) {
    		die("Cannot read from systemdefinitions .");
    	}
    	ApplicationContext.setDefinitionsConfigurationFolder(baseDir + "/conf/");
    	logger.info("systemdefinitions is " + systemDefinitions);    	
    	
    	// make sure numstages is set and is > 0
    	numStages = Integer.parseInt(readProperty(BuilderProps.DGMASTER_MASTERBUILD_NUMSTAGES)) ;
    	logger.info("numstages is " + numStages) ;
    	
    	// When to begin ....
    	SimpleDateFormat format = new SimpleDateFormat("ddMMyyyy");
    	try {
			startDate = format.parse(readProperty(BuilderProps.DGMASTER_MASTERBUILD_START_DATE)) ;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		logger.info("Data start_date is : " + startDate.toString());
    	// and for how long ...
        numDays  = Integer.parseInt(readProperty(BuilderProps.DGMASTER_MASTERBUILD_NUMBER_OF_DAYS));
        logger.info("numdays is : " + numDays );
        // when we finish ...
        Calendar cal = Calendar.getInstance() ;
        cal.setTime(startDate) ;
        cal.add(Calendar.DATE, numDays-1);
        Date end_date = cal.getTime() ;
        logger.info("Data end_date is : " + end_date.toString());
        
        setEntityEventModelFileDir(readProperty(BuilderProps.DGMASTER_MASTERBUILD_ENTITYEVENTMODELFILEDIR));
        keyStage = readProperty(BuilderProps.DGMASTER_MASTERBUILD_KEYSTAGE);
        keyStageAbsoluteNumber = Integer.parseInt(readProperty(BuilderProps.DGMASTER_MASTERBUILD_KEYSTAGEABSOLUTENUMBER));
        	//dgmaster.masterbuild.KeyStage=customer
        	//dgmaster.masterbuild.KeyStageAbsoluteNumber=10000
        
    	// stage build order 
        try{
        	buildOrder = readProperty(BuilderProps.DGMASTER_MASTERBUILD_BUILDORDER);
        }catch(PropertyNotFoundException e){
        	//do nothing, let the default value remain
        }
        	
        if ( !buildOrder.equals(TIME_BASE_STAGES) && !buildOrder.equals(STAGING_BASED_STAGES))  {
        	die("Invalid value for  buildorder : " + buildOrder  );
        }
    	logger.info("buildorder is " + buildOrder) ;        
        
    }

    /**
     * Builds the stage builder configuration
     * @param stageNumber - string with the number of the stage as defined in the application properties
     * @return
     * @throws PropertyNotFoundException
     */
    public StageBuilderConfig initStageConfig(String stageNumber) throws PropertyNotFoundException {
    	return MasterBuilder.initStageConfig(stageNumber, logger, stageBaseDir, sharedDir, prop);
    }

    /**
     * Builds the stage builder configuration
     * @param stageNumber - string with the number of the stage as defined in the application properties
     * @return
     * @throws PropertyNotFoundException
     */
	public static StageBuilderConfig initStageConfig(String stageNumber, Logger logger, String stageBaseDir, String sharedDir, Properties prop)
			throws PropertyNotFoundException {
		StageBuilderConfig stageconfig = new StageBuilderConfig() ;

		logger.info("Stage builder for stage " + stageNumber + " is " +  readProperty(BuilderProps.DGMASTER_MASTERBUILD_STAGEBUILDER + stageNumber, prop) );
		
		String stagedir = stageBaseDir + "/" + readProperty(BuilderProps.DGMASTER_MASTERBUILD_STAGEDIR + stageNumber  ,prop) ;
		String stagepropertiesfile = stagedir + "/conf/" +  readProperty(BuilderProps.DGMASTER_MASTERBUILD_STAGEPROPERTIESFILE + stageNumber  ,prop) ;
		
		stageconfig.setPropertiesfile(stagepropertiesfile) ;
		stageconfig.setShareddir(sharedDir) ;
		stageconfig.setStagedir(stagedir) ;
		return stageconfig;
	}
    
    
    
    /**
     * Execute all stages ordered by time;
     * It will first build all stages for a day before going to the next one (day).
     * @throws PropertyNotFoundException 
     * 
     */
    public void timeorderBuild (){
    	
    	logger.info("Starting time ordered build ." );
    	Date runDate = startDate ;
    	Iterator<String> it = nodeMap.keySet().iterator();

		logger.info("");
		logger.info("Creating Stages for Validation");
		logger.info("");
		for (int i=1; i <= numStages ; i++) { // this is also going to add randomiser names to the fields in the node
			currentNode = nodeMap.get(it.next());
			loadSingleStage(runDate, i, 0, numDays, systemDefinitions);
		}
		
		logger.info("");
		logger.info("All Stages Validated with no errors");
		logger.info("");
		
		
		int stageNum = 1;
		EntityEventNodeInterface tempNode;
		// generate the entities
		it = nodeMap.keySet().iterator();
		while(it.hasNext()){
			String nodeName = (String) it.next();
			tempNode = nodeMap.get(nodeName);
			if(tempNode.getType().equalsIgnoreCase("entity")){ // build and populate all of the entities now based on the nodeMap
				currentNode = tempNode;// now this node will be passed to the stage builder
				buildSingleStage(runDate, stageNum, 0, numDays, systemDefinitions);
			}
			stageNum++;
		}
		// generate the events
		
		it = nodeMap.keySet().iterator();
    	for (int o=1; o <= numDays ; o++) {
    		stageNum = 1;
    		it = nodeMap.keySet().iterator();
    		while(it.hasNext()){
    			String nodeName = (String) it.next();
    			tempNode = nodeMap.get(nodeName);
    			if(tempNode.getType().equalsIgnoreCase("event")){ // build and populate all of the entities now based on the nodeMap
    				currentNode = tempNode;// now this node will be passed to the stage builder
    				currentDay = o;
    				buildSingleStage(runDate, stageNum, o, numDays, systemDefinitions);
    				
    			}
    			stageNum++;
    		}
    		runDate = addOneDayToRunDate(runDate); // Could add a master property to not execute this, that would put all output in one file
    	}
    	
    }



    
    
    
    /**
     * Execute all stages ordered by stage;
     * It will first build all days for a stage before going to the next one.
     * @throws PropertyNotFoundException 
     * 
     */
    public void stageorderBuild (){
    	
    	logger.info("Starting stage ordered build ." );
    	Date runDate = startDate ;

    	logger.info("");
		logger.info("Creating Stages for Validation");
		logger.info("");
		for (int i=1; i <= numStages ; i++) {
			loadSingleStage(runDate, i, 0, numDays, systemDefinitions);
		}
		
		logger.info("");
		logger.info("All Stages Validated with no errors");
		logger.info("");
		
		
    	for (int o=1; o <= numStages ; o++) {
    		  
    		logger.info("Generating all days for stage " + o);
    		for (int i=1; i <= numDays ; i++) {
    			buildSingleStage(runDate, i, o, numDays, systemDefinitions);
        		runDate = addOneDayToRunDate(runDate);
    		}
    	}   
    }


    /**
     * Loads and validates the stage specified by the stageNumber, it also takes the ordering of the randomisers/columns and adds them to the model
     * @param runDate running date
	 * @param stageNumber number of the stage to be created
	 * @param currentDayNumber current day number for this stage day. (0 < currentDayNumber < totaDays)
	 * @param totalDays total number of days that are being generated externally.
	 * @param systemDefs system definitions
     * @throws PropertyNotFoundException
     */
   
    private void loadSingleStage(Date runDate, int stageNumber, int currentDayNumber, int totalDays, String systemDefs){
    	try {
    		Vector<RandomiserInstance> vRandomiserInstances = null;
			StageBuilderInterface currentStage = initSingleStage(runDate, stageNumber, currentDayNumber, totalDays, systemDefs); // init
			((AbstractBuilder) currentStage).initialise();
			
			ApplicationContext context = ApplicationContext.getInstance();
			vRandomiserInstances = context.getRandomiserInstances();
			
			Vector<DataFileDefinition> vDFDs = context.getDFD();
			
			//Verify file paths for inputSource files.
			for(RandomiserInstance i : vRandomiserInstances){
				if(i.getProperties().containsKey("inputSource")){
					if(i.getProperties().get("inputSource").equalsIgnoreCase("file")){
						File file = new File(i.getProperties().get("inputFile"));
						if(file.canRead() == false){
							logger.warn("Cannot find " + file.toString() + " for randomiser "+ i.getName() +" in stage:" + stageNumber);
							throw new FileNotFoundException();
						}
					}
				}
			}
			logger.info("InputSources for Randomisers Validated");

			
			
			logger.info("Adding Randomiser names to the Columns in the Entity Event Model Nodes");
			// need to somehow order the list
			int maxOrder = -1;
			Vector<DataFileItem> vDataItem = vDFDs.get(0).getOutDataItems();
			TreeMap<Integer,String> tree = new TreeMap<Integer,String>();
			
			 for(int k = 0; k < vDataItem.size();k++){ // find the highest ordering number
		        	if (maxOrder < vDataItem.elementAt(k).getOrder())
		        		maxOrder= vDataItem.elementAt(k).getOrder();
			 }
			for(int k = 0; k < vDataItem.size();k++){
		      	int value =  vDataItem.elementAt(k).getOrder();
		      	if(value != -2){ // Don't want to add the randomisers that have no output
			        if(value == -1){// default value
			        	value = ++maxOrder; 
			       	}
			        tree.put(value,vDataItem.elementAt(k).getRandomiserInstanceName()); // these will all be unique, if not this will not work 
		      	}
				//currentNode.addColumn(value,vDataItem.elementAt(k).getRandomiserInstanceName()); // these will all be unique, if not this will not work 
			}
			
			Iterator<Integer> it = tree.keySet().iterator();
			while(it.hasNext()){// this will add the ordered randomiser names to the node, the same order they will be generated
				currentNode.addColumn(tree.get(it.next()));
			}
			logger.info("Entity Event Model Nodes Updated with Randomiser Column Names");

			//ConfigFilesLoader.loadFileGeneratorConfigFiles(logger, ((AbstractBuilder) currentStage).getConfig() );
			stages.put(stageNumber, currentStage);	
    	} catch (FileNotFoundException e) {
			logger.error("Stage not built! A file cannot be found or read.", e);
			die("Please make sure the file path specified is correct");
		}
    	catch (Exception e){
    		logger.error("Stage not built! Configuration Error", e);
    		die("Please review the configuration error above");
    	}
    }
    
    /**
     * executes the build for a single stage
     * @param runDate running date
	 * @param stageNumber number of the stage to be created
	 * @param currentDayNumber current day number for this stage day. (0 < currentDayNumber < totaDays)
	 * @param totalDays total number of days that are being generated externally.
	 * @param systemDefs system definitions
     * @throws PropertyNotFoundException
     */
	private void buildSingleStage(Date runDate, int stageNumber, int currentDayNumber, int totalDays, String systemDefs){
		try {
			StageBuilderInterface currentStage = initSingleStage(runDate, stageNumber, currentDayNumber, totalDays, systemDefs);
			currentStage.build() ;
			currentStage.cleanup();
		} catch (StageBuilderConfigurationException e) {
			logger.error("Stage not built! There's a problem with the configuration.", e);
		} catch (InstantiationException e) {
			logger.error("Stage not built!", e);
		} catch (IllegalAccessException e) {
			logger.error("Stage not built!", e);
		} catch (ClassNotFoundException e) {
			logger.error("Stage not built! Class defined in configuration not found.", e);
		} catch (PropertyNotFoundException e) {
			logger.error("Stage not built! Property not found in configuration. Review the config files.", e);
		} 
	}


	/**
	 * Inits a single stage, sets the context, and returns the stage interface ready to be executed. 
	 * @param runDate running date
	 * @param stageNumber number of the stage to be created
	 * @param currentDayNumber current day number for this stage day. (0 < currentDayNumber < totaDays)
	 * @param totalDays total number of days that are being generated externally.
	 * @param systemDefs system definitions
	 * @return the stage interface
	 * @throws PropertyNotFoundException
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public StageBuilderInterface initSingleStage(Date runDate,
			int stageNumber, int currentDayNumber, int totalDays,
			String systemDefs) throws PropertyNotFoundException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		
		//Set the stage config properties
		logger.info("Initialising stage " + stageNumber + " . " );
		String stri = Integer.toString(stageNumber) ;
    	StageBuilderConfig stageconfig = initStageConfig(stri);
    	
    	//Setup the stage context
		StageBuilderContext ctx =  new StageBuilderContext() ; 
		ctx.addToCtx(StageBuilderContext.RUNDATE, runDate.getClass().getName() , runDate) ;
		ctx.addToCtx(StageBuilderContext.DAYNUM, "int" , currentDayNumber ) ;
		ctx.addToCtx(StageBuilderContext.NUMDAYS, "int" , totalDays ) ;
		ctx.addToCtx(StageBuilderContext.BASE_FOLDER, "String", stageconfig.getStagedir());
		ctx.addToCtx(StageBuilderContext.SHARE_FOLDER, "String", stageconfig.getShareddir());
    	
    	//get the stage builder class and instantiate it
		String stagebldstr = prop.getProperty(BuilderProps.DGMASTER_MASTERBUILD_STAGEBUILDER + stri  ) ;
		StageBuilderInterface currentStage = (StageBuilderInterface) Class.forName(stagebldstr).newInstance();
		
		//set the context
		currentStage.setConfig(stageconfig);
		currentStage.setCtx(ctx) ;
		currentStage.setEntityEventModel(MasterBuilder.currentNode);
		return currentStage;
		
	} 
    


	private Date addOneDayToRunDate(Date rundate) {
		Calendar cal = Calendar.getInstance() ;
		cal.setTime(rundate) ;
		cal.add(Calendar.DATE, 1);
		rundate = cal.getTime() ;
		return rundate;
	}
    
    
	 /**
     * Reads a property value from the properties file
     * @param propertyName
     * @return
     */
	private String readProperty(String propertyName) throws PropertyNotFoundException{
		return readProperty(propertyName, prop);
	}
	
    /**
     * Reads a property value from the properties file
     * @param propertyName
     * @return
     */
	private static String readProperty(String propertyName, Properties prop) throws PropertyNotFoundException{
		String value = prop.getProperty(propertyName);
    	if (value == null || value == "" )
    	{
    		throw new PropertyNotFoundException("Property not set "+propertyName+" .");
    	}
    	return value;
	}

	public Properties getProp() {
		return prop;
	}

	public void setProp(Properties prop) {
		this.prop = prop;
	}

	public String getBuildOrder() {
		return buildOrder;
	}

	public void setBuildOrder(String buildOrder) {
		this.buildOrder = buildOrder;
	}
	
	 /**
	  *  Loads the master conf file from file. File structure is very important, it must match the stages for the randomisers
	 * @param file
	 * @throws PropertyNotFoundException 
	 */
	public void loadStageDataContextData(String file) throws PropertyNotFoundException{
		 String filepath = readProperty(BuilderProps.DGMASTER_MASTERBUILD_BASEDIR)+readProperty(BuilderProps.DGMASTER_MASTERBUILD_ENTITYEVENTMODELFILEDIR); // hard coded, must change to use the passed para
		 
	    	SAXParserFactory factory = SAXParserFactory.newInstance();  
	        
	        logger.debug("About to parse xml file: " + filepath);
	        try
	        {
	            javax.xml.parsers.SAXParser saxParser;
	            saxParser = factory.newSAXParser();
	            SchemaSAXHandler saxHandler  = new SchemaSAXHandler();
	            saxParser.parse( new File(filepath), saxHandler );
	            this.entityEventMap = saxHandler.getData(); // list is now loaded from the config file
	            
	        }
	        catch (Throwable err)
	        {
	            logger.error("Error during parsing xml file: "+err.getMessage());
	        }
	        
	        
	    }
	 
 	/**
     * Builds the in memory node tree based on the master conf/ entityeventrealtion file, it then populates the model with placeholder stageInstances and parent refs to the instance that created them
     * @param 
     * @return
     */
	public void constructStages(){
		
		SchemaObjectInterface sdc = this.entityEventMap.get(0);
		headNode = new EntityEventNode(sdc, this.entityEventMap); // When you create the head node it cascades and creates the whole tree
		
		Timestamp time = new Timestamp(System.currentTimeMillis());
		headNode.getNode(keyStage).keyStageValue(keyStageAbsoluteNumber);// populate the tree based on the keystage and key values, this will now fully populate the tree with empty stage instances
		Timestamp timeAfter = new Timestamp(System.currentTimeMillis());
		System.out.println("Total Time taken in seconds : " + ((timeAfter.getTime() - time.getTime())/1000));
		
		//Calculate the numbers that are going to be generated
		nodeTotals = new LinkedHashMap<String,Integer>();
		nodeMap = new LinkedHashMap<String,EntityEventNodeInterface>();
		getStageTotals(headNode);
		Iterator<String> it = nodeTotals.keySet().iterator();
		while(it.hasNext()){
			String name = it.next();
			System.out.println(name + " : " + nodeTotals.get(name));
		}
		
	}
	private Map<String,Integer> nodeTotals;
	private LinkedHashMap<String,EntityEventNodeInterface> nodeMap;
	
	/**
     * Method that creates the node map and node totals by calling itself using the trees children, naturally ordering the maps 
     * @param node is the current node to process, get children from this node 
     * @return
     */
	public void getStageTotals(EntityEventNodeInterface node){
		nodeMap.put(node.getName(), node); // this will be used heavily in the ordered builds to cycle through the nodes
		nodeTotals.put(node.getName(),node.getStageInstancesCount().intValue());
		for(EntityEventNodeInterface child :node.getChildren()){ 
			getStageTotals(child);
		}
	}

	public String getEntityEventModelFileDir() {
		return entityEventModelFileDir;
	}

	public void setEntityEventModelFileDir(String entityEventModelFileDir) {
		this.entityEventModelFileDir = entityEventModelFileDir;
	}
	
}
