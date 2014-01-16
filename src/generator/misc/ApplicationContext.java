/*
 * ApplicationContext.java
 *
 * Created on 09 June 2007, 02:37
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package generator.misc;

import generator.db.SQLJavaMapping;
import generator.extenders.RandomiserInstance;
import generator.masterbuild.BuilderProps;
import generator.masterbuild.MasterBuilder;
import generator.masterbuild.PropertyNotFoundException;
import generator.stagebuild.StageBuilderConfig;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import org.apache.log4j.Logger;

/**
 *
 * @author Michael
 */
public class ApplicationContext
{
    private static ApplicationContext context = null;
    private static Vector<RandomiserInstance> vRI;
    private static Vector<RandomiserType> vRT;
    private static Vector<DataFileDefinition> vDFD;
    private static Vector<DBDriverInfo> vDBDriverInfo;
    private static Vector<DBFileDefinition> vDBFileDefinition;
    private static Vector<SQLJavaMapping> vSQLJavaMapping;
    private static Utils utils;
    private static String dataBuildBaseFolder = "";
    private static String baseConfigurationFolder = "";
    private static String definitionsConfigurationFolder = "";
	private static Properties masterBuilderProperties;
	private static Logger logger = Logger.getLogger(ApplicationContext.class);

    
    public static ApplicationContext getInstance()
    {
    	
        if(context==null)
        {
            context = new ApplicationContext(baseConfigurationFolder);
        }

        return context;
    }

    /** Creates a new instance of ApplicationContext */
    private ApplicationContext(String folder)
    {
    	baseConfigurationFolder = folder;
    	
        if(utils==null)
        {
            utils = new Utils();
        }
    }
    
    /**
     * Searchs for the randomiser instances configurations and refreshes the in memory configuration 
     * @param suffixSearchParameter if not provided it defaults to "repository.xml"
     * @throws FileNotFoundException
     */
    public void refreshRandomiserInstances(String... suffixSearchParameter) throws FileNotFoundException
    {
    	assert suffixSearchParameter.length <= 1;
    	String param = suffixSearchParameter.length > 0 ? suffixSearchParameter[0] : null;
    	
    	File[] repositoryFiles = findFileInFolder(baseConfigurationFolder, (param != null)?param:"repository.xml");
    	
    	if(vRI != null){
    		vRI.clear();
    	}
    	for (File file : repositoryFiles) {
    		Vector<RandomiserInstance> temp = utils.loadRandomiserInstances(file.getAbsolutePath());
    		if(vRI != null){
    			vRI.addAll(temp);
    		}else{
    			vRI = temp;
    		}
		}
    }
    
    public void loadStageDataContext(String file){
    	utils.loadStageDataContextData(file);
    }

    /**
     * Searchs for the randomiser instances configurations and refreshes the in memory configuration 
     * @param suffixSearchParameter if not provided it defaults to "definitions.xml"
     * @throws FileNotFoundException
     */
    public void refreshFileDefinitions(String... suffixSearchParameter) throws FileNotFoundException
    {
    	assert suffixSearchParameter.length <= 1;
    	String param = suffixSearchParameter.length > 0 ? suffixSearchParameter[0] : null;
    	
    	File[] definitionsFiles = findFileInFolder(baseConfigurationFolder, (param != null)?param:"Definitions.xml");
    	if(vDFD != null){
    		vDFD.clear();
    	}
    	for (File file : definitionsFiles) {
    		if(file.getName().toLowerCase().contains("db")) continue;
    		if(file.getName().toLowerCase().contains("system")) continue;
    		Vector<DataFileDefinition> temp = utils.loadDataFileDefinitions(file.getAbsolutePath());
    		if(vDFD != null){
    			vDFD.addAll(temp);
    		}else{
    			vDFD = temp;
    		}
		}
    }
    
    /**
     * Searchs for the randomiser types configurations and refreshes the in memory configuration 
     * @param suffixSearchParameter if not provided it defaults to "systemdefinitions.xml"
     * @throws FileNotFoundException
     */
    public  void refreshRandomiserTypes(String... suffixSearchParameter) throws FileNotFoundException
    {
    	assert suffixSearchParameter.length <= 1;
    	String param = suffixSearchParameter.length > 0 ? suffixSearchParameter[0] : null;
    	
    	File definitionsFile = findFileInFolder(definitionsConfigurationFolder, (param != null)?param:"systemdefinitions.xml")[0];
        vRT = utils.loadRandomiserTypes(definitionsFile.getAbsolutePath());
    }

    /**
     * Finds files in folder that are suffixed by the pattern
     * @param folder
     * @param pattern
     * @return
     * @throws FileNotFoundException
     */
	public static File[] findFileInFolder(String folder, final String pattern)
			throws FileNotFoundException {
		File baseFolder = new File(folder);
    	File[] definitionsFiles = baseFolder.listFiles(new FileFilter() {
			public boolean accept(File pathname) {
				return (pathname.getName().toLowerCase().endsWith(pattern.toLowerCase()));
			}
		});
    	if(definitionsFiles.length == 0){
    		throw new FileNotFoundException("No suitable configuration files found in "+folder);
    	}
		return definitionsFiles;
	}	

