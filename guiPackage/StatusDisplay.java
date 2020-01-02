package guiPackage;

import java.awt.Font;
import java.awt.Graphics2D;

// this class is for displaying text to the screen in an updating fashion
// where when a new status is displayed on the bottom it pushes all prior
// statuses up a line, like printing to the console

public class StatusDisplay implements Drawable {
	// array of text boxes, aka each "line"
	// index 0 being the bottom one
	private MyTextBox[] textBoxes;

	public StatusDisplay(int numLines, int lineHeights, Font font, UIConstraintSet baseConstraints) {
		textBoxes = new MyTextBox[numLines];	// allocates array
		// fills array with text boxes such that they are spaced out evenly
		// and centered at the given baseConstraints
		for (int i = 0; i < numLines; i++) {
			textBoxes[i] = new MyTextBox(null, font, baseConstraints.createOffsetContraintSet(
					0, (numLines/2 - i)*((double) lineHeights)));
		}
	}
	
	public void writeNewLine(String newLine) {
		// moves all prior Strings up by one
		for (int i = (textBoxes.length - 1); i > 0; i--)
			textBoxes[i].setText(textBoxes[i-1].getText());
		// writes new String to bottom text box
		textBoxes[0].setText(newLine);
	}
	
	// draws all text boxes
	public void drawToGraphics(Graphics2D g2) {
		for (MyTextBox textBox : textBoxes)
			textBox.drawToGraphics(g2);
	}
}
