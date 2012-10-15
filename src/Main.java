import java.awt.EventQueue;
import java.awt.Font;
import javax.swing.UIManager;
import java.util.Enumeration;
import javax.swing.plaf.FontUIResource;;

public class Main {
	public static Status status = new Status();
	public static Frame frame = null;
	public static Distributor distro = null;
	
	public static void main(String[] args){
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Enumeration keys = UIManager.getDefaults().keys();
					
				    while (keys.hasMoreElements()){
				        Object key = keys.nextElement();
				        Object value = UIManager.get(key);
				        
				        if (value instanceof javax.swing.plaf.FontUIResource){
				            UIManager.put(key, new FontUIResource(new Font("Calibri",Font.PLAIN, 12)));
				        }
				    }
					
					frame = new Frame(status);
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
		});
	}
}