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
	private boolean needsAdjusting = true;	// boolean tells us if text needs to be repositioned
	
	// Constructor that assigns default color
	public MyTextBox(String textString, Font textFont, double xPos, double yPos) {
		this(textString, textFont, defaultTextColor, xPos, yPos);
	}
	// Constructor that allows color to be set
	public MyTextBox(String textString, Font textFont, Color textColor, double xPos, double yPos) {
		this.textString = textString;
		this.textFont = textFont;
		this.textColor = textColor;
		centerX = xPos;
		centerY = yPos;
	}
	
	// allows super classes to change text color
	protected void setTextColor(Color c) {
		textColor = c;
	}
	
	// allows us to reposition text
	public void updatePos(double newX, double newY) {
		centerX = newX;
		centerY = newY;
		needsAdjusting = true;
	}
	
	// draws text in the specified location, recalculating the location as needed
	public void drawToGraphics(Graphics2D g2) {
		// if string Location hasn't been calculated yet
		if (needsAdjusting) {	// we do this each time we need some readjusting
			// uses the font metrics of the supplied font and the string to calculate how
			// to place the text in the center of the rectangle
			FontMetrics fontMetrics = g2.getFontMetrics(textFont);
			stringX = (float) (centerX - fontMetrics.stringWidth(textString)/2);
			// TODO the Y value might not be perfect here...
			stringY = (float) (centerY + (fontMetrics.getHeight())/2 - fontMetrics.getDescent());
			needsAdjusting = false;
		}
		// Draws the String
		g2.setFont(textFont);
		g2.setColor(textColor);
		g2.drawString(textString, stringX, stringY);
	}
}
