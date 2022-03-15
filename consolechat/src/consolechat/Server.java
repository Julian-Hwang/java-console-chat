package consolechat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Server {
	public static void main(String[] args) {
		try {
			int port = 1234;
			ServerSocket serverSocket = new ServerSocket(port);
			HashMap<String, PrintWriter> clientMap = new HashMap<String, PrintWriter>();
			Collections.synchronizedMap(clientMap);
			while (true) {
				System.out.println("¡¢º”¡ﬂ...");
				Socket socket = serverSocket.accept();
				ServerProcess sp = new ServerProcess(socket, clientMap);
				sp.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

class ServerProcess extends Thread {
	private Socket socket = null;
	private String id;
	private BufferedReader br;
	private Map<String, PrintWriter> clientMap;
	public ServerProcess(Socket socket, Map<String, PrintWriter> clientMap) {
		this.socket = socket;
		this.clientMap = clientMap;
	}
	
	@Override
	public void run() {
		try {
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
			id = br.readLine();
			clientMap.put(id, pw);
			broadcast(id+"¥‘¿Ã ø¨∞·µ ");
			System.out.println("æ∆¿Ãµ: "+id);
			
			String msg = "";
			
			while (true) {
				msg = br.readLine();
				if (msg.equals(":exit")) {
					broadcast(id+" ¥‘¿Ã ≈¿Â«ﬂΩ¿¥œ¥Ÿ.");
					System.out.println("≈¿Âæ∆¿Ãµ : "+id);
					break;
				}
				broadcast(id+ " : "+msg);
				System.out.println(id+" : "+msg);
			}
			
			clientMap.remove(id);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	public void broadcast(String msg){
		synchronized (clientMap) {
			Collection<PrintWriter> collection = clientMap.values();
			Iterator<PrintWriter> iterator = collection.iterator();
		
			while (iterator.hasNext()) {
				PrintWriter pw = (PrintWriter) iterator.next();
				pw.println(msg);
				pw.flush();
			}
		}
		
	}
	
}