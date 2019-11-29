package guiPackage;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JPanel;

@SuppressWarnings("serial")
class MenuPanel extends JPanel implements KeyListener, MouseListener, MouseMotionListener {
	/* Fonts */
	private static final Font comicSans = new Font("Comic Sans MS", Font.PLAIN, 24);
	private static final Font headerFont = new Font("TimeRoman", Font.PLAIN, 42);
	private static final Font defaultFont = new Font("TimeRoman", Font.PLAIN, 24);
	private static final Font smallFont = new Font("TimeRoman", Font.PLAIN, 16);
	
	// reference to gui manager that created this panel
	private guiManager gm;
	
	// ArrayList of buttons that appear on the menu
	private ArrayList<MyMouseButton> menuButtons;
	
	// Constructor
	public MenuPanel(guiManager gm) {
		this.gm = gm;	// saves gui manager reference
		
		// needed to make the keyboard/mouse inputs apply to this
		setFocusable(true);
		
		// adds this class' listeners to this class' JPanel
		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		
		setBackground(Color.BLACK);	// sets the background color of the panel
		
		menuButtons = new ArrayList<MyMouseButton>();
		// creates a button that does nothing
		menuButtons.add(new MyMouseButton("Quit", defaultFont, 300, 300, 80, 32));
		
	}
	
	/* overriding paintComponent allows us to paint things to the screen */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);	// passes g to overridden super method
		// cast the Graphics interface object to a Graphics2D object to allow us
		// access to specific painting methods
		Graphics2D g2 = (Graphics2D) g;
		
		// draws every button in the array list
		for (MyMouseButton button : menuButtons)
			button.drawToGraphics(g2);
		
	}
	
	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// saves mouse x and y
		int mouseX = e.getX();
		int mouseY = e.getY();
		// iterates along each button
		for (MyMouseButton button : menuButtons)
			button.mouseMoved(mouseX, mouseY);
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// saves mouse x and y
		int mouseX = e.getX();
		int mouseY = e.getY();
		// iterates along each button
		for (MyMouseButton button : menuButtons)
			button.mousePressed(mouseX, mouseY);

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// saves mouse x and y
		int mouseX = e.getX();
		int mouseY = e.getY();
		// iterates along each button
		for (int i = 0; i < menuButtons.size(); i++) {
			// if each button is clicked then perform the button's action
			if (menuButtons.get(i).mouseReleased(mouseX, mouseY))
				switch (i) {
					case 0:
						gm.quit();
						break;
				}
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int keyID = e.getKeyCode();	// saves the ID of the key pressed;
		switch (keyID) {
		case KeyEvent.VK_ESCAPE:
			gm.quit();	// quits program if escape clicked
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {}

	@Override
	public void keyTyped(KeyEvent arg0) {}
}
