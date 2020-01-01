package defaultPackage;

public class Performance{
	private String event; //the event the athlete competed in ex. 8k
    private double time;  //the time the athlete completed the event in, in seconds
    private Meet meet;    //the meet the performance happened at
    private Date date;

    public Performance(String event, double time, Date date, Meet meet){
    	this.event = event;
    	this.time = time;
    	this.date = date;
        this.meet = meet;
    }
    // Getters
    public String getEvent() { return event; }
    public double getTime() { return time; }
    public Meet getMeet(){ return meet; }
    public Date getDate() { return date; }
    
    // converts String of the form dd:hh:mm:ss.ms to a double
    public static double timeStringToDouble(String timeString) {
    	// if the mark was a DNF or a DNS store the result as a -1 or -2 respectively
    	if (timeString.equals("DNF"))
    		return -1;
    	if (timeString.equals("DNS"))
    		return -2;
    	
        double timeSecondsDouble = 0;   // initialize return value
        int indexOfFirstDecimal = timeString.indexOf(".");   // searches for the first decimal point
        // if a decimal was not found
        if (indexOfFirstDecimal == -1) {
            System.out.println("Parsing Performance Error: no decimal found in time mark");
            return 0;   // leaves function after error
        }
        
        // finds out if there are 1 or 2 digits after the decimal
        int numDecimalDigits = 2;   // defaults to 2 decimal digits
        // if there is exactly one character after the decimal
        if ((timeString.length() - indexOfFirstDecimal) == 2)
            numDecimalDigits = 1;   // changes num of decimal digits to 1
        
        // convert decimal digits to float
        timeSecondsDouble = Double.parseDouble("0" + timeString.substring(indexOfFirstDecimal, indexOfFirstDecimal + 1 + numDecimalDigits));
        
        if (indexOfFirstDecimal < 1)    // if no ones digit present in string
            return timeSecondsDouble; 
        // add on the ones digit value
        timeSecondsDouble += Character.getNumericValue(timeString.charAt(indexOfFirstDecimal - 1));
       
        // get tens digit
        if (indexOfFirstDecimal < 2)    // if no tens digit present in string
            return timeSecondsDouble;
        // add on the tens digit value
        timeSecondsDouble += 10 * Character.getNumericValue(timeString.charAt(indexOfFirstDecimal - 2));
        
        // get ones minute digit
        if (indexOfFirstDecimal < 4)    // if no ones minute digit present in string
            return timeSecondsDouble;
        // add on the ones minute digit value
        timeSecondsDouble += 60 * Character.getNumericValue(timeString.charAt(indexOfFirstDecimal - 4));
        
        // get tens minute digit
        if (indexOfFirstDecimal < 5)    // if no tens minute digit present in string
            return timeSecondsDouble;
        // add on the tens minute digit value
        timeSecondsDouble += 600 * Character.getNumericValue(timeString.charAt(indexOfFirstDecimal - 5));
        
        // get ones hour digit
        if (indexOfFirstDecimal < 7)    // if no ones hour digit present in string
            return timeSecondsDouble;
        // add on the ones hour digit value
        timeSecondsDouble += 3600 * Character.getNumericValue(timeString.charAt(indexOfFirstDecimal - 7));
        
        // get tens hour digit
        if (indexOfFirstDecimal < 8)    // if no tens hour digit present in string
            return timeSecondsDouble;
        // add on the tens hour digit value
        timeSecondsDouble += 36000 * Character.getNumericValue(timeString.charAt(indexOfFirstDecimal - 8));
        
        // get ones day digit
        if (indexOfFirstDecimal < 10)    // if no ones day digit present in string
            return timeSecondsDouble;
        // add on the ones day digit value
        timeSecondsDouble += 3600 * 24 * Character.getNumericValue(timeString.charAt(indexOfFirstDecimal - 10));
        
        // get tens day digit
        if (indexOfFirstDecimal < 11)    // if no tens days digit present in string
            return timeSecondsDouble;
        // add on the tens day digit value
        timeSecondsDouble += 36000 * 240 * Character.getNumericValue(timeString.charAt(indexOfFirstDecimal - 11));
        return timeSecondsDouble;
    }
    
    // converts a double to a String of the form dd:hh:mm:ss.ms
    public static String timeDoubleToString(double timeSecondsDouble) {
        // instantiates return string as milliseconds part of time, to 2 decimal places
        String ret = "." + String.format("%02d", (int) ((timeSecondsDouble * 100) % 100));
        if (timeSecondsDouble < 1.0)
            return "0" + ret;   // returns string if it is less than a second (adds a zero before decimal
        int timeSeconds = (int) timeSecondsDouble;  // casts to int to get rid of decimal digits
        // concatenates with number of seconds
        ret = String.format("%02d",timeSeconds % 60) + ret;
        if (timeSeconds < 60) { // returns string if it is less than a minute
            if (ret.startsWith("0"))    // removes leading zero if it exists
                return ret.substring(1);
            return ret;
        }
        // concatenates with number of minutes
        ret = String.format("%02d", (timeSeconds/60) % 60) + ":" + ret;
        if (timeSeconds < 3600) { // returns string if it is less than an hour
            if (ret.startsWith("0"))    // removes leading zero if it exists
                return ret.substring(1);
            return ret;
        }
        // concatenates on number of hours
        ret = String.format("%02d", (timeSeconds/3600) % 24) + ":" + ret;
        if (timeSeconds < 86400) { // returns string if it is less than a day
            if (ret.startsWith("0"))    // removes leading zero if it exists
                return ret.substring(1);
            return ret;
        }

        // (god forbid we get here)
        // concatenates on number of days
        ret = String.format("%1d", timeSeconds/86400) + ":" + ret;
        return ret; // returns string with format dd:hh:mm:ss.ms
    }
    
    public String toString(){
    	String perfString;
    	// if the performance is a DNF or DNS
    	if (time == -1)
    		perfString = "DNF";
    	else if (time == -2)
    		perfString = "DNS";
    	else // if it's a normal performance
    		perfString = timeDoubleToString(time);
        return event + ": " + perfString + " - " + date;
    }
}