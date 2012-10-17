import java.util.ArrayList;

public class Distributor extends Thread{
	private ArrayList<Thread> threads = new ArrayList<Thread>();
	private ArrayList<Manager> managers = new ArrayList<Manager>();
	
	private ArrayList<Integer> ports = new ArrayList<Integer>();
	
	public Distributor(){
		start();
	}
	
	private void refreshPool(){
		managers.clear();
		
		Pool pool = Main.status.getPool();
		
		String[][] files = pool.Files;
		
		for(int n = 0; n < files.length; n++){
			managers.add(new Manager(files[n][0], files[n][1], files[n][2], files[n][3], files[n][4]));
			ports.add(Integer.parseInt(files[n][4]));
		}
		
		for(int n = 0; n < managers.size(); n++){
			Thread thread = new Thread(managers.get(n));
			threads.add(thread);
			thread.start();
		}
	}
	
	public void run(){
		refreshPool();
	}
	
	public ArrayList<Integer> getPorts(){
		return ports;
	}
	
	public void addDrop(Drop drop){
		ports.add(Integer.parseInt(drop.getID()));
		
		Manager manager = new Manager(drop.getPath(), drop.getType(), drop.getIP(), drop.getDLTO(), drop.getID());
		
		managers.add(manager);
		
		Thread thread = new Thread(manager);
		threads.add(thread);
		thread.start();
		
	}
}