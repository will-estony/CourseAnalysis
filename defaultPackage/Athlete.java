package defaultPackage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import guiPackage.StatusDisplay;

public class Athlete implements Parsable {
	
	// hashmap of all athletes ever created
	// key is tfrrsID, value is Athlete
	private static HashMap<Long, Athlete> allAthletes = new HashMap<Long, Athlete>();
	
	private HashMap<String,String> careerBests;
	// performances are stored in a binary tree ordered by Date
	private TreeMap<Date, Performance> performances;
	private String name;
	private long tfrrsID;
	private AthleteParser parser;
	
	private Athlete(long tfrrsID, StatusDisplay statusObject) {
		this.tfrrsID = tfrrsID;
		this.performances = new TreeMap<Date, Performance>();
		this.careerBests = new HashMap<>();
		parser = new AthleteParser(this, statusObject);
	}
	
	// static factory method pattern
    // forces Athletes to be created through this static factory method,
    // thus preventing duplicate teams from being created
    public static Athlete createNew(long tfrrsID, StatusDisplay statusObject) {
    	// tests if Athlete exists already, and returns it if it does
    	Athlete newAthlete = allAthletes.get(tfrrsID);
    	if (newAthlete != null)
    		return newAthlete;
    	// if the Athlete doesn't exist yet: create it, add it to allAthletes, and return it
    	newAthlete = new Athlete(tfrrsID, statusObject);
    	allAthletes.put(tfrrsID, newAthlete);
    	return newAthlete;
    }
    
    public static Athlete createNew(long tfrrsID) {
    	return createNew(tfrrsID, null);
    }
	
	public boolean parse() {
    	// attempts to connect to team's URL
    	// if connection is unsuccessful, return false
    	if (!parser.connect())
    		return false;
    	
    	this.name = parser.getName();
    	parser.parseBests();
    	parser.parsePerformances();
    	// dereferences parser for cleanup as its not needed anymore
    	parser = null;
    	return true;
    }

	// Some getters //
	
	// athlete must already be parsed to have a local name variable
	public String getName() { return name; }
	public String getURL() { return idToUrl(tfrrsID); }
	public void addPerformance(Performance p) {	performances.put(p.getDate(), p); }
	public TreeMap<Date, Performance> getPerformances(){ return performances; }
	// returns a list of performances that happened at a given meet
	// runs in O(n)
	public List<Performance> getPerformances(Meet goalMeet) {
		List<Performance> retList = new ArrayList<Performance>();
		
		// searches through performances to find ones that match the given meet
		for (Performance p : performances.values()) 
			if (p.getMeet().equals(goalMeet))
				retList.add(p);
		
		if (retList.size() == 0)
			retList = null; // if no performance associated with given meet return null
		return retList;	
	}
	
	
	public static String idToUrl(long l){ return "https://xc.tfrrs.org/athletes/" + l + ".html"; }
	
