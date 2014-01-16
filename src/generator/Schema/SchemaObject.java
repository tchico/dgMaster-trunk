package generator.Schema;

import java.util.ArrayList;
import java.util.List;

public class SchemaObject implements SchemaObjectInterface {
	
	private List<SchemaObjectAttribute> attributes;
	private String name;
	private String type;

	public  SchemaObject() {
		this.attributes = new ArrayList<SchemaObjectAttribute>();
	}
	
	public SchemaObject(String name,String type) {
		this.name = name;
		this.setType(type);
		this.attributes = new ArrayList<SchemaObjectAttribute>();
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}


	public List<SchemaObjectAttribute> getAttributes() {
		return attributes;
	}
	
	public void setAttributes(List<SchemaObjectAttribute> attributes) {
		this.attributes = attributes;
	}
	
	public void addAttribute(SchemaObjectAttribute att) {
		attributes.add(att);
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		SchemaObject other = (SchemaObject) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "SchemaObject [name=" + name + ", type=" + type + " attributes=" + attributes + "]";
	}
	
	
}
