import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.Timer;

/**
 * The GUI window for File Pool.
 * @author Andrew
 *
 */
public class Frame extends JFrame {
	/**
	 * Reference variables for various components.
	 */
	private Status status = null;
	private MetaPanel metapanel = new MetaPanel();
	private Hub hub = null;
	private Image icon = null;
	
	/**
	 * Animates the frame size on a successful log in.
	 */
	private Action framer = new AbstractAction(){
		public void actionPerformed(ActionEvent e){
        	if(getHeight() < 500 && getWidth() < 800){
        		setSize(getWidth() + 8, getHeight() + 6);
        		
        		center();
        	}
        	else{
        		loggedTimer.stop();
        		setupFrame();
        	}
        	
        	repaint();
        }
    };
	
    /**
     * The timer to animate the frame on log in.
     */
	private Timer loggedTimer = new Timer(1000 / 60, framer);
	
	/**
	 * Default constructor.
	 * @param status The static status object created by the Main class on start.
	 */
	public Frame(Status status){
		super("File Pool");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		Image icon = Toolkit.getDefaultToolkit().getImage("icon.png");
		this.icon = icon;
		this.setIconImage(icon);
		
		this.status = status;
		add(metapanel);
		pack();
		
		center();
		
		setVisible(true);
	}

	/**
	 * Centers the frame.
	 */
	private void center(){
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((dimension.getWidth() - getSize().getWidth()) / 2);
		int y = (int) ((dimension.getHeight() - getSize().getHeight()) / 2);
		setLocation(x, y);
	}
	
	/**
	 * Adds the hub and starts it.
	 */
	private void setupFrame(){
		hub = new Hub();
		hub.setBounds(0, 0, 800, 600);
		add(hub);
		
		MenuStrip strip = new MenuStrip();
		setJMenuBar(strip);
	}
	
	/**
	 * Returns the hub.
	 * @return The hub.
	 */
	public Hub getHub(){
		return hub;
	}
	
	/**
	 * Executes various functions on log in.
	 */
	public void loggedIn(){
		getContentPane().remove(metapanel);
		validate();
		repaint();
		
		setSize(800, 540);
		setupFrame();
		center();
		hub.loadPool();
	}
	
	public void loggedOut(){
		
	}
	
	public Image getIcon(){
		return this.icon;
	}
}