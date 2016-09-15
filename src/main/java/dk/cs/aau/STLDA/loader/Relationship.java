package dk.cs.aau.STLDA.loader;

public class Relationship {

	String subject;
	String object;
	String property;
	
	public Relationship(String[] input) {
		subject = input[0];
		object = input[2];
		property = input[1];
	}

	private String getSubject() {
		return "( subject:node { identifier:\""+ subject + "\" })";
	}
	
	private String getProperty() {
		return "(subject)-[:"+property+"]->(object)";
	}
	
	private String getObject() {
		return "( object:node { identifier:\""+ object + "\" })";
	}
	
	@Override
	public String toString() {
		return "MATCH "+getSubject()+" MATCH "+getObject()+" CREATE "+getProperty();
	}
}
