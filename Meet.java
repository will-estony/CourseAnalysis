import java.util.HashMap;

public class Meet{
	
	// static HashMap of all Meets in existence
	private static HashMap<String, Meet> allMeets = new HashMap<String, Meet>();
	
	private String date;  //the date the meet took place on ex. 1/8/19 This should be a date object. 
    private String url;   //the url to the meet stats on tffrs
    private HashMap<Long, Athlete> competitors;
    
    private int year;

    private Meet(String url, String date){
        this.url = url;
        this.date = date;
        competitors = new HashMap<>();
        this.year = Integer.parseInt(date.substring(date.length() - 4, date.length()));
    }
    
    // static factory method pattern
    // forces meets to be created through this static factory method,
    // thus preventing duplicate meets from being created
    public static Meet createMeet(String url, String date) {
    	// test if meet exists already
    	if (allMeets.containsKey(url))
    		return allMeets.get(url);
    	// if the meet doesnt exist yet: create it, add it to allMeets, and return it
    	Meet newMeet = new Meet(url, date);
    	allMeets.put(url, newMeet);
    	return newMeet;
    }

    public int getYear(){ return year; }
    public void addCompetitor(Long id, Athlete a){ competitors.put(id, a); }

    public String getDate() { return date; }
    public String getUrl(){ return url; }

    // TODO: Get the year from the private date Object
    // so that we can use that year in the .getSeasonBest(int year) call down below
    
    // Creates and returns a results matrix for this meet
    public double [][] getResultsMatrix() {
    	// initializes return matrix
    	int numCols = 2;
    	int numRows = competitors.size();
    	double [][] returnMatrix = new double[numRows][numCols];
    	
    	// DEBUGGING
    	System.out.println("Number of competitors: " + numRows);
    	
    	// iterates along each athlete putting their season best in the first column
    	// and their time at this meet in the second column
    	int i = 0;
    	for (Athlete a : competitors.values()) {
    		// debugging
    		System.out.println(a.getSeasonBest(year).getTime());	// season best
    		System.out.println(a.getPerformance(this).getTime());	// time at this meet)
    		
    		returnMatrix[i][0] = a.getSeasonBest(year).getTime();	// season best
    		returnMatrix[i][1] = a.getPerformance(this).getTime();	// time at this meet
    		//debugging
    		System.out.println(Performance.timeDoubleToString(returnMatrix[i][0]) + 
    				" " + Performance.timeDoubleToString(returnMatrix[i][1]));
    		i++;
    	}
    	return returnMatrix;
    }
}