package guiPackage;

import java.awt.Frame;



/*
 * Maybe have this class statically listen for a component change and have it notify all other constraints
 * that the screen has changed and therefore every value needs to be recalculated.
 * 
 * This would only benefit if after a value is calculated it isn't calculated again, each call can just return a saved 
 * value without having to do any calculations and therefore save computation?
 * 
 * 
 * 
 * 
 * Or maybe all constrainable objects are part of a static collection and another class implements componenetListener
 * and any time the screen changes it calls a method that iterates through all the constrainable objects, updating
 * their values. That way we aren't calculating and updating every frame.
 */


/*
 * first you call one of the constructors,
 * then you add extra constraints as desired, which will add them to wall that is closest to the provided reference constraint
 */



public class UIConstraintSet {
	private UIConstraint topConstraint, bottomConstraint, leftConstraint, rightConstraint;
	public static UIConstraint topWall, bottomWall, leftWall, rightWall;
	private static Frame hostFrame;
	private double width = -1, height = -1;	// extra optional constraints, constraints take priority over these
	
	// initializes all the wall constraints, only once, until a new host frame is given, then
	// the host frame is reset and the wall constraints are rebuilt
	public UIConstraintSet(Frame hostFrame) {
		// if a new frame is given
		if (UIConstraintSet.hostFrame != hostFrame) {
			UIConstraintSet.hostFrame = hostFrame;	// saves hostFrame reference
			topWall = new UIConstraint(hostFrame, WALL.TOP);
			bottomWall = new UIConstraint(hostFrame, WALL.BOTTOM);
			leftWall = new UIConstraint(hostFrame, WALL.LEFT);
			rightWall = new UIConstraint(hostFrame, WALL.RIGHT);
		}
	}
	
//	public UIConstraintSet(Frame hostFrame, double width, double height) {
//		this(hostFrame);
//		this.width = width;
//		this.height = height;
//	}
	
	// probably the easiest constructor to use, doesn't require anything more to be set
	public UIConstraintSet(Frame hostFrame, double xPos, double yPos, double width, double height) {
		this(hostFrame);
		setLeftConstraint(xPos);
		setTopConstraint(yPos);
		this.width = width;
		this.height = height;
	}
	
	
	public void addConstraint(double constraintVal, UIConstraint referenceConstraint) {
		
		// calculate which wall is closest to the reference constraint
		// reference constraint should be
	}
	
	public void addConstraint(double constraintVal, UIConstraintSet referenceConstraintSet) {
		// same thing^^ but figure out which wall of the reference also set to use
	}
	
	
	
	
	/* constraint get methods to allow the creation of constraints that reference other constraints */
	public UIConstraint getTopConstraint() { return topConstraint; }
	public UIConstraint getBottomConstraint() { return bottomConstraint; }
	public UIConstraint getLeftConstraint() { return leftConstraint; }
	public UIConstraint getRightConstraint() { return rightConstraint; }
	
	/* the simpler constraints */
	public void setWidth(double newWidth) {
		this.width = newWidth;
	}
	public void setHeight(double newHeight) {
		this.height = newHeight;
	}
	
	/* all possible constraint update/set/reset methods */
	public void setTopConstraint(double constraintVal) {
		this.topConstraint = new UIConstraint(constraintVal, topWall);
	}
	public void setTopConstraint(double constraintVal, UIConstraint referenceConstraint1) {
		this.topConstraint = new UIConstraint(constraintVal, referenceConstraint1);
	}
	public void setTopConstraint(double constraintVal, UIConstraint referenceConstraint1, UIConstraint referenceConstraint2) {
		this.topConstraint = new UIConstraint(constraintVal, referenceConstraint1, referenceConstraint2);
	}
	public void setBottomConstraint(double constraintVal) {
		this.bottomConstraint = new UIConstraint(constraintVal, bottomWall);
	}
	public void setBottomConstraint(double constraintVal, UIConstraint referenceConstraint1) {
		this.bottomConstraint = new UIConstraint(constraintVal, referenceConstraint1);
	}
	public void setBottomConstraint(double constraintVal, UIConstraint referenceConstraint1, UIConstraint referenceConstraint2) {
		this.bottomConstraint = new UIConstraint(constraintVal, referenceConstraint1, referenceConstraint2);
	}
	public void setLeftConstraint(double constraintVal) {
		this.leftConstraint = new UIConstraint(constraintVal, leftWall);
	}
	public void setLeftConstraint(double constraintVal, UIConstraint referenceConstraint1) {
		this.leftConstraint = new UIConstraint(constraintVal, referenceConstraint1);
	}
	public void setLeftConstraint(double constraintVal, UIConstraint referenceConstraint1, UIConstraint referenceConstraint2) {
		this.leftConstraint = new UIConstraint(constraintVal, referenceConstraint1, referenceConstraint2);
	}
	public void setRightConstraint(double constraintVal) {
		this.rightConstraint = new UIConstraint(constraintVal, rightWall);
	}
	public void setRightConstraint(double constraintVal, UIConstraint referenceConstraint1) {
		this.rightConstraint = new UIConstraint(constraintVal, referenceConstraint1);
	}
	public void setRightConstraint(double constraintVal, UIConstraint referenceConstraint1, UIConstraint referenceConstraint2) {
		this.rightConstraint = new UIConstraint(constraintVal, referenceConstraint1, referenceConstraint2);
	}
	
	
	
	
	
	
	// any parameter (excluding the hostFrame param) can be accompanied by another constrainable object,
	// which becomes the reference constraint for that param
	
