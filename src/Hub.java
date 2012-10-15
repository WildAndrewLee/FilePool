import java.awt.Desktop;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Hub extends JPanel{
	private JLabel dist = new JLabel("Distributing:");
	private JLabel syncing = new JLabel("Storing:");
	
	private DefaultListModel dmodel = new DefaultListModel();
	private DefaultListModel smodel = new DefaultListModel();
	
	private JList distList = new JList(dmodel);
	private JList slist = new JList(smodel);
	
	private ArrayList<String> receive = new ArrayList<String>();
	
	public Hub(){
		setLayout(null);
		
		Pool pool = Main.status.getPool();
		
		String[][] files = pool.Files;
		
		for(int n = 0; n < files.length; n++){
			if(files[n][1].equals("distributor")){
				dmodel.add(dmodel.size(), files[n][0]);
			}
			else if(files[n][1].equals("client")){
				smodel.add(smodel.size(), files[n][3]);
				receive.add(files[n][3]);
			}
			else{
				JOptionPane.showMessageDialog(null, "A fatal error occured while loading your hub.");
				System.exit(0);
			}
		}
		
		distList.setBorder(BorderFactory.createEtchedBorder());
		slist.setBorder(BorderFactory.createEtchedBorder());
		
		distList.setBounds(10, 40, 375, 300);
		slist.setBounds(400, 40, 375, 300);
		
		dist.setBounds(10, 10, 200, 20);
		syncing.setBounds(400, 10, 200, 20);
		
		distList.setVisible(true);
		slist.setVisible(true);
		
		distList.addMouseListener(new ListListener());
		slist.addMouseListener(new ListListener());
		
		distList.setCellRenderer(new Row());
		slist.setCellRenderer(new Row());
		
		add(dist);
		add(syncing);
		add(distList);
		add(slist);
		
		Main.status.startDistro();
	}
	
	public void updateProgress(String dlto, int percent){
		for(int n = 0; n < receive.size(); n++){
			String val = receive.get(n).toString();
			
			if(val.equals(dlto)){				
				if(percent == 100){
					smodel.set(n, dlto);
				}
				else{
					smodel.set(n, dlto + "   " + percent + "%");
					slist.setCellRenderer(new Row());
				}
			}
		}
		
		slist.repaint();
	}
	
	private class ListListener implements MouseListener{
		public void mouseClicked(MouseEvent e){
			if(e.getClickCount() == 2 && !e.isConsumed()){
				JList source = (JList) e.getSource();
				
				try{
					File file = new File(source.getSelectedValue().toString());
					Desktop.getDesktop().open(file);
				}
				catch(IOException e1){
					JOptionPane.showMessageDialog(null, "Could not open file. Please set a default program for this file type first.");
				}
			}
		}
		public void mouseEntered(MouseEvent e){}
		public void mouseExited(MouseEvent e){}
		public void mousePressed(MouseEvent e){}
		public void mouseReleased(MouseEvent e){}
	}
}