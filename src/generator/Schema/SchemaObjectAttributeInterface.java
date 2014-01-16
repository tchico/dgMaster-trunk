package generator.Schema;

import java.util.Map;

public interface SchemaObjectAttributeInterface {
	public void add(String s,String v);
	public void setValues(Map<String,String> values);
	public Map<String,String> getValues();
	public String getValue(String s);
	public void setType(String name);
	public String getType();
}