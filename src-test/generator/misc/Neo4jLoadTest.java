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
import org.junit.Test;

public class Neo4jLoadTest {
	private List<SchemaObjectInterface> entityEventMap;
	private EntityEventNodeNeo4j headNode;

	@Before
	public void setUp() throws Exception {
		ApplicationContext.setBaseConfigurationFolder("C:/workspace/dgMaster-trunk/dist/DataBuild/conf");
	}

	@After
	public void tearDown() {
	}

	public void loadXmlconfig(String filePath) {
		SAXParserFactory factory = SAXParserFactory.newInstance();

		try {
			javax.xml.parsers.SAXParser saxParser;
			saxParser = factory.newSAXParser();
			SchemaSAXHandler saxHandler = new SchemaSAXHandler();
			saxParser.parse(new File(filePath), saxHandler);
			entityEventMap = saxHandler.getData(); // list is now loaded from
													// the config file
		} catch (Throwable err) {
			err.printStackTrace();
		}
	}

	@Test
	public void testSmallLoad() {
		loadXmlconfig("src-test/generator/misc/testConfigSmall.xml");
		loadTree();
	}

	@Test
	public void testSmallMediumLoad() {
		loadXmlconfig("src-test/generator/misc/testConfigSmallMedium.xml");
		loadTree();
	}

	@Test
	public void testMediumLoad() {
		loadXmlconfig("src-test/generator/misc/testConfigMedium.xml");
		loadTree();
	}

	// @Test
	public void testMediumBigLoad() {
		loadXmlconfig("src-test/generator/misc/testConfigMediumBig.xml");
		loadTree();
	}

	// @Test
	public void testBigLoad() {
		loadXmlconfig("src-test/generator/misc/testConfigBig.xml");
		loadTree();
	}

	private void loadTree() {
		// clean out the database if it exists
		try {
			EntityEventNodeNeo4j.initDb();
		} catch (IOException e1) {
			e1.printStackTrace();
			return;
		}

		// create the a bunch of node maps
		SchemaObjectInterface sdc = this.entityEventMap.get(0);
		try {
			headNode = new EntityEventNodeNeo4j(sdc, this.entityEventMap);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		headNode.getColumns().add("branchName");
		for (EntityEventNodeInterface stage : headNode.getChildren()) {
			stage.getColumns().add("customerName");
			stage.getColumns().add("gender");
			for (EntityEventNodeInterface stageAccount : stage.getChildren()) {
				stageAccount.getColumns().add("accountName");
				stageAccount.getColumns().add("Balance");
				for (EntityEventNodeInterface stageCard : stageAccount.getChildren()) {
					stageCard.getColumns().add("cardName");
				}
			}
		}

		System.out.println("\nStarting Tree Build");
		Timestamp time = new Timestamp(System.currentTimeMillis());
		headNode.getNode("customer").keyStageValue(1);// populate the tree based
														// on the keystage and
														// key values, this will
														// now fully populate
														// the tree
		Timestamp timeAfter = new Timestamp(System.currentTimeMillis());
		System.out.println("\nTotal Time taken in seconds : " + ((timeAfter.getTime() - time.getTime()) / 1000));
		printCount();

	}

	public void printCount() {
		Long count = headNode.getNode("branch").getStageInstancesCount();
		System.out.println("branch count = " + count);

		count = headNode.getNode("customer").getStageInstancesCount();
		System.out.println("customer count = " + count);

		count = headNode.getNode("employee").getStageInstancesCount();
		System.out.println("employee count = " + count);

		count = headNode.getNode("account").getStageInstancesCount();
		System.out.println("account count = " + count);

		count = headNode.getNode("card").getStageInstancesCount();
		System.out.println("card count = " + count);

		count = headNode.getNode("transaction").getStageInstancesCount();
		System.out.println("transaction count = " + count);
	}

}
