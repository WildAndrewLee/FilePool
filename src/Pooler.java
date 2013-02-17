import java.awt.EventQueue;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class Pooler implements Runnable{
	private JTable table;
	private DefaultTableModel model;
	
	public Pooler(JTable table){
		this.table = table;
	}
	
	public void run(){		
		try{
			String[][] contacts = Main.status.getUser().Contacts;
			
			model = new DefaultTableModel(){
				public boolean isCellEditable(int x, int y){
					return false;
				}
			};
			
			model.addColumn("Name");
			model.addColumn("File ID");
			model.addColumn("File");
			model.addColumn("IP Address");
			
			for(int n = 0; n < contacts.length; n++){			
				String ip = contacts[n][1];
				
				Drop[] thoseDrops = Main.status.getPool().getDrops(ip);
				
				if(thoseDrops == null){
					continue;
				}
				
				for(int k = 0; k < thoseDrops.length; k++){
					if(thoseDrops[k].getType().equals("distributor")){
						String[] row = {contacts[n][0], thoseDrops[k].getID(), thoseDrops[k].getName(), contacts[n][1]};
						
						model.addRow(row);
					}
				}
			}
			
			EventQueue.invokeLater(new Runnable(){
				public void run(){
					table.setModel(model);
				}
			});
			
			Thread.sleep(1000);
			run();
		}
		catch(InterruptedException e){
			e.printStackTrace();
		}
	}
}