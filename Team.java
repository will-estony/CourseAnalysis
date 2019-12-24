import java.util.ArrayList;
import java.util.HashMap;


import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import guiPackage.MyTextBox;

public class Team implements Parsable {

    private HashMap<Long, Athlete> teammates;
    private HashMap<Long, Athlete> competitors;
    private ArrayList<Meet> meets; //Contains a unique list of meets that have been competed in by a team
    private String tfrrsURL; //The url to a teams XC tffrs page
    private String name;     //The name of an xc team
    
    // the team parser for this team
    private TeamParser parser;

    public Team(String tfrrsURL) {
    	this(tfrrsURL, null);
    }
    // status object is an object that the parser can write to as its parsing
    // to display the status of the parsing without writing to System.out
    public Team(String tfrrsURL, MyTextBox statusObject){
        teammates = new HashMap<>();
        competitors = new HashMap<>();
        meets = new ArrayList<>();
        
        
        // saves the "name" of the team as the identifying section of the url
        // - 5 at the end to get rid of ".html"
        this.name = tfrrsURL.substring(tfrrsURL.indexOf("teams/"), tfrrsURL.length() - 5);
        this.tfrrsURL = tfrrsURL;

        parser = new TeamParser(this, statusObject);
        
        parseAthletes();
        //parseMeets();

    }

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
    	// attempts to connect to team's URL
    	// if connection is unsuccessful, return false
    	if (!parser.connect())
    		return false;
    	
    	parser.parseAthletes();
    	return true;
    }
    
    private void parseAthletes(){
        TeamParser tp = new TeamParser(this);
        tp.parseAthletes();
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
            System.out.println();
            a.printPRs();
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
        public TeamParser(Team t, MyTextBox statusObject) {
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

        public void parseAthletes() {
        	// if connected: proceed
        	// else, if not connected, attempt to connect and ONLY proceed if connection successful
        	if (isConnected || connect()) {
	            for(Element table: tables){
	                //Get to the table on the page that contains the names
	                if(table.select("thead").select("tr").select("th").text().contains("NAME")){
	                    for(Element td: table.select("td")){
	                        if(!td.select("a").attr("href").equals("")){
	
	                            Long id = Athlete.urlToLong("http:" + td.select("a").attr("href"));
	
	                            Athlete a = new Athlete(id);
	                            System.out.println(a.getName());
	                            team.addTeammate(id, a);
	                            for(Performance p: a.getPerformances()){
	                                team.addMeet(p.getMeet());
	                            }
	                        }
	                    }
	                }
	            }
        	}
        }
    }
}
// ^^^ have you ever seen a larger cascade of brackets?