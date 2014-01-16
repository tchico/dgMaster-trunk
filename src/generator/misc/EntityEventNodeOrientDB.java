package generator.misc;

import generator.Schema.SchemaObjectInterface;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.orientechnologies.orient.core.config.OGlobalConfiguration;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientDynaElementIterable;
import com.tinkerpop.blueprints.impls.orient.OrientGraphNoTx;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;

/**
 * @author joaoesteves 
 * The EntityEventNode holds all of the information relating
 * to a single stage in the generation. This implementation is based on
 * OrientDB and intents to minimize the memory consumption by storing
 * the node instances on the disk.
 */
public class EntityEventNodeOrientDB extends AbstractEntityEventNode implements Runnable{

	private static final String IS_PARENT = "isParent";
	private static final String _ID_PROPERTY = "_id";
	private static final String EMPTY_STRING = "";
	private static OrientGraphNoTx  graph;
	private static String dbURL;

	static {
		// TODO: transfer the url into properties
		EntityEventNodeOrientDB.dbURL = "plocal:/dev/OrientDb/databases/GratefulDeadConcerts";
	}
	
	/**
	 * holds the number of vertexes created so far
	 */
	private int numVertexesCreated = 0;
	private Vertex parentVertex;

	public EntityEventNodeOrientDB(SchemaObjectInterface s) {
		schemaObject = s;
		type = s.getType();
		name = s.getName();
		initDb();
		createVertexType();
	}

	// trees are structured in relation to their parent, so customers parent is
	// branch and its build ratio is 1000, then for every one f th parent make
	// 1000 of child
	// top node only
	public EntityEventNodeOrientDB(SchemaObjectInterface s, List<SchemaObjectInterface> list) {
		this(s);
		buildRatioQty = 1;
		children = new ArrayList<EntityEventNodeInterface>();

		try {
			buildChildEntityEventNodes(s, list);
		} catch (NumberFormatException | IOException | SQLException e) {
			// Not likely to happen for this class implementation
			e.printStackTrace();
		}
	}

	public EntityEventNodeOrientDB(SchemaObjectInterface s, double b, EntityEventNodeOrientDB parent,
			List<SchemaObjectInterface> list) {
		this(s);
		buildRatioQty = b;
		children = new ArrayList<EntityEventNodeInterface>();
		this.parent = parent;

		try {
			buildChildEntityEventNodes(s, list);
		} catch (NumberFormatException | IOException | SQLException e) {
			// Not likely to happen for this class implementation
			e.printStackTrace();
		}
	}

	/**
	 * builds the stages according do the total number of instances to be
	 * present in the keyValue Stage.
	 */
	public void keyStageValue(double totalNum) {
		if (this.parent != null) { // then this is not the top
			this.getParent().keyStageValue(totalNum / this.buildRatioQty);
		} else { // at the top
			System.out.println("\nBuilding the top node...");
			for (double i = 0; i < totalNum; i++) {
				Vertex vertex = createVertex(numVertexesCreated ++, this.name, columns, null);
				ExecutorService executor = Executors.newFixedThreadPool(20);
				for (EntityEventNodeInterface child : this.children) {
					((EntityEventNodeOrientDB) child).setParentVertex(vertex);
		            executor.submit((EntityEventNodeOrientDB) child);
				}
				shutDownExecutor(executor);
			}
		}
	}
	
