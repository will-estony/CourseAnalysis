package guiPackage;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

public class MyTextBox implements Drawable {
	
	// instance variables for text
	private Color textColor;
	private static Color defaultTextColor = Color.WHITE;
	private Font textFont;
	private String textString;
	private float stringX, stringY;
	private double centerX, centerY;
	
	
	private UIConstraintSet constraints = null;
	
	// Main Constructor that uses new Constraint system
	public MyTextBox(String textString, Font textFont, UIConstraintSet constraints) {
		this(textString, textFont, defaultTextColor, constraints);	// passes params with default color being passed
	}
	// Constructor that allows color to be set
	public MyTextBox(String textString, Font textFont, Color textColor, UIConstraintSet constraints) {
		this.textString = textString;
		this.textFont = textFont;
		this.constraints = constraints;
		this.textColor = textColor;
	}
	
	// allows super classes to change text color
	protected void setTextColor(Color c) {
		textColor = c;
	}
	
	// draws text in the specified location, recalculating the location as needed
	public void drawToGraphics(Graphics2D g2) {
		
		// TODO: these calculations shouldn't have to happen every time, they should only have to happen 
		// each time the screen size is changed
		
		centerX = constraints.getX();	// resets x position based on constraints and screen width
		centerY = constraints.getY();	// resets y position based on constraints and screen height
		// uses the font metrics of the supplied font and the string to calculate how
		// to place the text in the center of the rectangle
		FontMetrics fontMetrics = g2.getFontMetrics(textFont);
		stringX = (float) (centerX - fontMetrics.stringWidth(textString)/2);
		// TODO the Y value might not be perfect here...
		stringY = (float) (centerY + (fontMetrics.getHeight())/2 - fontMetrics.getDescent());
		
		
		
		// Draws the String
		g2.setFont(textFont);
		g2.setColor(textColor);
		g2.drawString(textString, stringX, stringY);
	}
}
