package programa;

import javax.swing.JOptionPane;

import modelo.Conexion;
import vista.Principal;

public class Main {
	
	public static void main(String[] args) {
		// iniciar conexion con el servidor
		try { new Conexion(); }
		catch(Exception e) {
			JOptionPane.showMessageDialog(null, "Error al conectarse al servidor, finalizando aplicacion.", "Error al conectar", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		Principal ventana = new Principal();
		ventana.setVisible(true);
	}

}
