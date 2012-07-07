import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import flexjson.JSONDeserializer;
import flexjson.locators.TypeLocator;

public class Main extends JFrame implements ActionListener{
	
	private static final long serialVersionUID = 3206847208968227199L;

	private JPanel      mapa, opciones;
	private JScrollPane scroll, scroll2;
	private JButton     generar;
	private JTextField  dx, dy;
	private JTextArea   codigo;
	
	private JMenuBar  menu;
	private JMenu	  archivo;
	private JMenuItem guardar;
	
	@SuppressWarnings("deprecation")
	public Main() {
		super("Prueba de componentes");
		
		setSize(1280,768);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		menu	 = new JMenuBar();
		mapa     = new JPanel();
		opciones = new JPanel();
		codigo   = new JTextArea(5, 30);
		dx		 = new JTextField("2000", 4);
		dy		 = new JTextField("1000", 4);
		archivo  = new JMenu("Archivo");
		guardar  = new JMenuItem("Guardar");
		generar  = new JButton("Generar");
		scroll   = new JScrollPane(mapa);
		scroll2  = new JScrollPane(codigo);
		
		generar.addActionListener(this);
		guardar.addActionListener(this);
		
		mapa.setPreferredSize(new Dimension(2000, 1000));
		mapa.setLayout(null);
		mapa.setBackground(Color.black);
		
		opciones.setLayout(new FlowLayout());
		opciones.add(new JLabel("Ancho:"));
		opciones.add(dx);
		opciones.add(new JLabel("Alto:"));
		opciones.add(dy);
		opciones.add(generar);
		
		
		getContentPane().add(scroll, BorderLayout.CENTER);
		getContentPane().add(scroll2, BorderLayout.WEST);
		getContentPane().add(opciones, BorderLayout.SOUTH);
		
		menu.add(archivo);
		archivo.add(guardar);
		
		setJMenuBar(menu);
		
		String line = "";
		try{
			DataInputStream fr = new DataInputStream(new FileInputStream(new File("json.txt")));
			while( ( line = fr.readLine()) != null )
				codigo.append(line + "\n");
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
		
		setVisible(true);
	}
	
	public static void main(String args[]){
		new Main();
	}
	
	public void guardar(){
		try{
	    	DataOutputStream fw = new DataOutputStream(new FileOutputStream(new File("json.txt")));
	    	fw.writeBytes(codigo.getText());
	    	fw.close();
	    }catch(IOException ioe){
	    	ioe.printStackTrace();
	    }
	}
	
	public void generar(){
		mapa.setPreferredSize(new Dimension(Integer.parseInt(dx.getText()), Integer.parseInt(dy.getText())));
		mapa.removeAll();
		
		JSONDeserializer<List<Elemento>> deserializer = new JSONDeserializer<List<Elemento>>();
		
		String jsonin = codigo.getText();
		jsonin = jsonin.replaceAll("largo:", "width:").replaceAll("alto:", "height:");
		
		@SuppressWarnings("unchecked")
		TypeLocator<String> add = new TypeLocator<String>("tipo")
                .add("VIA", Via.class)
                .add("VIA_CURVA", ViaCurva.class)
                .add("AGUJA", Aguja.class)
                .add("ETIQUETA", Etiqueta.class)
                .add("MARCA_ZONA", MarcaZona.class)
                .add("MARCA_SECCION", MarcaSeccion.class)
                .add("ESTACION", Estacion.class)
                .add("DMT", Dmt.class)
                .add("LINEA", Linea.class)
                .add("CORTE_ENERGIA", CorteEnergia.class)
                .add("DBO", Dbo.class)
                .add("RELOJ", Reloj.class)
                .add("RECTANGULO", Rectangulo.class)
                .add("CIRCULO", Circulo.class)
                .add("PERSONAL_EN_VIAS", PersonalEnVias.class)
                .add("SEMAFORO", Semaforo.class);

		List<Elemento> elems = deserializer.use("values", add).deserialize(jsonin);
		
		for( Elemento e : elems ){
			if( e != null ){
				Rectangle r = new Rectangle();
				r.setSize(e.getDimension());
				r.setLocation(e.getPosicion());
				e.setBounds(r);
				e.setSize(new Dimension(e.getDimension().width+1, e.getDimension().height+1));
				mapa.add(e);
			}
		}
		mapa.repaint();
	}

	public void actionPerformed(ActionEvent evento) {
		String cmd = evento.getActionCommand();
		if(cmd.equals("Generar")){
			generar();
		}else if(cmd.equals("Guardar")){
			guardar();
		}
	}
}
