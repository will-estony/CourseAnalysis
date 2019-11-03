
public class programMain {

	public static void main(String[] args) {
		
		//Athlete Stephen = new Athlete("Stephen Tyler", "https://www.tfrrs.org/athletes/6424193.html");
		String stephen = "https://www.tfrrs.org/athletes/6424193.html";

		AthleteParser ap = new AthleteParser(stephen);
		ap.printTables();
		
		
		
	}

}
