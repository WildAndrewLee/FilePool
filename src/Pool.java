import java.io.FileNotFoundException;
import java.util.Formatter;

import com.google.gson.Gson;

public class Pool {
	public String[][] Files;
	
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