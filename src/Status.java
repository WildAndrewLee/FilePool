import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Status {
	private Loader loader = new Loader();
	private boolean isLoggedIn = false;
	private User user = null;
	private Pool pool = null;
	private String ip = null;
	
	public Status(){
		try{
			ip = InetAddress.getLocalHost().getHostAddress();
			System.out.println(InetAddress.getLocalHost());
		}
		catch(UnknownHostException e){
			e.printStackTrace();
		}
	}
	
	public boolean isLoggedIn(){
		return isLoggedIn;
	}
	
	public boolean logIn(String id, String pass){
		if(loader.loadUser(id)){
			user = loader.getUser();
			
			if(user.ID.equalsIgnoreCase(id) && user.PW.equals(pass)){
				isLoggedIn = true;
				
				if(loader.loadPool()){
					this.pool = loader.getPool();
					
					return true;
				}
				else{
					
					return false;
				}
			}
			else{
				return false;
			}
		}
		else{
			return false;
			
		}
	}
	
	public String getIP(){
		return ip;
	}
	
	public Pool getPool(){
		return pool;
	}
	
	public User getUser(){
		return user;
	}
	
	public void startDistro(){
		Main.distro = new Distributor();
	}
}