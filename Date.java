
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
	
	// getters
	public byte getMonth() { return month; }
	public byte getDay() { return day; }
	public short getYear() { return year; }
	
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
	
	public String toString() {
		String retString = "";
		switch (month) {	// first month
			case 1:
				retString += "Jan";
				break;
			case 2:
				retString += "Feb";
				break;
			case 3:
				retString += "Mar";
				break;
			case 4:
				retString += "Apr";
				break;
			case 5:
				retString += "May";
				break;
			case 6:
				retString += "Jun";
				break;
			case 7:
				retString += "Jul";
				break;
			case 8:
				retString += "Aug";
				break;
			case 9:
				retString += "Sep";
				break;
			case 10:
				retString += "Oct";
				break;
			case 11:
				retString += "Nov";
				break;
			case 12:
				retString += "Dec";
				break;
		}
		// then day
		retString += " " + Byte.toString(day);
		// then year
		retString += ", " + Short.toString(year);
		return retString;
	}
}
