package defaultPackage;

/*
 * At the very least this class keeps track of the number of Athletes, Meets or "things" 
 * we are parsing and the current item we are on.

 * As the project grows this class can be used to measure the efficieny of our algorithms.

 */
public class Metrics{

    private double numItems;
    private double currentItem; 
    public Metrics(){

    }
    public void setNumItems(double n){ this.numItems = n;}
    public void setcurrentItem(double n){ this.currentItem = n;}
    public double getNumItems(){ return numItems; }
    public double getCurrentItem(){ return currentItem; }
}