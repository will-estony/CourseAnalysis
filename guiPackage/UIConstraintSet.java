package guiPackage;

import java.awt.Frame;
import java.awt.Insets;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;




// Assumptions:
// that all horizontal constraints will be created with horizontal reference constraints only,
// and vice versa for vertical constraints.
// ^ maybe I could enforce that in the code somehow?

// every constraint set needs two of three to be set:
// leftConstraint, rightConstraint, width

// and two of these three to be set also
// topConstraint, bottomConstraint, height



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
	private double width = -1, height = -1;	// extra optional constraints, constraints take priority over these
	
	/* static containers of all constraints in existence, grouped into two categories */
	private final static ArrayList<UIConstraint> verticalConstraints = new ArrayList<UIConstraint>();
	private final static ArrayList<UIConstraint> horizontalConstraints = new ArrayList<UIConstraint>();
	
	// this needs to be called first before starting to use Constraints.
	// initializes all the wall constraints, only once.
	// and creates a component listener that is fired every time the screen is resized
	// and adds it to the host frame.
	public static void init(Frame hostFrame) {
		UIConstraintSet temp = new UIConstraintSet();	// not 100% sure why I need this but I do.
		hostFrame.addComponentListener(temp.new ConstraintUpdater(hostFrame));
		topWall = temp.new UIConstraint(hostFrame, WALL.TOP);
		bottomWall = temp.new UIConstraint(hostFrame, WALL.BOTTOM);
		leftWall = temp.new UIConstraint(hostFrame, WALL.LEFT);
		rightWall = temp.new UIConstraint(hostFrame, WALL.RIGHT);
	}
	
	public UIConstraintSet() {
	}

	// probably the easiest constructor to use, doesn't require anything more to be set
	public UIConstraintSet(double xPos, double yPos, double width, double height) {
		setLeftConstraint(xPos);
		setTopConstraint(yPos);
		this.width = width;
		this.height = height;
	}
	
	
	/* constraint get methods to allow the creation of constraints that reference other constraints */
	public UIConstraint getTopConstraint() { return topConstraint; }
	public UIConstraint getBottomConstraint() { return bottomConstraint; }
	public UIConstraint getLeftConstraint() { return leftConstraint; }
	public UIConstraint getRightConstraint() { return rightConstraint; }
	// The wall-constraint getters
	public static UIConstraint getTopWall() { return topWall; }
	public static UIConstraint getBottomWall() { return bottomWall; }
	public static UIConstraint getLeftWall() { return leftWall; }
	public static UIConstraint getRightWall() { return rightWall; }
	
	/* the simpler constraints */
	public void setWidth(double newWidth) {
		this.width = newWidth;
	}
	public void setHeight(double newHeight) {
		this.height = newHeight;
	}
	
	/* all possible constraint update/set/reset methods:
	 * 		removes the constraint, being overwritten, from the static container 
	 * 		before overwriting it with the new constraint.
	 */
	
	public void setTopConstraint(double constraintVal) {
		removeFromContainer(topConstraint);
		this.topConstraint = new UIConstraint(constraintVal, topWall);
	}
	public void setTopConstraint(double constraintVal, UIConstraint referenceConstraint1) {
		removeFromContainer(topConstraint);
		this.topConstraint = new UIConstraint(constraintVal, referenceConstraint1);
	}
	public void setTopConstraint(double constraintVal, UIConstraint referenceConstraint1, UIConstraint referenceConstraint2) {
		removeFromContainer(topConstraint);
		this.topConstraint = new UIConstraint(constraintVal, referenceConstraint1, referenceConstraint2);
	}
	public void setBottomConstraint(double constraintVal) {
		removeFromContainer(bottomConstraint);
		this.bottomConstraint = new UIConstraint(constraintVal, bottomWall);
	}
	public void setBottomConstraint(double constraintVal, UIConstraint referenceConstraint1) {
		removeFromContainer(bottomConstraint);
		this.bottomConstraint = new UIConstraint(constraintVal, referenceConstraint1);
	}
	public void setBottomConstraint(double constraintVal, UIConstraint referenceConstraint1, UIConstraint referenceConstraint2) {
		removeFromContainer(bottomConstraint);
		this.bottomConstraint = new UIConstraint(constraintVal, referenceConstraint1, referenceConstraint2);
	}
	public void setLeftConstraint(double constraintVal) {
		removeFromContainer(leftConstraint);
		this.leftConstraint = new UIConstraint(constraintVal, leftWall);
	}
	public void setLeftConstraint(double constraintVal, UIConstraint referenceConstraint1) {
		removeFromContainer(leftConstraint);
		this.leftConstraint = new UIConstraint(constraintVal, referenceConstraint1);
	}
	public void setLeftConstraint(double constraintVal, UIConstraint referenceConstraint1, UIConstraint referenceConstraint2) {
		removeFromContainer(leftConstraint);
		this.leftConstraint = new UIConstraint(constraintVal, referenceConstraint1, referenceConstraint2);
	}
	public void setRightConstraint(double constraintVal) {
		removeFromContainer(rightConstraint);
		this.rightConstraint = new UIConstraint(constraintVal, rightWall);
	}
	public void setRightConstraint(double constraintVal, UIConstraint referenceConstraint1) {
		removeFromContainer(rightConstraint);
		this.rightConstraint = new UIConstraint(constraintVal, referenceConstraint1);
	}
	public void setRightConstraint(double constraintVal, UIConstraint referenceConstraint1, UIConstraint referenceConstraint2) {
		removeFromContainer(rightConstraint);
		this.rightConstraint = new UIConstraint(constraintVal, referenceConstraint1, referenceConstraint2);
	}
	
	// removes the supplied constraint from the container it exists in if it is non-null and not a wall constraint
	private void removeFromContainer(UIConstraint c) {
		// makes sure it's non-null and not a wall constraint
		if (c != null && !(c.equals(topWall) || c.equals(bottomWall) || c.equals(leftWall) || c.equals(rightWall))) {
			if (c.isVerticalConstraint)
				verticalConstraints.remove(c);
			else
				horizontalConstraints.remove(c);
		}
	}
	
	
	/* returns the calculated values of the constraint set */
	// returns x value of left of object
	public double getX() {
		// figures out which side of the object has a constraint set
		// favoring left side first, then right side & width
		if (leftConstraint != null)
			return leftConstraint.getActualVal();
		else	// else leftConstraint is null so other two are set
			return rightConstraint.getActualVal() - width;
	}
	// returns y value of top of object
	public double getY() {
		if (topConstraint != null)
			return topConstraint.getActualVal();
		else
			return bottomConstraint.getActualVal() - height;
	}
	// returns x value of center of object
	public double getCenterX() {
		// ignores width setting, prioritizing constraints first
		if (rightConstraint != null) {
			if (leftConstraint != null) {
				// both constraints are not null
				return (rightConstraint.getActualVal() + leftConstraint.getActualVal())/2;
			} else	// left constraint is null
				return rightConstraint.getActualVal() - (width/2);
		} else // right constraint is null
			return leftConstraint.getActualVal() + (width/2);
	}
	// returns y value of center of object
	public double getCenterY() {
		// ignores width setting, prioritizing constraints first
		if (topConstraint != null) {
			if (bottomConstraint != null) {
				// both constraints are not null
				return (bottomConstraint.getActualVal() + bottomConstraint.getActualVal())/2;
			} else	// bottom constraint is null
				return topConstraint.getActualVal() + (height/2);
		} else // top constraint is null
			return bottomConstraint.getActualVal() - (height/2);
	}
	// calculate and returns height of object, favoring constraints over height variable
	public double getHeight() {
		if (bottomConstraint != null && topConstraint != null)	// if both constraints are non-null
			return bottomConstraint.getActualVal() - topConstraint.getActualVal();
		else
			return height;
	}
	// returns width of object
	public double getWidth() {
		if (leftConstraint != null && rightConstraint != null)	// if both constraints are non-null
			return rightConstraint.getActualVal() - leftConstraint.getActualVal();
		else
			return width;
	}
	
	
	
	
	


	
	// creates a new constraint set that is the given offset away from this one, with the specified width and height
	public UIConstraintSet createOffsetContraintSet(double xOffset, double yOffset, double width, double height) {
		UIConstraintSet newSet = new UIConstraintSet();
		newSet.setWidth(width);
		newSet.setHeight(height);
		// have to check for null constraints in the case that constraints are defined using widths/heights
		
		// creates the horizontal constraint
		// if xOffset is negative then new object is to the left of this one
		if (xOffset < 0) {
			if (this.leftConstraint == null)
				newSet.setRightConstraint(xOffset - this.width, this.rightConstraint);
			else
				newSet.setRightConstraint(xOffset, this.leftConstraint);
		} else if (xOffset > 0) {
			if (this.rightConstraint == null)
				newSet.setLeftConstraint(xOffset + this.width, this.leftConstraint);
			else
				newSet.setLeftConstraint(xOffset, this.rightConstraint);
		} else {	// else xOffset is zero
			double currWidth = getWidth();
			if (currWidth == width) {	// if object widths are the same, then share same constraint
				if (this.leftConstraint == null)
					newSet.rightConstraint = this.rightConstraint;
				else
					newSet.leftConstraint = this.leftConstraint;
			} else { // else object widths are not the same and we need to calculate new constraints to keep x-centers equal
				if (this.rightConstraint == null)
					newSet.setLeftConstraint((currWidth - width)/2, this.leftConstraint);
				else
					newSet.setRightConstraint(-(currWidth - width)/2, this.rightConstraint);
			}
		}
		
		// creates the vertical constraint
		// if yOffset is negative then new object is above this one
		if (yOffset < 0) {
			if (this.topConstraint == null)
				newSet.setBottomConstraint(yOffset - this.height, this.bottomConstraint);
			else
				newSet.setBottomConstraint(yOffset, this.topConstraint);
		} else if (yOffset > 0) {
			if (this.bottomConstraint == null)
				newSet.setTopConstraint(yOffset + this.height, this.topConstraint);
			else
				newSet.setTopConstraint(yOffset, this.bottomConstraint);
		} else {	// else yOffset is zero
			double currHeight = getHeight();
			if (currHeight == height) {	// if object heights are the same, then share same constraint
				if (this.topConstraint == null)
					newSet.bottomConstraint = this.bottomConstraint;
				else
					newSet.topConstraint = this.topConstraint;
			} else {	// if object heights are not the same, calculate new constraint to keep y-centers same
				if (this.topConstraint == null)
					newSet.setBottomConstraint(-(currHeight - height)/2, this.bottomConstraint);
				else
					newSet.setTopConstraint((currHeight - height)/2, this.topConstraint);
			}
		}
		
		return newSet;
	}
	
	
	
	// Could run on it's own thread???:
	
	// static class
	// listens for changes in the width and height of the window, and only updates 
	// constraints that are related to height or width changes.
	private class ConstraintUpdater implements ComponentListener {
		private Frame hostFrame;
		private int prevWidth, prevHeight;
		
		public ConstraintUpdater(Frame hostFrame) {
			this.hostFrame = hostFrame;
		}
		@Override
		public void componentHidden(ComponentEvent arg0) {}

		@Override
		public void componentMoved(ComponentEvent arg0) {}

		@Override
		public void componentResized(ComponentEvent arg0) {
			// save new width and height values
			int currHeight = hostFrame.getHeight();
			int currWidth = hostFrame.getWidth();
			
			// if new width is different from prior width
			if (currWidth != prevWidth) {
				prevWidth = currWidth;	// update previous width
				// iterate along all horizontal constraints telling them they need to update
				for (UIConstraint c : horizontalConstraints)
					c.needsUpdating = true;
			}
			// if new height is different from prior height
			if (currHeight != prevHeight) {
				prevHeight = currHeight;	// update previous height
				// iterate along all vertical constraints telling them they need to update
				for (UIConstraint c : verticalConstraints)
					c.needsUpdating = true;
			}
		}

		@Override
		public void componentShown(ComponentEvent arg0) {}
	}
	
	// only used for Wall Constraints
	private enum WALL {
		TOP, BOTTOM, LEFT, RIGHT
	}
	private class UIConstraint {
		/* instance variables for constraints */
		private double constraintVal;
		private double actualVal;
		private boolean needsUpdating = true;
		private boolean isVerticalConstraint;	// if not vertical then horizontal
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
			// sets if this is a vertical or a horizontal constraint
			isVerticalConstraint = referenceConstraint.isVerticalConstraint;
			// and adds it to the static collection
			if (isVerticalConstraint)
				verticalConstraints.add(this);
			else
				horizontalConstraints.add(this);
		}
		
		// if a relative constraint (between two reference constraints)
		// the output value is computed as the ratio from the leftmost reference
		// to the rightmost reference
		private UIConstraint(double constraintRatio, UIConstraint referenceConstraint1, UIConstraint referenceConstraint2) {
			this.constraintVal = constraintRatio;
			this.mainReferenceConstraint = referenceConstraint1;
			this.secondaryReferenceConstraint = referenceConstraint2;
			// sets if this is a vertical or a horizontal constraint
			isVerticalConstraint = referenceConstraint1.isVerticalConstraint;
			// and adds it to the static collection
			if (isVerticalConstraint)
				verticalConstraints.add(this);
			else
				horizontalConstraints.add(this);
		}
		
		/* constructor for wall constraints */
		private UIConstraint(Frame hostFrame, WALL whichWall) {
			this.hostFrame = hostFrame;
			this.whichWall = whichWall;
			// sets if this is a vertical or a horizontal constraint
			if (whichWall == WALL.BOTTOM || whichWall == WALL.TOP)
				isVerticalConstraint = true;
			else
				isVerticalConstraint = false;	// this is the default value already but it's nice to be explicit sometimes.
			// only adds the bottom and right wall constraints as the other two never update
			if (whichWall == WALL.BOTTOM)
				verticalConstraints.add(this);
			if (whichWall == WALL.RIGHT)
				horizontalConstraints.add(this);
		}
		
		// returns the position value of this constraint,
		// only does computations if the value requires updating.
		// ideally increases efficiency by not doing computations every call.
		private double getActualVal() {
			// updates the value if it needs to be updated
			if (needsUpdating) {
				actualVal = computeVal();
				needsUpdating = false;
			}
			// returns the value
			return actualVal;
		}
		
		// updates and saves the position value of this constraint
		private double computeVal() {
			// if a regular constraint
			if (hostFrame == null) {
				// if a fixed constraint
				if (secondaryReferenceConstraint == null) {
					return mainReferenceConstraint.getActualVal() + constraintVal;
				} else {	// else a relative constraint
					// save both reference constraint values
					double refVal1 = mainReferenceConstraint.getActualVal();
					double refVal2 = secondaryReferenceConstraint.getActualVal();
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
				// we have to remove the values of the insets to get the position of the actual walls
				Insets insets = hostFrame.getInsets();
				switch (whichWall) {
				case TOP:
					return 0;
				case BOTTOM:
					return hostFrame.getHeight() - (insets.top + insets.bottom);
				case LEFT:
					return 0;
				case RIGHT:
					return hostFrame.getWidth() - (insets.left + insets.right);
				}
			}
			return 0;	// to suppress warnings
		}
	}
}
