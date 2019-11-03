import java.util.ArrayList;

public class Team{

    private ArrayList<Athlete> athletes;
    private String tfrrsURL; //The url to a teams XC tffrs page
    private String name;     //The name of an xc team
    public Team(String name, String tfrrsURL){
        athletes = new ArrayList<>();
        this.name = name;
        this.tfrrsURL = tfrrsURL;
    }

    public String getName(){ return name; }
    public String getUrl(){ return tfrrsURL; }
    
    public void addAthlete(Athlete a){
        athletes.add(a);
    }

    public void parseAthletes(){
        TeamParser tp = new TeamParser(this);
        tp.parseAthletes();
    }

    public void printTeam(){
        for(Athlete a: athletes){
            a.printAthlete();
        }
    }
}