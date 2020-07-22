package defaultPackage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import guiPackage.StatusDisplay;

public class Meet extends Parsable {
	
	// static HashMap of all Meets in existence
	// first entry is URL, second is the Meet
	private static HashMap<Long, Meet> allMeets = new HashMap<Long, Meet>();
	
	// Attributes
	private long ID;			// the id num of the meet stats on tffrs
	private boolean isXC;		// is this an XC meet or not
	private String name;		// The name of the meet as listed on tffrs
	private Date start_date;	// the first day of the meet as a date object.
	private short length;		// the length of the meet (in days)
	private Course course;		// the course this meet was hosted on
	
    private HashMap<Long, Athlete> competitors;

    private Meet(long tfrrsID, boolean isXC, StatusDisplay statusObject) {
    	super(statusObject);
    	super.parser = new MeetParser(this);
    	this.ID = tfrrsID;
    	this.isXC = isXC;
    	competitors = new HashMap<>();
    }
    
    // static factory method pattern
    // forces meets to be created through this static factory method,
    // thus preventing duplicate meets from being created
    public static Meet createNew(String url, StatusDisplay statusObject) {

    	long id = urlToID(url);
    	
    	// tests if Meet exists already, and returns it if it does
    	Meet newMeet = allMeets.get(id);
    	if (newMeet != null)
    		return newMeet;
    	// if the meet doesn't exist yet: create it, add it to allMeets, and return it
    	newMeet = new Meet(id, url.contains("xc/"), statusObject);
    	allMeets.put(id, newMeet);
    	return newMeet;
    }
    
    public static Meet createNew(String url) {
    	return createNew(url, null);
    }
    
	// requires a valid tfrrs URL to work
	public static long urlToID(String url){ 
		
		// removes xc section
		url = url.replace("xc/", "");
		
		// this method works for both tfrrs AND directathletics links
		// looks for "results/" in url and skips to end of that
		url = url.substring(url.indexOf("results/") + 8);
		
		// counts how many remaining/sequential chars are digits
		int i = 0;
		while(i < url.length() && Character.isDigit(url.charAt(i)))
			i++;
		try {
			// returns the sequential digit chars converted to Long
			return Long.parseLong(url.substring(0,i));
		} catch (NumberFormatException e) {
			e.printStackTrace();
			// prints erroneous url for debugging help
			System.out.println("Error: " + url);
		}
		// else we return error value
		return -1;
	}
	public boolean parse() {
		if (!isParsed) {	// only parses if not already parsed
			// attempts to connect to URL
			if (!super.parse())
				return false;
	    	
			// casts parser to this specific object's parser type
			MeetParser thisParser = (MeetParser) super.parser;
			
			thisParser.setDate(this);
	        this.name = thisParser.getName();
	        
	        thisParser.parseMeet();
	        
	        // TODO: need parser to parse out course (for now use placeholder values)
	        // defaulting to XC also for now
	    	this.course = new Course("UKNOWN", "CT", "UKNOWN", Course.TYPE.XC);
	        
	        super.isParsed = true;
	    	// dereferences parser for cleanup as its not needed anymore
	    	super.parser = thisParser = null;
		}
		return true;	// if already parsed or parsed successfully
    }
    
    public void addCompetitor(Long id, Athlete a){ competitors.put(id, a); }

    /* Getters */
    public long getID() { return ID; }
    public boolean getIsXC() { return isXC; }
    public String getName() { return name; }
    public Date getStartDate() { return start_date; }
    public short getLength() { return length; }
    public Course getCourse() { return course; }
    public static HashMap<Long, Meet> getAllMeets() { return allMeets; }
    public String getURL(){
		String ret = "https://www.tfrrs.org/results/";
		if (isXC)	// if the meet is an xc meet we need to add this little piece
			ret+= "xc/";
		return ret + ID; 
	}

