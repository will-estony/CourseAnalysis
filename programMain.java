
public class programMain {

	public static void main(String[] args) {
		
		//Athlete Stephen = new Athlete("Stephen Tyler", "https://www.tfrrs.org/athletes/6424193.html");
		//Athlete Will = new Athlete("Will Estony", "https://www.tfrrs.org/athletes/6424195.html");
		
	    //Stephen.parseStats();
		//Will.parseStats();
	
		/*Will.printPerformances();
		System.out.println();
		Stephen.printPerformances();
		System.out.println();
		Stephen.printPRs();
		System.out.println();
		Will.printPRs();*/
		
		Team t = new Team("Browdies", "https://www.tfrrs.org/teams/xc/CT_college_m_Trinity_CT.html");
	
		t.parseAthletes();
		t.printTeam();
		
	}

}
