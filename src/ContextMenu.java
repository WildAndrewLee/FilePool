import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.swing.DefaultListModel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

/**
 * Handles various right-click functions.
 * @author Andrew
 *
 */

public class ContextMenu extends JPopupMenu implements ActionListener{
	/**
	 * The buttons to be displayed on context menus.
	 */
	private JMenuItem remove = new JMenuItem("Remove");
	private JMenuItem properties = new JMenuItem("Properties");
	private JMenuItem open = new JMenuItem("Open");
	
	/**
	 * The default constructor for the context menu.
	 */
	public ContextMenu(){
		properties.setEnabled(false);
		
		add(open);
		add(remove);
		add(properties);
		
		open.addActionListener(this);
		remove.addActionListener(this);
		properties.addActionListener(this);
	}
	
	/**
	 * Performs various buttons depending on what button was clicked.
	 */
	public void actionPerformed(ActionEvent e){
		Hub hub = Main.frame.getHub();
		
		if(e.getSource() == open){
			try{
				DefaultListModel model = hub.getModel();
				
				File file = new File(model.get(hub.getIndex()).toString());
				Desktop.getDesktop().open(file);
			}
			catch(IOException e1){
				JOptionPane.showMessageDialog(null, "Could not open file. Please set a default program for this file type first.");
			}
		}
		else if(e.getSource() == remove){
			hub.remove();
		}
	}
}