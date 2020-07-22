package defaultPackage;

public class Course {

	public static enum TYPE {
		XC, INDOOR_FLAT, INDOOR_BANKED, INDOOR_OVERSIZED, OUTDOOR;
		public String toString() {
			switch(this) {
			case XC:
				return "XC";
			case INDOOR_FLAT:
				return "Indoor_flat";
			case INDOOR_BANKED:
				return "Indoor_banked";
			case INDOOR_OVERSIZED:
				return "Indoor_oversized";
			case OUTDOOR:
				return "Outdoor";
			default:
				return null;
			}
		}
	};
	
	private String name;	// name of the course
	private String state;	// state the course resides in
	private String city;	// city the course resides in
	private TYPE type;		// the type of course
	
	public Course(String name, String state, String city, TYPE type) {
		this.name = name;
		this.state = state;
		this.city = city;
		this.type = type;
	}
	
	/* Getters */
	public String getName() { return name; }
	public String getState() { return state; }
	public String getCity() { return city; }
	public TYPE getType() { return type; }
}
