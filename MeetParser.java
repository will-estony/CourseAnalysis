import org.jsoup.nodes.Element;
import org.jsoup.nodes.Document;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class MeetParser{

    private String url;
    private Elements tables;
    private Elements headers;
    private Elements rows;
    private Document doc;
    private Meet meet;

    public MeetParser(String url, Meet m){
        this.url = url;
        this.meet = m;
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
    public void parseMeet(){

        HashMap<String, Integer> headerMap = new HashMap<>();
        ArrayList<String> raceTitles = new ArrayList<>();

        for(Element race : doc.select("div.col-lg-12")){
            String raceTitle = race.select("div.custom-table-title.custom-table-title-xc").text();
            if(raceTitle.contains("Men") || raceTitle.contains("Women")){
                raceTitles.add(raceTitle);
            }
            
        }
        //PL, NAME, YEAR, TEAM, Avg. Mile, TIME, SCORE
        //There was some sort of JV/Varsity race distinction
        if(raceTitles.size() > 4){
            for(Element race : doc.select("div.col-lg-12")){
                String raceTitle = race.select("div.custom-table-title.custom-table-title-xc").text();
                if(raceTitle.contains("Men") && raceTitle.contains("Varsity") && raceTitle.contains("Individual")){
                    Elements headers = race.select("thead");
                    int i = 0;

                    for(Element head: headers.select("tr").select("th")){
                        headerMap.put(head.text(),i);
                        i++;
                    }

                    Elements results = race.select("tbody.color-xc");

                    for(Element result: results.select("tr")){
                        String name = result.select("td").get(headerMap.get("NAME")).text();
                        String url = "http:" + result.select("td").select("a").attr("href");
                        String time = result.select("td").get(headerMap.get("TIME")).text();
                        Athlete a = new Athlete(name, url);
                        Performance p = new Performance("8K", time, meet);
                        a.addPerformance(p);
                        meet.addCompetitor(a);
                    }
                }
            }

        }else{ //There was merely a women's race and men's race, no jv or varsity
        for(Element race : doc.select("div.col-lg-12")){
            String raceTitle = race.select("div.custom-table-title.custom-table-title-xc").text();
            if(raceTitle.contains("Men") && raceTitle.contains("Individual")){
                Elements headers = race.select("thead");
                int i = 0;

                for(Element head: headers.select("tr").select("th")){
                    headerMap.put(head.text(),i);
                    i++;
                }

                Elements results = race.select("tbody.color-xc");

                for(Element result: results.select("tr")){
                    String name = result.select("td").get(headerMap.get("NAME")).text();
                    String url = "http:" + result.select("td").select("a").attr("href");
                    String time = result.select("td").get(headerMap.get("TIME")).text();
                    Athlete a = new Athlete(name, url);
                    Performance p = new Performance("8K", time, meet);
                    a.addPerformance(p);
                    meet.addCompetitor(a);
                }
            }
            }
        }
    }
}
