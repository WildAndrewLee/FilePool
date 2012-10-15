import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.Timer;

public class Frame extends JFrame {
	private Status status = null;
	private MetaPanel metapanel = new MetaPanel();
	private Hub hub = null;
	
	private Action framer = new AbstractAction(){
		public void actionPerformed(ActionEvent e){
        	if(getHeight() < 500 && getWidth() < 800){
        		setSize(getWidth() + 8, getHeight() + 6);
        		
        		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        		int x = (int) ((dimension.getWidth() - getSize().getWidth()) / 2);
        		int y = (int) ((dimension.getHeight() - getSize().getHeight()) / 2);
        		setLocation(x, y);
        	}
        	else{
        		loggedTimer.stop();
        		setupFrame();
        	}
        	
        	repaint();
        }
    };
	
	private Timer loggedTimer = new Timer(1000 / 60, framer);
	
	public Frame(Status status){
		super("File Pool");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		this.status = status;
		add(metapanel);
		pack();
		
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((dimension.getWidth() - getSize().getWidth()) / 2);
		int y = (int) ((dimension.getHeight() - getSize().getHeight()) / 2);
		setLocation(x, y);
		
		setVisible(true);
	}
	
	private void setupFrame(){
		hub = new Hub();
		hub.setBounds(0, 0, 800, 500);
		add(hub);
	}
	
	public Hub getHub(){
		return hub;
	}
	
	public void loggedIn(){
		getContentPane().remove(metapanel);
		validate();
		repaint();
		
		loggedTimer.start();
	}
	
	public void loggedOut(){
		
	}
}