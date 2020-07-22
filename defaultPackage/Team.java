package defaultPackage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import guiPackage.StatusDisplay;

public class Team extends Parsable {

	// static HashMap of all Teams in existence
	// first entry is URL, second is the Team
	private static HashMap<String, Team> allTeams = new HashMap<String, Team>();
	
    private HashMap<Long, Athlete> teammates;
    private HashMap<Long, Athlete> competitors;
    private ArrayList<Meet> meets; //Contains a unique list of meets that have been competed in by a team
    private String tfrrsURL; //The url to a team's tffrs page
    private String name;     //The name of the team

    // status object is an object that the parser can write to as its parsing
    // to display the status of the parsing without writing to System.out
    private Team(String tfrrsURL, StatusDisplay statusObject){
    	super(statusObject);
    	super.parser = new TeamParser(this);
        teammates = new HashMap<>();
        competitors = new HashMap<>();
        meets = new ArrayList<>();
        
        // saves the "name" of the team as the identifying section of the url
        // removes everything before the first occurance of "teams/" (aka the www. stuff)
        this.name = tfrrsURL.substring(tfrrsURL.indexOf("teams/") + 6);
        this.tfrrsURL = tfrrsURL;
    }
    
    // static factory method pattern
    // forces teams to be created through this static factory method,
    // thus preventing duplicate teams from being created
    public static Team createNew(String url, StatusDisplay statusObject) {
    	// removes difference between http and https urls
		url = url.replace("http://","https://");
        url = url.replace(".html", "");	// gets rid of the .html if present
    	// tests if Team exists already, and returns it if it does
    	Team newTeam = allTeams.get(url);
        if (newTeam != null){
            System.out.println("Team has already been created.");
            return newTeam;
        }
    	// if the team doesn't exist yet: create it, add it to allTeams, and return it
    	newTeam = new Team(url, statusObject);
    	allTeams.put(url, newTeam);
    	return newTeam;
    }
    
    public static Team createNew(String url) {
    	return createNew(url, null);
    }
    public Metrics getMetrics(){  return this.metrics; }
    public String getName(){ return name; }
    public String getURL(){ return tfrrsURL; }
    public ArrayList<Meet> getMeets(){ return meets; }
    
    public void addTeammate(long id, Athlete a){
        teammates.put(id, a);
    }
    
    public ArrayList<Athlete> getTeammates() {
    	return new ArrayList<Athlete>(teammates.values());
    }

    public void addCompetitor(long id, Athlete a){
        if(!competitors.containsKey(id)){
            competitors.put(id, a);
        }
    }
    public boolean parse() {
		if (!isParsed) {	// only parses if not already parsed
			// attempts to connect to URL
			if (!super.parse())
				return false;
			
			// casts parser to this specific object's parser type
			TeamParser thisParser = (TeamParser) super.parser;
	    	
			// parser needs to get team "name" also
			thisParser.parseAthletes();
	    	
			super.isParsed = true;
	    	// dereferences parser for cleanup as its not needed anymore
	        super.parser = thisParser = null;
		}
		return true;	// if already parsed or parsed successfully
    }

    // untested method
    private void parseMeets(){
        for(Meet m : meets){
            m.parse();
        }
    }

    public void addMeet(Meet m){
        if(!meets.contains(m))
            meets.add(m);
    }

    public void printMeets(){
        for(Meet m: meets){
            System.out.println(m);
        }
    }

    public void printTeam(){
        for(Athlete a: teammates.values()){
            a.printPerformances();
            System.out.println("----------------------");
        }
    }
    
    public String toString() { return name; }
    
    
    // inserts this team and all its athletes into the given database
    public void insert_into(RunningDatabase db) {
    	// parses team if not parsed yet
    	this.parse();
    	
    	// prepares to insert all athletes who are part of this team into the athlete table
    	long[] athleteIDs = new long[teammates.size()];
    	String [] athleteNames = new String[teammates.size()];
    	String [] organizationNames = new String[teammates.size()];	// an array of all the same value to make inserting later easier
    	int i = 0;
    	for (long athleteID : teammates.keySet()) {
    		organizationNames[i] = this.name;
    		athleteIDs[i] = athleteID;
    		athleteNames[i++] = teammates.get(athleteID).getName();
    	}
    	
    	// inserts all athletes into database
    	db.insert_into_athlete(athleteIDs, athleteNames);
    	
    	// inserts team into database
    	// TODO: get the state and division from parsing, rn state is null and division is D3
    	db.insert_into_organization(name, null, "Division III");
    	
    	// tells database all of the athletes are associated with this team
    	db.insert_into_athlete_competes_for(athleteIDs, organizationNames);
    	
    }
    
    // a team parser is not needed outside of the team class so this is a private class to team
    private class TeamParser extends Parser {
        private Elements tables;
        private Elements headers;
        private Team team;
        
        private TeamParser(Team t) {
        	super(t);
        	this.team = t;
        }
        
        // attempts to connect to the team's URL and save tables and headers
        public boolean connect() {
        	if (super.connect()) {
        		
        		tables = doc.select("table");
                headers = doc.select("h1,h2,h3,h4,h5,h6");
        		
                return true;
        	}
        	return false;
        }

        private int getNumAthletes(){
            int numAthletes = 0;
            for(Element table: tables){
                //Get to the table on the page that contains the names
                if(table.select("thead").select("tr").select("th").text().contains("NAME")){
                    for(Element td: table.select("td")){
                        if(!td.select("a").attr("href").equals("")){
                            numAthletes++;
                        }
                    }
                }
            }
            return numAthletes;
        }

        public void parseAthletes() {
            metrics.setcurrentItem(1.0);
            int numItems = getNumAthletes();
            System.out.println("There are " + numItems + " athletes on this team.");
            metrics.setNumItems((double)numItems);
            int numAthletes = getNumAthletes();
            for(Element table: tables){
                //Get to the table on the page that contains the names
                if(table.select("thead").select("tr").select("th").text().contains("NAME")){
                    for(Element td: table.select("td")){
                        if(!td.select("a").attr("href").equals("")){
                            
                            Long id = Athlete.urlToID("https:" + td.select("a").attr("href"));

                            Athlete a = Athlete.createNew(id, statusObject);
                            a.parse();
                            System.out.println(a.getName());
                            team.addTeammate(id, a);
                            // iterate along all performance lists for the current athlete
                            for(List<Performance> perfList : a.getPerformances().values()){
                            	// adds the meet that the current list of performances occurred at
                                team.addMeet(perfList.get(0).getMeet());
                            }

                            metrics.setcurrentItem(metrics.getCurrentItem() + 1.0);
                           
                        }
                    }
                }
            }
        }
    }
}
