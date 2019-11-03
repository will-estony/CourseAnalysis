import org.jsoup.nodes.Element;
import org.jsoup.nodes.Document;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import java.io.IOException;

public class MeetParser{

    private String url;
    private Elements tables;
    private Elements headers;
    private Document doc;

    public MeetParser(String url){
        this.url = url;
        try{
            System.out.println("Establishing a connection to the website...");
            doc = Jsoup.connect(url).timeout(0).get();
            System.out.println("Connected");
        
            
            tables = doc.select("table");
        
            headers = doc.select("h1,h2,h3,h4,h5,h6");

        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void parseTables(){
        for(Element t: tables){
            System.out.println(t.select("thead").select("tr").select("th").text());
        }
    }

    public void parseHeaders(){
        for(Element h: headers){
            System.out.println(h.text());
        }
    }
}
