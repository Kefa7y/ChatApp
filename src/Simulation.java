import java.io.IOException;

public class Simulation implements Runnable {
	int i = 0;
	int port = 5999;

	public static void main(String[] args) throws InterruptedException {
		Simulation s = new Simulation();
		while (s.i < 5) {
			new Thread(s).start();
			Thread.sleep(800);
			s.i++;
		}
	}

	@Override
	public void run() {
		try {
			if(i==0)
				new Controller();
			else 
				new TCPServer(port + i, "Server " + i);
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
	}
}
