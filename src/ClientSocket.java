import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientSocket {
	Socket csocket;
	private ObjectInputStream inFromClient;
	private ObjectOutputStream outToClient;
	private TCPServer server;
	String username;
	
	public ClientSocket(Socket csocket , TCPServer server) throws IOException, ClassNotFoundException {
		this.server = server;
		this.csocket = csocket;
		outToClient = new ObjectOutputStream(csocket.getOutputStream());
		inFromClient = new ObjectInputStream(csocket.getInputStream());
	}

	
	public void send(ChatMessage c) throws IOException{
		outToClient.writeObject(c);
	}
	
	public void notify(String s) throws IOException{
		outToClient.writeObject(new ChatMessage(server.servername,username,s));
	}
	
	public void notify(String s , ChatMessageType type) throws IOException{
		switch(type){
			case NORMAL:notify(s); break;
			case CONFIRM_NEWCHAT: outToClient.writeObject(new ChatMessage(server.servername, username, s, type));break;
			case MEMBERS_RESPONSE: outToClient.writeObject(new ChatMessage(server.servername,username, s, type));break;
			default:
		}
	}
	
	public void notify(ChatMessageType type) throws IOException{
		switch(type){
			case ERROR_NEWCHAT:outToClient.writeObject(new ChatMessage(server.servername,username,type)); break;
			default:
		}
	}
	
	public ChatMessage receive() throws IOException, ClassNotFoundException{
		ChatMessage x = (ChatMessage)inFromClient.readObject();;
		if(x.ttl==0)
			return receive();
		x.ttl--;
		return x;
	}
}
