import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import javax.swing.JOptionPane;
import com.google.gson.Gson;

public class Loader {
	private Object user;
	private Object pool;
	
	public boolean loadUser(String user){
		try {
			Gson gson = new Gson();
			BufferedReader br = new BufferedReader(new FileReader(user + "/User"));
			Object data = gson.fromJson(br, User.class);
			
			this.user = data;
			
			return true;
		}
		catch(FileNotFoundException e){
			return false;
		}		
	}
	
	public boolean loadPool(){
		try {
			if(user != null){
				Gson gson = new Gson();
				BufferedReader br = new BufferedReader(new FileReader(getUser().ID + "/Pool"));
				Object data = gson.fromJson(br, Pool.class);
				
				this.pool = data;
				
				return true;
			}
			else{
				return false;
			}
		}
		catch(FileNotFoundException e){
			JOptionPane.showMessageDialog(null, "A fatal error has occured.");
			System.exit(0);
			return false;
		}		
	}
	
	public User getUser(){
		return (User) user;
	}
	
	public Pool getPool(){
		return (Pool) pool;
	}
}
