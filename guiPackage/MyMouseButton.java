package guiPackage;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;


public class MyMouseButton extends MyTextBox {
	
	// instance variables for the button
	private Color regularBoxColor, regularTextColor, highlightedBoxColor, highlightedTextColor, pressedBoxColor, pressedTextColor;
	private Shape boxShape;
	private UIConstraintSet constraints;
	private boolean isEnabled = true;
	
	// private boolean to know if button is highlighted or not
	private boolean isHighlighted;
	
	// private boolean to know if button is pressed in or not
	private boolean isPressed;
	
	public MyMouseButton(String textString, Font textFont, Color regularTextColor, Color highlightedTextColor,
			Color regularBoxColor, Color highlightedBoxColor, double width, double height, UIConstraintSet constraints) {
		// passes required params to super MyTextBox class
		super(textString, textFont, regularTextColor, constraints);
		// saves the needed params
		this.constraints = constraints;
		this.regularTextColor = regularTextColor;
		this.highlightedTextColor = highlightedTextColor;
		this.regularBoxColor = regularBoxColor;
		this.highlightedBoxColor = highlightedBoxColor;
		// creates and saves the shape of the box
		boxShape = new RoundRectangle2D.Double(constraints.getX() - width/2, constraints.getY() - height/2, width, height, 20, 20);
		
		// default pressed colors
		pressedBoxColor = Color.GRAY;
		pressedTextColor = Color.WHITE;
	}
	
	// constructor that uses some default values
	public MyMouseButton(String textString, Font textFont, double width, double height, UIConstraintSet constraints) {
		this(textString, textFont, Color.WHITE, Color.BLACK, Color.BLUE, Color.WHITE, width, height, constraints);
	}
	
	// constructor with even more default values
	public MyMouseButton(String textString, Font textFont, UIConstraintSet constraints) {
		this(textString, textFont, 0, 0, constraints);
		
		// Updates the shape of the box after the original constructor built an empty one
		Canvas c = new Canvas();
		FontMetrics fm = c.getFontMetrics(textFont);
		boxShape = new RoundRectangle2D.Double(0, 0, fm.stringWidth(textString)*1.05 + 6, fm.getHeight()*1.12, 20, 20);
	}
	
	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}
	
	/*
	 * Highlights/un-highlights button if the mouse is/isn't over it
	 */
	public void mouseMoved(int mouseX, int mouseY) {
		if (isEnabled) {
			if (boxShape.contains(mouseX, mouseY))
				isHighlighted = true;
			else
				isHighlighted = false;
		}
	}
	
	/*
	 * Returns true if this button should now do it's action.
	 * @return	true if the x and y values are ontop of this button and this button was pressed in.
	 */
	public boolean mouseReleased(int mouseX, int mouseY) {
		if (isEnabled) {
			if (isPressed) {	// if the button is pressed
				isPressed = false; // de-presses it
				isHighlighted = false;	// un highlights it too
				if (boxShape.contains(mouseX, mouseY))
					return true;	// do action
			} else if (boxShape.contains(mouseX, mouseY))	// if mouse released on a button that wasn't 
				isHighlighted = true;	// highlight it
		}
		return false;	// do not do action
	}
	
	// sets button to be pressed if given coords are ontop of it
	public void mousePressed(int x, int y) {
		if (isEnabled) {
			if (boxShape.contains(x, y))
				isPressed = true;
			else
				isPressed = false;
		}
	}
	
	public void drawToGraphics(Graphics2D g2) {
		// draws Button first, setting the text color along the way
		
		
		// TODO: we don't want to have to do this every frame update, only when frame size has changed
		// add a boolean? componenetListener? static componentListener class?
		
		// rebuilds the box
		double width = boxShape.getBounds2D().getWidth();
		double height = boxShape.getBounds2D().getHeight();
		boxShape = new RoundRectangle2D.Double(constraints.getX() - width/2, constraints.getY() - height/2, width, height, 20, 20);
		
		
		
		if (isPressed || !isEnabled) {	// draws the button pressed in
			g2.setColor(pressedBoxColor);
			g2.fill(boxShape);
			super.setTextColor(pressedTextColor);
		} else if (isHighlighted) {	// draws the button highlighted
			g2.setColor(highlightedBoxColor);
			g2.fill(boxShape);
			super.setTextColor(highlightedTextColor);
		} else {	// draws the regular button
			g2.setColor(regularBoxColor);
			g2.fill(boxShape);
			super.setTextColor(regularTextColor);
		}
		// then draws the text box inside the button
		super.drawToGraphics(g2);
	}
}
