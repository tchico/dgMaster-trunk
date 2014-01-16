package generator.misc;

import generator.Schema.SchemaObjectInterface;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import javax.xml.parsers.SAXParserFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class HSQLDBLoadTest {
	private List<SchemaObjectInterface> entityEventMap;
	private EntityEventNodeHSQLDB headNode;
	
	@Before
	public void setUp() throws Exception {
		ApplicationContext.setDefinitionsConfigurationFolder("C:/workspace/dgMaster-trunk/dist/DataBuild/staging");
		
	}
	
	@After
	public void tearDown(){
		EntityEventNodeHSQLDB.shutdownConnection();
	}

	public void loadXmlconfig(String filePath){
		SAXParserFactory factory = SAXParserFactory.newInstance();  
        
        try
        {
            javax.xml.parsers.SAXParser saxParser;
            saxParser = factory.newSAXParser();
            SchemaSAXHandler saxHandler  = new SchemaSAXHandler();
            saxParser.parse( new File(filePath), saxHandler );
            entityEventMap = saxHandler.getData(); // list is now loaded from the config file
        }
        catch (Throwable err)
        {
            err.printStackTrace();
        }
	}
	
	@Test
	public void test1SmallLoad(){
		loadXmlconfig("src-test/generator/misc/testConfigSmall.xml");
		loadTree();
	}
	
	@Test
	public void test2SmallMediumLoad(){
		loadXmlconfig("src-test/generator/misc/testConfigSmallMedium.xml");
		loadTree();
	}
	
	@Test
	public void test3MediumLoad(){
		loadXmlconfig("src-test/generator/misc/testConfigMedium.xml");
		loadTree();
	}
	
	@Test
	public void test4MediumBigLoad(){
		loadXmlconfig("src-test/generator/misc/testConfigMediumBig.xml");
		loadTree();
	}
	
	@Test
	public void test5BigLoad(){
		loadXmlconfig("src-test/generator/misc/testConfigBig.xml");
		loadTree();
	}
	
	@Test
	public void test6BiggerLoad(){
		loadXmlconfig("src-test/generator/misc/testConfigBigger.xml");
		loadTree();
	}

	private void loadTree() {
		
		//create the a bunch of node maps
		SchemaObjectInterface sdc = this.entityEventMap.get(0);
		try {
			headNode = new EntityEventNodeHSQLDB(sdc, this.entityEventMap);
		} catch (IOException | SQLException e) {
			e.printStackTrace();
		} 

		headNode.getColumns().add("branchName");
		for(EntityEventNodeInterface stage: headNode.getChildren()){
			stage.getColumns().add("customerName");
			stage.getColumns().add("gender");
			for(EntityEventNodeInterface stageAccount: stage.getChildren()){
				stageAccount.getColumns().add("accountName");
				stageAccount.getColumns().add("Balance");
				for(EntityEventNodeInterface stageCard: stageAccount.getChildren()){
					stageCard.getColumns().add("cardName");
				}
			}
		}
		
		System.out.println("\nStarting Tree Build");
		Timestamp time = new Timestamp(System.currentTimeMillis());
		headNode.getNode("customer").keyStageValue(1);// populate the tree based on the keystage and key values, this will now fully populate the tree
		Timestamp timeAfter = new Timestamp(System.currentTimeMillis());
		System.out.println("\nTotal Time taken in seconds : " + ((timeAfter.getTime() - time.getTime())/1000));
		printCount();
	}
	
	public void printCount() {
		Long count = headNode.getNode("branch").getStageInstancesCount();
		System.out.println("branch count = "+count);
		
		count = headNode.getNode("customer").getStageInstancesCount();
		System.out.println("customer count = "+count);
		
		count = headNode.getNode("employee").getStageInstancesCount();
		System.out.println("employee count = "+count);
		
		count = headNode.getNode("account").getStageInstancesCount();
		System.out.println("account count = "+count);
		
		count = headNode.getNode("card").getStageInstancesCount();
		System.out.println("card count = "+count);

		count = headNode.getNode("transaction").getStageInstancesCount();
		System.out.println("transaction count = "+count);
	}

}
