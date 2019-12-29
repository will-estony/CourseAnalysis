package guiPackage;



public class UIConstraint {
	protected enum ConstraintType {
		relative, fixed;
	}
	
	protected ConstraintType type;	// saves what type of constraint this is
	protected double value;			// saves the value that this constraint uses
	protected UIConstraintSet referenceConstraints;
	// if no reference frame:
	// a negative value means subtracted from the right wall, a positive value means from the left wall
	
	// two types of constraints, fixed and relative
	// relative only requires the constraint value
	// fixed requires constraint value and reference constraints to be based off of
	//		if reference frame is null, then screen edges are reference constraints
	public UIConstraint(double constraintValue, UIConstraintSet referenceConstraints) {
		this.type = ConstraintType.fixed;
		this.referenceConstraints = referenceConstraints;
		this.value = constraintValue;
	}
	public UIConstraint(double constraintValue) {
		this.type = ConstraintType.relative;
		this.referenceConstraints = null;
		// error checking constraint value
		if (constraintValue > 1 || constraintValue < 0) {
			System.out.println("Constraint Creation Error: did not get expected value between 0 and 1.");
			this.value = 0;
		} else {
			this.value = constraintValue;
		}
	}
}
