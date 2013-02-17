import java.awt.EventQueue;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Main {
	public static Status status = new Status();
	public static Frame frame = null;
	public static Distributor distro = null;
	
	public static void main(String[] args){
		EventQueue.invokeLater(new Runnable() {
			public void run() {
			    try{
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				}
				catch(ClassNotFoundException e){
					e.printStackTrace();
				}
				catch(InstantiationException e){
					e.printStackTrace();
				}
				catch(IllegalAccessException e){
					e.printStackTrace();
				}
				catch(UnsupportedLookAndFeelException e){
					e.printStackTrace();
				}
				
				frame = new Frame(status);
			}
		});
	}
}