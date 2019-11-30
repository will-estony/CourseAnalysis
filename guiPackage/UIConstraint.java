package guiPackage;



public class UIConstraint {
	protected enum ConstraintType {
		relative, fixed;
	}
	
	protected ConstraintType type;	// saves what type of constraint this is
	protected double value;			// saves the value that this constraint uses
	// a negative value means subtracted from the right wall, a positive value means from the left wall
	
	public UIConstraint(ConstraintType constraintType, double constraintValue) {
		this.type = constraintType;
		// error checking
		if (type == ConstraintType.relative && (constraintValue > 1 || constraintValue < 0)) {
			System.out.println("Constraint Creation Error: did not get expected value between 0 and 1.");
			this.value = 0;
		} else {
			this.value = constraintValue;
		}
	}
}
