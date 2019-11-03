import org.jsoup.nodes.Element;
import org.jsoup.nodes.Document;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

public class AthleteParser{

    private ArrayList<String> headings;
    private ArrayList<String> orderOfEvents;
    
    private Elements tables;
    private Elements headers;
    private Athlete a;
    Document doc;
    
    private String url;

    public AthleteParser(Athlete a){
        this.a = a;
        try {

            this.url = a.getUrl();
            System.out.println("Establishing a connection to the website...");
            doc = Jsoup.connect(this.url).timeout(0).get();
            System.out.println("Connected");
        
            
            tables = doc.select("table");
        
            headers = doc.select("h1,h2,h3,h4,h5,h6");

        }catch(IOException e){
            e.printStackTrace();
        }

    }
    public void parseMeets(){

        /*We want to skip over these when we parse an athlete.
        It seems like these are baked into every athlete (including track only) so no matter
        if for example they are a freshmen who hasn't had a track season yet,
        we can still create these tables without fear that they won't exist yet. 
        This makes our job easier.*/
        Element firstTable = doc.select("table").get(0); //overall bests
        Element secondTable = doc.select("table").get(1); //outdoor bests
        Element thirdTable = doc.select("table").get(2); //indoor bests
        Element fourthTable = doc.select("table").get(3); //xc bests

        for(Element table: tables){
            if(!table.equals(firstTable) && !table.equals(secondTable) && !table.equals(thirdTable) && !table.equals(fourthTable)){
                if(table.select("td").text().contains("8K")){
                    String info = table.select("td").text();
                    String split[] = info.split("\\s+");
                    Performance p = new Performance();
                    p.setEvent(split[0]);
                    p.setTime(split[1]);
                    p.setUrl("http:" + table.select("a").attr("href"));
                    p.setDate(table.select("span").text());
                    if(!split[1].equals("DNS") && !split[1].equals("DNF"))
                        a.addPeformance(p);
                    
                }    
            }
        }
    }

    public void parseBests(){

        /*
        The basic idea here is to parse the first table on the tffrs website for a given athlete
        every other "word" represents an event except for xc which has (XC) 
        So we get the first tables "text", scrub out all occurances of (XC) then we populate a hash table
        with the event as the key and the time as the value.
        */

        String careerBests = doc.select("table").get(0).text();

        String regex = "\\(\\d*\\.\\d+\\)|\\(\\d+\\.\\d*\\)"; //gets rid of wind if they ran <= 200 
        careerBests = careerBests.replaceAll(regex, "");
        careerBests = careerBests.replaceAll("\\(XC\\)", "");
        careerBests = careerBests.replaceAll("5 MILE", " 5MILE");
        String[] cb = careerBests.split("\\s+");

        //Skip every two strings in the array because each couple of strings represents 
        //the information necessary to populate a single PR.
        for(int i = 0; i < cb.length-1; i+=2){
            a.addPR(cb[i], cb[i+1]);
        }
    
    }

   // public String 
}