import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class komunikator {

	JTextArea receivedMessages;
	JTextField message;
	BufferedReader reader;
	PrintWriter writer;
	Socket sckt;
	
	public static void main(String[] args) {
		komunikator client = new komunikator();
		client.clientGo();
	}
	
	public void clientGo() {
		JFrame frame = new JFrame("Komunikator");
		JPanel pane = new JPanel();
		
		receivedMessages = new JTextArea(15, 50);
		receivedMessages.setLineWrap(true);
		receivedMessages.setWrapStyleWord(true);
		receivedMessages.setEditable(false);
		
		JScrollPane scroll = new JScrollPane(receivedMessages);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		message = new JTextField(20);
		
		JButton sendButton = new JButton("Wyślij");
		sendButton.addActionListener(new sendButtonListener());
		
		pane.add(scroll);
		pane.add(message);
		pane.add(sendButton);
		communicationConfig();

		Thread receiver = new Thread(new MessageReceiver());
		receiver.start();
		
		frame.getContentPane().add(BorderLayout.CENTER, pane);
		frame.setSize(400,500);
	}
	
	private void communicationConfig() {
		try {
			sckt = new Socket("127.0.0.1", 5000);
			InputStreamReader readerStrm = new InputStreamReader(sckt.getInputStream());
			reader = new BufferedReader(readerStrm);
			writer = new PrintWriter(sckt.getOutputStream());
			System.out.println("Connection");
		} catch(IOException ex) {
			ex.printStackTrace();
		}
	}
	
	public class sendButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent ev) {
			try {
				writer.println(message.getText());
				writer.flush();
				
			} catch(Exception ex) {
				ex.printStackTrace();
			}
			message.setText("");
			message.requestFocus();
		}
	}
	
	public class MessageReceiver implements Runnable {
		public void run() {
			String msg;
			try {
				while ((msg = reader.readLine()) != null) {
					System.out.println("Message: " + msg);
					receivedMessages.append(msg + "\n");
				}
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		}
	}
}
		