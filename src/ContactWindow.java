import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.Timer;

public class ContactWindow extends JFrame {
	private ContactPanel contactpanel = new ContactPanel();
	
	public ContactWindow(){
		super("Contact Manager");
		
		setIconImage(Main.frame.getIcon());
		
		add(contactpanel);
		pack();
		
		center();
	}

	private void center(){
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((dimension.getWidth() - getSize().getWidth()) / 2);
		int y = (int) ((dimension.getHeight() - getSize().getHeight()) / 2);
		setLocation(x, y);
	}
}