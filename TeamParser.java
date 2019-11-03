import org.jsoup.nodes.Element;
import org.jsoup.nodes.Document;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import java.io.IOException;

public class TeamParser{


    private Elements tables;
    private Elements headers;
    Document doc;
    private Team t;
    private String url;
    public TeamParser(Team t){

        this.t = t;
        try {

            this.url = t.getUrl();
            System.out.println("Establishing a connection to the website...");
            doc = Jsoup.connect(this.url).timeout(0).get();
            System.out.println("Connected");
        
            
            tables = doc.select("table");
        
            headers = doc.select("h1,h2,h3,h4,h5,h6");

        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void parseAthletes(){

        for(Element table: tables){
            //Get to the table on the page that contains the names
            if(table.select("thead").select("tr").select("th").text().contains("NAME")){
                for(Element td: table.select("td")){
                    if(!td.select("a").attr("href").equals("")){
                        
                        Athlete a = new Athlete(scrubName(td.text()), "http:" + td.select("a").attr("href"));
                        t.addAthlete(a);
                        for(Performance p: a.getPerformances()){
                            t.addMeet(p.getUrl());
                        }
                    }
                }
            }
        }
    }
    private String scrubName(String name){
        name = name.replace(",", "");
        String lastFirst[] = name.split("\\s");
        return lastFirst[1] + " " + lastFirst[0];
    }
}