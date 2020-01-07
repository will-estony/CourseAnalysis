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
import java.util.HashMap;

import javax.swing.JPanel;
import javax.swing.JTextField;

import defaultPackage.Athlete;
import defaultPackage.Meet;
import defaultPackage.Parsable;
import defaultPackage.ParsingThread;
import defaultPackage.Team;
import defaultPackage.tfrrsURL;
import defaultPackage.Metrics;
import defaultPackage.Spreadsheet;

@SuppressWarnings("serial")
public class MenuPanel extends JPanel implements KeyListener, MouseListener, MouseMotionListener {
	/* Fonts */
	private static final Font comicSans = new Font("Comic Sans MS", Font.PLAIN, 24);
	private static final Font headerFont = new Font("TimeRoman", Font.PLAIN, 42);
	private static final Font defaultFont = new Font("TimeRoman", Font.PLAIN, 24);
	private static final Font smallFont = new Font("TimeRoman", Font.PLAIN, 16);

	private static final String PARSE_BUTTON_TEXT = "Parse Team";
	
	// reference to gui manager that created this panel
	private guiManager gm;
	
	// ArrayList of buttons that appear on the menu
	private ArrayList<MyMouseButton> buttons;
	private MyMouseButton parsingButton;
	
	// ArrayList of text boxes that appear on the menu
	private ArrayList<MyTextBox> textBoxes;

	//Will be populated with each team and their respective urls
	ArrayList<HashMap<String, String>> teams;
	
	// search bar
	private JTextField searchField;
	// status display
	private StatusDisplay statusDisplay;
	// loading bar
	private MyLoadingBar load;

	private static boolean loading;

	private Metrics metrics;
	
	private TeamJList teamList;
	
	// Constructor
	public MenuPanel(guiManager gm) {
		this.setLayout(null);	// removes layout
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
		
		

		// creates all the buttons on the screen
		// creates quit button so that it is a fixed num of pixels from the bottom right corner
		MyMouseButton quitButton = new MyMouseButton("Quit", defaultFont, 66, 34,
				new UIConstraintSet(gm,
						new UIConstraint(-60, null),
						new UIConstraint(-70, null)));
		// creates options button so tht it is 100 pixels to the left of the quit button
		MyMouseButton optionsButton = new MyMouseButton("Options", defaultFont, 100, 34,
				quitButton.createOffsetContraintSet(-92, 0));
		
		// temp until we make an options screen
		optionsButton.setEnabled(false);
		
		// adds all buttons to list
		buttons = new ArrayList<MyMouseButton>();
		buttons.add(quitButton);
		buttons.add(optionsButton);
		
		// creates parse button
		parsingButton = new MyMouseButton(PARSE_BUTTON_TEXT, defaultFont,
				new UIConstraintSet(gm,
						new UIConstraint(129, null),
						new UIConstraint(72, null)));
		buttons.add(parsingButton);
		
		// creates all the text boxes on this panel
		textBoxes = new ArrayList<MyTextBox>();
		
		// creates a StatusDiplay object to display the current status of the search
		statusDisplay = new StatusDisplay(5, 26, smallFont,
			new UIConstraintSet(gm,
				new UIConstraint(0.70),
				new UIConstraint(70, null)));
		
		// places the loading bar 100 pixels below the center of the searchDisplay
		load = new MyLoadingBar("", defaultFont, 250, 10, 
				statusDisplay.createOffsetContraintSet(0, 100));
		
		searchField = new JTextField("Enter TFRRS URL here", 50);
		searchField.setBounds(20, 20, 300, 25);	// sets location/size
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
            public void focusLost(FocusEvent e) {}
        });
		add(searchField);
		
		
		
		
		/* Testing with a list */
		
		// create an action listener that fires when the screen resolution changes
		// and that should be when the draw method is called, not 60 fps like rn?
		// https://docs.oracle.com/javase/tutorial/uiswing/events/componentlistener.html
		// tried that ^ and it worked but then other components like my mouse buttons stop working the same way
		
		// void ensureIndexIsVisible(int): Scroll so that the specified index 
		// is visible within the viewport that this list is in.
		

		MyTextBox TeamHeader = new MyTextBox("Teams", headerFont, 
			new UIConstraintSet(gm,
				new UIConstraint(125, null),
				new UIConstraint(148, null)));
		textBoxes.add(TeamHeader);
		
		teams = Spreadsheet.importTeams();
		
		teamList = new TeamJList(this, searchField);
		teamList.populateList(teams, statusDisplay);
	}
	
	
	
	public static void setLoading(boolean b){ loading = b;}
	
	// attempts to read in the team that is currently in the search bar
	// CURRENTLY JUST TESTING NOT PERMANANT
	private void attemptURLParse() {
		// we identify the URL

		//THIS IS AN IMPORTANT LINE PLEASE READ!
		/*
		teams is the arraylist from the spreadsheet parser that contains the team names and the urls.
		currently every team has a women's url but not a mens url because there are more women's teams 
		than guys. When the user clicks on one of the teams in the beautiful scroll list (nice job implement that)
		instead of having the url appear in the search bar, we now have the team name appear in the search bar,
		this way no links have to displayed on the gui == much cleaner in my opinion. however in order for the link
		to get parsed we need to map the team name to the link which I use the hashmap located at teams.get(1) ie the womens
		map. The point of this comment is to say that we are parsing womens links not mens links but we should
		figure out a way (maybe a double column approach) to differentiate between the two. :)
		*/
		tfrrsURL potentialURL = new tfrrsURL(teams.get(1).get(searchField.getText()));
		
		// we create a different object depending on the type of url supplied
		Parsable urlObject = null;
		
		switch (potentialURL.getType()){
		case ATHLETE:
			urlObject = Athlete.createNew(Athlete.urlToID(potentialURL.getURLString()), statusDisplay);
			break;
		case TEAM:
			urlObject = Team.createNew(potentialURL.getURLString(), statusDisplay);
			break;
		case MEET:
			urlObject = Meet.createNew(potentialURL.getURLString(), statusDisplay);
			break;
		case UNKNOWN:	// end function call if URL unknown
			statusDisplay.writeNewLine("\"" + searchField.getText() + "\" is not a valid tfrrs URL");
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
		// draws loading bar
		// disables parse button while loading
		if(loading){
			parsingButton.setEnabled(false);
			load.drawToGraphics(g2,metrics.getCurrentItem(), metrics.getNumItems());
		} else
			parsingButton.setEnabled(true);
		statusDisplay.drawToGraphics(g2);
	}
	
	@Override
	public void mouseDragged(MouseEvent arg0) {}

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
	public void mouseClicked(MouseEvent arg0) {}

	@Override
	public void mouseEntered(MouseEvent arg0) {}

	@Override
	public void mouseExited(MouseEvent arg0) {}

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
			MyMouseButton currButton = buttons.get(i);
			// if each button is clicked then perform the button's action
			if (currButton.mouseReleased(mouseX, mouseY))
				switch (currButton.getText()) {
					case "Quit":	// if quit button is clicked
						gm.quit();
						break;
					case "Options":	// if options button is clicked
						break;
					case PARSE_BUTTON_TEXT:	// if parse button clicked
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
