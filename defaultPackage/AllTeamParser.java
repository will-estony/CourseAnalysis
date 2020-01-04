package defaultPackage;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.HashSet;

public class AllTeamParser{

    private static HashMap<String,Integer> divisions;
    

    /*Represents the url template for finding the page on a given league. 
      By appending the correct league id to the end of this url + .html,
      we can get to a given leaues page. */
    private static final String leagueTemplate = "https://www.tfrrs.org/leagues/";

    public AllTeamParser(){
        divisions = new HashMap<>();
        populateDivisions();
    }

    public HashSet<String> parseAllTeams(){
        HashSet<String> teams = new HashSet<>();
        Elements tables;
        Elements headers;
        Document doc;

        for(String division: divisions.keySet()){
            Integer leagueID = divisions.get(division);
            System.out.println("Attempting to parse all the teams from " + division);
            try {	// else: attempt connection
                doc = Jsoup.connect(leagueTemplate + leagueID + ".html").get();
                tables = doc.select("table");
                for(Element t: tables){
                    Elements tableHead = t.select("thead").select("tr").select("th");
                    if(tableHead.text().equals("MEN'S TEAM WOMEN'S TEAM")){
                        for(Element data: t.select("tbody").select("tr").select("td")){
                            if(!teams.contains(data.text()))
                                teams.add(data.text());
                        }
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
                System.out.println("Connection Unsucessful");
            }
        }
        return teams;
    }
    private void populateDivisions(){
        divisions.put("NCAA Division 1", 49);
        divisions.put("NCAA Division 2", 50);
        divisions.put("NCAA Division 3", 51);
    }
}