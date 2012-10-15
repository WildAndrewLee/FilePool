import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class MetaPanel extends JPanel {
	private JLabel title, id, pass;
	private JTextField idBox;
	private JPasswordField passBox;
	private JButton login;
	
	public MetaPanel(){
		setPreferredSize(new Dimension(400, 150));
		setLayout(null);
		
		title = new JLabel("File Pool Login");
		id = new JLabel("ID: ");
		pass = new JLabel("PW: ");
		idBox = new JTextField(10);
		passBox = new JPasswordField();
		login = new JButton("Log In");
		
		title.setFont(title.getFont().deriveFont(16.0f));
		
		title.setBounds(15, 8, 400, 25);
		id.setBounds(15, 42, 400, 25);
		idBox.setBounds(40, 44, 345, 22);
		pass.setBounds(15, 72, 400, 25);
		passBox.setBounds(40, 74, 345, 22);
		login.setBounds(15, 110, 370, 25);
		
		login.addMouseListener(new Login());
		idBox.addFocusListener(new FieldListener());
		passBox.addFocusListener(new FieldListener());
		idBox.addKeyListener(new FieldListener());
		passBox.addKeyListener(new FieldListener());
		
		add(title);
		add(id);
		add(idBox);
		add(pass);
		add(passBox);
		add(login);
	}
	
	private void login(){
		String id = idBox.getText();
		char[] pass = passBox.getPassword();
		
		String pw = "";
		
		for(int n = 0; n < pass.length; n++){
			pw += pass[n];
		}
		
		pass = null;
		
		if(Main.status.logIn(id, pw)){
			JOptionPane.showMessageDialog(null, "Successfully Logged In!");
			Main.frame.loggedIn();
		}
		else{
			JOptionPane.showMessageDialog(null, "Incorrect ID/PW provided. Please try again.");
		}
	}
	
	private class Login implements MouseListener{
		public void mouseClicked(MouseEvent e){
			login();
		}

		public void mouseEntered(MouseEvent e){}
		public void mouseExited(MouseEvent e){}
		public void mousePressed(MouseEvent e){}
		public void mouseReleased(MouseEvent e){}
	}
	
	private class FieldListener implements FocusListener, KeyListener{
		public void keyTyped(KeyEvent e){			
			if(e.getKeyChar() == '\n' || e.getKeyChar() == '\r'){
				login();
			}
		}
		
		public void keyPressed(KeyEvent e){}
		public void keyReleased(KeyEvent e){}

		public void focusGained(FocusEvent e){
			((JTextField) e.getSource()).selectAll();
		}

		public void focusLost(FocusEvent e){}
	}
}