    // Creates and returns a results matrix for this meet
    // WARNING: parses all athletes as it builds the results matrix so might take a while
    public double [][] getResultsMatrix() {
    	// initializes return matrix
    	int numCols = 2;
    	int numRows = competitors.size();
    	double [][] returnMatrix = new double[numRows][numCols];
    	
    	// DEBUGGING
    	System.out.println("Number of competitors: " + numRows);
    	
    	// the start of the season is defined as September 1st on the given year
		Date startOfSeason = new Date((byte) 9, (byte) 1, start_date.getYear());
		Date dayBeforeThisMeet = new Date(start_date.getMonth(), (byte) (start_date.getDay() - 1), start_date.getYear());	// the day before this meet 
		// we don't need to worry about the case where the day decrements to zero
		// because we're only using this day to compare with and a day of zero doesn't mess up the comparisons
		
    	// iterates along each athlete putting their season best UP TO BUT EXCLUDING this meet
    	// and their time at this meet in the second column
    	int i = 0;
    	for (Athlete a : competitors.values()) {
    		// DEBUGGING
    		List<Performance> temp1 = a.getPerformances(this);
    		System.out.println(temp1.get(0).getMark());
    		
    		// only continues for athletes that completed this race
    		if (a.getPerformances(this).get(0).getMark() > 0) {
    			// parses athlete first to get all their times and build their performance lists
    			a.parse();

    			// we get their fastest time of the season prior to this meet
    			Performance p = a.findFastestInRange(startOfSeason, dayBeforeThisMeet);
    			// and we make sure that they did a run in the range requested
    			if (p != null) {
	    			// then we build the results matrix
		    		returnMatrix[i][0] = p.getMark();	// fastest time of the season prior to this meet
		    		// DEBUGGING
		    		List<Performance> temp2 = a.getPerformances(this);
		    		System.out.println(temp2.get(0).getMark());
		    		returnMatrix[i][1] = a.getPerformances(this).get(0).getMark();	// time at this meet
		    		
		    		//debugging
		    		System.out.println(Performance.timeDoubleToString(returnMatrix[i][0]) + 
		    				" " + Performance.timeDoubleToString(returnMatrix[i][1]));
		    		i++;
    			}
    		}
    	}
    	return returnMatrix;
    }
    
    // override super.equals to be a little more lenient
    public boolean equals(Object o) {
    	if (o instanceof Meet) {
    		Meet m = (Meet) o;
    		// if isXC and tfrrsID are the same 
    		return m.ID == ID && m.isXC == isXC;
    	} else
    		return super.equals(o);
    }
    
    // inserts this meet, all its athletes, and this meet's performances into the given database
    public void insert_into(RunningDatabase db) {
    	// parses meet if not parsed yet
		this.parse();
    	
    	// prepares to insert all athletes who competed in this meet into the athlete table
    	long[] athleteIDs = new long[competitors.size()];
    	String [] athleteNames = new String[competitors.size()];
    	int i = 0;
    	for (long athleteID : competitors.keySet()) {
    		athleteIDs[i] = athleteID;
    		athleteNames[i++] = competitors.get(athleteID).getName();
    	}
    	
    	// inserts course
    	db.insert_into_course(this.course.getName(), this.course.getState(), this.course.getCity(),
    			this.course.getType().toString());
    	
    	// inserts meet
    	db.insert_into_meet(this.ID, this.isXC, this.name, this.start_date.toString(), this.length,
    			this.course.getName(), this.course.getState(), this.course.getCity());
    	
    	// inserts all athletes into database
    	db.insert_into_athlete(athleteIDs, athleteNames);
    	
    	

    }
    
    
    
    private class MeetParser extends Parser {
        private Elements tables;
        private Elements headers;
        private Elements rows;
        private Meet meet;

        private MeetParser(Meet m) {
        	super(m);
        	this.meet = m;
        }
        
        // attempts to connect to meet page
        public boolean connect() {
        	if (super.connect()) {
        		
        		tables = doc.select("table");
                rows = doc.select("row");
                headers = doc.select("h1,h2,h3,h4,h5,h6");
        		
                return true;
        	}
        	return false;
        }
        
        public void setDate(Meet m) {
        	// makes sure we're connected before accessing webpage details
        	if (!isConnected)
        		connect();
            Elements e = doc.select("div.panel-second-title").select("div.col-lg-8").select("div.panel-heading-normal-text");
            
            //TODO: get the start date and the meet length from the date string
            // rn we are just defaulting meet length to 1 day.
            
            m.start_date = new Date(e.get(0).text());
            m.length = 1;
        }

