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
	private ArrayList<Performance> performances;
	private HashMap<Integer, Performance> seasonBests;


	private String name, tfrrsURL;
	private double [] PR, seasonalPR;
	private int tfrrsValue;
	
	
	public Athlete(String tfrrsURL, boolean isTeammate) {
		
		this.tfrrsURL = tfrrsURL;
		this.meets = new ArrayList<>(); 
		this.performances = new ArrayList<>();
		this.careerBests = new HashMap<>();
		
		AthleteParser ap = new AthleteParser(this);
		this.name = ap.getName();

		if (isTeammate) {
			ap.parseBests();
			ap.parsePerformances();
		}
		//ap.parseSeasonBests();

	}



	public String getUrl(){ return tfrrsURL; }

	public void addPerformance(Performance p) {
		performances.add(p);
	}
	
	public void addMeet(Meet m){
		meets.add(m);
	}

	public ArrayList<Meet> getMeets(){
		return meets;
	}

	public void addPR(String event, String time){
		careerBests.put(event, time);
	}

	public void addSeasonBest(int year, Performance p){
		seasonBests.put(year, p);
	}

	public void printPerformances(){
		System.out.println(name + " has entered " + performances.size() + " 8k's in their career.");
		System.out.println();

		// prints every performance
		for(Performance p: performances){
			System.out.println(p);
		}
	}

	public void printPRs(){
		System.out.println(name + " has competed in " + careerBests.size() + " different event(s) in their career.");
		System.out.println("Here are their best times in each event");
		System.out.println();

		for(String event: careerBests.keySet()){
			System.out.println(event + " - " + careerBests.get(event));
		}
	}
}
