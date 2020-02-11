/*
 * An interface for all objects that can be drawn to a JPanel
 */
package guiPackage;

import java.awt.Graphics2D;

public abstract class Drawable {
	
	protected UIConstraintSet constraints = null;
	
	// forces all sub-classes to invoke this super constructor
	protected Drawable(UIConstraintSet constraints) {
		this.constraints = constraints;
	}
	// returns constraint set to make it easier to build custom constraints
	public UIConstraintSet getConstraintSet() { return constraints; }
	// the purpose of this method is to allow other objects to be built such that
	// they are always a fixed distance away from this given Drawable object
	public UIConstraintSet createOffsetContraintSet(double xOffset, double yOffset, double width, double height) {
		return constraints.createOffsetContraintSet(xOffset, yOffset, width, height);
	}
	// method called to add object to Graphics2D object to make it visible
	public abstract void drawToGraphics(Graphics2D g2);
}
