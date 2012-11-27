import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Formatter;
import com.google.gson.Gson;

public class Pool {
	public String[][] Files;
	
	public Drop[] getDrops(){
		Drop[] drops = new Drop[Files.length];
		
		for(int n = 0; n < Files.length; n++){
			Drop drop = new Drop(Files[n][0], Files[n][1], Files[n][2], Files[n][3], Files[n][4]);
			drops[n] = drop;
		}
		
		return drops;
	}
	
	public Drop[] getDrops(String ip){
		try{
			Socket socket = new Socket();
			socket.connect(new InetSocketAddress(ip, 6969), 1500);
			
			ObjectOutputStream ooutput = new ObjectOutputStream(socket.getOutputStream());
			ooutput.flush();
			
			ObjectInputStream oinput = new ObjectInputStream(socket.getInputStream());
			
			ooutput.writeObject(new Command("LIST", "", "", ""));
			ooutput.flush();
			
			Drop[] drop = (Drop[]) oinput.readObject();
			
			ooutput.close();
			oinput.close();
			socket.close();
			
			return drop;
		}
		catch(UnknownHostException e){
			return null;
		}
		catch(IOException e){
			return null;
		}
		catch(ClassNotFoundException e){
			return null;
		}
	}
	
	public boolean addFile(Drop drop){
		String[][] newPool = new String[Files.length + 1][5];
		
		for(int n = 0; n < Files.length; n++){
			newPool[n][0] = Files[n][0];
			newPool[n][1] = Files[n][1];
			newPool[n][2] = Files[n][2];
			newPool[n][3] = Files[n][3];
			newPool[n][4] = Files[n][4];
		}
		
		newPool[Files.length][0] = drop.getPath();
		newPool[Files.length][1] = drop.getType();
		newPool[Files.length][2] = drop.getIP();
		newPool[Files.length][3] = drop.getDLTO();
		newPool[Files.length][4] = drop.getID();
		
		Files = newPool;
		
		return savePool();
	}
	
	public boolean removeFile(Drop drop){
		String[][] newPool = new String[Files.length - 1][5];
		
		int cindex = 0;
		
		for(int n = 0; n < Files.length; n++){
			if(Files[n][4] != drop.getID()){
				newPool[cindex][0] = Files[n][0];
				newPool[cindex][1] = Files[n][1];
				newPool[cindex][2] = Files[n][2];
				newPool[cindex][3] = Files[n][3];
				newPool[cindex][4] = Files[n][4];
				
				cindex++;
			}
		}
		
		Files = newPool;
		
		return savePool();
	}
	
	public boolean savePool(){
		Gson gson = new Gson();
		
		String json = gson.toJson(this);
		
		try{
			Formatter format = new Formatter(Main.status.getUser().ID + "/Pool");
			format.format("%s", json);
			format.close();
		}
		catch(FileNotFoundException e){
			return false;
		}
		
		return true;
	}
}