        public String getName(){
        	// makes sure we're connected before accessing webpage details
        	if (!isConnected)
        		connect();
        	return doc.select("h3.panel-title").text();
    	}
        
        //Tables and titles combined found in div tag: col-lg-12
        //Just the titles are found in div tag: custom-table-title.custom-table-title-xc
        public void parseMeet(){

            HashMap<String, Integer> headerMap = new HashMap<>();
            ArrayList<String> raceTitles = new ArrayList<>();

            for(Element race : doc.select("div.col-lg-12")){
                String raceTitle = race.select("div.custom-table-title.custom-table-title-xc").text();
                if(raceTitle.contains("Men") || raceTitle.contains("Women")){
                    raceTitles.add(raceTitle);
                }
                
            }
            
            //PL, NAME, YEAR, TEAM, Avg. Mile, TIME, SCORE
            //There was some sort of JV/Varsity race distinction
            if(raceTitles.size() > 4){
                for(Element race : doc.select("div.col-lg-12")){
                    String raceTitle = race.select("div.custom-table-title.custom-table-title-xc").text();
                    if(raceTitle.contains("Men") && raceTitle.contains("Varsity") && raceTitle.contains("Individual")){
                    	
                    	parseRace(race, headerMap);
                    	
                    }
                }

            }else{ //There was merely a women's race and men's race, no jv or varsity
                for(Element race : doc.select("div.col-lg-12")){
                    String raceTitle = race.select("div.custom-table-title.custom-table-title-xc").text();
                    if(raceTitle.contains("Men") && raceTitle.contains("Individual")){
                        getNumCompetitors(race, headerMap);
                    	parseRace(race, headerMap);
                    	
                    }
                }
            }
		}
		
		private int getNumCompetitors(Element race, HashMap<String, Integer> headerMap) {
			int j = 0;
        	Elements headers = race.select("thead");
            int i = 0;

            for(Element head: headers.select("tr").select("th"))
                headerMap.put(head.text(), i++);

            Elements results = race.select("tbody.color-xc");

            for(Element result: results.select("tr")){

        
            	if (result.select("td").select("a").attr("href").contains("athlete")) {
					j++;
            	}
			}
			return j;
        }
        // parses given meet section (ex. JV race, varsity race, etc...)
        private void parseRace(Element race, HashMap<String, Integer> headerMap) {
			metrics.setcurrentItem(0.0);
			int numCompetitors = getNumCompetitors(race, headerMap);
			metrics.setNumItems((double)numCompetitors);
        	Elements headers = race.select("thead");
            int i = 0;

            for(Element head: headers.select("tr").select("th"))
                headerMap.put(head.text(), i++);

            Elements results = race.select("tbody.color-xc");

            for(Element result: results.select("tr")){

            	// sometimes there is no "href" attribute if the athlete does not have a tfrrs page
            	// in the case of alumni in a race this happens.
            	// also there is sometimes a "href" attribute but only for team URL,
            	// not for athlete URL.
            	// So to overcome both of these at once we check to see if the "href" attribute
            	// contains the word "athlete" (which is what all athlete URLs should)
            	
            	int place = 1;	// TEMP
            	
            	if (result.select("td").select("a").attr("href").contains("athlete")) {
	                long id = Athlete.urlToID("https:" + result.select("td").select("a").attr("href"));
					String time = result.select("td").get(headerMap.get("TIME")).text();
//					System.out.println(time);
					Athlete a = Athlete.createNew(id, statusObject);
					
					// TODO: parse out event_name, place, and athleteCredentials
					// rn makes all performances "8K", counts places manually (which might have some issues),
					// and makes athleteCredentials "First_Year" for everyone
	                Performance p = new Performance(meet, "8K",
	                		place++, Performance.timeStringToDouble(time), Performance.CREDENTIALS.FIRST_YEAR);
	                
	                a.addPerformance(p);
					meet.addCompetitor(id, a);
					metrics.setcurrentItem(metrics.getCurrentItem() + 1.0);
            	}
            }
        }
    }
}
