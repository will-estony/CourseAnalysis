import org.jsoup.nodes.Element;
import org.jsoup.nodes.Document;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.ArrayList;

public class MeetParser{

    private String url;
    private Elements tables;
    private Elements headers;
    private Elements rows;
    private Document doc;

    public MeetParser(String url){
        this.url = url;
        try{
            System.out.println("Establishing a connection to the website...");
            doc = Jsoup.connect(url).timeout(0).get();
            System.out.println("Connected");
        
            
            tables = doc.select("table");
            rows = doc.select("row");
            headers = doc.select("h1,h2,h3,h4,h5,h6");

        }catch(IOException e){
            e.printStackTrace();
        }
    }

    //Tables and titles combined found in div tag: col-lg-12
    //Just the titles are found in div tag: custom-table-title.custom-table-title-xc
    public void parseRows(){
        ArrayList<String> raceTitles = new ArrayList<>();
        for(Element race : doc.select("div.col-lg-12")){
            String raceTitle = race.select("div.custom-table-title.custom-table-title-xc").text();
            if(raceTitle.contains("Men") || raceTitle.contains("Women")){
                raceTitles.add(raceTitle);
            }
            
        }
        //There was some sort of JV/Varsity race distinction
        if(raceTitles.size() > 4){
            for(Element race : doc.select("div.col-lg-12")){
                String raceTitle = race.select("div.custom-table-title.custom-table-title-xc").text();
                if(raceTitle.contains("Men") && raceTitle.contains("Varsity") && raceTitle.contains("Individual")){
                    Elements results = race.select("tbody.color-xc");
                    for(Element result: results.select("tr")){
                        System.out.print(result.select("td").get(1).text() + " - ");
                        System.out.print(result.select("td").get(5).text());
                        System.out.println();
                    }
                }
            }

        }else{ //There was merely a women's race and men's race

        }
    }
}
