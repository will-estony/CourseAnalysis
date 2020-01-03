package defaultPackage;

import guiPackage.MenuPanel;

// I wrote this class while drunk...

public class ParsingThread extends Thread {
	
	private Parsable parsingObject;
	
	public ParsingThread(Parsable parsingObject) {
		this.parsingObject = parsingObject;
		MenuPanel.setLoading(true);
		this.start();
	}
	
	public void run() {
		if(parsingObject.parse() || !parsingObject.parse()){
			MenuPanel.setLoading(false);
			parsingObject.metrics.setNumItems(0.0);
			parsingObject.metrics.setcurrentItem(0.0);
		}
	}
	
	
// override run?
	// pass in object to parse
	// call parse on that object
	// but somehow have the original call return quickly
	// maybe only have original call set the next object to parse
	// and then calling start() will call run() and that will parse
	// the given object
	
}
