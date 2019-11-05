import java.util.ArrayList;
import java.util.HashMap;

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

	private HashMap<String,String> careerBests;
	private ArrayList<Meet> meets; 

	private String name, tfrrsURL;
	private double [] PR, seasonalPR;
	private int tfrrsValue;
	
	
	public Athlete(String name, String tfrrsURL) {
		
		this.name = name;
		this.tfrrsURL = tfrrsURL;
		this.meets = new ArrayList<>(); 
		this.careerBests = new HashMap<>();

		parseStats();
	
	}

	private void parseStats(){
		AthleteParser ap = new AthleteParser(this);
		ap.parseBests();
		ap.parseMeets();

	}

	public String getUrl(){ return tfrrsURL; }

	public void addMeet(Meet m){
		meets.add(m);
	}

	public ArrayList<Meet> getMeets(){
		return meets;
	}

	public void addPR(String event, String time){
		careerBests.put(event, time);
	}

	public void printPerformances(){
		System.out.println(name + " has raced " + meets.size() + " 8k's in his career.");
		System.out.println();

		for(Meet m: meets){
			System.out.println(m);
		}
	}

	public void printPRs(){
		System.out.println(name + " has competed in " + careerBests.size() + " different event(s) in his career.");
		System.out.println("Here are his best times in each event");
		System.out.println();

		for(String event: careerBests.keySet()){
			System.out.println(event + " - " + careerBests.get(event));
		}
	}

	public void printAthlete(){
		System.out.println(name + " - " + tfrrsURL);
	}
}
