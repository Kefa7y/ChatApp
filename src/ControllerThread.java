import java.io.IOException;

public class ControllerThread implements Runnable {

	Controller control;
	ControllerSocket csock;

	public ControllerThread(Controller control, ControllerSocket ss)
			throws IOException {
		this.control = control;
		this.csock = ss;
	}

	@Override
	public void run() {
		try {
			while (true) {
				ChatMessage x = (ChatMessage) csock.fromServer.readObject();
				switch (x.type) {
				case NEWMEMBER:
					control.newMemberResponse(x,csock);
					break;
				case REMOVE_MEMBER:
					control.memberList.remove(x.getContent());
				case NEWCHAT:
					control.chatResponse(x, csock);
					break;
				case NORMAL:
					control.sendToAll(x,csock); break;
				case MEMBERS:
					control.memberRequest(x,csock);break;
				default:
					break;
				}
			}
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
	}
	

}
