package generator.Schema;

import java.util.HashMap;
import java.util.Map;

public class SchemaObjectAttribute implements SchemaObjectAttributeInterface{
	private Map<String,String> values;
	private String type;
	
	public SchemaObjectAttribute(String e){
		this.type = e;
	}
	
	
	public SchemaObjectAttribute() {
		values = new HashMap<String,String>();
	}


	public void add(String s,String v){
		values.put(s, v);
	}
	public void setValues(Map<String,String> values) {
		this.values = values;
	}
	public Map<String,String> getValues() {
		return values;
	}
	public void setType(String name) {
		this.type = name;
	}
	public String getType() {
		return type;
	}
	public String getValue(String s){
		return values.get(s);
	}


	@Override
	public String toString() {
		return "SchemaObjectAttribute [values=" + values + ", type=" + type + "]";
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((values == null) ? 0 : values.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SchemaObjectAttribute other = (SchemaObjectAttribute) obj;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (values == null) {
			if (other.values != null)
				return false;
		} 
		return true;
	}

	
}
