import java.util.ArrayList;

public class Meet{

	private String date;  //the date the meet took place on ex. 1/8/19 This should be a date object. 
    private String url;   //the url to the meet stats on tffrs
    private ArrayList<Athlete> competitors;

    public Meet(String url, String date){
        this.url = url;
        this.date = date;
        competitors = new ArrayList<>();
    }

    public void addCompetitor(Athlete a){
        competitors.add(a);
    }

    public String getDate() { return date; }
    public String getUrl(){ return url; }

    // TODO: Get the year from the private date Object
    // so that we can use that year in the .getSeasonBest(int year) call down below
    
    // temp
    final static int year = 2019;
    
    
    // Creates and returns a results matrix for this meet
    public double [][] getResultsMatrix() {
    	// initializes return matrix
    	int numCols = 2;
    	int numRows = competitors.size();
    	double [][] returnMatrix = new double[numRows][numCols];
    	
    	// iterates along each athlete putting their season best in the first column
    	// and their time at this meet in the second column
    	for (int i = 0; i < numRows; i++) {
    		Athlete currCompetitor = competitors.get(i);
//    		returnMatrix[0][i] = currCompetitor.getSeasonBest(year);	// season best
    		returnMatrix[1][i] = currCompetitor.getPerformance(this).getTime();	// time at this meet
    	}
    	
    	return returnMatrix;
    }
}