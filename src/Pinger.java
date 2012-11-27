import java.awt.EventQueue;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import java.util.Arrays;

public class Pinger implements Runnable{
	private JTable table;
	private DefaultTableModel model;
	
	public Pinger(JTable table){
		this.table = table;
	}
	
	public void run(){		
		try{			
			model = new DefaultTableModel(){
				public boolean isCellEditable(int x, int y){
					return false;
				}
			};
			
			model.addColumn("IP Address");
			
			int timeout = 1000;
			String subnet = Main.status.getIP().substring(0, Main.status.getIP().lastIndexOf('.'));
			System.out.println(subnet);
			
			for(int n = 0; n < 254; n++){
				try {
					String host = subnet + "." + n;
					
					if(!Arrays.asList(Main.status.getUser().Contacts).contains("host")){
						InetSocketAddress inet = new InetSocketAddress(host, 69);
						
						Socket socket = new Socket();
						
						socket.connect(inet, timeout);
						
						if(socket.isConnected()){
							String[] row = {host};
							model.addRow(row);
						}
					}
				}
				catch(IOException e){
					continue;
				}
			}
			
			EventQueue.invokeLater(new Runnable(){
				public void run(){
					table.setModel(model);
				}
			});
			
			Thread.sleep(10000);
			run();
		}
		catch(InterruptedException e){
			e.printStackTrace();
		}
	}
}