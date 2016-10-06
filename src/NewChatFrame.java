import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

@SuppressWarnings("serial")
public class NewChatFrame extends JFrame implements ActionListener {

	JLabel userName;
	JTextField user;
	JButton accept;
	TCPClient client;
	JLabel feedBack;

	public NewChatFrame(TCPClient client) {
		this.client=client;
		setLayout(null);
		setSize(400, 220);
		setLocationRelativeTo(null);
		setTitle("New Chat");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setResizable(false);
		getContentPane().setBackground(null);
		setVisible(true);

		userName = new JLabel("Username");
		userName.setOpaque(false);
		userName.setHorizontalAlignment(0);
		userName.setBounds(25, 15, 100, 75);
		add(userName);

		accept = new JButton("OK");
		accept.setOpaque(false);
		accept.setBounds(150, 85, 100, 30);
		accept.setVisible(true);
		add(accept);
		accept.addActionListener(this);

		user = new JTextField(30);
		user.setBounds(125, 40, 200, 30);
		add(user);
		
		feedBack = new JLabel("");
		feedBack.setOpaque(false);
		feedBack.setHorizontalAlignment(0);
		feedBack.setBounds(70, 120, 250, 75);
		feedBack.setForeground(Color.RED);
		add(feedBack);
		
		AbstractAction action = new AbstractAction() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		    	accept.doClick();
		    }
		};
		user.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0), "TransferFocus");
		user.getActionMap().put("TransferFocus", action);
		user.requestFocus();

		this.repaint();

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String username = user.getText();
		user.setText("");
		client.newChat(username);
	}
}
