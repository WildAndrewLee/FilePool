import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class ContactPanel extends JPanel {
	private DefaultTableModel cmodel = new DefaultTableModel();
	private DefaultTableModel amodel = new DefaultTableModel(){
		public boolean isCellEditable(int x, int y){
			return false;
		}
	};
	
	private JTable clist = new JTable(cmodel);
	private JScrollPane cscroll = new JScrollPane(clist);
	
	private JTable alist = new JTable(amodel);
	private JScrollPane ascroll = new JScrollPane(alist);
	
	private JButton add = new JButton("Add Contact");
	private JButton remove = new JButton("Remove Contact");
	
	public ContactPanel(){
		setPreferredSize(new Dimension(700, 360));
		setLayout(null);
		
		String[][] contacts = Main.status.getUser().Contacts;
		
		cmodel.addColumn("Name");
		cmodel.addColumn("IP Address");
		
		amodel.addColumn("Name");
		amodel.addColumn("IP Address");
		
		for(int n = 0; n < contacts.length; n++){
			String[] row = {contacts[n][0], contacts[n][1]};
			
			cmodel.addRow(row);
		}
		
		clist.setModel(cmodel);
		
		clist.setBounds(0, 0, 335, 280);
		cscroll.setBounds(10, 10, 335, 280);
		
		alist.setBounds(0, 0, 335, 280);
		ascroll.setBounds(355, 10, 335, 280);
		
		add.setBounds(10, 300, 680, 25);
		remove.setBounds(10, 330, 680, 25);
		
		add.addActionListener(new Listener());
		remove.addActionListener(new Listener());
		
		add(add);
		add(remove);
		add(cscroll);
		add(ascroll);
		
		ping();
	}
	
	private void ping(){
		Pinger pinger = new Pinger(alist);
		Thread thread = new Thread(pinger);
		thread.start();
	}
	
	private class Listener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			if(e.getSource() == add){
				if(alist.getSelectedRow() != -1){
					DefaultTableModel copy = (DefaultTableModel) alist.getModel();
					String ip = (String) copy.getValueAt(alist.getSelectedRow(), 0);
					String name = JOptionPane.showInputDialog(null, "Please choose a name for this contact: ");
					
					if(Main.status.getUser().addContact(name, ip)){
						copy.removeRow(alist.getSelectedRow());
						alist.setModel(copy);
						
						cmodel.addRow(new String[]{name, ip});
						clist.setModel(cmodel);
					}
					else{
						JOptionPane.showMessageDialog(null, "An error occured while adding the new contact.");
					}
				}
			}
			else if(e.getSource() == remove){
				if(clist.getSelectedRow() != -1){
					DefaultTableModel copy = (DefaultTableModel) clist.getModel();
					
					if(Main.status.getUser().removeContact((String) copy.getValueAt(clist.getSelectedRow(), 1))){
						copy.removeRow(clist.getSelectedRow());
						clist.setModel(copy);
					}
					else{
						JOptionPane.showMessageDialog(null, "An error occured while removing the contact.");
					}
				}
			}
		}
	}
}