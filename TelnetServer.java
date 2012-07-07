import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class TelnetServer extends Thread {

	private static ServerSocket server = null;
	private static Integer		port   = 4444;

	public TelnetServer(int port){
		try{
			this.server = new ServerSocket(port);
			this.port = port;
		}catch(Exception e){
			e.printStackTrace();
			return;
		}

	}

	public void run(){
		if( server == null ) return;
		System.out.println("Telnet Server Listening on port " + port);
		while (true) {
			try {
				TelnetClient tc = new TelnetClient(server.accept());
			  	tc.start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} 
	}

	public static void main(String[] args) {
		TelnetServer ts = new TelnetServer(Integer.parseInt(args[0]));
		ts.start();
	}

}
