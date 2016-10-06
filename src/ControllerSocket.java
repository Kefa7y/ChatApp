import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class ControllerSocket {

	Socket csock;
	ObjectOutputStream toServer;
	ObjectInputStream fromServer;
	
	public ControllerSocket(Socket csock) throws IOException{
		this.csock = csock;
		toServer = new ObjectOutputStream(csock.getOutputStream());
		fromServer = new ObjectInputStream(csock.getInputStream());
	}
	
}
