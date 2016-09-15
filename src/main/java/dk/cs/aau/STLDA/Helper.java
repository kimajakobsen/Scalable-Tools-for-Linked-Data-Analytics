package dk.cs.aau.STLDA;

public class Helper {
	
	public static String trimNode(String input)
	{
		String output = "";
		output = input.replaceAll("<", "");
		output = output.replaceAll("http://example.com/", "");
		output = output.replaceAll("http://www.w3.org/1999/02/22-rdf-syntax-ns#", "");
		output = output.replaceAll("http://www.w3.org/2000/01/rdf-schema#", "");
		output = output.replaceAll("http://www.w3.org/2001/xmlschema#", "");
		output = output.replaceAll(">", "");
		output = output.replaceAll("-", "");
		
		return output;
	}
}
