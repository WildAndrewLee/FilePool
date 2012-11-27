import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.FileOutputStream;
import javax.swing.JOptionPane;

public class Manager implements Runnable{
	private InputStream input;
	private ObjectOutputStream ooutput;
	private ObjectInputStream oinput;
	private ServerSocket server;
	private Socket socket;
	private int progress = 0;
	private Drop drop = null;
	
	public Manager(String location, String type, String ip, String dlto, String id){
		System.out.println(type);
		
		drop = new Drop(location, type, ip, dlto, id);
	}
	
	public void run(){		
		try{
			if(drop.getType().equals("distributor")){
				while(true){
					server = new ServerSocket(Integer.parseInt(drop.getID()), 69);
					
					socket = server.accept();
					
					oinput = new ObjectInputStream(socket.getInputStream());
										
					Object obj = oinput.readObject();
					
					//System.out.println(obj);
					
					Command command = (Command) obj;
					
					server.close();
					
					Provider provider = new Provider(command);
					
					Thread thread = new Thread(provider);
					thread.start();
					
					socket.close();
					oinput.close();
				}
			}
			else if(drop.getType().equals("client")){
				socket = new Socket(drop.getIP(), Integer.parseInt(drop.getID()));
				
				ooutput = new ObjectOutputStream(socket.getOutputStream());
				
				Command command = new Command("GET", drop.getPath(), Main.status.getIP(), drop.getID());
				
				ooutput.writeObject(command);
				ooutput.flush();
				
				server = new ServerSocket(Integer.parseInt(drop.getID()), 69);
				
				socket = server.accept();
				
				oinput = new ObjectInputStream(socket.getInputStream());
				input = socket.getInputStream();
				
				FileOutputStream foutput = new FileOutputStream(drop.getDLTO());
				byte[] buffer = new byte[32 * 1024];
				
				int read = 0, total = 0, size = 0;
				
				size = (Integer) oinput.readObject();
				
				while((read = input.read(buffer)) != -1){
					total += read;
					foutput.write(buffer, 0, read);

					progress = (int) ((double) total / size * 100);
					
					Main.frame.getHub().updateProgress(drop.getDLTO(), progress);
					
					if(total / size == 1){
						break;
					}
				}
				
				foutput.flush();
				foutput.close();
				ooutput.close();
				oinput.close();
				input.close();
				socket.close();
				server.close();
				
				JOptionPane.showMessageDialog(null, "Download finished.");
			}
		}
		catch(ConnectException e){
			try{
				Thread.sleep(250);
				run();
			}
			catch(InterruptedException ex){
				e.printStackTrace();
			}
		}
		catch(IOException e){
			e.printStackTrace();		
		}
		catch(ClassCastException e){
			e.printStackTrace();
		}
		catch(ClassNotFoundException e){
			e.printStackTrace();
		}
	}
	
	public double getProgress(){
		return progress;
	}
	
	public Drop getDrop(){
		return drop;
	}
}