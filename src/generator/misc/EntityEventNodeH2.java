package generator.misc;

import generator.Schema.SchemaObjectInterface;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.h2.jdbcx.JdbcDataSource;

/**
 * @author joaoesteves The EntityEventNode holds all of the information relating
 *         to a single stage in the generation. This implementation is based on
 *         OrientDB and intents to minimise the memory consumption by storing
 *         the node instances on the disk.
 */
public class EntityEventNodeH2 extends AbstractEntityEventNode {
	private static final int COMMIT_THRESHOLD = 200000;
	private static final String NODE = "NODE";
	private static final String RELATIONSHIP = "RELATIONSHIP";
	private static final String NODE_PROPERTIES = "NODE_PROPERTIES";
	private static final String _ID_PROPERTY = "id";
	private static final String EMPTY_STRING = "";
	private static String dbURL;
	private static Connection connection;

	/**
	 * holds the number of vertexes created so far
	 */
	private static long numVertexesCreated = 0;
	private static boolean nodesCreatedSinceLastCommit = false;

	/*
	 * Statements
	 */
	private static PreparedStatement saveNodeStatement;
	private static PreparedStatement createRelStatement;
	private static PreparedStatement createNodePropsStatement;
	private static PreparedStatement updateNodePropsStatement;
	private static PreparedStatement selectNodePropsStatement;
	private static PreparedStatement selectNodeAllPropsStatement;
	private static PreparedStatement selectRelationshipStatement;
	private PreparedStatement countNodesStatement;

	static {
		// TODO: transfer the url into properties
		EntityEventNodeH2.dbURL = "jdbc:h2:c://tmp//db-dgMaster";
	}

	/**
	 * Entity representation
	 * 
	 * @author joaoesteves
	 * 
	 */
	public static class Node {
		private long id;

		public Node(long id) {
			super();
			this.id = id;
		}

		public boolean createRelationshipTo(Node createdVertex, RelTypes isParent) {
			return createRelationship(id, createdVertex.getId());
		}

