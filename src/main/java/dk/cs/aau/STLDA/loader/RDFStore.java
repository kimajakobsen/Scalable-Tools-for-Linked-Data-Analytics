package dk.cs.aau.STLDA.loader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Session;

public class RDFStore {
	DataModel data;
	String path;
	String username;
	String password;
	
	
	public RDFStore(String optionValue, String username, String password) {
		this.path = optionValue;
		this.username = username;
		this.password = password;
	}

	private DataModel getData(String path) throws FileNotFoundException, IOException {
		if (data == null) {
			data = new DataModel();
			try (BufferedReader br = new BufferedReader(new FileReader(path))) {
			    String line;
			    while ((line = br.readLine()) != null) {
			    	data.addTriple(line);
			    }
			}
		} 
		
		return data;
	}

	public void load() throws FileNotFoundException, IOException {
		
		DataModel data = getData(path);

		Driver driver = GraphDatabase.driver( "bolt://localhost", AuthTokens.basic( username, password ) );
		Session session = driver.session();
			
		session.run("MATCH (n) DETACH DELETE n");
		
		for (String node : data.getEntities()) {
			session.run(node);
		}
		for (String relationship : data.getRelationships()) {
			session.run(relationship);
		}
		
		session.close();
		driver.close();
	}
}
