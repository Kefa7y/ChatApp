import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;


@SuppressWarnings("serial")
public class LoginFrame extends JFrame implements ActionListener{
	JButton button;
	JTextField text;
	JLabel label;
	JLabel feedBack;
	JComboBox<String> combo;
	TCPClient client;
	
	public LoginFrame() throws IOException{
		
		setLayout(null);
		setSize(400, 300);
		setLocationRelativeTo(null);
		setTitle("Dardasha - Login");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		getContentPane().setBackground(null);
		setVisible(true);
		
		String [] x= {"Server 1","Server 2","Server 3","Server 4"};
		combo=new JComboBox<String>(x);
		combo.setBounds(120, 110, 160,30 );
		//to center the text in the combobox
		DefaultListCellRenderer dfcl = new DefaultListCellRenderer();
		dfcl.setHorizontalAlignment(DefaultListCellRenderer.CENTER);
		combo.setRenderer(dfcl);
		add(combo);
		
		JLabel UserName=new JLabel("Username");
		UserName.setOpaque(false);
		UserName.setHorizontalAlignment(0);
		UserName.setBounds(20, 25, 100, 75);
		add(UserName);
		
		text=new JTextField(30);
		text.setBounds(120,50, 220, 30);
		add(text);
		
		button = new JButton("log in");
		button.setOpaque(false);
		button.setBounds(120,150, 160, 30);
		button.setVisible(true);
		add(button);
		button.addActionListener(this);
		
		feedBack = new JLabel("");
		feedBack.setOpaque(false);
		feedBack.setHorizontalAlignment(0);
		feedBack.setBounds(70, 170, 250, 75);
		feedBack.setForeground(Color.RED);
		add(feedBack);
		
		AbstractAction action = new AbstractAction() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		    	button.doClick();
		    }
		};
		text.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0), "TransferFocus");
		text.getActionMap().put("TransferFocus", action);
		combo.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0), "TransferFocus");
		combo.getActionMap().put("TransferFocus", action);
		text.requestFocus();
		
		this.repaint();
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String s = text.getText();
		String server =(String) combo.getSelectedItem();
		try {
			client = new TCPClient(server,5999+Integer.parseInt(server.substring(server.length()-1)));
			client.outToServer.writeObject(new ChatMessage("tempuser1234",client.servername,s,ChatMessageType.NEWMEMBER));
			ChatMessage x =(ChatMessage)client.inFromServer.readObject();
			if(x.type==ChatMessageType.ERROR_NEWMEMBER){
				feedBack.setText("Username already exists. Please try again.");
				text.setText("");
			}
			else {
				client.username = s;
				new Thread(client).start();
				client.gui =new Gui(client);
				dispose();
			}
		} catch (IOException | ClassNotFoundException e1) {
			feedBack.setText("Server is down, please try again later.");
		} 
	}
	
	public static void main(String[] args) throws IOException {
		new LoginFrame();
	}
}
