package defaultPackage;

// I wrote this class while drunk...

public class ParsingThread extends Thread {
	
	private Parsable parsingObject;
	
	public ParsingThread(Parsable parsingObject) {
		this.parsingObject = parsingObject;
	}
	
	public void run() {
		parsingObject.parse();
	}
	
	
// override run?
	// pass in object to parse
	// call parse on that object
	// but somehow have the original call return quickly
	// maybe only have original call set the next object to parse
	// and then calling start() will call run() and that will parse
	// the given object
	
}
