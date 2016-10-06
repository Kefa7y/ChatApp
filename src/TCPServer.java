import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class TCPServer implements Runnable {

	ServerSocket ssock;
	Socket master; // port 7000
	ObjectOutputStream toMaster;
	ObjectInputStream fromMaster;
	ArrayList<ClientSocket> sockets;
	String servername;
	ClientSocket newUser;

	public TCPServer(int port, String servername) throws IOException,
			ClassNotFoundException {
		ssock = new ServerSocket(port);
		sockets = new ArrayList<ClientSocket>();
		this.servername = servername;
		master = new Socket("localhost", 7000);
		toMaster = new ObjectOutputStream(master.getOutputStream());
		fromMaster = new ObjectInputStream(master.getInputStream());
		new Thread(this).start();
		while (true) {
			try {
				Socket sock = ssock.accept();
				ClientSocket csock = new ClientSocket(sock, this);
				sockets.add(csock);
				new Thread(new TCPServerThread(csock, this)).start();
			} catch (SocketException e) {
			}
		}
	}

	@Override
	public void run() {
		ChatMessage x = null;
		try {
			while (true) {
				x = (ChatMessage) fromMaster.readObject();
				if(x.type == ChatMessageType.CONFIRM_NEWMEMBER || x.type == ChatMessageType.ERROR_NEWMEMBER){
					if(x.type == ChatMessageType.CONFIRM_NEWMEMBER)
						newUser.username =x.getContent();
					newUser.send(x);
					newUser = null;
					continue;
				}
				route(x, false);
			}
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
	}
		
	public void newMemberResponse(ChatMessage x ,ClientSocket csocket) throws IOException, ClassNotFoundException{
		toMaster.writeObject(x);
		newUser = csocket;
	}

	public void newChatResponse(ChatMessage c, ClientSocket csocket)
			throws IOException {
		for (int i = 0; i < sockets.size(); i++){
			if (sockets.get(i).username.equalsIgnoreCase(c.getContent())) {
				csocket.notify(c.getContent(), ChatMessageType.CONFIRM_NEWCHAT);
				return;
			}
		}
		toMaster.writeObject(c);
	}

	public void getMemberResponse(ChatMessage x) throws IOException {
		toMaster.writeObject(x);
	}

	public void route(ChatMessage x, boolean f) throws IOException {
		for (int i = 0; i < sockets.size(); i++) {
			ClientSocket c = sockets.get(i);
			if (x.destination.equalsIgnoreCase(c.username)) {
				c.send(x);
				return;
			}
		}
		if (f)
			toMaster.writeObject(x);
	}

}
