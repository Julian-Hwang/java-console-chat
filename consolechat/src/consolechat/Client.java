package consolechat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Collection;

public class Client {
	public static void main(String[] args) {
		Socket socket = null;
		BufferedReader input = null;
		PrintWriter pw = null;
		String id = "";
		
		try {
			socket = new Socket("localhost",1234);
			pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
			input = new BufferedReader(new InputStreamReader(System.in));
			
			System.out.println("채팅창에 입장하셨습니다.(나가실 때는 :exit 입력)");
			System.out.print("id 입력: ");
			id = input.readLine();
			pw.println(id);
			pw.flush();
			
			ClientServerProcess csp = new ClientServerProcess(socket/*,br*/);
			csp.start();
			
			String line = "";
			
			while (true) {
				line = input.readLine();
				if (line.equals(":exit")) {
					pw.println(line);
					pw.flush();
				}
				csp.stopThread();
				pw.println(line);
				pw.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

class ClientServerProcess extends Thread {
	private Socket socket=null;
	private boolean isRunning;
	public ClientServerProcess(Socket socket) {
		this.socket=socket;
		isRunning = true;
	}
	
	@Override
	public void run() {	
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String msg = "";
			while (true) {
				msg = br.readLine();
				System.out.println(msg);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	public void stopThread() {
		isRunning = false;
	}
}
