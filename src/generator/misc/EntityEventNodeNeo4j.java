package generator.misc;

import generator.Schema.SchemaObjectInterface;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.helpers.collection.MapUtil;
import org.neo4j.kernel.impl.util.FileUtils;
import org.neo4j.unsafe.batchinsert.BatchInserter;
import org.neo4j.unsafe.batchinsert.BatchInserters;

/**
 * @author joaoesteves The EntityEventNode holds all of the information relating
 *         to a single stage in the generation. This implementation is based on
 *         OrientDB and intents to minimize the memory consumption by storing
 *         the node instances on the disk.
 */
public class EntityEventNodeNeo4j extends AbstractEntityEventNode implements Runnable {
	private static enum RelTypes implements RelationshipType {
		IS_PARENT
	}

	private static final String _ID_PROPERTY = "_id";
	private static final String EMPTY_STRING = "";
	private static GraphDatabaseService graph;
	private static String dbURL;
	private static BatchInserter currentInserter;
	private static boolean batchMode = true;
	
	static {
		// TODO: transfer the url into properties
		EntityEventNodeNeo4j.dbURL = "/tmp/dgmaster3";
	}

	/**
	 * holds the number of vertexes created so far
	 */
	private int numVertexesCreated = 0;
	private Node parentVertex;
	

	public EntityEventNodeNeo4j(SchemaObjectInterface s) throws FileNotFoundException, IOException {
		schemaObject = s;
		type = s.getType();
		name = s.getName();
		initDb();
	}

	// trees are structured in relation to their parent, so customers parent is
	// branch and its build ratio is 1000, then for every one f th parent make
	// 1000 of child top node only
	public EntityEventNodeNeo4j(SchemaObjectInterface s, List<SchemaObjectInterface> list) throws FileNotFoundException, IOException, NumberFormatException, SQLException {
		this(s);
		buildRatioQty = 1;
		children = new ArrayList<EntityEventNodeInterface>();

		buildChildEntityEventNodes(s, list);
	}

	public EntityEventNodeNeo4j(SchemaObjectInterface s, double b, EntityEventNodeNeo4j parent, List<SchemaObjectInterface> list) throws FileNotFoundException, IOException, NumberFormatException, SQLException {
		this(s);
		buildRatioQty = b;
		children = new ArrayList<EntityEventNodeInterface>();
		this.parent = parent;

		buildChildEntityEventNodes(s, list);
	}

