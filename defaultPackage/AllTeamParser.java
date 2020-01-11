package defaultPackage;

/* This class utilizes the external library JSoup to parse through various tfrrs webpages.
 * For JSoup documentation see: https://jsoup.org/apidocs/overview-summary.html
 */
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

//Data Structures used in this class.
import java.util.HashMap;
import java.util.HashSet;

/*
 * The purpose of this class is to gather all of the NCAA cross country teams from tffrs.org
 * and store them in a HashMap called teams. The key of teams is a String representing the team name,
 * the value of teams is another HashMap that contains two String keys Men's Url and Women's Url
 * their values are the urls that point the team pages on tfrrs.org for the men's and women's 
 * cross country teams respectively. If there exists a women's team and not a men's team for a school, 
 * the value stored for the key Men's Url is a blank String ""
 * 
 * For a team's url to be valid it must point to a page that contains at least one athlete
 * and a meet from 2019 within the range of August to November (a cross country meet)
 * 
 */
public class AllTeamParser{

    /* 
     * Stores the integer values that point to a given NCAA Division page on tfrrs.org
     * The idea being that if we parse over each NCAA division we will effectively 
     * parse over NCAA Cross Country team.
     */
    private static HashMap<String,Integer> divisions; 

    /*
     * When attempting to parse all of the urls in the three NCAA divisions
     * there were 6 urls that were invalid. This HashSet will hold those urls as Strings.
     */ 
    private static HashSet<String> invalidUrls;

    /*
     * Represents the url template for finding the page on a given league. 
     * By appending the correct league ID to the end of this url + .html,
     * we can get to a given leaues page. 
     */
    private static final String leagueTemplate = "https://www.tfrrs.org/leagues/";

    /*
     * 
     */
    private HashMap<String, HashMap<String, String>> teams;

    public AllTeamParser(){
        teams = new HashMap<>();
        invalidUrls = new HashSet<>();
        divisions = new HashMap<>();
        populateDivisions();
        populateInvalidUrls();
        populateTeams();
    }

    public HashMap<String, HashMap<String, String>> getTeams(){ return teams;}

    public void populateTeams(){

        Elements tables;
        Document doc;

        //for testing purposes only
         

        //for(String division: divisions.keySet()){
            //Integer leagueID = divisions.get(division);
            //System.out.println("Attempting to collect all the teams from " + division);
            try {	// else: attempt connection
                //doc = Jsoup.connect(leagueTemplate + leagueID + ".html").get();
                doc = Jsoup.connect("https://www.tfrrs.org/teams/xc/TX_college_f_Howard_Payne.html").get();
                tables = doc.select("table");
                hasValidMeet(tables);
                /*for(Element t: tables){
                    Elements tableHead = t.select("thead").select("tr").select("th");
                    if(tableHead.text().equals("MEN'S TEAM WOMEN'S TEAM")){
                        for(int i = 0; i <=1; i++){ //this 2-time loop is for men's and women's
                            for(Element data: t.select("tbody").select("tr")){
                                Elements d = data.select("td");
                                
                                //First we loop over the men's teams, then the women's
                                //d.get(0) - men's team
                                //d.get(1) - women's team
                                String url = d.get(i).select("a").attr("href");
                                    
                                    if(!url.isEmpty()){
                                        url = cleanUrl(url);
                                    }
                                    if(isValidTeam(url)){
                                        if(teams.containsKey(d.get(i).text())){
                                            teams.get(d.get(i).text()).put("Women's Url", url);
                                        }else if(!d.get(i).text().equals("")){
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
                }*/
            }   catch(Exception e){
                e.printStackTrace();
                System.out.println("Connection Unsucessful");
            }
            //System.out.println("Successfully collected all teams from " + division);
            //System.out.println();
        }
    //}

    /*
     * 
     */ 
    private boolean isValidTeam(String url){
        
        if(invalidUrls.contains(url) || url.isEmpty()) return false; 
        
        try{
            Document doc = Jsoup.connect(url).get();
            Elements tables = doc.select("table");
            int numAthletes = getNumAthletes(tables);

        
            if(numAthletes == 0) return false;
            
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Invalid Url: " + url);
        }
        
        return true;
    }
    private int getNumAthletes(Elements tables){
        int numAthletes = 0;
        for(Element table: tables){
            //Count the number of athletes
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

    /* 
     * Method loops through all the meets on a given teams url. Checks to see if it contains at least
     * one valid meet entry.
     *
     * @return true if the url contains a Meet entry within the range of August 2019 - November 2019
     * false otherwise.
     */
    private boolean hasValidMeet(Elements tables){
        int numMeets = 0;
        Elements meetDates = null;
        for(Element table: tables){
            if(table.select("thead").select("tr").select("th").text().contains("DATE")){
                meetDates = table.select("tbody").select("tr");
                for(Element td: table.select("td")){
                    if(!td.select("a").attr("href").equals("")){
                        numMeets++;
                    }
                }
            }
        }
        System.out.println("There are " + numMeets + " meets on this page.");
        if(numMeets == 0){
            return false;
        }else{
            for(Element data: meetDates){
                Elements d = data.select("td");
                System.out.println(d.get(0));
            }
        }
        

        return true;
    }

    /*
     * This method cleans the urls pulled from the NCAA Division pages by appending https to the beginning 
     * of the url and if the string does not contain the cross country indicator "/xc/" it also appends
     * this String to the middle of the url. 
     * 
     * @param url - a String that represents an unformatted url to a tfrss webpage that needs to be "cleaned"
     * @return a String representing the cleaned url 
     */
    private String cleanUrl(String url){
        if(!url.contains("/xc/")){
            return "https:" + url.substring(0,22) + "xc/" + url.substring(22);
        }else{
            return "https:" + url;
        }
    }

    /*
     * Simple private method that hard codes the six invalid team urls that get pulled 
     * down from tfrrs.org into a HashSet.
     * 
     * @return void
     * 
     */
    private void populateInvalidUrls(){

        invalidUrls.add("https://www.tfrrs.org/teams/xc/MN_college_f_Minnesota-Crookston.html");
        invalidUrls.add("https://www.tfrrs.org/teams/xc/MN_college_m_Minnesota-Crookston.html");
        invalidUrls.add("https://www.tfrrs.org/teams/xc/NJ_college_f_St_Elizabeth.html");
        invalidUrls.add("https://www.tfrrs.org/teams/xc/NJ_college_m_St_Elizabeth.html");
        invalidUrls.add("https://www.tfrrs.org/teams/xc/OH_college_f_Kent_State-Trumbull.html");
        invalidUrls.add("https://www.tfrrs.org/teams/xc/OH_college_m_Kent_State-Trumbull.html");
    }

    /*
     * Simple method to print the contents of the HashMap teams.
     * 
     * @return void
     */ 
    public void printTeams(){
        for(String team: teams.keySet()){
            System.out.println(team + ":");
            System.out.println("    " + teams.get(team).get("Men's Url"));
            System.out.println("    " + teams.get(team).get("Women's Url"));
        }
    }

    /*
     * Simple private method to populate the HashMap divisions with each NCAA division as a String as the key
     * and the corresponding integer that represents the url id of the division.
     * 
     */
    private void populateDivisions(){
        divisions.put("NCAA Division 1", 49);
        divisions.put("NCAA Division 2", 50);
        divisions.put("NCAA Division 3", 51);
    }
}