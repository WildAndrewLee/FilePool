import java.io.File;
import java.io.FileNotFoundException;
import java.util.Formatter;
import com.google.gson.Gson;

public class User {
	public String ID;
	public String PW;
	public int UID;
	public String DN;
	public String[][] Contacts;
	
	public boolean addContact(String name, String ip){
		String[][] newContacts = new String[Contacts.length + 1][2];
		
		for(int n = 0; n < Contacts.length; n++){
			newContacts[n][0] = Contacts[n][0];
			newContacts[n][1] = Contacts[n][1];
		}
		
		newContacts[Contacts.length][0] = name;
		newContacts[Contacts.length][1] = ip;
		
		Contacts = newContacts;
		
		return saveUser();
	}
	
	public boolean removeContact(String ip){
		String[][] newContacts = new String[Contacts.length - 1][2];
		
		int cindex = 0;
		
		for(int n = 0; n < Contacts.length; n++){
			if(Contacts[n][1] != ip){
				newContacts[cindex][0] = Contacts[n][0];
				newContacts[cindex][1] = Contacts[n][1];
				
				cindex++;
			}
		}
		
		Contacts = newContacts;
		
		return saveUser();
	}
	
	public boolean create(){
		String json = "{\"ID\":\"" + ID + "\",\"PW\":\"" + PW + "\",\"UID\":" + UID + ",\"DN\":\"" + DN + "\",\"Contacts\":[]}";
		String pool = "{\"Files\":[]}";
		
		try{
			File dir = new File(ID + "");
			dir.mkdirs();
			
			Formatter format = new Formatter(ID + "/User");
			format.format("%s", json);
			format.close();
			
			format = new Formatter(ID + "/Pool");
			format.format("%s", pool);
			format.close();
		}
		catch(FileNotFoundException e){
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	private boolean saveUser(){
		Gson gson = new Gson();
		
		String json = gson.toJson(this);
		
		try{
			Formatter format = new Formatter(Main.status.getUser().ID + "/User");
			format.format("%s", json);
			format.close();
		}
		catch(FileNotFoundException e){
			return false;
		}
		
		return true;
	}
}