    public void refreshDBDefinitions()
    {
        vDBFileDefinition = utils.loadDBFileDefinitions(ApplicationContext.getDefinitionsConfigurationFolder());
    }
    
    public void refreshDriverInfo()
    {
        vDBDriverInfo = utils.loadDBDriversInfo(ApplicationContext.getDefinitionsConfigurationFolder());
    }

    public void refreshSQLJavaMappings()
    {
        vSQLJavaMapping = utils.loadSQLJavaMappings(ApplicationContext.getDefinitionsConfigurationFolder());
    }
    
    public Vector<RandomiserInstance> getRandomiserInstances()
    {
        return vRI;
    }

    public Vector<RandomiserType> getRandomiserTypes()
    {
        return vRT;
    }

    public Vector<DBDriverInfo> getDriverInfo()
    {
        return vDBDriverInfo;
    }

    public Vector<DataFileDefinition> getDFD()
    {
        return vDFD;
    }

    public void setDFD( Vector<DataFileDefinition> vDFDs)
    {
        vDFD = vDFDs;
    }


    public Vector<SQLJavaMapping> getSQLJavaMappings()
    {
        return vSQLJavaMapping;
    }


    public Vector<DBFileDefinition> getDBD()
    {
        return vDBFileDefinition;
    }

    public void setDBD( Vector<DBFileDefinition> vDBDs)
    {
        vDBFileDefinition = vDBDs;
    }    
    
    
    
    public void setRandomiserInstances(Vector<RandomiserInstance> vRandomInstances)
    {
        vRI = vRandomInstances;
    }

    public void setDBDriver(Vector<DBDriverInfo> vDBDInfo)
    {
        vDBDriverInfo = vDBDInfo;
    }

	public static synchronized String getBaseConfigurationFolder() {
		return baseConfigurationFolder;
	}

	public static synchronized void setBaseConfigurationFolder(String baseConfigurationFolder) {
		ApplicationContext.baseConfigurationFolder = baseConfigurationFolder;
	}

	public static synchronized String getDefinitionsConfigurationFolder() {
		return definitionsConfigurationFolder;
	}

	public static synchronized void setDefinitionsConfigurationFolder(
			String definitionsConfigurationFolder) {
		ApplicationContext.definitionsConfigurationFolder = definitionsConfigurationFolder;
		
		if(definitionsConfigurationFolder != null && !definitionsConfigurationFolder.isEmpty()){
	    	File foldr = new File(definitionsConfigurationFolder);
	    	dataBuildBaseFolder = foldr.getParentFile().getAbsolutePath();
    	}
		
		if(!ApplicationContext.definitionsConfigurationFolder.endsWith("\\")){
			ApplicationContext.definitionsConfigurationFolder = ApplicationContext.definitionsConfigurationFolder+"\\";
		}
		
		resetMasterBuilderProperties();
	}
	
	public static synchronized void resetMasterBuilderProperties() {
		masterBuilderProperties = null;
	}
	
	public static synchronized Properties getMasterBuilderProperties() throws FileNotFoundException, IOException{
		if(masterBuilderProperties == null){
			masterBuilderProperties = new Properties();
			masterBuilderProperties.load(new FileInputStream(getDefinitionsConfigurationFolder()+"application.properties"));
		}
		return masterBuilderProperties;
	}
	
	public static String getStagesRootFolder() throws FileNotFoundException, IOException{
		return ApplicationContext.getMasterBuilderProperties().getProperty(BuilderProps.DGMASTER_MASTERBUILD_STAGEBASEDIR);
	}

	public static synchronized List<StageBuilderConfig> findConfiguredStages() throws FileNotFoundException, IOException, PropertyNotFoundException{
		List<StageBuilderConfig> stagesConfig = new ArrayList<StageBuilderConfig>();
		String numStagesStr = getMasterBuilderProperties().getProperty(BuilderProps.DGMASTER_MASTERBUILD_NUMSTAGES);
		if(numStagesStr != null){
			int numStages = Integer.parseInt(numStagesStr);
			for(int i = 1 ; i <= numStages; i++){
				String stageBaseDir = getDataBuildBaseFolder()+"/"+getMasterBuilderProperties().getProperty(BuilderProps.DGMASTER_MASTERBUILD_STAGEBASEDIR);
				String sharedDir = getMasterBuilderProperties().getProperty(BuilderProps.DGMASTER_MASTERBUILD_SHAREDEDIR);
				StageBuilderConfig stageconfig = MasterBuilder.initStageConfig(""+i, logger, stageBaseDir, sharedDir, getMasterBuilderProperties());
				stagesConfig.add(stageconfig);
			}
		}
		return stagesConfig;
	}

	public static String getDataBuildBaseFolder() {
		return dataBuildBaseFolder;
	}



}
