package generator.stagebuild.impl;

import generator.extenders.IStageAwareRandomiserFunctionality;
import generator.extenders.RandomiserInstance;
import generator.misc.ApplicationContext;
import generator.misc.DataFileDefinition;
import generator.misc.EntityEventNode;
import generator.misc.EntityEventNodeInterface;
import generator.misc.RandomiserType;
import generator.stagebuild.StageBuilderConfig;
import generator.stagebuild.StageBuilderContext;
import generator.stagebuild.StageBuilderInterface;

import java.io.File;
import java.util.Date;
import java.util.Map;
import java.util.Vector;

import org.apache.log4j.Logger;


/**
 * Abstract Stage Builder
 * @author joaoesteves
 */
public abstract class AbstractBuilder implements StageBuilderInterface{
	protected static Vector<RandomiserType> vRandomiserTypes;
	protected static Vector<RandomiserInstance> vRandomiserInstances;
	protected static Vector<DataFileDefinition> vDataFileDefinitions;
	protected static EntityEventNodeInterface entityEventModel;

	private StageBuilderConfig config;
	private StageBuilderContext ctx ;
	private String name;
	protected Logger logger;
	
	public AbstractBuilder(String name, Logger logger){
		this.name = name;
		this.logger = logger;
	}
	
	public int build() throws StageBuilderConfigurationException{
		logger.info("Start stage build ["+getname()+"]. "+
				"Generating day "+ getCtx().getValString(StageBuilderContext.DAYNUM)
				+" of "+ getCtx().getValString(StageBuilderContext.NUMDAYS));
		logger.debug(config.getProp()) ;
		logger.debug(config.getShareddir()) ;
		logger.debug(config.getStagedir()) ;
		logger.debug("rundate : " + getCtx().getValString(StageBuilderContext.RUNDATE));
		initialise();
		return 0;
	}

	
	public int initialise() throws StageBuilderConfigurationException {
		
		
		String baseFolder = getCtx().getValString(StageBuilderContext.BASE_FOLDER);
		if(baseFolder == null || baseFolder.isEmpty()){
			throw new StageBuilderConfigurationException("Base folder not configured.");
		}
		if(!(new File(baseFolder)).exists()){
			throw new StageBuilderConfigurationException("Base folder not found.");
		}
		String baseConfFolder = baseFolder+"/conf";
		ApplicationContext.setBaseConfigurationFolder(baseConfFolder);

		/*
		 * LOAD THE FILE GENERATOR OUTPUT CONFIG FILES
		 */
		ConfigFilesLoader.loadFileGeneratorConfigFiles(logger, config);
		
		return 0;
	}

	
	
	/**
	 * Adds properties and context to the Randomisers 
	 * that are stage aware.
	 * {@link IStageAwareRandomiserFunctionality}
	 * @param vRandomiserInstances2
	 */
	protected void processStageAwareRandomisers(Vector<RandomiserInstance> vRandomiserInstances2, Date runDate) {
		for (RandomiserInstance randomiserInstance : vRandomiserInstances2) {
			if(randomiserInstance instanceof IStageAwareRandomiserFunctionality){
				((IStageAwareRandomiserFunctionality) randomiserInstance).setRunDate(runDate);
			}
		}
	}
	
	/**
	 * Release context data
	 */
	public void cleanup() {
		for (Map<String, Object> map : getCtx().getCtxList()) {
			map.clear();
		} 
		getCtx().getCtxList().clear();
	}
	
	public void setCtx(StageBuilderContext ctx) {
		this.ctx = ctx;
	}

	public StageBuilderContext getCtx() {
		if(ctx == null){
			ctx = new StageBuilderContext();
		}
		return ctx;
	}
	
	public String getname() {
		return this.name ;
	}


	public StageBuilderConfig getConfig() {
		return config;
	}


	public void setConfig(StageBuilderConfig config) {
		this.config = config;
	}
	
	public void setEntityEventModel(EntityEventNodeInterface entityEventModel){
		this.entityEventModel = entityEventModel;
	}

}
