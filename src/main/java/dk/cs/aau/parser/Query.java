package dk.cs.aau.parser;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;

public class Query {
	
	String query = "";
	String username;
	String password;

	
	public Query(String path, String username, String password) throws IOException
	{
		this.username = username;
		this.password = password;
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
			cypherQuery += "MATCH ("+triple.getSubject()+")"+"-["+triple.getProperty()+"]->("+triple.getObject()+") ";
		}
		cypherQuery += "RETURN "+select;
		
		return cypherQuery;
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
		Driver driver = GraphDatabase.driver( "bolt://localhost", AuthTokens.basic( username, password ) );
		Session session = driver.session();
			
		System.out.println("Executing query:");
		System.out.println(query+"\n");
		
		StatementResult result = session.run(this.toString());
		
		System.out.println("Results:");
		while (result.hasNext()) {
			System.out.println(result.next().toString());
		}
		
		session.close();
		driver.close();
	}
}
