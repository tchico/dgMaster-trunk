package generator.misc;

import generator.Schema.SchemaObjectInterface;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author shanewalsh
 * The EntityEventNode holds all of the information realating to a single stage in the generation. 
 * The order these are connected in in the tree is the order of the stages
 */
public class EntityEventNode extends AbstractEntityEventNode{
	//randomiser specific
	private List<StageInstanceInterface> stageInstances; // position in the list is the refId, each instance contains the parents refId, this is the thing that caused its creation

	
	public EntityEventNode(SchemaObjectInterface  s){
		schemaObject = s;
		type = s.getType();
		name = s.getName();
		stageInstances = new ArrayList<StageInstanceInterface>();
	}
	
	// trees are structured in relation to their parent, so customers parent is branch and its build ratio is 1000, then for every one f th parent make 1000 of child
	//top node only
	public EntityEventNode(SchemaObjectInterface  s, List<SchemaObjectInterface> list){
		this(s);
		buildRatioQty = 1;
		children = new ArrayList<EntityEventNodeInterface>();
		
		try {
			buildChildEntityEventNodes(s, list);
		} catch (Exception e) {
			//Not likely to happen for this class implementation 
			e.printStackTrace();
		}
	}

	
	
	public EntityEventNode(SchemaObjectInterface  s, double b,  EntityEventNodeInterface parent, List<SchemaObjectInterface> list){
		this(s);
		buildRatioQty = b;
		children = new ArrayList<EntityEventNodeInterface>();
		this.parent = parent;
		
		try {
			buildChildEntityEventNodes(s, list);
		} catch (Exception e) {
			// Not likely to happen for this class implementation
			e.printStackTrace();
		}
	}
	
	// Possibly a way to add percentage distribution to values, would have a cascade effect thought, as is the cost of using trees.
	public EntityEventNode(SchemaObjectInterface  s, double [] b,  EntityEventNodeInterface parent, List<SchemaObjectInterface> list){ 
		this(s);
		
		buildRatioQtyArray = b;
		children = new ArrayList<EntityEventNodeInterface>();
		this.parent = parent;

		try {
			buildChildEntityEventNodes(s, list);
		} catch (Exception e) {
			// Not likely to happen for this class implementation
			e.printStackTrace();
		}
		
	}
	
	
	/** 
	 * A method that calls itself multiple times until it finds the parent node, then the loop breaks and it creates stage instances using the total number
	 * @param totalNum is the value of orignal key stage and is divided by the build ratios until it reaches the top node.
	 */
	public void keyStageValue(double totalNum){
		if(this.parent != null){ // then this is not the top
			this.getParent().keyStageValue(totalNum/this.buildRatioQty);
		}
		else{ // at the top
			for(double i = 0; i < totalNum; i++){
				stageInstances.add(new StageInstance(this.name,-1,stageInstances.size()));
				for(EntityEventNodeInterface child: this.children){
					((EntityEventNode)child).build(stageInstances.size()-1, stageInstances.get(stageInstances.size()-1));
				}
			}
		}
	}
	
	
	/**
	 * The Build method is used to populate the entityeventnodes with stage instances.
	 * @param parentRef is the location of the stage instance in the entityeventnode that created it.
	 */
	public void build(int parentRef,StageInstanceInterface parentStageInstance){
			// total amount of values we need to create.
		double total = this.buildRatioQty + leftOver;
		leftOver = total % 1;
		for(double i = 1; i <= total; i++){
			stageInstances.add(new StageInstance(this.name,parentRef,stageInstances.size()));
			if(parentStageInstance!=null)
				parentStageInstance.addChild(stageInstances.get(stageInstances.size()-1));
			for(EntityEventNodeInterface child: this.children){
				((EntityEventNode)child).build(stageInstances.size()-1, stageInstances.get(stageInstances.size()-1));
			}
		}
	}
	
	/**
	 * This build method is used to build a specific amount of stage instances, but uses the ratios back down the tree
	 * @param parentRef is the location of the stage instance in the entityeventnode that created it.
	 */
	public void build(int parentRef,int amount,StageInstanceInterface parentStageInstance){
		for(int i = 1; i <= amount; i++){
			stageInstances.add(new StageInstance(this.name,parentRef,stageInstances.size()));
			if(parentStageInstance!=null)
				parentStageInstance.addChild(stageInstances.get(stageInstances.size()-1));
			for(EntityEventNodeInterface child: this.children){
				((EntityEventNode)child).build(stageInstances.size()-1,stageInstances.get(stageInstances.size()-1));
			}
		}	
	}
	

