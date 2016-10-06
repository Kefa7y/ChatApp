import java.io.*;
import java.net.*;

import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;

class TCPClient implements Runnable {

	static String hostname = "localhost";
	static int ttl = 2;
	Gui gui;
	NewChatFrame newChat;
	Socket clientSocket;
	String servername;
	String username;
	ObjectOutputStream outToServer;
	ObjectInputStream inFromServer;

	public TCPClient(String s , int port) throws IOException {
		clientSocket = new Socket(hostname, port);
		outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
		inFromServer = new ObjectInputStream(clientSocket.getInputStream());
		servername =s;
	}

	public void run() {
		read();
	}

	public void read() {
		while(true){
			try {
				ChatMessage x = (ChatMessage) inFromServer.readObject();
				switch(x.type){
					case NORMAL:
					case MEMBERS_RESPONSE:receive(x);break;
					case CONFIRM_NEWCHAT:gui.addTab(x.getContent());newChat.dispose();newChat = null;break;
					case ERROR_NEWCHAT: newChat.feedBack.setText("Username is not found.");break;
					default:break;
				}
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		} 
	}

	public void write() {
		try {
			String sentence = gui.message.getText();
			if(gui.chatHistory.getTabCount()!=0){
				outToServer.writeObject(new ChatMessage(username, gui.chatHistory.getTitleAt(gui.chatHistory.getSelectedIndex()),sentence));
				JScrollPane p = (JScrollPane) gui.chatHistory.getComponentAt(gui.chatHistory.getSelectedIndex());
				JTextPane t = (JTextPane)p.getViewport().getView();
				t.setText(t.getText() + '\n' +username+": "+ sentence);
			}
			gui.message.setText("");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void newChat(String username){
		try {
			if(username.equalsIgnoreCase(this.username)){
				newChat.feedBack.setText("Why chat with yourself ?");
				return;
			}
			for(int i=0;i<gui.chatHistory.getTabCount();i++)
				if(gui.chatHistory.getTitleAt(i).equalsIgnoreCase(username)){
					newChat.feedBack.setText("You are already chatting with that user.");
					return;
				}
			outToServer.writeObject(new ChatMessage(this.username ,servername, username , ChatMessageType.NEWCHAT));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void getMemberList(){
		try {
			outToServer.writeObject(new ChatMessage(username,servername,ChatMessageType.MEMBERS));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	public void receive(ChatMessage x){
		JTabbedPane p =gui.chatHistory;
		if(p.getTabCount()!=0)
			for(int i=0;i<gui.chatHistory.getTabCount();i++)
				if(p.getTitleAt(i).equalsIgnoreCase(x.source)){
					JTextPane t = (JTextPane)((JScrollPane)p.getComponentAt(i)).getViewport().getView();
					t.setText(t.getText()+'\n'+x.source+": "+x.getContent());
					return;
				}	
		gui.addTab(x.source);
		JTextPane t = (JTextPane)((JScrollPane)p.getComponentAt(p.getTabCount()-1)).getViewport().getView();
		t.setText(t.getText()+'\n'+x.source+": "+x.getContent());
	}

}