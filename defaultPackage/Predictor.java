package defaultPackage;
/* Used to make predictions for courses/athletes/team placings/etc
 * 
 */

import java.util.Collection;
import java.util.HashMap;
import org.apache.commons.math3.stat.regression.SimpleRegression;


public class Predictor {
	// private class to hold calculated meet data so we don't have
	// to do it twice if we want to run something again for the same meet
	private class MeetData {
		private Meet meet;	// saves reference to meet
		private double slope;	// saves calculated slop
		public MeetData(Meet m) {
			this.meet = m;
			// performs linear Least Squares for meets results matrix
			SimpleRegression sr = new SimpleRegression(false);
			sr.addData(meet.getResultsMatrix());	// feeds the regression
			slope = sr.getSlope();	// saves the slope
		}
		// uses the calculated slope to translate seasonBest to time at this meet
		public double estimatePerformance(double seasonBest) {
			return seasonBest*slope;
		}
	}
	
	// collection of meets we've already calculated stuff for
	private HashMap<Meet, MeetData> calculatedMeets;
	
	// Constructor
	public Predictor() {
		calculatedMeets = new HashMap<Meet,  MeetData>();
	}
	
	
	// calculates and returns a HashMap of given team athletes and their estimated times at the given meet
	// for a given year
	public HashMap<Athlete, Double> meetPrediction(Meet meet, Collection<Athlete> group, int year) {
		// checks if we've already calculated meet data for the given meet
		// if we haven't then calculate and save new meet data into 
		// calculatedMeets HashMap
		if (!calculatedMeets.containsKey(meet))
			calculatedMeets.put(meet, new MeetData(meet));
		MeetData md = calculatedMeets.get(meet);	// saves local reference to meet data

		HashMap<Athlete, Double> returnMap = new HashMap<Athlete, Double>();
		// the start of the season is defined as September 1st on the given year
		// the end of the season is defined as November 30th on the given year
		Date startOfSeason = new Date((byte) 9, (byte) 1, (short) year);
		Date endOfSeason = new Date((byte) 11, (byte) 30, (short) year);
		Date dayBeforeMeet = new Date(meet.getStartDate().getMonth(), (byte) (meet.getStartDate().getDay() - 1), (short) year);
		// iterates along each athlete in the team, calculating and populating their estimates into the HashMap
		for (Athlete a : group) {
			
			// can use end of season or meets up until the day before, up to how you want to predict
			Performance seasonBest = a.findFastestInRange(startOfSeason, dayBeforeMeet);
			// if no race for the year, only a DNF, or only a DNS, then don't parse them
			if (seasonBest == null || seasonBest.getMark() < 0) {
				System.out.println(a.getName() + " has not completed a race in the goal year " + year);
			} else
				returnMap.put(a, md.estimatePerformance(seasonBest.getMark()));
		}
		return returnMap;
	}
	// Allowing teams to be passed as well as Collections
	public HashMap<Athlete, Double> meetPredictions(Meet meet, Team team, int year) {
		return meetPrediction(meet, team.getTeammates(), year);
	}
	
	// calculates and returns the estimated time a given athlete would run at a given meet
	public double meetPrediction(Meet meet, Athlete a, int year) {
		// checks if we've already calculated meet data for the given meet
		// if we haven't then calculate and save new meet data into 
		// calculatedMeets HashMap
		if (!calculatedMeets.containsKey(meet))
			calculatedMeets.put(meet, new MeetData(meet));
		MeetData md = calculatedMeets.get(meet);	// saves local reference to meet data
		// the start of the season is defined as September 1st on the given year
		// the end of the season is defined as November 30th on the given year
		Date startOfSeason = new Date((byte) 9, (byte) 1, (short) year);
		Date endOfSeason = new Date((byte) 11, (byte) 30, (short) year);
		// uses the meet data to estimate the athlete's performance at the meet using a given 
		// year's season best for that athlete
		return md.estimatePerformance(a.findFastestInRange(startOfSeason, endOfSeason).getMark());
	}
}
