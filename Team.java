import java.util.ArrayList;

public class Team{

    private ArrayList<Athlete> athletes;
    private ArrayList<String> meetUrls; //Contains a unique list of urls to meets that have been competed in by a team
    private String tfrrsURL; //The url to a teams XC tffrs page
    private String name;     //The name of an xc team

    public Team(String name, String tfrrsURL){
        athletes = new ArrayList<>();
        meetUrls = new ArrayList<>();
        this.name = name;
        this.tfrrsURL = tfrrsURL;

        parseAthletes();
        //parseMeets();

    }

    public String getName(){ return name; }
    public String getUrl(){ return tfrrsURL; }

    public ArrayList<String> getMeetUrls(){ return meetUrls; }
    
    public void addAthlete(Athlete a){
        athletes.add(a);
    }

    private void parseAthletes(){
        TeamParser tp = new TeamParser(this);
        tp.parseAthletes();
    }

    private void parseMeets(){
        MeetParser mp;
        for(String s : meetUrls){
            //mp = new MeetParser(s);
            //mp.parseMeets();
        }

    }

    public void addMeetUrl(String m){
        if(!meetUrls.contains(m))
            meetUrls.add(m);
    }

    public void printMeets(){
        for(String m: meetUrls){
            System.out.println(m);
        }
    }

    public void printTeam(){
        for(Athlete a: athletes){
            a.printPerformances();
            System.out.println();
            a.printPRs();
            System.out.println("----------------------");
        }
    }

    
}