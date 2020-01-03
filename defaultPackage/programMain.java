package defaultPackage;
import java.util.HashMap;

import guiPackage.guiManager;

public class programMain {

	public static void main(String[] args) {
		
		guiManager gm = new guiManager();
		
		
		//Athlete a = Athlete.createNew(5984486);
		//a.parse();
		//a.printPRs();
		//a.printPerformances();
		
		/*
		long l = 6891464;
		Athlete a1 = Athlete.createNew(l);
		a1.parse();
		a1.printPRs();
		System.out.println();
		a1.printPerformances();
		a1.printSeasonBests();
		*/
		

		
		
		
		
		//Team browdies = Team.createNew("https://www.tfrrs.org/teams/xc/CT_college_m_Trinity_CT.html");
		//browdies.parse();
		//browdies.printTeam();

		
		//Meet Regionals2018 = Meet.createNew("https://www.tfrrs.org/results/xc/14521/NCAA_Division_III_New_England_Region_Cross_Country_Championships");
		//Regionals2018.parse();
		
		/*
		System.out.println("Predicting results for browdies at REGIONALS 2018 meet");
		Predictor p = new Predictor();
		HashMap<Athlete, Double> estimatedResults = p.meetPrediction(Regionals2018, browdies.getTeammates(), 2019);
		for (Athlete a : estimatedResults.keySet())
			System.out.println("Athlete " + a.getName() + ":\t" + Performance.timeDoubleToString(estimatedResults.get(a)));
		
		
		
		/*Meet NESCACs2019 = Meet.createMeet("https://www.tfrrs.org/results/xc/16670/NESCAC_Cross_Country_Championships");
		MeetParser mp = new MeetParser(Regionals2018);
		mp.parseMeet();
		
		System.out.println("Predicting results for browdies at REGIONALS 2018 meet");
		Predictor p = new Predictor();
		HashMap<Athlete, Double> estimatedResults = p.meetPrediction(Regionals2018, browdies.getTeammates(), 2019);
		for (Athlete a : estimatedResults.keySet())
			System.out.println("Athlete " + a.getName() + ":\t" + Performance.timeDoubleToString(estimatedResults.get(a)));
		*/
		
	

		/*long l = 6891464;
		Athlete a = new Athlete(l, true);
		a.printPRs();
		System.out.println();
		a.printPerformances();

		
		
		
		
		Athlete a = new Athlete(7421730, true);
		a.printPerformances();
		a.printSeasonBests();*/
	
		//Athlete a = new Athlete(5997232, true);
		//a.printSeasonBests();

		//Meet m = Meet.createMeet("https://www.tfrrs.org/results/xc/16670/NESCAC_Cross_Country_Championships");
		
	}
}
