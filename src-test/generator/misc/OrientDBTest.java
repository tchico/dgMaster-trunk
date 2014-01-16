package generator.misc;

import generator.Schema.SchemaObjectInterface;

import java.io.File;
import java.sql.Timestamp;
import java.util.List;

import javax.xml.parsers.SAXParserFactory;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.orientechnologies.orient.server.OServer;
import com.orientechnologies.orient.server.OServerMain;

public class OrientDBTest {
	private List<SchemaObjectInterface> entityEventMap;
	private EntityEventNodeInterface headNode;
	private static OServer server;
	private static boolean started = false;
	
	@Before
	public void setUp() throws Exception {
		if(!started){
			server = OServerMain.create();
			server.startup(OrientDBTest.class.getResourceAsStream("orientdb-server-config.xml"));
			server.activate();
			
	
			loadXmlconfig("src-test/generator/misc/testConfig.xml");
			
			//clean out the database if it exists
			EntityEventNodeOrientDB.initDb();
			EntityEventNodeOrientDB.getGraph().drop();
			EntityEventNodeOrientDB.setGraph(null);
			
			//create the a bunch of node map
			SchemaObjectInterface sdc = this.entityEventMap.get(0);
			headNode = new EntityEventNodeOrientDB(sdc, this.entityEventMap); // new tree map of nodes representing the stages creates itself using the entityEventMap
			
			headNode.getColumns().add("branchName");
			for(EntityEventNodeInterface stage: headNode.getChildren()){
				stage.getColumns().add("customerName");
				stage.getColumns().add("gender");
				for(EntityEventNodeInterface stageAccount: stage.getChildren()){
					stageAccount.getColumns().add("accountName");
					stageAccount.getColumns().add("Balance");
				}
			}
			
			
			Timestamp time = new Timestamp(System.currentTimeMillis());
			headNode.getNode("customer").keyStageValue(1);// populate the tree based on the keystage and key values, this will now fully populate the tree
			Timestamp timeAfter = new Timestamp(System.currentTimeMillis());
			System.out.println("\nTotal Time taken in seconds : " + ((timeAfter.getTime() - time.getTime())/1000));
			started = true;
		}
	}
	
	@After
	public void tearDown(){
		if(!started){
			EntityEventNodeOrientDB.setGraph(null);
			server.shutdown();
		}
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
	public void testCount() {
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
		
		Assert.assertNotNull(count);
		Assert.assertEquals(count, new Long(1000));
	}
	
	@Test
	public void testUpdateValue(){
		String name = "Joao";
		headNode.getNode("customer").putValueToStageInstance(2, "customerName", name);
		String storedName = headNode.getNode("customer").getColumnValue(2, "customerName", "customer");
		Assert.assertEquals(name, storedName);
	}
	
	@Test
	public void tearDownServer(){
		started = false;
	}

}
