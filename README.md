# Scalable-Tools-for-Linked-Data-Analytics
This application loads RDf data in n3 format into neo4j, 
saturate the graph according to 10 entailment rules, 
and allows the user to query the data using a simple SPARQL like language.


## Prerequisites
* Neo4j server running on localhost:7474
* Java 7 or higher


The JAR file in the root is the newest version of the program and can be used as follows:

java -jar kim.jar <parameters>

It accepts the following parameters
*  -h,--help             Display this message.
*  -l,--load <arg>       load specified file, deletes the existing graph
*  -p,--password <arg>   neo4j password. (required)
*  -q,--query <arg>      reads a file with a query and executes it
*  -s,--saturate         saturates the graph
*  -u,--user <arg>       neo4j username. (required)

## Example
java -jar kim.jar -l "units.n3" -s -q "query1.sparql" -u "neo4j" -p "neo4j"

## SPARQL lanauge
Only a SELECT clause and a WHERE clause can be parsed.
FILTER, GROUP BY, ORDER BY, LIMIT, FROM, etc. are not supported.


## TODO
* Test all 10 saturation rules
