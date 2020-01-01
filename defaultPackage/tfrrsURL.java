package defaultPackage;


// Class to figure out what type of tfrrs.org url is given
public class tfrrsURL {

	public enum TYPE {
		ATHLETE, TEAM, MEET, UNKNOWN
	}
	
	// boolean isXCMeet = url.contains("xc/");	// the way to know if the meet is an xc meet or not
	
	private TYPE urlType;
	private String rawURL;
	
	public tfrrsURL(String rawURL) {
		this.rawURL = rawURL;
		
		// figures out what type of tfrrs url this is
		if (rawURL.contains("tfrrs.org")) {
			if (rawURL.contains("athletes"))
				urlType = TYPE.ATHLETE;
			else if (rawURL.contains("results"))
				urlType = TYPE.MEET;
			else if (rawURL.contains("teams"))
				urlType = TYPE.TEAM;
			else
				urlType = TYPE.UNKNOWN;	// the catch-all
		} else
			urlType = TYPE.UNKNOWN;	// the catch-all	
	}
	
	public String getURLString() { return rawURL; }
	public TYPE getType() { return urlType; }
	// return true if a valid url
	//public boolean isValid() { return urlType != TYPE.UNKNOWN; }
}
