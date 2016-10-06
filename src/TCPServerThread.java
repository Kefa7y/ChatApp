import java.io.IOException;

public class TCPServerThread implements Runnable {

	TCPServer server;
	ClientSocket csocket;

	public TCPServerThread(ClientSocket csocket, TCPServer server)
			throws IOException {
		this.csocket = csocket;
		this.server = server;
	}

	public void run() {
		ChatMessage message = null;
		try {
			while (true) {
				message = csocket.receive();
				switch (message.type) {
				case NEWMEMBER:
					server.newMemberResponse(message ,csocket);
					break;
				case NEWCHAT:
					server.newChatResponse(message, csocket);
					break;
				case MEMBERS:
					server.getMemberResponse(message);
					break;
				case NORMAL:
					server.route(message, true);
					break;
				default:
					break;
				}
			}
		} catch (IOException e) {
			server.sockets.remove(csocket);
			try {
				server.toMaster.writeObject(new ChatMessage(server.servername,"master",csocket.username,ChatMessageType.REMOVE_MEMBER));
				csocket.csocket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

}
