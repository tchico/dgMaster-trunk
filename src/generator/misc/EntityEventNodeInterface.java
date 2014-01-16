package generator.misc;

import generator.Schema.SchemaObjectInterface;

import java.util.ArrayList;
import java.util.List;

public interface EntityEventNodeInterface {
	public static final String SEPARATOR_MULTIFIELD_REGEX = "\\|";
	public static final String SEPARATOR_MULTIFIELD = "|";
	
	
	
	public void addChild(EntityEventNodeInterface e);

	public List<EntityEventNodeInterface> getChildren();

	public void setParent(EntityEventNodeInterface e);

	public EntityEventNodeInterface getParent();

	public void setBuildRatioQty(double buildRatioQty);

	public double getBuildRatioQty();

	public void setStageDataContext(SchemaObjectInterface stageDataContext);

	public SchemaObjectInterface getStageDataContext();

	/**
	 * Gets the EntityEventNodeInterface corresponding to the node denoted by the name
	 * @param name   of the Node or EntityEventNodeInterface
	 * @return EntityEventNodeInterface
	 */
	public EntityEventNodeInterface getNode(String name);

	public void keyStageValue(double d);

	/**
	 * return this node's name
	 * @return
	 */
	public String getName();

	public String getType();

	/**
	 * Adds a column name to this node set of columns
	 * @param s
	 */
	public void addColumn(String s);

	/**
	 * gets the set of columns for this node
	 * @return
	 */
	public List<String> getColumns();

	/**
	 * Gets the value present for the column in the stage level given 
	 * by the targetEntityName. This is all referring to the instance node object with the given id.
	 * @param id  of the node instance we want to refer to.
	 * @param columnName 
	 * @param targetName  stage name / node name 
	 * @return the value in that column
	 */
	public String getColumnValue(int id, String columnName, String targetName);
	
	public void updatePropertyValue(int id, String columnName, String replacement);
	
	//public StageInstanceInterface getStageInstance(int rowRefId);
	
	/**
	 * if true returns a random value from the multi value fields
	 * @param val
	 */
	public void setRandomReturnOnMultiFieldColumns(boolean val);

	public boolean isRandomReturnOnMultiFieldColumns();


	/**
	 * Returns the number of node instances present in this Stage Node.
	 * @return
	 */
	public Long getStageInstancesCount();

	/**
	 * Updates the value in the Stage Node Instance
	 * @param id
	 * @param string
	 * @return  modified status
	 */
	public int putValueToStageInstance(int id, String columnName, String string);

	/**
	 * 
	 * @param id
	 * @return modification status
	 */
	public int cleanValuesOfInstance(int id);

	public StageInstanceInterface getStageInstance(int instanceId);

	public List<StageInstanceInterface> findLinkageStage(String findNode,int currentId);
	
	public int getRefForStageInstance(StageInstanceInterface stageinstance );
	
}
