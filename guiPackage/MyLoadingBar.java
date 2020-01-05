package guiPackage;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;


public class MyLoadingBar extends MyTextBox {
	
	// instance variables for the button
	private Color regularBoxColor, regularTextColor, highlightedBoxColor, highlightedTextColor, pressedBoxColor, pressedTextColor;
    private Shape boxShape;
    private Shape progressBar;
	private UIConstraintSet constraints;
	
	// private boolean to know if button is highlighted or not
	private boolean isHighlighted;
	
	// private boolean to know if button is pressed in or not
	private boolean isPressed;
	
	public MyLoadingBar(String textString, Font textFont, Color regularTextColor, Color highlightedTextColor,
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
	public MyLoadingBar(String textString, Font textFont, double width, double height, UIConstraintSet constraints) {
		this(textString, textFont, Color.WHITE, Color.BLACK, Color.BLUE, Color.WHITE, width, height, constraints);
	}
	
	// constructor with even more default values
	public MyLoadingBar(String textString, Font textFont, UIConstraintSet constraints) {
		this(textString, textFont, 0, 0, constraints);
		
		// Updates the shape of the box after the original constructor built an empty one
		Canvas c = new Canvas();
		FontMetrics fm = c.getFontMetrics(textFont);
		boxShape = new RoundRectangle2D.Double(0, 0, fm.stringWidth(textString)*1.05 + 6, fm.getHeight()*1.12, 20, 20);
	}
	

	public void drawToGraphics(Graphics2D g2, double currentItem, double numItems) {
        
		double width = boxShape.getBounds2D().getWidth();
		double height = boxShape.getBounds2D().getHeight();
		boxShape = new RoundRectangle2D.Double(constraints.getX() - width/2, constraints.getY() - height/2, width, height, 20, 20);
        progressBar = new RoundRectangle2D.Double(constraints.getX() - width/2, constraints.getY() - height/2, (currentItem/numItems) * width, height, 20, 20);
		g2.setColor(regularBoxColor);
        g2.draw(boxShape);
        g2.fill(progressBar);

	}
}