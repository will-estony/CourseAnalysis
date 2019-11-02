
public class Athlete {
	public enum Distance {
		XC,
		outdoor_1500m, outdoor_800m,
		indoor_mile, indoor_600m;
		
		public String getDistanceString() {
			switch (this) {
			case XC:
				return "XC";
			case outdoor_1500m:
				return "1500 Meters";
			case outdoor_800m:
				return "800 Meters";
			case indoor_mile:
				return "mile";
			case indoor_600m:
				return "600 Meters";
			}
			return "";
		}
	};
	public final short numDistances = 5;
	
	private String name, tfrrsURL;
	private double [] PR, seasonalPR;
	private int tfrrsValue;
	
	
	public Athlete(String name, String tfrrsURL) {
		this.name = name;
		this.tfrrsURL = tfrrsURL;
		// instantiate PR arrays
		PR = new double[numDistances];
		seasonalPR = new double[numDistances];
		
		
		updatePRs();
		
		
	}
	
	public void updatePRs() {
		String tfrrsString = webURLHandler.pageToString(tfrrsURL);
		
		// find the start and end of the "College Bests" table
		
		
		// iterate through each event and try to find the times
		for (Distance distance : Distance.values()) {
			int eventStartIndex = tfrrsString.indexOf(distance.getDistanceString());
			if (eventStartIndex != -1) {	// if .indexOf didn't return a null value
				int eventEndIndex = eventStartIndex + 1000;
				String eventTimeString = tfrrsString.substring(eventStartIndex, eventEndIndex);
				System.out.println(eventTimeString);
			} else
				System.out.println("Couldn't find event " + distance.getDistanceString());
			
			
			
		}
		
		
	}
	
}
