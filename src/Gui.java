import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.border.TitledBorder;
import javax.swing.text.DefaultCaret;

@SuppressWarnings("serial")
public class Gui extends JFrame implements ActionListener{
	JButton NewChat;
	JButton endChat;
	JButton getMemberList;
	JTextField message;
	JTabbedPane chatHistory;
	TCPClient client;
	JButton send;

	public Gui(TCPClient client) {
		super();
		this.client = client;
		setTitle("Dardasha - " + client.username);
		setLayout(null);
		setSize(1370, 770);
		setBackground(new Color(192, 192, 192));
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		getContentPane().setBackground(null);
		setVisible(true);
		

		NewChat = new JButton("New Chat");
		NewChat.setOpaque(false);
		NewChat.setBounds(50, 100, 200, 30);
		NewChat.setVisible(true);
		NewChat.addActionListener(this);
		add(NewChat);

		endChat = new JButton("End Chat");
		endChat.setOpaque(false);
		endChat.setBounds(50, 170, 200, 30);
		endChat.setVisible(true);
		endChat.addActionListener(this);
		add(endChat);

		getMemberList = new JButton("Member List");
		getMemberList.setOpaque(false);
		getMemberList.setBounds(50, 240, 200, 30);
		getMemberList.setVisible(true);
		getMemberList.addActionListener(this);
		add(getMemberList);

		message = new JTextField(100);
		message.setBounds(450, 500, 800, 75);
		add(message);
		
		send=new JButton("Send");
		send.setOpaque(false);
		send.setBounds(700, 600, 200, 30);
		send.setVisible(true);
		send.addActionListener(this);
		AbstractAction action = new AbstractAction() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		    	send.doClick();
		    }
		};
		send.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0), "TransferFocus");
		message.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0), "TransferFocus");
		send.getActionMap().put("TransferFocus", action);
		message.getActionMap().put("TransferFocus", action);
		add(send);

		JLabel msg = new JLabel("message:");
		msg.setOpaque(false);
		msg.setBounds(350, 500, 100, 75);
		msg.setVisible(true);
		add(msg);

		chatHistory = new JTabbedPane();
		chatHistory.setBounds(450, 120, 800, 350);
		
		add(chatHistory);

		TitledBorder title = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black),"Chat history");
		title.setTitleJustification(TitledBorder.CENTER);
		chatHistory.setBorder(title);

		this.repaint();

	}

	
	@Override
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand()){
			case "New Chat":client.newChat =new NewChatFrame(client);break;
			case "End Chat":if(chatHistory.getTabCount()>0)chatHistory.removeTabAt(chatHistory.getSelectedIndex());break;
			case "Member List":client.getMemberList();break;
			case "Send":client.write();
		}
	}


	public void addTab(String title){
		
		JTextPane x = new JTextPane();
		x.setEditable(false);
		x.setBackground(Color.white);
		x.setVisible(true);
		DefaultCaret c = (DefaultCaret)x.getCaret();
		c.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
		JScrollPane scroll = new JScrollPane(x);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setAutoscrolls(true);
		
		chatHistory.addTab(title, scroll);
	}
}
