public class Drop {
	private String path, type, ip, dlto, id;
	
	public Drop(String path, String type, String ip, String dlto, String id){
		this.path = path;
		this.type = type;
		this.ip = ip;
		this.dlto = dlto;
		this.id = id;
	}
	
	public String getPath(){
		return path;
	}
	
	public String getType(){
		return type;
	}
	
	public String getIP(){
		return ip;
	}
	
	public String getDLTO(){
		return dlto;
	}
	
	public String getID(){
		return id;
	}
}