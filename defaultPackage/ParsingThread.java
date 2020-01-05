package defaultPackage;

import guiPackage.MenuPanel;

public class ParsingThread extends Thread {
	
	private Parsable parsingObject;
	
	public ParsingThread(Parsable parsingObject) {
		this.parsingObject = parsingObject;
		MenuPanel.setLoading(true);
		this.start();
	}
	
	public void run() {
		parsingObject.parse();	// attempt to parse
		
		// when done parsing or when failed parsing
		MenuPanel.setLoading(false);
		parsingObject.metrics.setNumItems(0.0);
		parsingObject.metrics.setcurrentItem(0.0);
	}
}
