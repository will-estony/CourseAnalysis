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
    Document doc;
    
    private String url;

    public AthleteParser(String url){
        try {
        System.out.println("Establishing a connection to the website...");
        doc = Jsoup.connect(url).timeout(0).get();
        System.out.println("Connected");
        
        this.url = url;
        tables = doc.select("table");
        
        headers = doc.select("h1,h2,h3,h4,h5,h6");

        }catch(IOException e){
            e.printStackTrace();
        }

    }

    public void printTables1(){
        System.out.println(doc.select("table[class=table bests]"));
        System.out.println(doc.select("table").get(1).select("td").text());
    }

    public void printTables(){

        if(tables.isEmpty()){
            System.out.println("Athlete hasn't competed yet");
        }

        /*We want to skip over these when we parse an athlete.
        It seems like these are baked into every athlete (including track only) so no matter
        if for example they are a freshmen who hasn't had a track season yet,
        we can still create these tables without fear that they won't exist yet. 
        This makes our job easier.*/
        Element firstTable = doc.select("table").get(0); //overall bests
        Element secondTable = doc.select("table").get(1); //outdoor bests
        Element thirdTable = doc.select("table").get(2); //xc bests
        Element fourthTable = doc.select("table").get(3); //indoor bests

        for(Element table: tables){
            if(!table.equals(firstTable) && !table.equals(secondTable) && !table.equals(thirdTable) && !table.equals(fourthTable)){
                if(table.select("td").text().contains("8K")){
                    System.out.println(table.select("td").text());
                    System.out.println("http:" + table.select("a").attr("href"));
                    System.out.println();
                }    
            }

        }
    }

}