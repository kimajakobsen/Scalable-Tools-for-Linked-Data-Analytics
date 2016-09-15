package dk.cs.aau.STLDA;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import dk.cs.aau.STLDA.loader.RDFStore;
import dk.cs.aau.STLDA.loader.Saturater;

public class App {

	public static void main( String[] args )
	{
		// create the command line parser
		CommandLineParser parser = new DefaultParser();
	
		// create the Options
		Options options = new Options();
		options.addOption("h", "help", false, "Display this message." );
		options.addOption("l", "load", true, "load specified file, deletes the existing graph");
		options.addOption("s", "saturate", false, "saturates the graph");
		options.addOption("q", "query", true, "executes a query");
	
		RDFStore store = new RDFStore();
		
		try {
		    CommandLine line = parser.parse( options, args );
		   
		    if (line.hasOption( "help" )) {
		    	printHelp(null,options);
		    	System.exit(0);
			} 
		    if (line.hasOption("load")) {
		    	store.load(line.getOptionValue("load"));
			}
		    if (line.hasOption("saturate")) {
		    	Saturater saturate = new Saturater();
		    	saturate.all();
			}

		    if (line.hasOption("query")) {
		    	
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
