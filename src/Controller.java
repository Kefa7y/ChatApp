import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Controller{

	ArrayList <ControllerSocket> servers;
	ServerSocket ssocket;
	ArrayList <String> memberList;
	
	public Controller() throws IOException{
		servers = new ArrayList<ControllerSocket>();
		memberList = new ArrayList<String>();
		ssocket = new ServerSocket(7000);
		while(true){
			Socket s = ssocket.accept();
			ControllerSocket ss = new ControllerSocket(s);
			servers.add(ss);
			new Thread(new ControllerThread(this,ss)).start();
		}
	}
	

	public void chatResponse(ChatMessage x , ControllerSocket csock) throws IOException {
		for(int i=0;i<memberList.size();i++)
			if(memberList.get(i).equalsIgnoreCase(x.getContent())){
				ChatMessage c =new ChatMessage(x.destination,x.source,x.getContent(),ChatMessageType.CONFIRM_NEWCHAT);
				csock.toServer.writeObject(c);
				return;
			}
		ChatMessage c =new ChatMessage(x.destination,x.source,ChatMessageType.ERROR_NEWCHAT);
		csock.toServer.writeObject(c);
	}
	
	public void sendToAll(ChatMessage x, ControllerSocket csock) throws IOException {
		for (int i = 0; i < servers.size(); i++)
			if(!csock.equals(servers.get(i)))
				servers.get(i).toServer.writeObject(x);
	}

	public void memberRequest(ChatMessage x, ControllerSocket csock) throws IOException{
		ChatMessage c =new ChatMessage(x.destination,x.source,memberList.toString(),ChatMessageType.MEMBERS_RESPONSE);
		csock.toServer.writeObject(c);
	}
	
	public void newMemberResponse(ChatMessage x, ControllerSocket csock) throws IOException{
		for(int i=0;i<memberList.size();i++)
			if(memberList.get(i).equalsIgnoreCase(x.getContent())){
				csock.toServer.writeObject(new ChatMessage(x.destination,x.source,ChatMessageType.ERROR_NEWMEMBER));
				return;
			}	
		memberList.add(x.getContent());
		csock.toServer.writeObject(new ChatMessage(x.destination,x.source,x.getContent(),ChatMessageType.CONFIRM_NEWMEMBER));
	}
}