    public void clearStageInstances(){
    	stageInstances = new ArrayList<StageInstanceInterface>();
    }
	
	/**
	 * getColumnValue is used to get a value stored in a particular column in a stageInstance
	 * @param id	is the parent ref taken from the stageInstance that called the method, it is the location of the required stageInstance in the stageInstances of this node
	 * @param columnName 	is the name of the column/randomiser to get the data from
	 * @param targetName	is the name of the stage/EntityEventNode you want the data from 
	 * @return 		The data you requested is returned as a String
	 */
	public String getColumnValue( int id, String columnName, String targetName){
		if(this.name.equalsIgnoreCase(targetName)){ // is this the node where the data is
			if(columns.indexOf(columnName) != -1){
				String val = ""; 
				if(stageInstances.get(id).getValues().size() > columns.indexOf(columnName)){
					val = stageInstances.get(id).getValues().get(columns.indexOf(columnName));
				}
				if(val.contains(SEPARATOR_MULTIFIELD)){ // multiple options
					String [] val2 = val.split(SEPARATOR_MULTIFIELD_REGEX);
					Random rand = new Random();
					if(!isRandomReturnOnMultiFieldColumns()){
						return val2[rand.nextInt(val2.length)]; // now it should return a random value from the 
					} 
					
					//takes the first one and puts it on the end of the string, and returns it, natural distribution in ordering
					// this approach also allows for control of percentages, if you want 50 , 25, 25 enter the first value twice, followed by the other two individuals. 
					String replacement = ""; boolean first = true; String returnValue= "";
					for(String s : val2){
						if(first){
							returnValue = s;
							first = false;
						}else{
							replacement += s+ SEPARATOR_MULTIFIELD;
						}
					}
					replacement += returnValue; // now the first value is at the end of the list
					updatePropertyValue(id, columnName, replacement); // update the stage instance with the new column value
					return returnValue; // now pass back what was the first entry
					
				}
				else
					return val; // just a standard column, return the value
			}else{
				System.out.println("Error: cannot find the Column " + columnName + " , pls note names must match exactly");
				return "";// could not find the column specified
			}
		}
		else{
			return this.parent.getColumnValue(stageInstances.get(id).getParentRefId(), columnName, targetName); // not this one so keep going up the tree until its found
		}
	}
	
	/**
	 * updates the value of a particular stage instance, used with multi field values
	 * @param id	is the position of the stage instance in the collection
	 * @param columnName	is the column/randomiser you want to make the change in
	 * @param replacement 	is the new value you want to insert
	 */
	public void updatePropertyValue(int id, String columnName, String replacement){
		stageInstances.get(id).getValues().set(columns.indexOf(columnName),replacement);
	}
	
	/**
	 * Get the stage instance object corresponding to the row id
	 * @param rowRefId
	 */
//	public StageInstanceInterface getStageInstance(int rowRefId) {
//		return stageInstances.get(rowRefId);
//	}

	public Long getStageInstancesCount() {
		return new Long(stageInstances.size());
	}
	
	public StageInstanceInterface getStageInstance(int i){
		return stageInstances.get(i);
	}
	
	public void addValueToStageInstance(int id, String value) {
		((StageInstance)stageInstances.get(id)).addValue(value);
	}

	public int cleanValuesOfInstance(int id) {
		stageInstances.get(id).cleanValues();
		return 1;
	}

	public int putValueToStageInstance(int id, String columnName, String value) {
		addValueToStageInstance(id, value);
		return 1;
	}

	@Override
	protected EntityEventNodeInterface getNewInstanceOfNodeClass(SchemaObjectInterface childFromList, double value, AbstractEntityEventNode abstractEntityEventNode,
			List<SchemaObjectInterface> list) {
		return new EntityEventNode(childFromList,value,this,list);
	}
	 
	public int getRefForStageInstance(StageInstanceInterface stageinstance ){

		return stageInstances.indexOf(stageinstance);
	}
}




