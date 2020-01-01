package defaultPackage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import guiPackage.StatusDisplay;

public class Meet implements Parsable {
	
	// static HashMap of all Meets in existence
	// first entry is URL, second is the Meet
	private static HashMap<String, Meet> allMeets = new HashMap<String, Meet>();
	
	private Date date;  //the date the meet took place on ex. 1/8/19 This should be a date object. 
    private String url;   //the url to the meet stats on tffrs
    private String name; //The name of the meet as listed on tffrs
    private HashMap<Long, Athlete> competitors;
    private MeetParser parser;

    private Meet(String url, StatusDisplay statusObject) {
    	this.url = url;
    	competitors = new HashMap<>();
        parser = new MeetParser(this, statusObject);
    }
    
    // static factory method pattern
    // forces meets to be created through this static factory method,
    // thus preventing duplicate meets from being created
    public static Meet createNew(String url, StatusDisplay statusObject) {
    	// removes difference between http and https urls
		url = url.replace("http://","https://");
    	// tests if Meet exists already, and returns it if it does
    	Meet newMeet = allMeets.get(url);
    	if (newMeet != null)
    		return newMeet;
    	// if the meet doesn't exist yet: create it, add it to allMeets, and return it
    	newMeet = new Meet(url, statusObject);
    	allMeets.put(url, newMeet);
    	return newMeet;
    }
    
    public static Meet createNew(String url) {
    	return createNew(url, null);
    }
    
    public boolean parse() {
    	// attempts to connect to meet's URL
    	// if connection is unsuccessful, return false
    	if (!parser.connect())
    		return false;
    	
    	this.date = new Date(parser.getDateString());
        this.name = parser.getName();
        
    	parser.parseMeet();
    	// dereferences parser for cleanup as its not needed anymore
    	parser = null;
    	return true;
    }
    
    public void addCompetitor(Long id, Athlete a){ competitors.put(id, a); }

    public Date getDate() { return date; }
    public String getName() { return name; }
    public String getURL(){ return url; }

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
		Date startOfSeason = new Date((byte) 9, (byte) 1, date.getYear());
		Date dayBeforeThisMeet = new Date(date.getMonth(), (byte) (date.getDay() - 1), date.getYear());	// the day before this meet 
		// we don't need to worry about the case where the day decrements to zero
		// because we're only using this day to compare with and a day of zero doesn't mess up the comparisons
		
    	// iterates along each athlete putting their season best UP TO BUT EXCLUDING this meet
    	// and their time at this meet in the second column
    	int i = 0;
    	for (Athlete a : competitors.values()) {
    		// DEBUGGING
    		List<Performance> temp1 = a.getPerformances(this);
    		System.out.println(temp1.get(0).getTime());
    		
    		// only continues for athletes that completed this race
    		if (a.getPerformances(this).get(0).getTime() > 0) {
    			// parses athlete first to get all their times and build their performance lists
    			a.parse();

    			// we build the results matrix
	    		returnMatrix[i][0] = a.findFastestInRange(startOfSeason, dayBeforeThisMeet).getTime();	// fastest time of the season prior to this meet
	    		// DEBUGGING
	    		List<Performance> temp2 = a.getPerformances(this);
	    		System.out.println(temp2.get(0).getTime());
	    		returnMatrix[i][1] = a.getPerformances(this).get(0).getTime();	// time at this meet
	    		
	    		//debugging
	    		System.out.println(Performance.timeDoubleToString(returnMatrix[i][0]) + 
	    				" " + Performance.timeDoubleToString(returnMatrix[i][1]));
	    		i++;
    		}
    	}
    	return returnMatrix;
    }
    
    
    private class MeetParser extends Parser {
        private Elements tables;
        private Elements headers;
        private Elements rows;
        private Meet meet;

        public MeetParser(Meet m){
        	this(m, null);
        }
        public MeetParser(Meet m, StatusDisplay statusObject) {
        	this.meet = m;
        	this.parsingObject = meet;
        	this.statusObject = statusObject; 
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
        
        public String getDateString(){
        	// makes sure we're connected before accessing webpage details
        	if (!isConnected)
        		connect();
            Elements e = doc.select("div.panel-second-title").select("div.col-lg-8").select("div.panel-heading-normal-text");
            return e.get(0).text();
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
                       
                    	parseRace(race, headerMap);
                    	
                    }
                }
            }
        }
        // parses given meet section (ex. JV race, varsity race, etc...)
        private void parseRace(Element race, HashMap<String, Integer> headerMap) {

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
            	if (result.select("td").select("a").attr("href").contains("athlete")) {
	                long id = Athlete.urlToLong("https:" + result.select("td").select("a").attr("href"));
	                String time = result.select("td").get(headerMap.get("TIME")).text();
	                Athlete a = Athlete.createNew(id, statusObject);
	                Performance p = new Performance("8K", 
	                		Performance.timeStringToDouble(time), meet.getDate(), meet);
	                a.addPerformance(p);
	                meet.addCompetitor(id, a);
            	}
            }
        }
    }
}