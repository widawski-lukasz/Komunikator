import java.io.*;
import java.net.*;
import java.util.*;

public class komunikatorSerwer { 
	ArrayList outputStrm;
	
	public class clientServices implements Runnable {
		BufferedReader reader;
		Socket sckt;
		
		public clientServices(Socket clientSckt) {
			try {
				sckt = clientSckt;
				InputStreamReader isReader = new InputStreamReader(sckt.getInputStream());
				reader = new BufferedReader(isReader);
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		}
		
		public void run() {
			String msg;
			try {
				while ((msg = reader.readLine()) != null) {
					System.out.println("Message: " + msg);
					sendForAll(msg);
				}
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	
	public static void main (String[] args) {
		new komunikatorSerwer().serverGo();
	}
	
	public void serverGo() {
		outputStrm = new ArrayList();
		try {
			ServerSocket serverSckt = new ServerSocket(5000);
			
			while(true) {
				Socket clientSckt = serverSckt.accept();
				PrintWriter writer = new PrintWriter(clientSckt.getOutputStream());
				outputStrm.add(writer);
				
				Thread t = new Thread(new clientServices(clientSckt));
				t.start();
				System.out.println("Connection");
			}
		} catch(Exception ex) {
			ex.printStackTrace() ;
		}
	}
	
	public void sendForAll(String message) {
		Iterator it = outputStrm.iterator();
		while(it.hasNext()) {
			try {
			PrintWriter writer = (PrintWriter) it.next();
			writer.println(message);
			writer.println(message);
			writer.flush();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
}
