package generator.masterbuild;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class MasterBuilderTest {
	private MasterBuilder masterBuilder;
	private String baseFolder;
	private int numberOfDays = 0;
	private int numberOfStages = 0;
	
	@Before
	public void setUp() throws Exception {
		masterBuilder = new MasterBuilder("DataBuild/conf/application.properties");
		String numDays = masterBuilder.getProp().getProperty(BuilderProps.DGMASTER_MASTERBUILD_NUMBER_OF_DAYS);
		numberOfDays = Integer.parseInt(numDays);
		
		String numStages = masterBuilder.getProp().getProperty(BuilderProps.DGMASTER_MASTERBUILD_NUMSTAGES);
		numberOfStages = Integer.parseInt(numStages);
		
		baseFolder = masterBuilder.getProp().getProperty(BuilderProps.DGMASTER_MASTERBUILD_BASEDIR);

		// cleanup the output folder
		for(int i = 1; i <= numberOfStages; i++){
			File folder = new File(baseFolder + "/staging/stage"+i+"/output/");
			System.out.println("Deleting contents of " + folder.getAbsolutePath());
			for (File childFile : folder.listFiles()) {
				childFile.delete();
			}
		}
	}
	
	@Test
	public void launchesDifferentTypesOfBuild() throws FileNotFoundException, IOException{
		Properties prop = new Properties();
		prop.load(new FileInputStream("DataBuild/conf/application.properties"));
		
		String buildOrder = prop.getProperty(BuilderProps.DGMASTER_MASTERBUILD_BUILDORDER);
		
        if  ( masterBuilder.getBuildOrder().compareTo(MasterBuilder.TIME_BASE_STAGES) == 0 ) {
        	Assert.assertEquals(buildOrder, masterBuilder.getBuildOrder());
        }
        else if ( masterBuilder.getBuildOrder().compareTo(MasterBuilder.STAGING_BASED_STAGES) == 0 ) {
        	Assert.assertEquals(buildOrder, masterBuilder.getBuildOrder());
		}
        else {
        	fail("Please specify the order to build.");
        }
	}

	@Test
	public void testStageorderBuild() {
		masterBuilder.stageorderBuild();
		assertCreatedFiles();
	}

	@Test
	public void testTimeorderBuild() {
		masterBuilder.timeorderBuild();
		assertCreatedFiles();
	}
	
	
	
	
	
	private void assertCreatedFiles() {
		// check if the files were created for all stages
		for(int stageNum = 1; stageNum <= numberOfStages; stageNum++){
			File folder = new File(baseFolder+"/staging/stage"+stageNum+"/output");
			boolean foundFile = false;
			int filesFound = 0;
			for (File childFile : folder.listFiles()) {
				foundFile = childFile.getName().endsWith("test_"+stageNum+".txt");
				if (foundFile) filesFound++;
			}
			if (filesFound != numberOfDays) {
				fail("Not all output files were created for stage "+stageNum);
			}
		}
	}

}
