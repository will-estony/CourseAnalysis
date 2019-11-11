public class Meet{
	private String date;  //the date the meet took place on ex. 1/8/19 This should be a date object. 
    private String url;   //the url to the meet stats on tffrs

    public Meet(String url, String date){
        this.url = url;
        this.date = date;
    }

    public String getDate() { return date; }
    public String getUrl(){ return url; }

    
}