		public static boolean createRelationship(long parentId, long childId) {
			try {
				createRelStatement.setLong(1, parentId);
				createRelStatement.setLong(2, childId);
				createRelStatement.addBatch();
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
			return true;
		}

		public boolean setProperty(String name, String value, boolean updateOnly) {
			try {
				PreparedStatement preparedStatement;
				if (updateOnly) {
					preparedStatement = updateNodePropsStatement;
				} else {
					preparedStatement = createNodePropsStatement;
				}
				if (updateOnly) {
					preparedStatement.setString(1, value);
					preparedStatement.setLong(2, id);
					preparedStatement.setString(3, name);
				} else {
					preparedStatement.setLong(1, id);
					preparedStatement.setString(2, name);
					preparedStatement.setString(3, value);
				}
				preparedStatement.addBatch();
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
			return true;
		}

		public boolean setProperties(Map<String, String> properties, boolean updateOnly) throws SQLException {

			PreparedStatement preparedStatement;
			if (updateOnly) {
				preparedStatement = updateNodePropsStatement;
			} else {
				preparedStatement = createNodePropsStatement;
			}
			for (String propName : properties.keySet()) {
				if (updateOnly) {
					preparedStatement.setString(1, properties.get(propName));
					preparedStatement.setLong(2, id);
					preparedStatement.setString(3, propName);
				} else {
					preparedStatement.setLong(1, id);
					preparedStatement.setString(2, propName);
					preparedStatement.setString(3, properties.get(propName));
				}
				preparedStatement.addBatch();
			}

			return true;
		}

		public boolean save(String nodeName) {
			try {
				saveNodeStatement.setLong(1, id);
				saveNodeStatement.setString(2, nodeName);
				saveNodeStatement.addBatch();
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
			return true;
		}

		public long getId() {
			return id;
		}

		public void setId(long id) {
			this.id = id;
		}

	}

	public EntityEventNodeH2(SchemaObjectInterface s) throws FileNotFoundException, IOException, SQLException {
		schemaObject = s;
		type = s.getType();
		name = s.getName();
		initDb();
		saveNodeStatement = connection.prepareStatement("insert into " + NODE + " values (?,?)");
		createRelStatement = connection.prepareStatement("insert into " + RELATIONSHIP + " values (?,?)");
		createNodePropsStatement = connection.prepareStatement("insert into " + NODE_PROPERTIES + " values( ?, ?, ?)");
		updateNodePropsStatement = connection.prepareStatement("update " + NODE_PROPERTIES + " set property_value = ? where node_id = ? and property_name = ?");
		countNodesStatement = connection.prepareStatement("select count(*) from "+NODE+" where name = '" + this.name + "'");
		selectNodePropsStatement = connection.prepareStatement("select property_value from "+NODE_PROPERTIES+" where node_id = ? and property_name = ?");
		selectNodeAllPropsStatement = connection.prepareStatement("select property_name, property_value from "+NODE_PROPERTIES+" where node_id = ?");
		selectRelationshipStatement = connection.prepareStatement("select node_in_id from "+RELATIONSHIP+" where node_out_id = ?");
	}

	// trees are structured in relation to their parent, so customers parent is
	// branch and its build ratio is 1000, then for every one f th parent make
	// 1000 of child top node only
	public EntityEventNodeH2(SchemaObjectInterface s, List<SchemaObjectInterface> list) throws FileNotFoundException, IOException, SQLException {
		this(s);
		buildRatioQty = 1;
		children = new ArrayList<EntityEventNodeInterface>();

		buildChildEntityEventNodes(s, list);
	}

	public EntityEventNodeH2(SchemaObjectInterface s, double b, EntityEventNodeH2 parent, List<SchemaObjectInterface> list) throws FileNotFoundException, IOException, SQLException {
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
		if (this.parent != null) { // then this is not the top
			this.getParent().keyStageValue(totalNum / this.buildRatioQty);
		} else { // at the top
			numVertexesCreated = 0;

			System.out.println("\nBuilding the top node...");
			try {
				connection.setAutoCommit(false);
				for (double i = 0; i < totalNum; i++) {
					try {
						Node vertex = createVertex(incNumVertexesCreated(), this.name, columns, null);
						for (EntityEventNodeInterface child : this.children) {
							((EntityEventNodeH2) child).build(vertex.getId());
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}

				}
				commitBatches();
				connection.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private static synchronized long getNumVertexesCreated() {
		synchronized (EntityEventNodeH2.class) {
			return numVertexesCreated;
		}
	}

	private static synchronized long incNumVertexesCreated() {
		synchronized (EntityEventNodeH2.class) {
			nodesCreatedSinceLastCommit = true;
			return numVertexesCreated++;
		}
	}

	/**
	 * builds the stage under the parentVertex
	 * 
	 * @param createdVertex2
	 */
	public void build(Long parentVertexId) {
		// total amount of values we need to create.
		double total = this.buildRatioQty + leftOver;
		leftOver = total % 1;
		for (double i = 1; i <= total; i++) {
			try {
				Long thisVertexId = (long) incNumVertexesCreated();
				/* Node createdVertex = */createVertex(thisVertexId, this.name, columns, null);

				// build the vertex children
				for (EntityEventNodeInterface child : this.children) {
					((EntityEventNodeH2) child).build(parentVertexId);
				}

				// create and edge from this vertex to its parent
				createEdgeToParent(thisVertexId, parentVertexId);

				if (nodesCreatedSinceLastCommit && getNumVertexesCreated() % COMMIT_THRESHOLD == 0) {
					try {
						commitBatches();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
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

	public static synchronized void initDb() throws FileNotFoundException, IOException, SQLException {
		// 'Server' is a class of HSQLDB representing
		// the database server
		if (connection != null)
			return;
		numVertexesCreated = 0;

		File folder = new File(ApplicationContext.getDataBuildBaseFolder() + "/db/");
		folder.delete();

		connection = createConnection();
		try {
			connection.prepareStatement("drop table " + NODE + ";").execute();
			connection.prepareStatement("drop table " + RELATIONSHIP + ";").execute();
			connection.prepareStatement("drop table " + NODE_PROPERTIES + ";").execute();
		} catch (Exception e) {
			// do nothing, tables dont exist
		}
		connection.prepareStatement("create table node ( id INTEGER, name VARCHAR(16) );").execute();
		connection.prepareStatement("create table relationship ( node_in_id INTEGER, node_out_id INTEGER);").execute();
		connection.prepareStatement("create table node_properties ( node_id INTEGER, property_name VARCHAR(32), property_value VARCHAR(128));").execute();

	}

	public static void shutdownConnection() {
		// Closing the connection
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Creates an Edge to the parent vertex
	 * 
	 * @param createdVertex
	 * @param parentVertex
	 * @return
	 */
	private boolean createEdgeToParent(Long createdVertex, Long parentVertex) {
		return Node.createRelationship(parentVertex, createdVertex);
	}

	/**
	 * Creates a Vertex base on the stage instance object.
	 * 
	 * @param schemaObject
	 * @param nodeInstance
	 * @param columnNames
	 * @return
	 * @throws SQLException
	 */
	private Node createVertex(double id, String name, List<String> columnNames, List<String> values) throws SQLException {
		Node newVertex = new Node((long) id);

		Map<String, String> properties = new HashMap<String, String>();
		properties.put(_ID_PROPERTY, "" + id);

		// fill out the properties in the columns
		for (int i = 0; i < columnNames.size(); i++) {
			String value = EMPTY_STRING;
			if (values != null && values.size() > i) {
				value = values.get(i);
			}
			properties.put(columnNames.get(i), value);
		}

		newVertex.setProperties(properties, false);

		newVertex.save(name);
		return newVertex;
	}

	public Long getStageInstancesCount() {
		try {
			ResultSet rs = countNodesStatement.executeQuery();

			// get the number of rows from the result set
			rs.next();
			return rs.getLong(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0L;
	}

	private void commitBatches() {
		try {
			System.out.println("\n[COMMIT]  Built " + getNumVertexesCreated() + " vertexes");

			EntityEventNodeH2.saveNodeStatement.executeBatch();
			EntityEventNodeH2.createNodePropsStatement.executeBatch();
			EntityEventNodeH2.createRelStatement.executeBatch();

			// EntityEventNodeHSQLDB.updateNodePropsStatement.executeBatch();
			nodesCreatedSinceLastCommit = false;
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static Connection createConnection() {
		JdbcDataSource ds = new JdbcDataSource();
		ds.setURL(dbURL);
		ds.setUser("sa");
		ds.setPassword("sa");
		try {
			return ds.getConnection();
		} catch (Exception e) {
			System.err.println("Caught IOException: " + e.getMessage());
		} finally {
		}
		return null;
	}

	public static void runStatement(String sqlstmt) {
		System.out.println(sqlstmt);

		Statement stmt;
		try {
			stmt = connection.createStatement();
			stmt.executeUpdate(sqlstmt);
			stmt.close();

		} catch (SQLException ex) {
			System.err.println("SQLException: " + ex.getMessage());
		}
	}

	public static void doQuery(String sqlstmt) {

		try {
			Statement select = connection.createStatement();
			ResultSet result = select.executeQuery(sqlstmt);
			ResultSetMetaData resultMetaData = result.getMetaData();
			int numberOfColumns = resultMetaData.getColumnCount();
			int rownum = 0;

			System.out.println(sqlstmt);
			System.out.println("Got results:");
			while (result.next()) { // process results one row at a time
				rownum++;
				System.out.print(" Row " + rownum + " | ");
				for (int i = 1; i <= numberOfColumns; i++) {
					System.out.print(resultMetaData.getColumnName(i) + " : " + result.getString(i));
					if (i < numberOfColumns) {
						System.out.print(", ");
					}
				}
				System.out.println("");

			}
		} catch (Exception e) {
			System.err.println("SQLException: " + e.getMessage());
		}

	}


	public StageInstanceInterface getStageInstance(int rowRefId) {
		Map<String, String> value = new HashMap<String, String>();
		try {
			selectNodeAllPropsStatement.setLong(1, rowRefId);
			ResultSet result = selectNodePropsStatement.executeQuery();
			while (result.next()) { // process results one row at a time
				value.put(result.getString(1), result.getString(2));
			}
			
			StageInstance nodeInstance = new StageInstance(name, -1);
			for (String propertyKey : columns) {
				// ordering is maintained by the columns array
				nodeInstance.addValue(value.get(propertyKey));
			}

			selectRelationshipStatement.setLong(1, rowRefId);
			ResultSet set = selectRelationshipStatement.executeQuery();
			while (set.next()) { // process results one row at a time
				nodeInstance.setParentRefId((int) result.getLong(1));
			}
			
			return nodeInstance;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;

	}

	public void updatePropertyValue(int id, String columnName, String replacement) {
		Node n = new Node(id);
		n.setProperty(columnName, replacement, true);
	}

	public String getEntityPropertyValue(int id, String columnName) {
		try {
			selectNodePropsStatement.setLong(1, id);
			selectNodePropsStatement.setString(2, columnName);
			ResultSet result = selectNodePropsStatement.executeQuery();
			while (result.next()) { // process results one row at a time
				return result.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public int putValueToStageInstance(int id, String columnName, String value) {
		Node n = new Node(id);
		return n.setProperty(columnName, value, false) ? 1 : 0;
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
	}

	@Override
	protected EntityEventNodeInterface getNewInstanceOfNodeClass(SchemaObjectInterface childFromList, double value, AbstractEntityEventNode abstractEntityEventNode, List<SchemaObjectInterface> list)
			throws FileNotFoundException, IOException, SQLException {
		return new EntityEventNodeH2(childFromList, value, this, list);
	}

}
