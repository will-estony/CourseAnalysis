package defaultPackage;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;

public class AllTeamParser{

    private static HashMap<String,Integer> divisions;
    HashMap<String, HashMap<String, String>> teams;

    /*Represents the url template for finding the page on a given league. 
      By appending the correct league id to the end of this url + .html,
      we can get to a given leaues page. */
    private static final String leagueTemplate = "https://www.tfrrs.org/leagues/";

    public AllTeamParser(){
        teams = new HashMap<>();
        divisions = new HashMap<>();
        populateDivisions();
    }

    public HashMap<String,HashMap<String,String>> parseAllTeams(){

        
        Elements tables;
        Elements headers;
        Document doc;

        for(String division: divisions.keySet()){
            Integer leagueID = divisions.get(division);
            System.out.println("Attempting to collect all the teams from " + division);
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
                                String url = d.get(i).select("a").attr("href");

                                if(!url.contains("/xc/") && !url.isEmpty()){
                                    url = convertToXcUrl(url);
                                }

                                if(teams.containsKey(d.get(i).text())){
                                    teams.get(d.get(i).text()).put("Women's Url", url);
                                }else{
                                    HashMap<String, String> urls = new HashMap<>();
                                    if(i == 0){ 
                                        urls.put("Men's Url", url);
                                    }else{
                                        urls.put("Women's Url", url);
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
            System.out.println("Successfully collected all teams from " + division);
            System.out.println();
        }
        return teams;
    }

    private String convertToXcUrl(String trackUrl){
        return "https:" + trackUrl.substring(0,22) + "xc/" + trackUrl.substring(22);
    }

    public void printTeams(){
        for(String team: teams.keySet()){
            System.out.println(team + ":");
            System.out.println("    " + teams.get(team).get("Men's Url"));
            System.out.println("    " + teams.get(team).get("Women's Url"));
        }
    }

    private void populateDivisions(){
        divisions.put("NCAA Division 1", 49);
        divisions.put("NCAA Division 2", 50);
        divisions.put("NCAA Division 3", 51);
    }
}