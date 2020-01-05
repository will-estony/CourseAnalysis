/*
 * This Class is to contain everything needed for a Java Swing gui that will run the program
 */

package guiPackage;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class guiManager extends JFrame implements Runnable {
	// starting and minimum resolution
	private static int startingWidth = 800, startingHeight = 600;
	private final static double desiredFPS = 60;

	private JPanel activePanel;	// pointer to currently active JPanel
	private boolean running;
	private Thread guiThread;
	
	public guiManager() {
		// initialization stuff for the frame
		setTitle("D3 XC Course Analyzer");		// sets the title of the Window (the JFrame)
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	// frees up memory when the window is closed
		setResizable(true);						// allows/disallows resizing of window
		setLocationByPlatform(true);			// lets the OS handle the positioning of the window
		
		//setPreferredSize(new Dimension(startingWidth, startingHeight));
		// sets the minimum size the user is allowed to resize to
		setMinimumSize(new Dimension(startingWidth, startingHeight));
		
		// creates a MenuPanel and adds it to this JFrame
		activePanel = new MenuPanel(this);
		//setPreferredSize(new Dimension(startingWidth, startingHeight));
		//setMinimumSize(new Dimension(startingWidth, startingHeight));
		//activePanel.setSize(startingWidth, startingHeight);
		add(activePanel);	// adds the MenuPanel to this JFrame
		setVisible(true);	// makes this JFrame visible
		
		// creates and starts a Thread for the JFrame rendering/drawing
		running = true;
		guiThread = new Thread(this);
		guiThread.start();	// "starting" the thread calls the run() method from attached class
	}
	
	// Disposes of this JFrame
	public void quit() {
		if (running) {
			System.out.println("Ending program...");
			running = false;
			try {
				guiThread.join();	// waits for main thread to join
			} catch (InterruptedException e) {e.printStackTrace();}
		}
		this.dispose();	// kills this JFrame
		System.out.println("Done.");
		System.exit(0);	// we end with a call to System.exit(0) to terminate ALL working threads.
	}
	
	/**
	 * This method is called when the guiThread begins. Contains main loop.
	 * Found this strategy on an online game tutorial.
	 */
	@Override
	public void run() {
		final double desiredCalculationsPerSec = desiredFPS;
		final double desiredFramesPerSecond = desiredFPS;
		long prev_frame_tick = System.nanoTime(), curr_frame_tick, timeElapsed,
				prev_physics_tick = prev_frame_tick, prev_paint_tick = prev_physics_tick;
		double ns = 1000000000 / desiredCalculationsPerSec;
		double delta = 0;
		
		// Main Loop
		while (running) {
		    curr_frame_tick = System.nanoTime();
		    timeElapsed = curr_frame_tick - prev_frame_tick;
		    delta += timeElapsed / ns;
		    prev_frame_tick = curr_frame_tick;
		    
		    /* not used rn
		    // updates panel desiredCalculationsPerSec times a second
		    if (delta >= 1) {
		    	// sends the number of nanoseconds since the last physics tick happened
		    	if (activePanel != null)
		    		activePanel.tick(curr_frame_tick - prev_physics_tick);
		    	prev_physics_tick = curr_frame_tick;
		    	delta--;
		    } */
		    
		    // if statement fires only as many times as allowed by desiredFramesPerSecond
		    // draws panel desiredFramesPerSecond times per second
		    if (curr_frame_tick - prev_paint_tick >= (1000000000 / desiredFramesPerSecond)) {
				if (activePanel != null)
					activePanel.repaint();

			    prev_paint_tick = curr_frame_tick;
		    }
		}
	}
}