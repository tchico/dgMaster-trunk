package generator.misc;

import java.util.List;

/**
 * @link {@link StageInstance}
 */
public interface StageInstanceInterface {
	public void setName(String name);
	public String getName();

	//public void setParentRefId(int parentRefId);
	public int getParentRefId();
	//public void setValues(List<String> values);
	
	public List<String> getValues();
	//public void addValue(String s);
	public void cleanValues();
	
	public List<StageInstanceInterface> getChildren();
	
	public void addChild(StageInstanceInterface child);
	
	public void cleanChildren();
	
	public List<StageInstanceInterface> getChildren(String name);
	

	public void setId(int id);

	public int getId();
	
}
