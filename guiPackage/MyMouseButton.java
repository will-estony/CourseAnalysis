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
	private Color boxColor, regularTextColor, highlightedBoxColor, highlightedTextColor, pressedBoxColor, pressedTextColor;
	private Shape boxShape;
	
	// private boolean to know if button is highlighted or not
	private boolean isHighlighted;
	
	// private boolean to know if button is pressed in or not
	private boolean isPressed;
	
	/*
	private interface actionEventListener {	
		void actionMethod(); 
	}
	// function call to execute when the button is activated
	private actionEventListener myListener;
	*/
	
	public MyMouseButton(String textString, Font textFont, Color regularTextColor, Color highlightedTextColor,
			Color boxColor, Color highlightedBoxColor, double xPos, double yPos, double width, double height) {
		// passes required params to super MyTextBox class
		super(textString, textFont, regularTextColor, xPos, yPos);
		// saves the needed params
		this.regularTextColor = regularTextColor;
		this.highlightedTextColor = highlightedTextColor;
		this.boxColor = boxColor;
		this.highlightedBoxColor = highlightedBoxColor;
		// creates and saves the shape of the box
		boxShape = new RoundRectangle2D.Double(xPos - width/2, yPos - height/2, width, height, 20, 20);
		
		// default pressed colors
		pressedBoxColor = Color.GRAY;
		pressedTextColor = Color.WHITE;
	}
	
	// constructor that uses some default values
	public MyMouseButton(String textString, Font textFont, double xPos, double yPos, double width, double height) {
		this(textString, textFont, Color.WHITE, Color.BLACK, Color.BLUE, Color.WHITE, xPos, yPos, width, height);
	}
	
	// constructor with even more default values
	public MyMouseButton(String textString, Font textFont) {
		this(textString, textFont, 0, 0, 0, 0);
		Canvas c = new Canvas();
		FontMetrics fm = c.getFontMetrics(textFont);
		// creates and updates the shape of the box
		boxShape = new RoundRectangle2D.Double(0, 0, fm.stringWidth(textString)*1.05 + 6, fm.getHeight()*1.12, 20, 20);
	}
	
	public void setIsPressed(boolean isPressed) {
		this.isPressed = isPressed;
	}
	
	/*
	 * Highlights/un-highlights button if the mouse is/isn't over it
	 */
	public void mouseMoved(int mouseX, int mouseY) {
		if (boxShape.contains(mouseX, mouseY))
			isHighlighted = true;
		else
			isHighlighted = false;
	}
	
	/*
	 * Returns true if this button should now do it's action.
	 * @return	true if the x and y values are ontop of this button and this button was pressed in.
	 */
	public boolean mouseReleased(int mouseX, int mouseY) {
		if (isPressed) {	// if the button is pressed
			isPressed = false; // de-presses it
			isHighlighted = false;	// un highlights it too
			if (boxShape.contains(mouseX, mouseY))
				return true;	// do action
		} else if (boxShape.contains(mouseX, mouseY))	// if mouse released on a button that wasn't 
			isHighlighted = true;	// highlight it
		return false;	// do not do action
	}
	
	// sets button to be pressed if given coords are ontop of it
	public void mousePressed(int x, int y) {
		if (boxShape.contains(x, y))
			isPressed = true;
		else
			isPressed = false;
	}
	
	// allows us to reposition text
	public void updatePos(double newX, double newY) {
		super.updatePos(newX, newY);
		// updates position of the button box
		double width = boxShape.getBounds2D().getWidth();
		double height = boxShape.getBounds2D().getHeight();
		boxShape = new RoundRectangle2D.Double(newX - width/2, newY - height/2, width, height, 20, 20);
	}
	
	
	public void drawToGraphics(Graphics2D g2) {
		// draws Button first, setting the text color along the way
		if (isPressed) {	// draws the button pressed in
			g2.setColor(pressedBoxColor);
			g2.fill(boxShape);
			super.setTextColor(pressedTextColor);
		} else if (isHighlighted) {	// draws the button highlighted
			g2.setColor(highlightedBoxColor);
			g2.fill(boxShape);
			super.setTextColor(highlightedTextColor);
		} else {	// draws the regular button
			g2.setColor(boxColor);
			g2.fill(boxShape);
			super.setTextColor(regularTextColor);
		}
		// then draws the text box inside the button
		super.drawToGraphics(g2);
	}
}
