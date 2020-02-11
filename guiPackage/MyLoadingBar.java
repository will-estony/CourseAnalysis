package guiPackage;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;


public class MyLoadingBar extends MyTextBox {
	
	// instance variables for the button
	private Color regularBoxColor;
    private Shape boxShape;
    private Shape progressBar;
	private UIConstraintSet constraints;
	
	public MyLoadingBar(String textString, Font textFont, Color regularTextColor,
			Color regularBoxColor, UIConstraintSet constraints) {
		// passes required params to super MyTextBox class
		super(textString, textFont, regularTextColor, constraints);
		// saves the needed params
		this.constraints = constraints;
		this.regularBoxColor = regularBoxColor;
		// creates and saves the shape of the box
		boxShape = new RoundRectangle2D.Double(constraints.getX(), constraints.getY(), constraints.getWidth(), constraints.getHeight(), 20, 20);
		
	}
	
	// constructor that uses some default values
	public MyLoadingBar(String textString, Font textFont, UIConstraintSet constraints) {
		this(textString, textFont, Color.WHITE, Color.BLUE, constraints);
	}
	

	public void drawToGraphics(Graphics2D g2, double currentItem, double numItems) {
        
		double width = constraints.getWidth();
		double height = constraints.getHeight();
		double xPos = constraints.getX();
		double yPos = constraints.getY();
		boxShape = new RoundRectangle2D.Double(xPos, yPos, width, height, 20, 20);
        progressBar = new RoundRectangle2D.Double(xPos, yPos, (currentItem/numItems) * width, height, 20, 20);
		g2.setColor(regularBoxColor);
        g2.draw(boxShape);
        g2.fill(progressBar);

	}
}