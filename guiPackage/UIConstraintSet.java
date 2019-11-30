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
	
	// returns the center x position of this object as defined by the xConstraint
	public double getX() {
		switch (xConstraint.type) {
			case fixed:	// if fixed constraint
				if (xConstraint.value >= 0)		// if value positive
					return xConstraint.value;	// then place as distance from left wall
				else							// if value negative
					return hostFrame.getWidth() + xConstraint.value;	// then place as distance from right wall
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
				if (yConstraint.value >= 0)		// if value positive
					return yConstraint.value;	// then place as distance from top wall
				else							// if value negative
					return hostFrame.getHeight() + yConstraint.value;	// then place as distance from bottom wall
			case relative:	// if relative constraint
				return hostFrame.getHeight() * yConstraint.value;		// then place as percentage of height
			default:	// needed to suppress error
				return 0;
		}
	}
	
}
