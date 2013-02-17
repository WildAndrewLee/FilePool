import java.awt.Desktop;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class Hub extends JPanel{
	private JLabel dist = new JLabel("Distributing:");
	private JLabel syncing = new JLabel("Storing:");
	private JLabel pool = new JLabel("Pool:");
	
	private DefaultListModel dmodel = new DefaultListModel();
	private DefaultListModel smodel = new DefaultListModel();
	private DefaultTableModel pmodel = new DefaultTableModel(){
		public boolean isCellEditable(int x, int y){
			return false;
		}
	};
	
	private JList distList = new JList(dmodel);
	private JList slist = new JList(smodel);
	private JTable plist = new JTable(pmodel);
	
	private JScrollPane dscroll = new JScrollPane(distList);
	private JScrollPane sscroll = new JScrollPane(slist);
	private JScrollPane pscroll = new JScrollPane(plist);
	
	private ArrayList<Drop> receive = new ArrayList<Drop>();
	private ArrayList<Drop> contactDrops = new ArrayList<Drop>();
	
	private int cIndex = -1;
	private JList sList = null;
	
	private ContextMenu menu = null;
	
	public Hub(){
		setLayout(null);
		setDoubleBuffered(true);
		
		Pool pool = Main.status.getPool();
		
		Drop[] drops = pool.getDrops();
		
		for(int n = 0; n < drops.length; n++){
			if(drops[n].getType().equals("distributor")){
				dmodel.add(dmodel.size(), drops[n].getName());
			}
			else if(drops[n].getType().equals("client")){				
				smodel.add(smodel.size(), drops[n].getName());
				receive.add(drops[n]);
			}
			else{
				JOptionPane.showMessageDialog(null, "A fatal error occured while loading your hub.");
				System.exit(0);
			}
		}
		
		pmodel.addColumn("Name");
		pmodel.addColumn("File ID");
		pmodel.addColumn("File");
		pmodel.addColumn("IP Address");
		
		dscroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		sscroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		pscroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		distList.setBounds(10, 40, 370, 300);
		slist.setBounds(400, 40, 370, 300);
		plist.setBounds(10, 375, 800, 100);
		
		dscroll.setBounds(10, 40, 375, 300);
		sscroll.setBounds(400, 40, 375, 300);
		pscroll.setBounds(10, 372, 765, 100);
		
		dist.setBounds(10, 10, 200, 20);
		syncing.setBounds(400, 10, 200, 20);
		this.pool.setBounds(10, 345, 200, 20);
		
		distList.setVisible(true);
		slist.setVisible(true);
		plist.setVisible(true);
		
		plist.setShowGrid(false);
		
		ListListener ll = new ListListener();
		
		distList.addMouseListener(ll);
		distList.addMouseMotionListener(ll);
		slist.addMouseListener(ll);
		slist.addMouseMotionListener(ll);
		plist.addMouseListener(new PoolListener());
		
		distList.setCellRenderer(new Row());
		slist.setCellRenderer(new Row());
		
		add(dist);
		add(syncing);
		add(dscroll);
		add(sscroll);
		add(this.pool);
		add(pscroll);
		
		distList.setDropTarget(new DropTarget(){
			public synchronized void drop(DropTargetDropEvent e){
				try{
					e.acceptDrop(DnDConstants.ACTION_COPY);
					
					List<File> droppedFiles = (List<File>) e.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
					
					for (File file : droppedFiles){
						Random rand = new Random();
						int id = rand.nextInt(40001) + 10000;
						
						while(Main.distro.getPorts().contains(id)){
							id = rand.nextInt(40001) + 10000;
						}
						
						Drop drop = new Drop(file.getAbsolutePath(), "distributor", Main.status.getIP(), "", id + "");
						
						if(Main.status.getPool().addFile(drop)){
							dmodel.addElement(drop.getPath());
							Main.distro.addDrop(drop);
						}
						else{
							JOptionPane.showMessageDialog(null, "An error occured while adding this file.");
						}
					}
				}
				catch(Exception ex){
					ex.printStackTrace();
				}
			}
		});
		
		menu = new ContextMenu();
		
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
	
	public void loadPool(){
		Pooler pooler = new Pooler(plist);
		Thread thread = new Thread(pooler);
		thread.start();
	}
	
	public int getIndex(){
		return cIndex;
	}
	
	public JList getList(){
		return sList;
	}
	
	public DefaultListModel getModel(){
		return sList == distList ? dmodel : smodel;
	}
	
	public void remove(){
		Object removed = null;
		
		if(sList == distList){
			removed = dmodel.get(cIndex);
			dmodel.removeElementAt(cIndex);
			Main.distro.remove(removed, 1);
		}
		else{
			removed = smodel.get(cIndex);
			smodel.removeElementAt(cIndex);
			Main.distro.remove(removed, 2);
		}
		
		cIndex = -1;
	}
	
	private class PoolListener implements MouseListener{
		public void mouseClicked(MouseEvent e){
			if(SwingUtilities.isLeftMouseButton(e)){
				if(e.getClickCount() == 2 && !e.isConsumed()){
					DefaultTableModel copy = (DefaultTableModel) plist.getModel();
					
					int row = plist.getSelectedRow();
					System.out.println(row);
					
					JFileChooser dialog = new JFileChooser("Choose a save location:");
					dialog.setApproveButtonText("Save");
					
					int choice = dialog.showOpenDialog(null);
					
					if(choice == JFileChooser.APPROVE_OPTION){
						File file = dialog.getSelectedFile();
						
						String path = (String) copy.getValueAt(row, 2);
						String ip = (String) copy.getValueAt(row, 3);
						String id = (String) copy.getValueAt(row, 1);
						
						Drop drop = new Drop(path, "client", ip, file.getAbsolutePath(), id);
						receive.add(drop);
						smodel.addElement((String) drop.getName());
						
						Main.status.getPool().addFile(drop);
						Main.distro.addDrop(drop);
					}
				}
			}
		}

		public void mouseEntered(MouseEvent e){}
		public void mouseExited(MouseEvent e){}
		public void mousePressed(MouseEvent e){}
		public void mouseReleased(MouseEvent e){}
	}
	
	private class ListListener implements MouseListener, MouseMotionListener{
		public void mouseClicked(MouseEvent e){
			if(SwingUtilities.isRightMouseButton(e)){
				JList source = (JList) e.getSource();
				
				int index = source.locationToIndex(e.getPoint());
				
				if(index > -1){
					sList = source;
					cIndex = index;
					
					menu.show(e.getComponent(), e.getX(), e.getY());
					menu.setVisible(true);
				}
			}
			else if(SwingUtilities.isLeftMouseButton(e)){
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
		}
		
		public void mouseMoved(MouseEvent e){
			JList source = (JList) e.getSource();
			
			int index = source.locationToIndex(e.getPoint());
			Rectangle bounds = source.getCellBounds(index, index);
			
			String model = source == distList ? "distributor" : "client";
			
			if(index > -1 && bounds.contains(e.getPoint())){
				Drop drop = null;
				int at = -1;
				Drop[] drops = Main.status.getPool().getDrops();
				
				for(int n = 0; at != index && n < drops.length; n++){
					if(drops[n].getType().equals(model)){
						at++;
						drop = drops[n];
					}
				}
				
				source.setToolTipText(drop.getPath().replaceAll("\\\\", "/"));
			}
			else{
				source.setToolTipText(null);
			}
		}

		public void mouseEntered(MouseEvent e){}
		public void mouseExited(MouseEvent e){}
		public void mousePressed(MouseEvent e){}
		public void mouseReleased(MouseEvent e){}
		public void mouseDragged(MouseEvent e){}
	}
}