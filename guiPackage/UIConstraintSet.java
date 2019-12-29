package guiPackage;

import java.awt.Frame;

public class UIConstraintSet {
	private Frame hostFrame;	// a reference to the host frame so we can get width and height
	private UIConstraint xConstraint, yConstraint;
	
	
	public UIConstraintSet(Frame hostFrame, UIConstraint xConstraint, UIConstraint yConstraint) {
		this.hostFrame = hostFrame;
		this.xConstraint = xConstraint;
		this.yConstraint = yConstraint;
	}
	
	
	// creates a clone of this set but with the given offsets
	public UIConstraintSet createOffsetContraintSet(double xOffset, double yOffset) {
		// creates a new fixed xConstraint that is xOffset away from the current xConstraint
		UIConstraint newXConstraint = new UIConstraint(xConstraint.value + xOffset, this);
		UIConstraint newYConstraint = new UIConstraint(yConstraint.value + yOffset, this);
		
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
}
