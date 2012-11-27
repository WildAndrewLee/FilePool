import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Handles the distribution of files as well as other functions.
 * @author Andrew
 *
 */
public class Distributor extends Thread{
	/**
	 * ArrayList created to store all the threads and managers currently running.
	 */
	private ArrayList<Thread> threads = new ArrayList<Thread>();
	private ArrayList<Manager> managers = new ArrayList<Manager>();
	
	/**
	 * Used to handle networking functionality.
	 */
	private ObjectOutputStream ooutput;
	private ObjectInputStream oinput;
	private Socket socket;
	private ServerSocket server;
	
	/**
	 * Used to manage the ports currently in use.
	 */
	private ArrayList<Integer> ports = new ArrayList<Integer>();
	
	/**
	 * Default constructor which starts the distributor thread.
	 */
	public Distributor(){
		start();
	}
	
	/**
	 * Refreshes the user's current pool.
	 */
	private void refreshPool(){
		managers.clear();

		for(int n = 0; n < threads.size(); n++){
			threads.get(n).interrupt();
		}
		
		Pool pool = Main.status.getPool();
		
		Drop[] drops = pool.getDrops();
		
		for(int n = 0; n < drops.length; n++){
			managers.add(new Manager(drops[n].getPath(), drops[n].getType(), drops[n].getIP(), drops[n].getDLTO(), drops[n].getID()));
			ports.add(Integer.parseInt(drops[n].getID()));
		}
		
		for(int n = 0; n < managers.size(); n++){
			Thread thread = new Thread(managers.get(n));
			threads.add(thread);
			thread.start();
		}
	}
	
	/**
	 * Fetches the user's pool and listens for file listing requests.
	 */
	public void run(){
		refreshPool();
		
		try {
			while(true){
				server = new ServerSocket(6969, 69);
				
				socket = server.accept();
				
				ooutput = new ObjectOutputStream(socket.getOutputStream());
				ooutput.flush();
				
				oinput = new ObjectInputStream(socket.getInputStream());
				
				Object obj = oinput.readObject();
				Command command = (Command) obj;
				
				if(command.getType().equals("LIST")){
					ooutput.writeObject(Main.status.getPool().getDrops());
					ooutput.flush();
				}
				
				ooutput.close();
				oinput.close();
				socket.close();
				server.close();
			}
		}
		catch(IOException e){
			e.printStackTrace();
		}
		catch(ClassNotFoundException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns the ports currently in use.
	 * @return The ports currently in use.
	 */
	public ArrayList<Integer> getPorts(){
		return ports;
	}
	
	/**
	 * Adds a new "Drop" to manage as well as starts a new thread.
	 * @param drop The "Drop" to manage.
	 */
	public void addDrop(Drop drop){
		ports.add(Integer.parseInt(drop.getID()));
		
		Manager manager = new Manager(drop.getPath(), drop.getType(), drop.getIP(), drop.getDLTO(), drop.getID());
		
		managers.add(manager);
		
		Thread thread = new Thread(manager);
		threads.add(thread);
		thread.start();
		
	}

	/**
	 * Removes a "Drop" from the user's pool.
	 * @param removed The drop to remove.
	 * @param type The type of drop.
	 */
	public void remove(Object removed, int type){
		Drop drop = null;
		
		if(type == 1){
			for(int n = 0; n < managers.size(); n++){
				if(managers.get(n).getDrop().getPath().equals((String) removed)){
					drop = managers.get(n).getDrop();
					
					ports.remove((Integer) Integer.parseInt(managers.get(n).getDrop().getID()));
					
					managers.remove(n);
					threads.get(n).interrupt();
					threads.remove(n);
				}
			}
		}
		else if(type == 2){
			for(int n = 0; n < managers.size(); n++){
				if(managers.get(n).getDrop().getDLTO().equals((String) removed)){
					drop = managers.get(n).getDrop();
					
					managers.remove(n);
					threads.get(n).interrupt();
					threads.remove(n);
				}
			}
		}
		
		Main.status.getPool().removeFile(drop);
	}
}