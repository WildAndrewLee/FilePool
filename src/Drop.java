import java.io.Serializable;

public class Drop implements Serializable{
	/**
	 * Basic instance variables used to handle "Drop" information.
	 */
	private String path, type, ip, dlto, id, name;
	
	/**
	 * Creates a new "Drop" using the specified information.
	 * @param path The path of the file drop.
	 * @param type The type of drop.
	 * @param ip The IP associated with the drop.
	 * @param dlto The save path of the drop. Only needed for client drops.
	 * @param id The ID of the drop.
	 */
	public Drop(String path, String type, String ip, String dlto, String id){
		this.path = path;
		this.type = type;
		this.ip = ip;
		this.dlto = dlto;
		this.id = id;
		
		if(this.type.equals("client")){
			this.name = dlto.substring(dlto.lastIndexOf('\\') + 1);
		}
		else{
			this.name = path.substring(path.lastIndexOf('\\') + 1);
		}
	}
	
	/**
	 * Returns the path specified by this drop.	
	 * @return The path used by this drop.
	 */
	public String getPath(){
		return path;
	}
	
	/**
	 * Returns the type of drop.
	 * @return The type of drop.
	 */
	public String getType(){
		return type;
	}
	
	/**
	 * The IP specified by this drop.
	 * @return The IP used by this drop.
	 */
	public String getIP(){
		return ip;
	}
	
	/**
	 * The path to save the file to.
	 * @return The path to save the file to.
	 */
	public String getDLTO(){
		return dlto;
	}
	
	/**
	 * The ID of this drop.
	 * @return The ID used by this drop.
	 */
	public String getID(){
		return id;
	}
	
	/**
	 * The file name of this drop.
	 * @return The file name used by this drop.
	 */
	public String getName(){
		return name;
	}
}