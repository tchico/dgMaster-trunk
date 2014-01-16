package generator.stagebuild.impl;

import generator.misc.ApplicationContext;
import generator.stagebuild.StageBuilderConfig;

import java.io.FileNotFoundException;

import org.apache.log4j.Logger;


/**
 * Config files loader
 * @author joaoesteves
 *
 */
public class ConfigFilesLoader {
	
	/**
	 * Loads all the configuration files related to a file generation.
	 * @param logger
	 * @param config
	 * @throws StageBuilderConfigurationException
	 */
	static public void loadFileGeneratorConfigFiles(Logger logger, StageBuilderConfig config)
			throws StageBuilderConfigurationException {
		logger.debug("LOADING RANDOMISER TYPES");
		try {
			ApplicationContext.getInstance().refreshRandomiserTypes();
		} catch (FileNotFoundException e2) {
			throw new StageBuilderConfigurationException(e2.getMessage());
		}

		logger.debug("LOADING RANDOMISER INSTANCES");
		try {
			String suffix = null;
			if(config.getProp() != null){
				suffix = config.getProp().getProperty(StageBuilderConfig.PROPERTY_REPOSITORY_SUFFIX);
				suffix = (suffix != null && suffix.isEmpty())?null:suffix;
			}
			ApplicationContext.getInstance().refreshRandomiserInstances(suffix);
		} catch (FileNotFoundException e1) {
			throw new StageBuilderConfigurationException(e1.getMessage());
		}

		logger.debug("LOADING FILE DEFINITIONS");
		try {
			String suffix = null;
			if(config.getProp() != null){
				suffix = config.getProp().getProperty(StageBuilderConfig.PROPERTY_DEFINITION_SUFFIX);
				suffix = (suffix != null && suffix.isEmpty())?null:suffix;
			}
			ApplicationContext.getInstance().refreshFileDefinitions(suffix);
		} catch (FileNotFoundException e) {
			throw new StageBuilderConfigurationException(e.getMessage());
		}
	}
	
	/**
	 * Loads all the configuration related to a DB generation
	 * @param logger
	 */
	static public String loadDBGeneratorConfigFiles(Logger logger){
		String errorMessage = null;
		try{
			logger.debug("LOADING DB DEFINITIONS");
	        ApplicationContext.getInstance().refreshDBDefinitions();
		}catch(Exception e){
			errorMessage = e.getMessage();
		}
        
		try{
			logger.debug("LOADING DRIVERS INFORMATION");
			ApplicationContext.getInstance().refreshDriverInfo();
		}catch(Exception e){
			errorMessage = "\n"+e.getMessage();
		}
		
		try{
	        logger.debug("LOADING JAVA-SQL MAPPINGS");
	        ApplicationContext.getInstance().refreshSQLJavaMappings();
		}catch(Exception e){
			errorMessage = "\n"+e.getMessage();
		}
		return errorMessage;
	}
}
