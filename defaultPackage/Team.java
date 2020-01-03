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
    private String tfrrsURL; //The url to a teams XC tffrs page
    private String name;     //The name of an xc team

    // status object is an object that the parser can write to as its parsing
    // to display the status of the parsing without writing to System.out
    private Team(String tfrrsURL, StatusDisplay statusObject){
        teammates = new HashMap<>();
        competitors = new HashMap<>();
        meets = new ArrayList<>();
        
        
        // saves the "name" of the team as the identifying section of the url
        // - 5 at the end to get rid of ".html"
        this.name = tfrrsURL.substring(tfrrsURL.indexOf("teams/"), tfrrsURL.length() - 5);
        this.tfrrsURL = tfrrsURL;

        super.parser = new TeamParser(this, statusObject);
        

    }
    
    // static factory method pattern
    // forces teams to be created through this static factory method,
    // thus preventing duplicate teams from being created
    public static Team createNew(String url, StatusDisplay statusObject) {
    	// removes difference between http and https urls
		url = url.replace("http://","https://");
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
		// casts parser to this specific objects parser type
		TeamParser thisParser = (TeamParser) super.parser;
		// attempts to connect to URL
		if (!super.parse())
			return false;
    	
		thisParser.parseAthletes();
    	
		super.isParsed = true;
    	// dereferences parser for cleanup as its not needed anymore
        super.parser = thisParser = null;
        //System.out.println("TEAM PARSED!");
    	return true;
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
    
    // a team parser is not needed outside of the team class so this should be a private class to team
    private class TeamParser extends Parser {
        private Elements tables;
        private Elements headers;
        private Team team;
        
        public TeamParser(Team t){
        	this(t, null);
        }
        public TeamParser(Team t, StatusDisplay statusObject) {
        	this.team = t;
            this.parsingObject = team;
            this.statusObject = statusObject;
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

        public int getNumAthletes(){
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
