package generator.misc;

import generator.Schema.SchemaObjectAttributeInterface;
import generator.Schema.SchemaObjectInterface;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 
 *
 */
public abstract class AbstractEntityEventNode  implements EntityEventNodeInterface{

	protected SchemaObjectInterface schemaObject;
	protected double buildRatioQty;
	protected double [] buildRatioQtyArray;
	protected List<EntityEventNodeInterface> children;
	protected EntityEventNodeInterface parent;
	protected String type;
	protected String name;
	protected List<String> columns = new ArrayList<String>();
	private boolean randomReturnOnMultiFieldColumns;
	protected double leftOver;

	public AbstractEntityEventNode() {
		super();
	}
	
	protected void buildChildEntityEventNodes(SchemaObjectInterface s, List<SchemaObjectInterface> list) throws NumberFormatException, FileNotFoundException, IOException, SQLException{
		try{
		for(SchemaObjectAttributeInterface a :s.getAttributes()){
			if(a.getType().equalsIgnoreCase("forEvery")){ // link is unused and can be removed
				if(a.getValue("value").contains(SEPARATOR_MULTIFIELD_REGEX)){ // then we have a distribution of values
					//TODO: do something here?
				}
				else{
					if(a.getValue("entity") != null){
						addChild( getNewInstanceOfNodeClass(getChildFromList(list,a.getValue("entity")),Double.parseDouble(a.getValue("value")),this,list) );
					}
					else{
						addChild( getNewInstanceOfNodeClass(getChildFromList(list,a.getValue("event")),Double.parseDouble(a.getValue("value")),this,list) );
					}
				}
			}
		}
		}
		catch(Exception e){
			// to do, add logging
		}
	}
	

	protected abstract EntityEventNodeInterface getNewInstanceOfNodeClass(SchemaObjectInterface childFromList, double parseDouble, AbstractEntityEventNode abstractEntityEventNode,
			List<SchemaObjectInterface> list) throws FileNotFoundException, IOException, SQLException;

	public void addChild(EntityEventNodeInterface e) {
		children.add(e);
	}

	public List<EntityEventNodeInterface> getChildren() {
		return this.children;
	}

	public void setParent(EntityEventNodeInterface e) {
		this.parent = e;
	}

	public EntityEventNodeInterface getParent() {
		return this.parent;
	}

	public void setBuildRatioQty(double buildRatioQty) {
		this.buildRatioQty = buildRatioQty;
	}

	public double getBuildRatioQty() {
		return buildRatioQty;
	}

	public void setStageDataContext(SchemaObjectInterface  schemaObject) {
		this.schemaObject = schemaObject;
	}

	public SchemaObjectInterface getStageDataContext() {
		return schemaObject;
	}

	protected SchemaObjectInterface getChildFromList(List<SchemaObjectInterface> list, String name) {
		for(SchemaObjectInterface s: list){
			if(s.getName().equals(name))
				return s;
		}
		return null;
		
	}

	/**
	 * Get the node specified by name
	 *@param name
	 */
	public EntityEventNodeInterface getNode(String name) {
		if(this.name.equalsIgnoreCase(name))
			return this;
		else{
			for(EntityEventNodeInterface n:children){
				EntityEventNodeInterface e = n.getNode(name);
				if(e != null){
					return e;
				}
			}
		}
		return null;
	}

	public String getName() {
		return this.name;
	}

	public String getType() {
		return this.type;
	}

	public void addColumn(String s) {
		columns.add( s);
	}

	public void setRandomReturnOnMultiFieldColumns(boolean val) {
		randomReturnOnMultiFieldColumns = val;
	}

	public boolean isRandomReturnOnMultiFieldColumns() {
		return randomReturnOnMultiFieldColumns;
	}

	public List<String> getColumns() {
		return columns;
	}
	
	public List<StageInstanceInterface> findLinkageStage(String findNode,int currentId ){
		List<StageInstanceInterface> tempList = null;
		List<List<StageInstanceInterface>> lists = new  ArrayList<List<StageInstanceInterface>>();
			for(EntityEventNodeInterface child:this.children){
				//tempList = new ArrayList<StageInstanceInterface>();
				if(child.getName().equalsIgnoreCase(findNode)){
					tempList = this.getStageInstance(currentId).getChildren(findNode); // the stage instances we want
				}
				else{
					for(StageInstanceInterface si:this.getStageInstance(currentId).getChildren(child.getName())){
						lists.add(child.findLinkageStage(findNode, si.getId()));
					}
					boolean foundValue = false;
					tempList = new ArrayList<StageInstanceInterface>();
					for(List l:lists){
						if(l != null){
							foundValue = tempList.addAll(l);
						}
					}
					if(foundValue == false){
						tempList = null;
					}
				}
				if(tempList!=null){
					return tempList;
				}
			}
			return tempList;
	}
	
	public int getRefForStageInstance(StageInstanceInterface stageinstance ){

		return 0;
	}

}