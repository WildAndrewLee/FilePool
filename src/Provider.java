import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Provider implements Runnable{
	private OutputStream output;
	private ObjectOutputStream ooutput;
	private String command, ip, port;
	
	public Provider(Command command){
		this.command = command.getFile();
		this.ip = command.getIP();
		this.port = command.getPort();
	}
	
	public void run(){		
		try {
			Socket socket = new Socket(ip, Integer.parseInt(port));
			
			output = socket.getOutputStream();
			output.flush();
			ooutput = new ObjectOutputStream(socket.getOutputStream());
				
			if(command != null){				
		        FileInputStream finput = new FileInputStream(command);
		        
		        Integer size = finput.available();
		        ooutput.writeObject(size);
		        ooutput.flush();
		        
		        byte [] buffer = new byte[32 * 1024];
		        int read = 0;

		        while((read = finput.read(buffer)) != -1){
		        	if(read > 0){
		        		output.write(buffer, 0, read);
		        	}
		        }
		        
		        finput.close();
		        output.close();
		        ooutput.close();
			}
		}
		catch(NumberFormatException e){}
		catch(UnknownHostException e){}
		catch(IOException e){}
	}
}