	/**
	 * builds the stages according do the total number of instances to be
	 * present in the keyValue Stage.
	 */
	public void keyStageValue(double totalNum) {
		boolean threaded = false;
		if (this.parent != null) { // then this is not the top
			this.getParent().keyStageValue(totalNum / this.buildRatioQty);
		} else { // at the top
			System.out.println("\nBuilding the top node...");
			for (double i = 0; i < totalNum; i++) {
				
				if(batchMode){
					try {
						long vertex = createVertexBatchMode(numVertexesCreated++, this.name, columns, null);
						for (EntityEventNodeInterface child : this.children) {
							((EntityEventNodeNeo4j) child).buildBatchMode(vertex);
						}
						shutdownCurrentInserter();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}else{
					try (Transaction tx = graph.beginTx()) {
						Node vertex = createVertex(numVertexesCreated++, this.name, columns, null);
						if (threaded) {
							ExecutorService executor = Executors.newFixedThreadPool(20);
							for (EntityEventNodeInterface child : this.children) {
								((EntityEventNodeNeo4j) child).setParentVertex(vertex);
								executor.submit((EntityEventNodeNeo4j) child);
							}
							shutDownExecutor(executor);
						} else {
							for (EntityEventNodeInterface child : this.children) {
	
								((EntityEventNodeNeo4j) child).build(vertex);
							}
						}
	
						tx.success();
					}
				}
			}
		}
	}

	public synchronized void shutDownExecutor(ExecutorService executor) {
		executor.shutdown();
		try {
			executor.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * builds the stage under the parentVertex
	 * 
	 * @param createdVertex2
	 */
	public void build(Node createdVertex2) {
		// total amount of values we need to create.
		double total = this.buildRatioQty + leftOver;
		leftOver = total % 1;
		for (double i = 1; i <= total; i++) {
			Node createdVertex = createVertex(numVertexesCreated++, this.name, columns, null);

			// build the vertex children
			for (EntityEventNodeInterface child : this.children) {
				((EntityEventNodeNeo4j) child).build(createdVertex);
			}

			// create and edge from this vertex to its parent
			createEdgeToParent(createdVertex, createdVertex2);
		}
		if (numVertexesCreated % 10000 == 0) {
			System.out.println("\nBuilt " + numVertexesCreated + " " + this.name);
		}
	}

	public void buildBatchMode(long createdVertex2) {
		// total amount of values we need to create.
		double total = this.buildRatioQty + leftOver;
		leftOver = total % 1;
		for (double i = 1; i <= total; i++) {
			try {
				long createdVertex = createVertexBatchMode(numVertexesCreated++, this.name, columns, null);
				
				if (createdVertex % 50000 == 0) {
					System.out.println("\n>>> [Commit] Built " + createdVertex + " nodes ");
					shutdownCurrentInserter();
				}
				
				// build the vertex children
				for (EntityEventNodeInterface child : this.children) {
					((EntityEventNodeNeo4j) child).buildBatchMode(createdVertex);
				}

				// create and edge from this vertex to its parent
				createEdgeToParentBatchMode(createdVertex, createdVertex2);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (numVertexesCreated % 10000 == 0) {
			System.out.println("\nBuilt " + numVertexesCreated + " " + this.name);
		}
	}

	public String getColumnValue(int id, String columnName, String targetName) {
		if (this.name.equalsIgnoreCase(targetName)) {
			if (columns.indexOf(columnName) != -1) {
				String val = getEntityPropertyValue(id, columnName);
				if (val.contains(SEPARATOR_MULTIFIELD)) { // multiple options
					String[] val2 = val.split(SEPARATOR_MULTIFIELD_REGEX);
					Random rand = new Random();
					if (!isRandomReturnOnMultiFieldColumns()) {
						return val2[rand.nextInt(val2.length)]; // now it should
																// return a
																// random value
																// from the
																// array
					}

					// takes the first one and puts it on the end of the string,
					// and returns it, natural distribution in ordering
					// this approach also allows for control of percentages, if
					// you want 50 , 25, 25 enter the first value twice,
					// followed by the other two individuals.
					String replacement = EMPTY_STRING;
					boolean first = true;
					String returnValue = EMPTY_STRING;

					for (String s : val2) {
						if (first) {
							returnValue = s;
							first = false;
						} else {
							replacement += s + SEPARATOR_MULTIFIELD;
						}
					}
					replacement += returnValue; // now the first value is at the
												// end of the list
					updatePropertyValue(id, columnName, replacement); // update
																		// the
																		// stage
																		// instance
																		// with
																		// the
																		// new
																		// column
																		// value
					return returnValue; // now pass back what was the first
										// entry
				} else
					return val; // just a standard column, return the value
			} else {
				System.out.println("Error: cannot find the Column " + name + " , pls note names must match exactly");
				return EMPTY_STRING;// could not find the column specified
			}
		} else {
			return this.parent.getColumnValue(id, columnName, targetName);
		}
	}

	public static synchronized GraphDatabaseService getGraph() {
		return graph;
	}

	public static synchronized void setGraph(GraphDatabaseService graphDb) {
		EntityEventNodeNeo4j.graph = graphDb;
	}

	public static BatchInserter getCurrentInserter() throws IOException {
		if (currentInserter == null) {
			currentInserter = getNewInserter();
		}
		return currentInserter;
	}

	public static void shutdownCurrentInserter() {
		if (currentInserter != null) {
			currentInserter.shutdown();
			currentInserter = null;
		}
	}

	public static BatchInserter getNewInserter() throws IOException {
		try (FileInputStream input = new FileInputStream(new File(ApplicationContext.getBaseConfigurationFolder() + "/neo4j.properties"))) {
			Map<String, String> config = MapUtil.load(input);
			return BatchInserters.inserter(dbURL, config);
		}
	}

	public static synchronized void initDb() throws FileNotFoundException, IOException {
		if (getGraph() == null) {
			GraphDatabaseService graphDb = new GraphDatabaseFactory().newEmbeddedDatabaseBuilder(dbURL).loadPropertiesFromFile(ApplicationContext.getBaseConfigurationFolder() + "/neo4j.properties").newGraphDatabase();
			registerShutdownHook(graphDb);
			if(batchMode){
				graphDb.shutdown();
			}
			setGraph(graphDb);
		}
	}

	public static void clearDb() {
		try {
			FileUtils.deleteRecursively(new File(dbURL));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static void registerShutdownHook(final GraphDatabaseService graphDb) {
		// Registers a shutdown hook for the Neo4j instance so that it
		// shuts down nicely when the VM exits (even if you "Ctrl-C" the
		// running application).
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				graphDb.shutdown();
			}
		});
	}

	/**
	 * Creates an Edge to the parent vertex
	 * 
	 * @param createdVertex
	 * @param parentVertex
	 * @return
	 */
	private Relationship createEdgeToParent(Node createdVertex, Node parentVertex) {
		Relationship rel = parentVertex.createRelationshipTo(createdVertex, RelTypes.IS_PARENT);
		return rel;
	}

	/**
	 * Creates an Edge to the parent vertex
	 * 
	 * @param createdVertex
	 * @param parentVertex
	 * @return
	 * @throws IOException
	 */
	private long createEdgeToParentBatchMode(long createdVertex, long parentVertex) throws IOException {
		long rel1 = getCurrentInserter().createRelationship(createdVertex, parentVertex, RelTypes.IS_PARENT, null);
		return rel1;
	}

	/**
	 * Creates a Vertex base on the stage instance object.
	 * 
	 * @param schemaObject
	 * @param nodeInstance
	 * @param columnNames
	 * @return
	 */
	private Node createVertex(double id, String name, List<String> columnNames, List<String> values) {
		Node newVertex;

		newVertex = graph.createNode();
		newVertex.setProperty(_ID_PROPERTY, id);
		// fill out the properties in the columns
		for (int i = 0; i < columnNames.size(); i++) {
			String value = EMPTY_STRING;
			if (values != null && values.size() > i) {
				value = values.get(i);
			}
			newVertex.setProperty(columnNames.get(i), value);
		}

		return newVertex;
	}

	/**
	 * used when creating nodes in batch mode
	 * 
	 * @param id
	 * @param name
	 * @param columnNames
	 * @param values
	 * @return
	 * @throws IOException
	 */
	private long createVertexBatchMode(double id, String name, List<String> columnNames, List<String> values) throws IOException {
		Label entityLabel = DynamicLabel.label(name);
		//getCurrentInserter().createDeferredSchemaIndex(entityLabel).on(_ID_PROPERTY).create();

		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(_ID_PROPERTY, id);

		// fill out the properties in the columns
		for (int i = 0; i < columnNames.size(); i++) {
			String value = EMPTY_STRING;
			if (values != null && values.size() > i) {
				value = values.get(i);
			}
			properties.put(columnNames.get(i), value);
		}

		return getCurrentInserter().createNode(properties, entityLabel);
	}

	public Long getStageInstancesCount() {
		return new Long(numVertexesCreated);
	}

	@SuppressWarnings("unchecked")
	public StageInstanceInterface getStageInstance(int rowRefId) {

		StageInstance nodeInstance = new StageInstance(name, -1);
		for (String propertyKey : columns) {
			// ordering is maintained by the columns array
			// nodeInstance.addValue((String)v.getProperty(propertyKey));
		}

		// TODO: set the parentRefId

		return nodeInstance;

	}

	public void updatePropertyValue(int id, String columnName, String replacement) {
		putValueToStageInstance(id, columnName, replacement);
	}

	@SuppressWarnings("unchecked")
	public String getEntityPropertyValue(int id, String columnName) {
		// TODO:
		return null;
	}

	public int putValueToStageInstance(int id, String columnName, String value) {
		// TODO:
		return 1;
	}

	public int cleanValuesOfInstance(int id) {
		StringBuilder sb = new StringBuilder();
		sb.append("update ");
		sb.append(name);
		sb.append(" set ");
		for (String fieldName : columns) {
			sb.append(fieldName);
			sb.append(" = '");
			sb.append("null");
			sb.append("'");
		}
		sb.append("where _id = ");
		sb.append(id);
		return -1;
		// TODO:
		// return graph.command(new OCommandSQL(sb.toString())).execute();
	}

	@Override
	protected EntityEventNodeInterface getNewInstanceOfNodeClass(SchemaObjectInterface childFromList, double value, AbstractEntityEventNode abstractEntityEventNode, List<SchemaObjectInterface> list)
			throws FileNotFoundException, IOException, NumberFormatException, SQLException {
		return new EntityEventNodeNeo4j(childFromList, value, this, list);
	}

	public void run() {
		build(parentVertex);
	}

	public synchronized void setParentVertex(Node vertex) {
		this.parentVertex = vertex;
	}

}
