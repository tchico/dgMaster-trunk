package generator.stagebuild;

import static org.junit.Assert.fail;
import generator.masterbuild.MasterBuilder;
import generator.stagebuild.impl.GenericStageBuilder;
import generator.stagebuild.impl.StageBuilderConfigurationException;

import java.io.File;
import java.util.Date;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;


/**
 * 
 * @author joaoesteves
 *
 */
public class GenericStageBuilderTest {
	private GenericStageBuilder builder;
	private String baseFolder;
	private String baseOutFolder;
	
	@Before
	public void setUp() throws Exception {
		builder = new GenericStageBuilder();
		baseFolder = (new File("DataBuild/staging/stage1")).getAbsolutePath();
		baseOutFolder = baseFolder+"/output";
		
		MasterBuilder masterBuilder = new MasterBuilder("DataBuild/conf/application.properties");
		StageBuilderConfig config = masterBuilder.initStageConfig("1");
		
		Date runDate = new Date();
		StageBuilderContext ctx =  new StageBuilderContext() ; 
		ctx.addToCtx(StageBuilderContext.RUNDATE, runDate.getClass().getName() , runDate) ;
		ctx.addToCtx(StageBuilderContext.DAYNUM, "int" , 1 ) ;
		ctx.addToCtx(StageBuilderContext.NUMDAYS, "int" , 10 ) ;
		ctx.addToCtx(StageBuilderContext.BASE_FOLDER, "String" , baseFolder) ;
		builder.setConfig(config);
		builder.setCtx(ctx);
		
		//cleanup the output folder
		File folder = new File(baseOutFolder);
		for (File childFile : folder.listFiles()) {
			childFile.delete();
		}
	}

	@Test
	public void testCleanup() {
		for(int i = 0; i < 100; i++){
			builder.getCtx().addToCtx("key"+i, "String"+i, "This is the value"+i);
		}
		int beforeSize = builder.getCtx().getCtxList().size();
		builder.cleanup();
		int afterSize = builder.getCtx().getCtxList().size();
		Assert.assertTrue(afterSize == 0);
		Assert.assertTrue(beforeSize > afterSize);
	}

	@Test
	public void testBuild() {
		try {
			builder.build();
		} catch (StageBuilderConfigurationException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
		//check if the file was created
		File folder = new File(baseOutFolder);
		boolean foundFile = false;
		for (File childFile : folder.listFiles()) {
			foundFile = childFile.getName().endsWith("test_1.txt");
			if(foundFile) break;
		}
		if(!foundFile){
			fail("output file was not created.");
		}
	}

}
