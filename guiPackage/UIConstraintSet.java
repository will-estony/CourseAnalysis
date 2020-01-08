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



public class UIConstraintSet {
	private UIConstraint topConstraint, bottomConstraint, leftConstraint, rightConstraint;
	private static UIConstraint topWall, bottomWall, leftWall, rightWall;
	
	// initializes all the wall constraints, only once
	private UIConstraintSet(Frame hostFrame) {
		if (topWall == null) {
			topWall = new UIConstraint(hostFrame, WALL.TOP);
			bottomWall = new UIConstraint(hostFrame, WALL.BOTTOM);
			leftWall = new UIConstraint(hostFrame, WALL.LEFT);
			rightWall = new UIConstraint(hostFrame, WALL.RIGHT);
		}
	}
	
	// the most basic set, a rectangle with a fixed (x,y) position
	public UIConstraintSet(Frame hostFrame, double xPos, double yPos, double width, double height) {
		this(hostFrame);
		topConstraint = new UIConstraint(yPos, topWall);
		bottomConstraint = new UIConstraint(height,topConstraint);
		leftConstraint = new UIConstraint(xPos, leftWall);
		rightConstraint = new UIConstraint(width, leftConstraint);
	}
	
	// any parameter (excluding the hostFrame param) can be accompanied by another constrainable object,
	// which becomes the reference constraint for that param
	
	// that would be 16 different constructors? is that really how I want to do this??
	// I could just have the basic constructor up above and then add methods to adjust individual constraints
	// like this?:
	// public void setLeftConstraint(double constraintVal, Constrainable referenceObject);
	
	// Do I want an abstract Constrainable class? and Drawable would extend Constrainable, TeamJList would also extend Constrainable
	
	
	
	
	
	
	
	
	
	/* returns the calculated values of the constraint set */
	// returns x value of left of object
	public double getX() {
		return leftConstraint.computeVal();
	}
	// returns y value of top of object
	public double getY() {
		return topConstraint.computeVal();
	}
	// returns x value of center of object
	public double getCenterX() {
		return (leftConstraint.computeVal() + rightConstraint.computeVal())/2;
	}
	// returns y value of center of object
	public double getCenterY() {
		return (topConstraint.computeVal() + bottomConstraint.computeVal())/2;
	}
	// returns height of object
	public double getHeight() {
		return bottomConstraint.computeVal() - topConstraint.computeVal();
	}
	// returns width of object
	public double getWidth() {
		return rightConstraint.computeVal() - leftConstraint.computeVal();
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
	
	
	// returns the center x position of this object as defined by the xConstraint
	public double getX() {
		switch (xConstraint.type) {
			case fixed:	// if fixed constraint
				// if no reference object, then constraint is based off of frame walls
				if (xConstraint.referenceConstraints == null) {
					if (xConstraint.value >= 0)		// if value positive
						return xConstraint.value;	// then place as distance from left wall
					else							// if value negative
						return hostFrame.getWidth() + xConstraint.value;	// then place as distance from right wall
				} // else our position is based off distance from a reference object
				return xConstraint.referenceConstraints.getX() + xConstraint.value;
				
			case relative:	// if relative constraint
				return hostFrame.getWidth() * xConstraint.value;		// then place as percentage of width
			default:	// needed to suppress error
				return 0;
		}
	}
	
	// returns the center y position of this object as defined by the yConstraint
	public double getY() {
		switch (yConstraint.type) {
			case fixed:	// if fixed constraint
				// if no reference object, then constraint is based off of frame walls
				if (xConstraint.referenceConstraints == null) {
					if (yConstraint.value >= 0)		// if value positive
						return yConstraint.value;	// then place as distance from top wall
					else							// if value negative
						return hostFrame.getHeight() + yConstraint.value;	// then place as distance from bottom wall
				} // else our position is based off distance from a reference object
				return yConstraint.referenceConstraints.getY() + yConstraint.value;
				
			case relative:	// if relative constraint
				return hostFrame.getHeight() * yConstraint.value;		// then place as percentage of height
			default:	// needed to suppress error
				return 0;
		}
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
