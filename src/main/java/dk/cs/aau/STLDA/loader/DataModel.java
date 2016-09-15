package dk.cs.aau.STLDA.loader;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dk.cs.aau.STLDA.Helper;

public class DataModel {
	private List<Tuple> data = new ArrayList<Tuple>();
	private List<Relationship> relationships = new ArrayList<Relationship>();

	public void addTriple(String triple) {
		String[] input = trimLine(triple);
		boolean match = false;
		
		//if the object does not exist, create it
		if (objectProperty(input)) {
			if (!objectExists(input)) {
				Tuple tuple = new Tuple(input[2]);
				data.add(0,tuple);
			}
		} 
		
		//If the subject already exists
		for (Tuple tuple : data) {
			if (tuple.equals(input)) {
				match = true;
				
				if (objectProperty(input)) {
					Relationship relationship = new Relationship(input);
					relationships.add(relationship);
				} else {
					tuple.addAttribute(input);
				}
			}
		}
		
		if (!match) {
			if (objectProperty(input)) {
				Tuple tuple = new Tuple(input[0]);
				data.add(tuple);
				Relationship relationship = new Relationship(input);
				relationships.add(relationship);
			} else {
				Tuple tuple = new Tuple(input);
				data.add(tuple);
			}
		}
	}
	
	private boolean objectExists(String[] input) {
			for (Tuple tuple : data) {
				if (tuple.getSubject().equals(input[2])) {
					return true;
				}
			}
		return false;
	}

	private boolean objectProperty(String[] input) {
		if (input[2].contains("\"") || input[2].matches("\\d+")) {
			return false;
		}
		return true;
	}

	private String[] trimLine(String line) {
		
		List<String> matchList = new ArrayList<String>();
		Pattern regex = Pattern.compile("[^\\s\"']+|\"[^\"]*\"|'[^']*'");
		Matcher regexMatcher = regex.matcher(line);
		while (regexMatcher.find()) {
		    matchList.add(regexMatcher.group());
		} 
		
		String[] res = new String[3];
		res[0] = matchList.get(0);
		res[1] = matchList.get(1);
		res[2] = matchList.get(2);
		
		for (int i = 0; i < res.length; i++) {
			res[i] = Helper.trimNode(res[i]);
		}
		return res;
	}
	
	@Override
	public String toString() {
		String result = "";
		for (Tuple tuple : data) {
			result += tuple.toString()+"\n";
		}
		return result;
	}
	
	public List<String> getEntities() {
		List<String> results = new ArrayList<String>();
		for (Tuple tuple : data) {
			results.add("CREATE "+tuple.toString());
		}
		return results;
	}

	public List<String> getRelationships() {
		List<String> results = new ArrayList<String>();
		for (Relationship relationship : relationships) {
			results.add(relationship.toString());
		}
		return results;
	}
}
