import java.util.HashMap;

public class programMain {

	public static void main(String[] args) {
		//System.out.println(Performance.timeDoubleToString(3* 3600*24 + 27*60 + 34.23));		
		
		//t.printTeam();

		//MeetParser mp = new MeetParser("https://www.tfrrs.org/results/xc/15785/Connecticut_College_XC_Invitational");
		//mp.parseTables();
		//mp.parseHeaders();
		//t.printTeam();
		//t.printMeets();
		
		Team browdies = new Team("Browdies", "https://www.tfrrs.org/teams/xc/CT_college_m_Trinity_CT.html");
		
		
		Meet Regionals2018 = Meet.createMeet("https://www.tfrrs.org/results/xc/14521/NCAA_Division_III_New_England_Region_Cross_Country_Championships");
		Meet NESCACs2019 = Meet.createMeet("https://www.tfrrs.org/results/xc/16670/NESCAC_Cross_Country_Championships");
		MeetParser mp = new MeetParser(Regionals2018);
		mp.parseMeet();
		
		System.out.println("Predicting results for browdies at REGIONALS 2018 meet");
		Predictor p = new Predictor();
		HashMap<Athlete, Double> estimatedResults = p.meetPrediction(Regionals2018, browdies.getTeammates(), 2019);
		for (Athlete a : estimatedResults.keySet())
			System.out.println("Athlete " + a.getName() + ":\t" + Performance.timeDoubleToString(estimatedResults.get(a)));
		
		
	

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
