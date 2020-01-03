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
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import defaultPackage.Athlete;
import defaultPackage.Meet;
import defaultPackage.Parsable;
import defaultPackage.ParsingThread;
import defaultPackage.Team;
import defaultPackage.tfrrsURL;
import defaultPackage.Metrics;
import guiPackage.UIConstraint.ConstraintType;


@SuppressWarnings("serial")
public class MenuPanel extends JPanel implements KeyListener, MouseListener, MouseMotionListener {
	/* Fonts */
	private static final Font comicSans = new Font("Comic Sans MS", Font.PLAIN, 24);
	private static final Font headerFont = new Font("TimeRoman", Font.PLAIN, 42);
	private static final Font defaultFont = new Font("TimeRoman", Font.PLAIN, 24);
	private static final Font smallFont = new Font("TimeRoman", Font.PLAIN, 16);
	
	// reference to gui manager that created this panel
	private guiManager gm;
	
	// ArrayList of buttons that appear on the menu
	private ArrayList<MyMouseButton> buttons;
	
	// ArrayList of text boxes that appear on the menu
	private ArrayList<MyTextBox> textBoxes;
	
	// search bar
	private JTextField searchField;
	// status display
	private StatusDisplay searchDisplay;
	// loading bar
	private MyLoadingBar load;

	private static boolean loading;

	private Metrics metrics;
	
	// Constructor
	public MenuPanel(guiManager gm) {
		this.gm = gm;	// saves gui manager reference
		this.metrics = new Metrics();
		
		
		// needed to make the keyboard/mouse inputs apply to this
		setFocusable(true);
		
		// adds this class' listeners to this class' JPanel
		this.addKeyListener(this);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);

		loading = false;
		setBackground(Color.BLACK);	// sets the background color of the panel
		
		load = new MyLoadingBar("", defaultFont, 250, 10, new UIConstraintSet(gm,
		new UIConstraint(0.5),
		new UIConstraint(0.75)));

		// creates all the buttons on the screen
		buttons = new ArrayList<MyMouseButton>();
		// creates quit button
		buttons.add(new MyMouseButton("Quit", defaultFont, 80, 34,
				new UIConstraintSet(gm,
						new UIConstraint(-75, null),
						new UIConstraint(-75, null))));
		// creates test button
		buttons.add(new MyMouseButton("Parse TFRRS URL", defaultFont,
				new UIConstraintSet(gm,
						new UIConstraint(0.2),
						new UIConstraint(80, null))));
		
		// creates all the text boxes on this panel
		textBoxes = new ArrayList<MyTextBox>();
		
		// creates a StatusDiplay object to display the current status of the search
		searchDisplay = new StatusDisplay(6, 26, smallFont,
				new UIConstraintSet(gm,
					new UIConstraint(0.5),
					new UIConstraint(0.5)));
		
		
		// TESTING
		searchField = new JTextField("Enter TFRRS URL here", 50);
		searchField.addKeyListener(this);
		searchField.addFocusListener(new FocusListener(){
			@Override
            public void focusGained(FocusEvent e) {
				//This clears the textfield when the user clicks on it
				//the first time
                if(searchField.getText().equals("Enter TFRRS URL here")){
					searchField.setText("");
				}
            }

            @Override
            public void focusLost(FocusEvent e) {
                
            }
        });
		
		
		
		add(searchField);
	}
	
	public static void setLoading(boolean b){ loading = b;}
	
	// attempts to read in the team that is currently in the search bar
	// CURRENTLY JUST TESTING NOT PERMANANT
	private void attemptURLParse() {
		
		// we identify the URL
		tfrrsURL potentialURL = new tfrrsURL(searchField.getText());
		
		// we create a different object depending on the type of url supplied
		Parsable urlObject = null;
		
		switch (potentialURL.getType()){
		case ATHLETE:
			urlObject = (Athlete)Athlete.createNew(Athlete.urlToID(potentialURL.getURLString()), searchDisplay);
			break;
		case TEAM:
			urlObject = Team.createNew(potentialURL.getURLString(), searchDisplay);
			break;
		case MEET:
			urlObject = Meet.createNew(potentialURL.getURLString(), searchDisplay);
			break;
		case UNKNOWN:	// end function call if URL unknown
			searchDisplay.writeNewLine("\"" + searchField.getText() + "\" is not a valid tfrrs URL");
			return;
		}
		metrics.setNumItems(100.0);
		urlObject.setMetrics(metrics);
		

		// attempt to parse the object
		// starts a thread that will parse the object in the background
		// while updating the status object
		new ParsingThread(urlObject);
	}
	
	
	//private int lastScreenWidth, lastScreenHeight;
	/* overriding paintComponent allows us to paint things to the screen */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);	// passes g to overridden super method
		// cast the Graphics interface object to a Graphics2D object to allow us
		// access to specific painting methods
		Graphics2D g2 = (Graphics2D) g;
		
		/*
		// if the screen size was changed then update all the component positions
		if (gm.getWidth() != lastScreenWidth || gm.getHeight() != lastScreenHeight) {
			lastScreenWidth = gm.getWidth();
			lastScreenHeight = gm.getHeight();
			updateComponentPositions();
		}*/
		
		// draws every button in the array list
		for (MyMouseButton button : buttons)
			button.drawToGraphics(g2);
		// draws every text box in the array list
		for (MyTextBox textBox : textBoxes)
			textBox.drawToGraphics(g2);
		// draws search status and loading bar
		if(loading){
			load.drawToGraphics(g2,metrics.getCurrentItem(), metrics.getNumItems());
			
		}
		searchDisplay.drawToGraphics(g2);
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
		for (MyMouseButton button : buttons)
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
		for (MyMouseButton button : buttons)
			button.mousePressed(mouseX, mouseY);

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// saves mouse x and y
		int mouseX = e.getX();
		int mouseY = e.getY();
		// iterates along each button
		for (int i = 0; i < buttons.size(); i++) {
			// if each button is clicked then perform the button's action
			if (buttons.get(i).mouseReleased(mouseX, mouseY))
				switch (i) {
					case 0:	// if quit button is clicked
						gm.quit();
						break;
					case 1:	// if search button is clicked
						attemptURLParse();
						break;
				}
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int keyID = e.getKeyCode();	// saves the ID of the key pressed;
		switch (keyID) {
		case KeyEvent.VK_ESCAPE:
			gm.quit();	// quits program if escape key pressed
			break;
		case KeyEvent.VK_ENTER:	// searches URL if enter is pressed
			attemptURLParse();
			break;
			
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {}

	@Override
	public void keyTyped(KeyEvent arg0) {}
}
