package defaultPackage;
// abstract class for all objects that should be parsable
// ex: athletes, meets, teams.

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import guiPackage.StatusDisplay;

public abstract class Parsable {
	
	protected Metrics metrics = new Metrics();
	protected Parser parser;
	protected boolean isParsed;

	public abstract String getURL();
	
	// attempts to parse the current object and return true or false based on success of operation
	public boolean parse() {
    	// if already parsed, return false
		if (isParsed)
			return false;
		
    	// attempts to connect to team's URL
    	// if connection is unsuccessful, return false
    	if (!parser.connect())
    		return false;
    	return true;
	}
	
	public void setMetrics(Metrics m){ this.metrics = m; }
	
	protected abstract class Parser {

		protected Parsable parsingObject;
		protected StatusDisplay statusObject;
		protected String url;
		protected Document doc;
	    protected boolean isConnected = false;
		
	    // attempts to connect to URL of parsingObject
		protected boolean connect() {
			if (isConnected) {	// if already connected
				updateStatus("Already connected to " + url);
				return true;
			}
			try {	// else: attempt connection
	            this.url = parsingObject.getURL();
	            updateStatus("Attempting connection to " + url + "...");
	            
	            // attempts to connect to URL
	            doc = Jsoup.connect(this.url).timeout(0).get();
	            // connection successful
	            updateStatus("Connected.");
	            isConnected = true;
	            return true;
	        } catch(IOException e) { // unsuccessful connection
	            e.printStackTrace();
	            updateStatus("Connection unsuccessful.");
	            return false;	
	        }
		}
		
		// writes to status object if non-null, else writes to System.out
		protected void updateStatus(String newStatus) {
			if (statusObject != null)
				statusObject.writeNewLine(newStatus);
			else
				System.out.println(newStatus);
		}
	}
}
