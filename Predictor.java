/* Used to make predictions for courses/athletes/team placings/etc
 * 
 */

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
	public HashMap<Athlete, Double> meetPrediction(Meet meet, Team team, int year) {
		// checks if we've already calculated meet data for the given meet
		// if we haven't then calculate and save new meet data into 
		// calculatedMeets HashMap
		if (!calculatedMeets.containsKey(meet))
			calculatedMeets.put(meet, new MeetData(meet));
		MeetData md = calculatedMeets.get(meet);	// saves local reference to meet data

		HashMap<Athlete, Double> returnMap = new HashMap<Athlete, Double>();
		// iterates along each athlete in the team, calculating and populating their estimates into the HashMap
		for (Athlete athlete : team.getTeammates()) {
			Performance seasonBest = athlete.getSeasonBest(year);
			// if no race for the year, only a DNF, or only a DNS, then don't parse them
			if (seasonBest == null || seasonBest.getTime() < 0) {
				System.out.println(athlete.getName() + " has not completed a race in the goal year " + year);
			} else
				returnMap.put(athlete, md.estimatePerformance(athlete.getSeasonBest(year).getTime()));
		}
		return returnMap;
	}
	
	// calculates and returns the estimated time a given athlete would run at a given meet
	public double meetPrediction(Meet meet, Athlete athlete, int year) {
		// checks if we've already calculated meet data for the given meet
		// if we haven't then calculate and save new meet data into 
		// calculatedMeets HashMap
		if (!calculatedMeets.containsKey(meet))
			calculatedMeets.put(meet, new MeetData(meet));
		MeetData md = calculatedMeets.get(meet);	// saves local reference to meet data
		// uses the meet data to estimate the athlete's performance at the meet using a given 
		// year's season best for that athlete
		return md.estimatePerformance(athlete.getSeasonBest(year).getTime());
	}
}
