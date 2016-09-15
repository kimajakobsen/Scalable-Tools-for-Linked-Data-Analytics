package dk.cs.aau.STLDA.loader;

import java.util.ArrayList;
import java.util.List;

public class Tuple {
	
	private String subject;
	private List<Attribute> attributes = new ArrayList<Attribute>();
	
	public Tuple(String[] triple) {
		subject = triple[0];
		addIdentifier(triple[0]);
		addAttribute(triple);
	}
	
	public Tuple(String object) {
		subject = object;
		addIdentifier(object);
	}

	private void addIdentifier(String object) {
		Attribute identifier = new Attribute("identifier",'"'+object+'"');
		attributes.add(identifier);
	}

	public boolean equals(String[] triple) {
		if (subject.equals(triple[0])) {
			return true;
		}
		return false;
	}

	public void addAttribute(String[] triple) {
		if (!alreadyContainsAttribute(triple[1],triple[2])) {
			Attribute attribute = new Attribute(triple[1],triple[2]);
			attributes.add(attribute);
		}
	}

	private boolean alreadyContainsAttribute(String property, String object) {
		
		for (Attribute attribute : attributes) {
			if (attribute.equals(property,object)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public String toString() {
		String propertyObject = "";
		for (Attribute attribute : attributes) {
			propertyObject += attribute.getProperty()+":"+attribute.getObject()+", ";
		}
		propertyObject = removeTrailingComma(propertyObject);
		return "( "+subject+":node { "+ propertyObject + " })";
	}

	private String removeTrailingComma(String propertyObject) {
		if (propertyObject.length() > 2) {
			return propertyObject.substring(0, propertyObject.length()-2);
		} else {
			return propertyObject;
		}
	}

	public String getSubject() {
		return subject;
	}
}
