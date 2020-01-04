package defaultPackage;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;

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

    public HashMap<String,HashMap<String,String>> parseAllTeams(){

        HashMap<String, HashMap<String, String>> teams = new HashMap<>();
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
                        for(int i = 0; i <=1; i++){
                            for(Element data: t.select("tbody").select("tr")){
                                Elements d = data.select("td");
                                
                                //First we loop over the men's teams, then the women's
                                //d.get(0) - men's team
                                //d.get(1) - women's team
                                if(teams.containsKey(d.get(i).text())){
                                    teams.get(d.get(i).text()).put("Women's Url", d.get(i).select("a").attr("href"));
                                }else{
                                    HashMap<String, String> urls = new HashMap<>();
                                    if(i == 0){ 
                                        urls.put("Men's Url", d.get(i).select("a").attr("href"));
                                    }else{
                                        urls.put("Women's Url", d.get(i).select("a").attr("href"));
                                    }
                                    teams.put(d.get(i).text(), urls);
                                }
                            }
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