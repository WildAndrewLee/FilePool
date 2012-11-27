import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class MenuStrip extends JMenuBar implements ActionListener{	
	private JMenu file = new JMenu();
	private JMenuItem contacts = new JMenuItem();
	private JMenuItem exit = new JMenuItem();
	private ContactWindow contact = new ContactWindow();
	
	public MenuStrip(){
		file.setText("File");
		contacts.setText("Contacts");
		exit.setText("Exit");
		
		file.add(contacts);
		file.add(exit);
		
		contacts.addActionListener(this);
		exit.addActionListener(this);

		add(file);
	}
	
	public void actionPerformed(ActionEvent e){
		if(e.getSource() == exit){
			Main.frame.dispose();
		}
		else if(e.getSource() == contacts){
			contact.setVisible(true);
		}
	}
}