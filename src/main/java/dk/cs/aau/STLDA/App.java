package dk.cs.aau.STLDA;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import dk.cs.aau.STLDA.loader.RDFStore;
import dk.cs.aau.STLDA.loader.Saturater;
import dk.cs.aau.parser.Query;

public class App {

	public static void main( String[] args )
	{
		// create the command line parser
		CommandLineParser parser = new DefaultParser();
	
		// create the Options
		Options options = new Options();
		
		options.addOption("h", "help", false, "Display this message." );
		options.addOption("u", "user", true, "neo4j username." );
		options.addOption("p", "password", true, "neo4j password." );
		options.addOption("l", "load", true, "load specified file, deletes the existing graph");
		options.addOption("s", "saturate", false, "saturates the graph");
		options.addOption("q", "query", true, "reads a file with a query and executes it");
	
		
		
		try {
		    CommandLine line = parser.parse( options, args );
		    
		    if (line.hasOption( "help" )) {
		    	printHelp(null,options);
		    	System.exit(0);
			} 
		    
		    if (!line.hasOption( "user" )) {
		    	System.out.println("missing neo4j username");
		    	System.exit(0);
			} 
		    String username = line.getOptionValue("user");
		    
		    if (!line.hasOption( "password" )) {
		    	System.out.println("missing neo4j password");
		    	System.exit(0);
			} 
		    String password = line.getOptionValue("password");
		    
		    if (line.hasOption("load")) {
		    	RDFStore store = new RDFStore(line.getOptionValue("load"),username,password);
		    	store.load();
			}
		    
		    if (line.hasOption("saturate")) {
		    	Saturater saturate = new Saturater();
		    	saturate.all();
			}

		    if (line.hasOption("query")) {
		    	Query query = new Query(line.getOptionValue("query"),username,password);
		    	query.execute();
			}

		}
		catch( ParseException exp ) {
			printHelp(exp, options);
		} catch (Exception exp) {
			exp.printStackTrace();
		}
	}
	
	private static void printHelp(ParseException exp, Options options) {
		String header = "";
		HelpFormatter formatter = new HelpFormatter();
		if (exp != null) {
			header = "Unexpected exception:" + exp.getMessage();
		}
		formatter.printHelp("myapp", header, options, null, true);
	}
}
