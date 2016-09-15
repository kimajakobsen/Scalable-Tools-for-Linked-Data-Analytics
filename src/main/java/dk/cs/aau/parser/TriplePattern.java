package dk.cs.aau.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dk.cs.aau.STLDA.Helper;

public class TriplePattern {

	private String subject = "";
	private String property = "";
	private String object = "";
	
	public TriplePattern(String line) {
		List<String> matchList = new ArrayList<String>();
		Pattern regex = Pattern.compile("[^\\s\"']+|\"[^\"]*\"|'[^']*'");
		Matcher regexMatcher = regex.matcher(line);
		while (regexMatcher.find()) {
		    matchList.add(regexMatcher.group());
		} 
		
		setSubject(matchList.get(0));
		setProperty(matchList.get(1));
		setObject(matchList.get(2));
	}
	
	public String getSubject() {
		return subject;
	}
	
	public String getProperty() {
		return property;
	}
	
	public String getObject() {
		return object;
	}
	
	private void setSubject(String element){
		element = Helper.trimNode(element);
		if (isVariable(element)) {
			element = element.replace("?", "");
		} else {
			element = element+"{identifier:\""+element+"\"}";
		}
		
		this.subject = element;
	}
	
	private boolean isVariable(String element) {
		if (element.contains("?")) {
			return true;
		}
		return false;
	}

	private void setProperty(String element){
		element = Helper.trimNode(element);
		if (isVariable(element)) {
			element = element.replace("?", "");
		} else {
			element = ":"+element;
		}
		this.property = element;
	}

	private void setObject(String element){
		element = Helper.trimNode(element);
		if (isVariable(element)) {
			element = element.replace("?", "");
		} else {
			element = element+"{identifier:\""+element+"\"}";
		}
		
		this.object = element;
	}
	
	@Override
	public String toString() {
		return getSubject()+" "+getProperty()+" "+getObject();
	}
}
