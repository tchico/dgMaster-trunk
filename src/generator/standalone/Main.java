package generator.standalone;

import generator.masterbuild.MasterBuilder;
import generator.misc.ApplicationContext;
import generator.stagebuild.StageBuilderContext;
import generator.stagebuild.impl.GenericStageBuilder;
import generator.stagebuild.impl.StageBuilderConfigurationException;

import java.io.File;
import java.util.Date;

import org.apache.log4j.xml.DOMConfigurator;


public class Main {

	public static void main(String[] args) throws Exception {
    	String baseStageFolder = "";
    	String definitionsConfigFolder ="";
    	
    	if (args.length > 1 ) {
    		baseStageFolder = args[0];
    		definitionsConfigFolder = args[1];
    		if(baseStageFolder.equals(".")){
    			baseStageFolder = "";
    		}
    		if(definitionsConfigFolder.equals(".")){
    			definitionsConfigFolder = "";
    		}
    	}
    	
    	DOMConfigurator.configure(definitionsConfigFolder+"generator.xml");
    	ApplicationContext.setBaseConfigurationFolder(baseStageFolder);
    	ApplicationContext.setDefinitionsConfigurationFolder(definitionsConfigFolder);
    	
    	GenericStageBuilder builder = new GenericStageBuilder();
		String baseFolder = (new File("DataBuild/staging/stage1")).getAbsolutePath();
		
		MasterBuilder masterBuilder = new MasterBuilder("DataBuild/conf/application.properties");
		masterBuilder.initStageConfig("1");
		
		Date runDate = new Date();
		StageBuilderContext ctx =  new StageBuilderContext() ; 
		ctx.addToCtx(StageBuilderContext.RUNDATE, runDate.getClass().getName() , runDate) ;
		ctx.addToCtx(StageBuilderContext.DAYNUM, "int" , 1 ) ;
		ctx.addToCtx(StageBuilderContext.NUMDAYS, "int" , 10 ) ;
		ctx.addToCtx(StageBuilderContext.BASE_FOLDER, "String" , baseFolder) ;
		
		builder.setCtx(ctx);
		try {
			builder.build();
		} catch (StageBuilderConfigurationException e) {
			e.printStackTrace();
			System.exit(1);
		}
		System.exit(0) ;
        
    }
    
    
}
