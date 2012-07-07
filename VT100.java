import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

public class VT100{

	public final static Character ESC = '\u001B';

    private PrintWriter out = null;

	public VT100(OutputStream out){
		this.out = new PrintWriter(out, true);
	}

	public void print(String s){
		out.print(s);
		out.flush();
	}

	public void gotoxy(int x, int y){
    	out.print(ESC + "[" + y + ";" + x + "H");
	}

	public void setColor(Color color){
		out.print(ESC + "[" + color + "m");
	}
    
	public void setBackground(Color color){
		out.println(ESC + "[" + (color.getCode() + 10) + "m");
	}

	public void clearLine(){
        out.print(ESC + "[2K");
	}

    public void clearScreen(){
		out.print(ESC + "[2J");
	}

 	public void print(int x, int y, String s){
    	out.print(ESC + "[" + y + ";" + x + "H" + s);
	}

	public void print(File file){
		try {
			InputStream is = new FileInputStream(file);
            Scanner in = new Scanner(is);
			while( in.hasNextLine() ) {
				String line =  in.nextLine();
				out.println(line);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