	// that would be 16 different constructors? is that really how I want to do this??
	// I could just have the basic constructor up above and then add methods to adjust individual constraints
	// like this?:
	// public void setLeftConstraint(double constraintVal, Constrainable referenceObject);
	
	// Do I want an abstract Constrainable class/interface? and Drawable would extend Constrainable, TeamJList would also extend Constrainable
	
	
	
	// every constraint set needs two of three to be set:
	// leftConstraint, rightConstraint, width
	
	// and two of these three to be set also
	// topConstraint, bottomConstraint, height
	
	
	
	
	
	/* returns the calculated values of the constraint set */
	// returns x value of left of object
	public double getX() {
		// figures out which side of the object has a constraint set
		// favoring left side first, then right side & width
		if (leftConstraint != null)
			return leftConstraint.computeVal();
		else	// else leftConstraint is null so other two are set
			return rightConstraint.computeVal() - width;
	}
	// returns y value of top of object
	public double getY() {
		if (topConstraint != null)
			return topConstraint.computeVal();
		else
			return bottomConstraint.computeVal() - height;
	}
	// returns x value of center of object
	public double getCenterX() {
		// ignores width setting, prioritizing constraints first
		if (rightConstraint != null) {
			if (leftConstraint != null) {
				// both constraints are not null
				return (rightConstraint.computeVal() + leftConstraint.computeVal())/2;
			} else	// left constraint is null
				return rightConstraint.computeVal() - (width/2);
		} else // right constraint is null
			return leftConstraint.computeVal() + (width/2);
	}
	// returns y value of center of object
	public double getCenterY() {
		// ignores width setting, prioritizing constraints first
		if (topConstraint != null) {
			if (bottomConstraint != null) {
				// both constraints are not null
				return (bottomConstraint.computeVal() + bottomConstraint.computeVal())/2;
			} else	// bottom constraint is null
				return topConstraint.computeVal() + (height/2);
		} else // top constraint is null
			return bottomConstraint.computeVal() - (height/2);
	}
	// calculate and returns height of object, favoring constraints over height variable
	public double getHeight() {
		if (bottomConstraint != null && topConstraint != null)	// if both constraints are non-null
			return bottomConstraint.computeVal() - topConstraint.computeVal();
		else
			return height;
	}
	// returns width of object
	public double getWidth() {
		if (leftConstraint != null && rightConstraint != null)	// if both constraints are non-null
			return rightConstraint.computeVal() - leftConstraint.computeVal();
		else
			return width;
	}
	
	
	
	
	/*


	
	// creates a new constraint set that is the given offset away from this one
	public UIConstraintSet createOffsetContraintSet(double xOffset, double yOffset) {
		// creates a new fixed xConstraint that is xOffset away from the current xConstraint
		UIConstraint newXConstraint = new UIConstraint(xOffset, this);
		UIConstraint newYConstraint = new UIConstraint(yOffset, this);
		
		// hostFrame reference can be "null" because we never actually reference it
		// because our new clone constraint set is of type "fixed"
		return new UIConstraintSet(null, newXConstraint, newYConstraint);
	}
	*/
	// only used for Wall Constraints
	private enum WALL {
		TOP, BOTTOM, LEFT, RIGHT
	}
	private class UIConstraint {
		/* instance variables for constraints */
		private double constraintVal;
		private UIConstraint mainReferenceConstraint = null;
		private UIConstraint secondaryReferenceConstraint = null;
		/* instance variables for wall constraints */
		private WALL whichWall;
		private Frame hostFrame = null;
		
		
		/* regular constructors */
		// if a fixed constraint (to a reference constraint)
		private UIConstraint(double fixedDistance, UIConstraint referenceConstraint) {
			this.constraintVal = fixedDistance;
			this.mainReferenceConstraint = referenceConstraint;
		}
		
		// if a relative constraint (between two reference constraints)
		// the output value is computed as the ratio from the leftmost reference
		// to the rightmost reference
		private UIConstraint(double constraintRatio, UIConstraint referenceConstraint1, UIConstraint referenceConstraint2) {
			this.constraintVal = constraintRatio;
			this.mainReferenceConstraint = referenceConstraint1;
			this.secondaryReferenceConstraint = referenceConstraint2;
		}
		
		/* constructor for wall constraints */
		private UIConstraint(Frame hostFrame, WALL whichWall) {
			this.hostFrame = hostFrame;
			this.whichWall = whichWall;
		}
		
		// computes and returns the position value of this constraint
		private double computeVal() {
			// if a regular constraint
			if (hostFrame == null) {
				// if a fixed constraint
				if (secondaryReferenceConstraint == null) {
					return mainReferenceConstraint.computeVal() + constraintVal;
				} else {	// else a relative constraint
					// save both reference constraint values
					double refVal1 = mainReferenceConstraint.computeVal();
					double refVal2 = secondaryReferenceConstraint.computeVal();
					// set refVal1 to be the smaller of the two
					if (refVal1 > refVal2) {	// swap them if ^ is not the case
						double temp = refVal2;
						refVal2 = refVal1;
						refVal1 = temp;
					}
					// at this point refVal1 is the smaller value
					// we return the position of the relative distance from the first constraint to the second
					return (refVal2 - refVal1)*constraintVal + refVal1;
				}
			} else {	// else this is a wall constraint
				switch (whichWall) {
				case TOP:
					return 0;
				case BOTTOM:
					return hostFrame.getHeight();
				case LEFT:
					return 0;
				case RIGHT:
					return hostFrame.getWidth();
				}
			}
			return 0;	// to suppress warnings
		}
	}
}
