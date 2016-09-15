package dk.cs.aau.STLDA.loader;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;

public class Saturater {

	public Saturater() {
	}
	
	
	private void saturateSubPropertyOf(Session session) {
		StatementResult sr = session.run("MATCH p=(a)-[r:subpropertyof]->(b) RETURN a.identifier, b.identifier");
		List<Pair<String,String>> subPropertyOf = new ArrayList<Pair<String,String>>();
		
		
		while (sr.hasNext()) {
			
			Record r = sr.next();
			Pair<String, String> p = new ImmutablePair<>(r.get(0).toString(),r.get(1).toString());
			subPropertyOf.add(removeQuoutes(p));
		}

		for (Pair<String, String> pair : subPropertyOf) {
			StatementResult asds;
			do {
				//System.out.println("MATCH (a)-[:"+pair.getLeft().replace("\"", "")+"]->(b) WHERE NOT (a)-[:"+pair.getRight().replace("\"", "")+"]->(b) CREATE (a)-[:"+pair.getRight().replace("\"", "")+"]->(b) RETURN c");
				asds = session.run("MATCH (a)-[:"+pair.getLeft()+"]->(b) WHERE NOT (a)-[:"+pair.getRight()+"]->(b) CREATE  c=(a)-[:"+pair.getRight()+"]->(b) RETURN c");
				
			} while (asds.hasNext());
		}
	}
	
	private void saturateSubClassOf(Session session) {
		StatementResult sr = session.run("MATCH p=(a)-[r:subclassof]->(b) RETURN a.identifier, b.identifier");
		List<Pair<String,String>> subClassOf = new ArrayList<Pair<String,String>>();
		
		while (sr.hasNext()) {
			
			Record r = sr.next();
			Pair<String, String> p = new ImmutablePair<>(r.get(0).toString(),r.get(1).toString());
			subClassOf.add(removeQuoutes(p));
		}

		for (Pair<String, String> pair : subClassOf) {
			StatementResult asds;
			do {
				asds = session.run("MATCH (a)-[:type]->(b:node{identifier:\""+pair.getLeft()+"\"}) MATCH (d{identifier:\""+pair.getRight()+"\"})  WHERE NOT (a)-[:type]->(d) CREATE  c=(a)-[:type]->(d) RETURN c");
				
			} while (asds.hasNext());
		}
	}
	
	private void saturateDomain(Session session) {
		StatementResult sr = session.run("MATCH p=(a)-[r:domain]->(b) RETURN a.identifier, b.identifier");
		List<Pair<String,String>> hasDomain = new ArrayList<Pair<String,String>>();
		
		while (sr.hasNext()) {
			
			Record r = sr.next();
			Pair<String, String> p = new ImmutablePair<>(r.get(0).toString(),r.get(1).toString());
			hasDomain.add(removeQuoutes(p));
		}

		for (Pair<String, String> pair : hasDomain) {
			session.run("MATCH (subject)-[:"+pair.getLeft()+"]->(object) "
					+ "MATCH (type{identifier:\""+pair.getRight()+"\"}) "
					+ "WHERE NOT (subject)-[:type]->(type) "
					+ "CREATE  c=(subject)-[:type]->(type)");
		}
	}
	
	private void saturateRange(Session session) {
		StatementResult sr = session.run("MATCH p=(a)-[r:range]->(b) RETURN a.identifier, b.identifier");
		List<Pair<String,String>> hasRange = new ArrayList<Pair<String,String>>();
		
		while (sr.hasNext()) {
			
			Record r = sr.next();
			Pair<String, String> p = new ImmutablePair<>(r.get(0).toString(),r.get(1).toString());
			hasRange.add(removeQuoutes(p));
		}

		for (Pair<String, String> pair : hasRange) {
			session.run("MATCH (subject)-[:"+pair.getLeft()+"]->(object) "
					+ "MATCH (type{identifier:\""+pair.getRight()+"\"}) "
					+ "WHERE NOT (object)-[:type]->(type) "
					+ "CREATE  c=(object)-[:type]->(type)");
		}
	}

	private void saturateRDFS11(Session session) {
		session.run("MATCH (a)-[r:subclassof]->(b) MATCH (b)-[q:subclassof]->(c) CREATE (a)-[p:subclassof]->(c)");
	}
	
	private void saturateRDFS5(Session session) {
		session.run("MATCH (a)-[r:subpropertyof]->(b) MATCH (b)-[q:subpropertyof]->(c) CREATE (a)-[p:subpropertyof]->(c)");
	}
	
	private void saturateEXT1(Session session) {
		session.run("MATCH (a)-[r:domain]->(b) MATCH (b)-[q:subclassof]->(c) CREATE (a)-[p:domain]->(c)");
	}
	
	private void saturateEXT2(Session session) {
		session.run("MATCH (a)-[r:range]->(b) MATCH (b)-[q:subclassof]->(c) CREATE (a)-[p:range]->(c)");
	}
	
	private void saturateEXT3(Session session) {
		session.run("MATCH (a)-[r:subpropertyof]->(b) MATCH (b)-[q:domain]->(c) CREATE (a)-[p:domain]->(c)");
	}
	
	private void saturateEXT4(Session session) {
		session.run("MATCH (a)-[r:subpropertyof]->(b) MATCH (b)-[q:range]->(c) CREATE (a)-[p:range]->(c)");
	}
	
	private Pair<String, String> removeQuoutes(Pair<String, String> p) {
		Pair<String,String> output = new ImmutablePair<String, String>(p.getLeft().replace("\"", ""), p.getRight().replace("\"", ""));
		return output;
	}


	public void all() {
		Driver driver = GraphDatabase.driver( "bolt://localhost", AuthTokens.basic( "neo4j", "neo4j" ) );
		Session session = driver.session();
		
		saturateEXT1(session);
		saturateEXT2(session);
		saturateEXT3(session);
		saturateEXT4(session);
		saturateRDFS11(session);
		saturateRDFS5(session);
		saturateSubPropertyOf(session);
		saturateSubClassOf(session);
		saturateDomain(session);
		saturateRange(session);

		session.close();
		driver.close();
	}
}
