package generator.Schema;

import java.util.List;

public interface SchemaObjectInterface {
	public void addAttribute(SchemaObjectAttribute att);
	public List<SchemaObjectAttribute> getAttributes();
	
	public void setName(String name);
	public String getName();
	public void setType(String type);
	public void setAttributes(List<SchemaObjectAttribute> attributes);
	public String getType();
}
