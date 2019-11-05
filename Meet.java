public class Meet{
    private String event; //the event the athlete competed in ex. 8k
    private String time; //the time the athlete complete the event in, in seconds
    private String date;  //the date the meet took place on ex. 1/8/19 This should be a date object. 
    private String url;   //the url to the meet stats on tffrs

    public Meet(){
        
    }

    public void setEvent(String event){ this.event = event; }
    public void setTime(String time){ this.time = time; }
    public void setDate(String date){ this.date = date; }
    public void setUrl(String url){ this.url = url; }

    public String getUrl(){ return url; }

    public String toString(){
        return event + ":" + time + " - " + date;
    }
}