	// requires a valid tfrrs athlete URL to work
	public static long urlToLong(String url){ 
		// remove https's
		url = url.replace("https://","");
		url = url.replace("http://","");
		// remove the url up to the ID num
		url = url.replace("www.tfrrs.org/athletes/","");
		url = url.replace("xc.tfrrs.org/athletes/","");
	
		// counts how many sequential chars are digits
		int i = 0;
		while(Character.isDigit(url.charAt(i))){
			i++;
		}
		try {
			// returns the sequential digit chars converted to Long
			return Long.parseLong(url.substring(0,i));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		// else we return error value
		return -1;
	}

	
	// finds and returns the fastest performance within the given date range (inclusive)
	public Performance findFastestInRange(Date start, Date end) {
		
		// gets all of an athletes performances from start to finish, inclusively, into a Map
		Map<Date, Performance> subPerformances = performances.subMap(start, true, end, true);
		
		// then we iterate along the subPerformances and find fastest
		Performance fastestInRange = null;
		for (Performance p : subPerformances.values())
			if (fastestInRange == null || fastestInRange.getTime() > p.getTime())
				fastestInRange = p;
		return fastestInRange;	// and return it
	}
	
	public void addPR(String event, String time){
		careerBests.put(event, time);
	}

	public void printPerformances(){
		System.out.println(name + " has entered " + performances.size() + " 8k's in their career.");
		System.out.println();

		// prints every performance
		for(Performance p: performances.values()){
			System.out.println(p);
		}
		System.out.println();
	}

	public void printPRs(){
		System.out.println(name + " has competed in " + careerBests.size() + " different event(s) in their career.");
		if (careerBests.size() > 0)
			System.out.println("Here are their best times in each event");
		System.out.println();

		for(String event: careerBests.keySet()){
			System.out.println(event + " - " + careerBests.get(event));
		}
	}

	
	private class AthleteParser extends Parser {
	    private Elements tables;
	    private Elements headers;
	    private Elements divs;
	    private Athlete athlete;

	    public AthleteParser(Athlete a){
	    	this(a, null);
	    }
        public AthleteParser(Athlete a, StatusDisplay statusObject) {
	        this.athlete = a;
	        this.parsingObject = athlete;
            this.statusObject = statusObject;
        }
	    
	    // attempts to connect to the athlete's URL
        public boolean connect() {
        	if (super.connect()) {
        		
        		tables = doc.select("table");
	            divs = doc.select("div#meet-results");
	            headers = doc.select("h1,h2,h3,h4,h5,h6");
        		
                return true;
        	}
        	return false;
        }
        
        
        public String getName(){
        	// makes sure we're connected before accessing webpage details
        	if (!isConnected)
        		connect();
    		return headers.get(0).text();
		}
        
	    public void parsePerformances(){

	        /*We want to skip over these when we parse an athlete.
	        It seems like these are baked into every athlete (including track only) so no matter
	        if for example they are a freshmen who hasn't had a track season yet,
	        we can still create these tables without fear that they won't exist yet. 
	        This makes our job easier.*/
	        Element firstTable = doc.select("table").get(0); //overall bests
	        Element secondTable = doc.select("table").get(1); //outdoor bests
	        Element thirdTable = doc.select("table").get(2); //indoor bests
	        Element fourthTable = doc.select("table").get(3); //xc bests
	        
	        for(Element table: divs.select("table")){
	            if(!table.equals(firstTable) && !table.equals(secondTable) && !table.equals(thirdTable) && !table.equals(fourthTable)){
	                if(table.select("tbody").text().contains("8K")){
	                    
	                    String info = table.select("tbody").text();
	                    String split[] = info.split("\\s+");
	                    
	                    // creates a new Meet passing it the tfrrs URL
	                    Meet m = Meet.createNew("https:" + table.select("a").attr("href"), statusObject);
	                    
	                    //m.parse();
	                    
	                    // creates a new performance passing it the Event, the event time (as a String), 
	                    // the meet (as a Date), and the meet it occurred at
	                    Performance p = new Performance(split[0], 
	                    		Performance.timeStringToDouble(split[1]), 
	                    		new Date(table.select("span").text()), m);

	                    // in order for each performance to have a date, our old approach
	                    // required the meet to be parsed first and then take the date from there.
	                    // But by passing the date now instead of just the meet we don't
	                    // have to parse every meet for every performance in order to get the date.
	                    //System.out.println(table.select("span").text());	// the date of the performance
	                    
	                    // adds performance to athlete
	                    athlete.addPerformance(p);
	                }    
	            }
	        }
	    }
	    

	    public void parseBests(){

	        /*
	        The basic idea here is to parse the first table on the tffrs website for a given athlete
	        every other "word" represents an event except for xc which has (XC) 
	        So we get the first tables "text", scrub out all occurances of (XC) then we populate a hash table
	        with the event as the key and the time as the value.
	        */
	        
	        Element careerBests = doc.select("table").get(0);
	        for(int i = 0; i < careerBests.select("td").size()-1; i+=2){
	            if(!careerBests.select("td").get(i).text().equals("")){
	                athlete.addPR(careerBests.select("td").get(i).text(), careerBests.select("td").get(i+1).text());
	            }
	        }
	    }
	}
}