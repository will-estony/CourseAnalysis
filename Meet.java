import java.util.ArrayList;
import java.util.HashMap;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import guiPackage.MyTextBox;

public class Meet implements Parsable {
	
	// static HashMap of all Meets in existence
	private static HashMap<String, Meet> allMeets = new HashMap<String, Meet>();
	
	private String date;  //the date the meet took place on ex. 1/8/19 This should be a date object. 
    private String url;   //the url to the meet stats on tffrs
    private String name; //The name of the meet as listed on tffrs
    private HashMap<Long, Athlete> competitors;
    private int year;
    private MeetParser parser;

    private Meet(String url, MyTextBox statusObject) {
    	this.url = url;
    	competitors = new HashMap<>();
        parser = new MeetParser(this, statusObject);
    }
    
    // static factory method pattern
    // forces meets to be created through this static factory method,
    // thus preventing duplicate meets from being created
    public static Meet createMeet(String url, MyTextBox statusObject) {
    	// test if meet exists already
    	if (allMeets.containsKey(url))
    		return allMeets.get(url);
    	// if the meet doesn't exist yet: create it, add it to allMeets, and return it
    	Meet newMeet = new Meet(url, statusObject);
    	allMeets.put(url, newMeet);
    	return newMeet;
    }
    
    public static Meet createMeet(String url) {
    	return createMeet(url, null);
    }
    
    public boolean parse() {
    	// attempts to connect to meet's URL
    	// if connection is unsuccessful, return false
    	if (!parser.connect())
    		return false;
    	
    	this.date = parser.getDate();
        this.name = parser.getName();
        this.year = Integer.parseInt(date.substring(date.length() - 4, date.length()));
    	parser.parseMeet();
    	return true;
    }
    
    
    
    

    public int getYear(){ return year; }
    public void addCompetitor(Long id, Athlete a){ competitors.put(id, a); }

    public String getDate() { return date; }
    public String getURL(){ return url; }

    // Creates and returns a results matrix for this meet
    public double [][] getResultsMatrix() {
    	// initializes return matrix
    	int numCols = 2;
    	int numRows = competitors.size();
    	double [][] returnMatrix = new double[numRows][numCols];
    	
    	// DEBUGGING
    	System.out.println("Number of competitors: " + numRows);
    	
    	// iterates along each athlete putting their season best in the first column
    	// and their time at this meet in the second column
    	int i = 0;
    	for (Athlete a : competitors.values()) {
    		// if they have a completed a race for the given year AND
    		// they completed this meet's race
    		if (a.getSeasonBest(year) != null && a.getPerformances(this).get(0).getTime() > 0) {
	    		returnMatrix[i][0] = a.getSeasonBest(year).getTime();	// season best
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
        public MeetParser(Meet m, MyTextBox statusObject) {
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
        
        public String getDate(){
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
            //Meet meet;

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
                        Elements headers = race.select("thead");
                        int i = 0;

                        for(Element head: headers.select("tr").select("th")){
                            headerMap.put(head.text(),i);
                            i++;
                        }

                        Elements results = race.select("tbody.color-xc");

                        for(Element result: results.select("tr")){

                            String time = result.select("td").get(headerMap.get("TIME")).text();
                            Long id = Athlete.urlToLong("http:" + result.select("td").select("a").attr("href"));
                            Athlete a = new Athlete(id); 
                            Performance p = new Performance("8K", time, meet);
                            a.addPerformance(p);
                            meet.addCompetitor(id, a);
                        }
                    }
                }

            }else{ //There was merely a women's race and men's race, no jv or varsity
                for(Element race : doc.select("div.col-lg-12")){
                    String raceTitle = race.select("div.custom-table-title.custom-table-title-xc").text();
                    if(raceTitle.contains("Men") && raceTitle.contains("Individual")){
                        Elements headers = race.select("thead");
                        int i = 0;

                        for(Element head: headers.select("tr").select("th")){
                            headerMap.put(head.text(),i);
                            i++;
                        }

                        Elements results = race.select("tbody.color-xc");

                        for(Element result: results.select("tr")){
                            Long id = Athlete.urlToLong("http:" + result.select("td").select("a").attr("href"));
                            String time = result.select("td").get(headerMap.get("TIME")).text();
                            Athlete a = new Athlete(id);
                            Performance p = new Performance("8K", time, meet);
                            a.addPerformance(p);
                            meet.addCompetitor(id, a);
                        }
                    }
                }
            }
        }
    }
}