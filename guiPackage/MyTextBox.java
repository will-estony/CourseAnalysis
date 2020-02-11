package guiPackage;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

public class MyTextBox extends Drawable {
	
	// instance variables for text
	private Color textColor;
	private static Color defaultTextColor = Color.WHITE;
	private Font textFont;
	private String textString;
	private float stringX, stringY;
	private double centerX, centerY;
	
	// Constructor that uses default text color
	public MyTextBox(String textString, Font textFont, UIConstraintSet constraints) {
		this(textString, textFont, defaultTextColor, constraints);	// passes params with default color being passed
	}
	// Constructor that allows color to be set
	public MyTextBox(String textString, Font textFont, Color textColor, UIConstraintSet constraints) {
		super(constraints);
		this.textString = textString;
		this.textFont = textFont;
		this.textColor = textColor;
	}
	
	// allows super classes to change text color
	protected void setTextColor(Color c) {
		textColor = c;
	}
	
	// allows changing the text in this text box.
	// text should be automatically re-centered as currently every call to drawToGraphics
	// re-centers text.
	public void setText(String newString) {	textString = newString; }
	public String getText() { return textString; }
	
	// draws text in the specified location, recalculating the location as needed
	public void drawToGraphics(Graphics2D g2) {
		if (textString != null) {	// only draw if string actually exists rn
			
			centerX = constraints.getCenterX();	// resets x position based on constraints
			centerY = constraints.getCenterY();	// resets y position based on constraints
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
}
