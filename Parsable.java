// interface for all objects that should be parsable
// ex: athletes, meets, teams.


public interface Parsable {

	public String getURL();
	// attempts to parse the current object and return true or false based on success of operation
	public boolean parse();
	
	//public boolean isParsed();
}
