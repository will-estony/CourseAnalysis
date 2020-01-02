/*
 * An interface for all objects that can be drawn to a JPanel
 */
package guiPackage;

import java.awt.Graphics2D;

public interface Drawable {
	// method called to add object to Graphics2D object to make it visible
	public void drawToGraphics(Graphics2D g2);
}
