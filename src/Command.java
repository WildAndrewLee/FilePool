import java.io.Serializable;

/**
 * Used to handle commands sent and received.
 * @author Andrew
 */

public class Command implements Serializable{
	/**
	 * Base variables as well as serialVersionUID to fix serialization errors.
	 */
	/**
	 * 
	 */
	private static final long serialVersionUID = 4454449098198890276L;
	private String ip, type, file, port;
	
	/**
	 * Default constructor
	 * @param type The type of command LIST, GET, or SEND.
	 * @param file The path of the file to get or send.
	 * @param ip The IP to connect to or host from.
	 * @param port The port to connect to or host from.
	 */
	public Command(String type, String file, String ip, String port){
		this.ip = ip;
		this.type = type;
		this.file = file;
		this.port = port;
	}
	
	/**
	 * Returns the type of command LIST, GET, or SEND.
	 * @return The type of command.
	 */
	public String getType(){
		return type;
	}
	
	/**
	 * Returns the IP to connect to or host from.
	 * @return The IP associated with this command.
	 */
	public String getIP(){
		return ip;
	}
	
	/**
	 * Returns the path of the file to get or send.
	 * @return The file path associated with this command.
	 */
	public String getFile(){
		return file;
	}
	
	/**
	 * Returns the port to connect to or host from.
	 * @return The port associated with this command.
	 */
	public String getPort(){
		return port;
	}
	
	/**
	 * Returns the command in a readable format.
	 */
	public String toString(){
		return type + " " + file + " " + ip + " " + port;
	}
}