
public class programMain {

	public static void main(String[] args) {
		//System.out.println(Performance.timeDoubleToString(3* 3600*24 + 27*60 + 34.23));		
		Team t = new Team("Browdies", "https://www.tfrrs.org/teams/xc/CT_college_m_Trinity_CT.html");
		//t.printTeam();

		//MeetParser mp = new MeetParser("https://www.tfrrs.org/results/xc/15785/Connecticut_College_XC_Invitational");
		//mp.parseTables();
		//mp.parseHeaders();
		//t.printTeam();
		//t.printMeets();

		//Athlete a = new Athlete("Tim", "https://www.tfrrs.org/athletes/5997232/Trinity_CT/Timothy_Bogomolov.html");

		//a.printPerformances();

		Meet m = new Meet("", "2019");
		MeetParser mp = new MeetParser("https://www.tfrrs.org/results/xc/15464/TriState_Invitational_at_CCRI", m);
		mp.parseMeet();
		m.getResultsMatrix();


		
		//System.out.println(Athlete.urlToLong("https://www.tfrrs.org/athletes/5997232/Trinity_CT/Timothy_Bogomolov.html"));
		/*long l = 6891464;
		Athlete a = new Athlete(l, true);
		a.printPRs();
		System.out.println();
		a.printPerformances();

		
		
		*/
		
		Athlete a = new Athlete(7421730, true);
		a.printPerformances();
		a.printSeasonBests();
	
		//Athlete a = new Athlete(5997232, true);
		//a.printSeasonBests();
		
	}
}
