package defaultPackage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import guiPackage.StatusDisplay;

public class Athlete extends Parsable {
	
	// hashmap of all athletes ever created
	// key is tfrrsID, value is Athlete
	private static HashMap<Long, Athlete> allAthletes = new HashMap<Long, Athlete>();
	
	private HashMap<String,String> careerBests;
	// performances are stored in a binary tree ordered by Date
	private TreeMap<Date, List<Performance>> performances;
	private String name;
	private long tfrrsID;
	
	private Athlete(long tfrrsID, StatusDisplay statusObject) {
		super(statusObject);
		super.parser = new AthleteParser(this);
		this.tfrrsID = tfrrsID;
		this.performances = new TreeMap<Date, List<Performance>>();
		this.careerBests = new HashMap<>();
	}
	
	// static factory method pattern
    // forces Athletes to be created through this static factory method,
    // thus preventing duplicate athletes from being created
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
		if (!isParsed) {	// only parses if not already parsed
			// attempts to connect to URL
			if (!super.parse())
				return false;
	    	
			// casts parser to this specific object's parser type
			AthleteParser thisParser = (AthleteParser) super.parser;
			
	    	this.name = thisParser.getName();
	    	thisParser.parseBests();
			thisParser.parsePerformances();
	    	
	    	super.isParsed = true;
	    	// dereferences parser for cleanup as its not needed anymore
			super.parser = thisParser = null;
		}
		return true;	// if already parsed or parsed successfully
    }
	
	// Some getters //
	// athlete must already be parsed to have a local name variable
	public String getName() { return name; }
	public String getURL() { return idToUrl(tfrrsID); }
	public TreeMap<Date, List<Performance>> getPerformances(){ return performances; }
	public static HashMap<Long, Athlete> getAllAthletes() { return allAthletes; }
	// returns a list of performances that happened at a given meet
	// runs in O(log(n))
	public List<Performance> getPerformances(Meet goalMeet) {
		// gets the list of performances that happened on the same date
		List<Performance> retList = performances.get(goalMeet.getStartDate());
		// checks if the meets are actually the same
		if (retList.get(0).getMeet().equals(goalMeet))
			return retList;
		return null;	// if this athlete didn't perform at this meet then return null
	}
	
	// adds performance to collection
	public void addPerformance(Performance p) {
		// if there is already a performance for this date, and they're not the same performance, then append it to list
		if (performances.containsKey(p.getMeet().getStartDate()) && !performances.get(p.getMeet().getStartDate()).contains(p))
			performances.get(p.getMeet().getStartDate()).add(p);
		else {	// else create a new list containing the performance and add that
			List<Performance> newList = new ArrayList<Performance>();
			newList.add(p);
			performances.put(p.getMeet().getStartDate(), newList);
		}
	}
	
	public static String idToUrl(long l){ return "https://www.tfrrs.org/athletes/" + l; }
	
	// requires a valid tfrrs athlete URL to work
	public static long urlToID(String url){ 
		// remove https's
		url = url.replace("https://","");
		url = url.replace("http://","");
		// remove the url up to the ID num
		url = url.replace("www.tfrrs.org/athletes/","");
		url = url.replace("xc.tfrrs.org/athletes/","");
	
		// counts how many remaining sequential chars are digits
		int i = 0;
		while(i < url.length() && Character.isDigit(url.charAt(i))){
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
		
		// gets all of an athlete's performances from start to finish, inclusively, into a Map
		Map<Date, List<Performance>> subPerformances = performances.subMap(start, true, end, true);
		
		// then we iterate along the subPerformances and find fastest valid performance
		Performance fastestInRange = null;
		for (List<Performance> perfList : subPerformances.values())
			for (Performance p : perfList)
				if (fastestInRange == null || (p.getMark() > 0 && fastestInRange.getMark() > p.getMark()))
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
		for (List<Performance> perfList : performances.values())
			for (Performance p: perfList)
				System.out.println(p);
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
	
	public Metrics getMetrics(){  return this.metrics; }
	
	// inserts this athlete and all their performances into the given database
    public void insert_into(RunningDatabase db) {
    	// parses this athlete (if not parsed yet)
		this.parse();
		
//    	// parses all meets this athlete competed in (if they aren't already parsed)
//    	for (List<Performance> performanceList : performances.values()) {
//    		performanceList.get(0).getMeet().parse();
//    	}
    	
    	// prepares to insert all performances
		// counts number of performances
		int numPerformances = 0;
		for (List<Performance> performanceList : performances.values()) {
    		numPerformances += performanceList.size();
    	}
		// for inserting meets
    	long[] meetIDs = new long[numPerformances];
    	boolean [] isXC_arr = new boolean[numPerformances];
    	String [] meetNames = new String[numPerformances];
    	String [] meetStartDates = new String[numPerformances];
    	short [] meetLengths = new short[numPerformances];
    	String [] courseNames = new String[numPerformances];
    	String [] courseStates = new String[numPerformances];
    	String [] courseCities = new String[numPerformances];
    	String [] courseTypes = new String[numPerformances];
    	
    	// for inserting performances
    	String [] eventNames = new String[numPerformances];
    	int [] places = new int[numPerformances];
    	long [] athleteIDs = new long[numPerformances];
    	Performance.CREDENTIALS [] athleteCredentials = new Performance.CREDENTIALS[numPerformances];
    	double [] marks = new double[numPerformances];
    	int i = 0;
    	// iterate along all performance lists
    	for (List<Performance> performanceLists : performances.values()) {
    		Meet m = performanceLists.get(0).getMeet();	// meet is the same within a performance list
    		Course c = m.getCourse();					// same with course
    		// iterate along each performance list
    		for (Performance p : performanceLists) {
    			// meet stuff
    			meetIDs[i] = m.getID();
    			isXC_arr[i] = m.getIsXC();
    			meetNames[i] = m.getName();
    			meetStartDates[i] = m.getStartDate().toString();
    			meetLengths[i] = m.getLength();
    			courseNames[i] = c.getName();
    			courseStates[i] = c.getState();
    			courseCities[i] = c.getCity();
    			courseTypes[i] = c.getType().toString();
    			
    			// performance stuff
    			eventNames[i] = p.getEvent();
    			places[i] = p.getPlace();
    			athleteIDs[i] = this.tfrrsID;
    			athleteCredentials[i] = p.getAthleteCredentials();
    			marks[i++] = p.getMark();
    		}
    	}

    	// inserts this athlete into database
    	db.insert_into_athlete(this.tfrrsID, this.name);
    	
    	// inserts all courses into database
    	db.insert_into_course(courseNames, courseStates, courseCities, courseTypes);
    	
    	// inserts all meets into database
    	db.insert_into_meet(meetIDs, isXC_arr, meetNames, meetStartDates, meetLengths, courseNames, courseStates, courseCities);
    	
    	// inserts all the performances this athlete has participated in into database
    	db.insert_into_performance(meetIDs, eventNames, places, athleteIDs, athleteCredentials, marks);

    }

	private class AthleteParser extends Parser {
	    private Elements tables;
	    private Elements headers;
	    private Elements divs;
		private Athlete athlete;

        private AthleteParser(Athlete a) {
        	super(a);
	        this.athlete = a;
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
		
		public int getNumPerformances(){
			int numMeets = 0;
			Element firstTable = doc.select("table").get(0); //overall bests
	        Element secondTable = doc.select("table").get(1); //outdoor bests
	        Element thirdTable = doc.select("table").get(2); //indoor bests
	        Element fourthTable = doc.select("table").get(3); //xc bests
			for(Element table: divs.select("table")){
				if(!table.equals(firstTable) && !table.equals(secondTable) && !table.equals(thirdTable) && !table.equals(fourthTable)){
	                if(table.select("tbody").text().contains("8K") || table.select("tbody").text().contains("5M")){
						numMeets++;
					}
				}
			}
			return numMeets;
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
			metrics.setcurrentItem(1.0);
			int numPerformances = getNumPerformances();
			metrics.setNumItems((double)numPerformances);
	        for(Element table: divs.select("table")){
	            if(!table.equals(firstTable) && !table.equals(secondTable) && !table.equals(thirdTable) && !table.equals(fourthTable)){
	                if(table.select("tbody").text().contains("8K") || table.select("tbody").text().contains("5M")){
						
						
	                    String info = table.select("tbody").text();
	                    String split[] = info.split("\\s+");
	                    
	                    // creates a new Meet passing it the tfrrs URL
	                    Meet m = Meet.createNew("https:" + table.select("a").attr("href"), statusObject);
	                    
	                    m.parse();
	                    
	                    
	                    // TODO: need to parse out place and atheteCredentials
	                    int place = 1;
	                    Performance.CREDENTIALS athleteCredentials = Performance.CREDENTIALS.FIRST_YEAR;
	                    
	                    // creates a new performance passing it the Event, the event mark (as a double), 
	                    // the meet (as a Date), and the meet it occurred at
	                    Performance p = new Performance(m, split[0], place, Performance.timeStringToDouble(split[1]), athleteCredentials);


	                    // in order for each performance to have a date, our old approach
	                    // required the meet to be parsed first and then take the date from there.
	                    // But by passing the date now instead of just the meet we don't
	                    // have to parse every meet for every performance in order to get the date.
	                   
	                    
	                    // adds performance to athlete
						athlete.addPerformance(p);
						metrics.setcurrentItem(metrics.getCurrentItem() + 1.0);
						
						
					
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
