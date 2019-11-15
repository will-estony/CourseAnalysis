import java.util.ArrayList;
import java.util.HashMap;

public class Athlete {
	
	private HashMap<String,String> careerBests;
	private ArrayList<Performance> performances;
	private HashMap<Integer, Performance> seasonBests;
	private String name;
	private long tfrrsID;
	
	public Athlete(long tffrsID, boolean isTeammate) {
		
		this.tfrrsID = tffrsID;
		this.performances = new ArrayList<>();
		this.careerBests = new HashMap<>();
		this.seasonBests = new HashMap<>();
		AthleteParser ap = new AthleteParser(this);
		this.name = ap.getName();

		if (isTeammate) {
			ap.parseBests();
		}
		ap.parsePerformances();

	}

	public static String idToUrl(long l){ return "https://xc.tfrrs.org/athletes/" + l + ".html"; }
	public String getUrl() { return "https://xc.tfrrs.org/athletes/" + tfrrsID + ".html"; }
	
	public static long urlToLong(String url){ 
		// remove https's
		url = url.replace("https://","");
		url = url.replace("http://","");
		// remove the url up to the ID num
		url = url.replace("www.tfrrs.org/athletes/","");
		url = url.replace("xc.tfrrs.org/athletes/","");
	
		int i = 0;
		while(Character.isDigit(url.charAt(i))){
			i++;
		}
		return Long.parseLong(url.substring(0,i));
	}

	public ArrayList<Performance> getPerformances(){ return performances; }

	public void addPerformance(Performance p) {
		performances.add(p);
	}
	
	// returns the performance that occurred at the given meet
	public Performance getPerformance(Meet goalMeet) {
		// searches through performances to find the given meet
		for (Performance p : performances) 
			if (p.equals(goalMeet))
				return p;
		return null;	// if no performance associated with given meet return null
	}

	public void addPR(String event, String time){
		careerBests.put(event, time);
	}

	public void addSeasonBest(int year, Performance p){
		if(!seasonBests.containsKey(year)){		
			seasonBests.put(year, p);
		}else if(seasonBests.get(year).getTime() > p.getTime()){
			seasonBests.put(year, p);
		}
	}
	
	// returns season best Performance for a given year
	public Performance getSeasonBest(int year) {
		return seasonBests.get(year);
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

	public void printSeasonBests(){
		for(int i : seasonBests.keySet()){
			System.out.println(i + " - " + seasonBests.get(i));
		}
	}
}
