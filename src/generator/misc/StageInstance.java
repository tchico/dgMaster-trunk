package generator.misc;

import java.util.ArrayList;
import java.util.List;

public class StageInstance implements StageInstanceInterface{
	private String name;
	private int parentRefId; // this is the position in the parent node stage list of this reference
	private List<String> values; // output from the randomiser, ordering? unknown atm
	private List<StageInstanceInterface> children;
	private int Id;
	
	public StageInstance(String name, int parentRefId){
		this.name = name;
		this.parentRefId = parentRefId;
		this.setId(0);
		values = new ArrayList<String>();
		children = new ArrayList<StageInstanceInterface>();
	}
	
	public StageInstance(String name, int parentRefId, int i){
		this.name = name;
		this.parentRefId = parentRefId;
		this.setId(i);
		values = new ArrayList<String>();
		children = new ArrayList<StageInstanceInterface>();
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setParentRefId(int parentRefId) {
		this.parentRefId = parentRefId;
	}
	public int getParentRefId() {
		return parentRefId;
	}
	public void setValues(List<String> values) {
		this.values = values;
	}
	public List<String> getValues() {
		return values;
	}
	public void addValue(String s){
		values.add(s);
	}
	public void cleanValues(){
		values = new ArrayList<String>();
	}

	@Override
	public String toString() {
		String str = "StageInstance [name=" + name + ", parentRefId=" + parentRefId + ", values=" + values + "]";
		return str;
	}

	public List<StageInstanceInterface> getChildren() {
		return children;
	}
	
	public List<StageInstanceInterface> getChildren(String targetName) { // get only the children with this specific name
		List<StageInstanceInterface> temp = new ArrayList<StageInstanceInterface>();
		for(StageInstanceInterface c : children){
			if(c.getName().equalsIgnoreCase(targetName)){
				temp.add(c);
			}
		}
		return temp;
	}

	public void addChild(StageInstanceInterface child) {
		children.add(child);
	}

	public void cleanChildren() {
		children = new ArrayList<StageInstanceInterface>();
		
	}

	public void setId(int id) {
		Id = id;
	}

	public int getId() {
		return Id;
	}
	
	
	
}
