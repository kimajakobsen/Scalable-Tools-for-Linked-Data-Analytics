package dk.cs.aau.STLDA.loader;

public class Attribute {

	private String property;
	private String object;
	
	public Attribute(String property, String object) {
		this.property = property;
		this.object = object;
	}

	public boolean equals(String property, String object) {
		if (this.property.equals(property) && this.object.equals(object)) {
			return true;
		}
		return false;
	}
	
	public String getProperty() {
		return property;
	}
	
	public String getObject() {
		return object;
	}
}
