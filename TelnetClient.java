import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.File;
import java.net.Socket;
import java.util.Scanner;

public class TelnetClient extends Thread{
	private Socket clientSocket;

	public TelnetClient(Socket clientSocket){
		this.clientSocket = clientSocket;
	}

	public void run(){
		InputStream inps  = null;
		OutputStream outs = null;
		try {
			inps = clientSocket.getInputStream();
			outs = clientSocket.getOutputStream();
		} catch(Exception e) {
			e.printStackTrace();
			return;
		}

		Scanner in = new Scanner(inps);
		
		VT100 out = new VT100(outs);

		out.print(new File("lyrics.txt"));

		boolean done = false;
		try {
			while (!done && in.hasNextLine()) {

				String line = in.nextLine();

				if (line.trim().equalsIgnoreCase("exit")) {
					done = true;
				} else {
					for( int i = 0; i < line.length(); i++ ){
						out.clearLine();
						try {
							Thread.sleep(600);
						} catch(Exception e) {
							e.printStackTrace();
						}

			    		out.print(line + "..." + i);
						//out.flush();
					}
				}

			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}

		try {
			clientSocket.close();
			clientSocket = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
