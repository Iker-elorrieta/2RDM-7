package programa;

import javax.swing.JOptionPane;

import modelo.Conexion;
import vista.Principal;

public class Main {
	
	public static void main(String[] args) {
		try { new Conexion("127.0.0.1"); }
		catch(Exception e2) {
			JOptionPane.showMessageDialog(null, "Error al conectarse al servidor, finalizando aplicacion.", "Error al conectar", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		Principal ventana = new Principal();
		ventana.setVisible(true);
	}

}
