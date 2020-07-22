package defaultPackage;

public class Date implements Comparable<Date> {

	private byte month;
	private byte day;
	private short year;
	
	public Date(String dateString) {
		// parses month first by looking at first 3 letters
		switch(dateString.substring(0, 3).toLowerCase()) {
			case ("jan"):
				month = 1;
				break;
			case ("feb"):
				month = 2;
				break;
			case ("mar"):
				month = 3;
				break;
			case ("apr"):
				month = 4;
				break;
			case ("may"):
				month = 5;
				break;
			case ("jun"):
				month = 6;
				break;
			case ("jul"):
				month = 7;
				break;
			case ("aug"):
				month = 8;
				break;
			case ("sep"):
				month = 9;
				break;
			case ("oct"):
				month = 10;
				break;
			case ("nov"):
				month = 11;
				break;
			case ("dec"):
				month = 12;
				break;
		}
		// then parses day
		// finds first occurrence of a digit
		int startDigit = 2;
		while (!Character.isDigit(dateString.charAt(++startDigit)));
		// finds index of last digit
		int endDigit = startDigit;
		while(Character.isDigit(dateString.charAt(++endDigit)));
		day = Byte.parseByte(dateString.substring(startDigit, endDigit));
		// finally year
		year = Short.parseShort(dateString.substring(endDigit+2));
	}
	
	public Date(byte month, byte day, short year) {
		this.month = month;
		this.day = day;
		this.year = year;
	}
	// autocasts data types to make it life a little easier
	public Date(int month, int day, int year) {
		this((byte) month, (byte) day, (short) year);
	}
	
	// getters
	public byte getMonth() { return month; }
	public byte getDay() { return day; }
	public short getYear() { return year; }
	
	// Dates of NCAA D3 XC Nationals
	// (cross country championships are the Saturday prior to Thanksgiving
	//  cool to see that they run the race on that Saturday regardless of weather.)
	private static final Date [] D3XCNationals = {
			new Date(11, 18, 2000),	// index 0 = year 2000	- Trinity sends team to NCAAs [14th/24]
			new Date(11, 17, 2001),
			new Date(11, 23, 2002),	// - Ryan Bak wins NCAAs
			new Date(11, 22, 2003),
			new Date(11, 20, 2004),
			new Date(11, 19, 2005),
			new Date(11, 18, 2006),	// - Trinity sends team to NCAAs [31st/32] (second to last...)
			new Date(11, 17, 2007),	// - Trinity sends team to NCAAs [29th/32]
			new Date(11, 22, 2008),	// - Trinity sends team to NCAAs [7th/32] (damn...)
			new Date(11, 21, 2009),
			new Date(11, 20, 2010),	// index 10 = year 2010
			new Date(11, 19, 2011),
			new Date(11, 17, 2012),
			new Date(11, 23, 2013),
			new Date(11, 22, 2014),
			new Date(11, 21, 2015),
			new Date(11, 19, 2016),
			new Date(11, 18, 2017),
			new Date(11, 17, 2018),
			new Date(11, 23, 2019),
			new Date(11, 21, 2020),// index 20 = year 2020
			new Date(11, 20, 2021),
	};
	
	// TODO: fill this out
	// Dates of NCAA D3 Indoor Nationals
	// (more specifically the date of the last day of the meet, bc these are multi-day meets)
	private static final Date [] D3IndoorNationals = {
			new Date(3, 11, 2000)//	March 10-11, 2000	Ryan Bak runs 800m ,fails to make finals by .07
	};
	
	// TODO: fill this out
	// Dates of NCAA D3 Outdoor Nationals
	// the last day of the meet
	private static final Date [] D3OutdoorNationals = {
			
	};
	
	// returns the official D3 season of this date
	public String getD3Season() {
		
		// everything is already compared to major events of the same year as this date
		if (this.compareTo(D3IndoorNationals[this.year % 100]) != 1	// if before or on indoor nationals
			|| this.compareTo(D3XCNationals[this.year % 100]) == 1)	// or after xc nationals
			return "Winter";
		else if (this.compareTo(D3OutdoorNationals[this.year % 100 ]) != 1)	// if before or on outdoor nationals
			return "Spring";
		else {	// Fall or Summer
			// TODO: add a way to differentiate between Fall and Summer
			return "Fall";	// for now we'll just default to Fall
		}
	}
	
	// returns 1 if this date is later than Date o
	public int compareTo(Date o) {
		if (year > o.getYear())
			return 1;
		else if (year < o.getYear())
			return -1;
		// years are the same
		if (month > o.getMonth())
			return 1;
		else if (month < o.getMonth())
			return -1;
		// months are the same
		if (day > o.getDay())
			return 1;
		else if (day < o.getDay())
			return -1;
		// days, months, and years are all the same
		return 0;
	}
	
	// converts date to yyyy-[m]m-[d]d format
	public String toString() {
		return Short.toString(year) + "-" + Byte.toString(month) + "-" + Byte.toString(day);
	}
}