	public synchronized void shutDownExecutor(ExecutorService executor){
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
	 * @param parentVertex
	 */
	public void build(Vertex parentVertex) {
		// total amount of values we need to create.
		double total = this.buildRatioQty + leftOver;
		leftOver = total % 1;
		for (double i = 1; i <= total; i++) {
			Vertex createdVertex = createVertex(numVertexesCreated++, this.name, columns, null);

			//build the vertex children
			for (EntityEventNodeInterface child : this.children) {
				((EntityEventNodeOrientDB) child).build(createdVertex);
//				((EntityEventNodeOrientDB) child).setParentVertex(parentVertex);
//	            executor.submit((EntityEventNodeOrientDB) child);
			}
			
			//create and edge from this vertex to its parent
			createEdgeToParent(createdVertex, parentVertex);
		}
		if(numVertexesCreated % 10000 == 0){
			System.out.println("\nBuilt "+numVertexesCreated+" "+this.name);
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
						return val2[rand.nextInt(val2.length)]; // now it should return a random value from the array
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
					replacement += returnValue; // now the first value is at the end of the list
					updatePropertyValue(id, columnName, replacement); // update the stage instance with the new column value
					return returnValue; // now pass back what was the first entry
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

	public static synchronized OrientGraphNoTx  getGraph() {
		return graph;
	}

	public static synchronized void setGraph(OrientGraphNoTx  graph) {
		EntityEventNodeOrientDB.graph = graph;
	}

	public static synchronized void initDb() {
		if (getGraph() == null) {
			OGlobalConfiguration.CACHE_LEVEL2_ENABLED.setValue(false);
			OGlobalConfiguration.ENVIRONMENT_CONCURRENT.setValue( true );
			setGraph(new OrientGraphNoTx(dbURL));
		}
	}

	/**
	 * Create the vertex type in the database.
	 * 
	 * @param schemaObject
	 *            OClass
	 * @return
	 */
	protected OClass createVertexType() {
		OClass savedType = graph.getVertexType(this.schemaObject.getName());
		if (savedType == null) {
			return graph.createVertexType(this.schemaObject.getName());
		}
		return savedType;
	}

	/**
	 * Creates an Edge to the parent vertex
	 * 
	 * @param createdVertex
	 * @param parentVertex
	 * @return
	 */
	private Edge createEdgeToParent(Vertex createdVertex, Vertex parentVertex) {
		Edge parentEdge = graph.addEdge(null, parentVertex, createdVertex, IS_PARENT);
		return parentEdge;
	}

	/**
	 * Creates a Vertex base on the stage instance object.
	 * 
	 * @param schemaObject
	 * @param nodeInstance
	 * @param columnNames
	 * @return
	 */
	private Vertex createVertex(double id, String name, List<String> columnNames, List<String> values) {
		Vertex newVertex = graph.addVertex(id);
		newVertex.setProperty(_ID_PROPERTY, id);

		//fill out the properties in the columns
		for (int i = 0; i < columnNames.size(); i++) {
			String value = EMPTY_STRING;
			if (values != null && values.size() > i) {
				value = values.get(i);
			}
			newVertex.setProperty(columnNames.get(i), value);
		}

		return newVertex;
	}

	public Long getStageInstancesCount() {
		OrientDynaElementIterable result = getGraph().command(new OCommandSQL("SELECT COUNT(*) as count from "+name)).execute();
		Iterator<Object> iter = result.iterator();
		while(iter.hasNext()){
			OrientVertex res = (OrientVertex) iter.next();
			Long count = res.getProperty("count");
			return count == null ? 0L : count;
		}
		return 0L;
	}

	@SuppressWarnings("unchecked")
	public StageInstanceInterface getStageInstance(int rowRefId) {
		for (Vertex v : (Iterable<Vertex>) graph.command(new OCommandSQL("select * from "+name+" where _id = "+rowRefId)).execute()) {
			StageInstance nodeInstance = new StageInstance(name, -1);
			for(String propertyKey: columns){
				//ordering is maintained by the columns array
				nodeInstance.addValue((String)v.getProperty(propertyKey));
			}
			
			for(Edge e: v.getEdges(Direction.IN, IS_PARENT)){
				Vertex parent = e.getVertex(Direction.IN);
				nodeInstance.setParentRefId(((Double)parent.getProperty(_ID_PROPERTY)).intValue());
			}
			
			return nodeInstance;
		}
		return null;
	}
	
	public void updatePropertyValue(int id, String columnName, String replacement) {
		putValueToStageInstance(id, columnName, replacement);
	}

	@SuppressWarnings("unchecked")
	public String getEntityPropertyValue(int id, String columnName) {
		for (Vertex v : (Iterable<Vertex>) graph.command(new OCommandSQL("select * from "+name+" where _id = "+id)).execute()) {
			return v.getProperty(columnName);
		}
		return null;
	}


	public int putValueToStageInstance(int id, String columnName, String value) {
		return graph.command(new OCommandSQL("update "+name+" set "+columnName+" = '"+value+"' where _id = "+id)).execute();
	}
	

	public int cleanValuesOfInstance(int id) {
		StringBuilder sb = new StringBuilder();
		sb.append("update ");
		sb.append(name);
		sb.append(" set ");
		for(String fieldName: columns){
			sb.append(fieldName);
			sb.append(" = '");
			sb.append("null");
			sb.append("'");
		}
		sb.append("where _id = ");
		sb.append(id);
		return graph.command(new OCommandSQL(sb.toString())).execute();
	}
	
	@Override
	protected EntityEventNodeInterface getNewInstanceOfNodeClass(SchemaObjectInterface childFromList, double value, AbstractEntityEventNode abstractEntityEventNode,
			List<SchemaObjectInterface> list) {
		return new EntityEventNodeOrientDB(childFromList,value,this,list);
	}

	public void run() {
		build(parentVertex);
	}

	public synchronized Vertex getParentVertex() {
		return parentVertex;
	}

	public synchronized void setParentVertex(Vertex parentVertex) {
		this.parentVertex = parentVertex;
	}



}
