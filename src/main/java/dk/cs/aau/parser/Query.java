package dk.cs.aau.parser;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;

public class Query {
	
	String query = "";

	
	public Query(String path) throws IOException
	{
		String semiSparql = "";
		try(FileInputStream inputStream = new FileInputStream(path)) {
		    semiSparql = IOUtils.toString(inputStream);
		}
		
		SPARQLparser parser = new SPARQLparser();
		try {
			parser.check(semiSparql);
			this.query = semiSparql;
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	@Override
	public String toString() {
		return convertToCypher(query);
		
	}

	private String convertToCypher(String semiSparql) {
		String select = "";
		String where = "";
		String cypherQuery = "";
		
		Matcher matcher = Pattern.compile("SELECT(.*?)WHERE",Pattern.DOTALL).matcher(semiSparql);
		if (matcher.find()) {
		  select = selectClauseFormatter(matcher.group(1).trim());
		}

		matcher = Pattern.compile("\\{(.*?)\\}",Pattern.DOTALL).matcher(semiSparql);
		if (matcher.find()) {
		  where = matcher.group(1).trim();
		}
		
		for (String triplePattern : where.split(" \\.")) {
			TriplePattern triple = new TriplePattern(triplePattern);
			System.out.println(triple);
			cypherQuery += "MATCH ("+triple.getSubject()+")"+"-["+triple.getProperty()+"]->("+triple.getObject()+") ";
		}
		cypherQuery += "RETURN "+select;
		
		System.out.println(cypherQuery);
		return null;
	}

	private String selectClauseFormatter(String select) {
		String result = "";
		
		Matcher matcher = Pattern.compile("([a-zA-Z])").matcher(select);
		while (matcher.find()) {
			  result += matcher.group()+".identifier ";
			}
		result = result.trim();
		result = result.replace(" ", ", ");
		
		return result;
	}


	public void execute() {
		System.out.println(this.toString());
	}
}
