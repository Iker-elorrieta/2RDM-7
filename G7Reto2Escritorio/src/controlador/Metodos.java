package controlador;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import modelo.Conexion;

public class Metodos {
	
	private final String mensajeDesconectar = "DESCONECTAR";
	private final String mensajeLogout = "LOGOUT";
	
	public boolean Login(JTextField campoUsuario, JTextField campoClave, JLabel errorLabel) {
		String usuario = campoUsuario.getText().toString();
		String clave = campoClave.getText().toString();
		
		if (usuario.isEmpty()) errorLabel.setText("el campo de usuario es invalido");
		else if (clave.isEmpty()) errorLabel.setText("el campo de clave es invalido");
		else {
			errorLabel.setText("");
			try {
				DataOutputStream output = new DataOutputStream(Conexion.conexion.getOutputStream());
				DataInputStream input = new DataInputStream(Conexion.conexion.getInputStream());
				
				output.writeUTF(usuario);
				output.writeUTF(clave);
				
				boolean respuesta = input.readBoolean();
				//System.out.println("recieved: " + String.valueOf(respuesta));
				
				if (!respuesta) errorLabel.setText("credenciales invalidas");
				return respuesta;
			} catch(Exception ioe) { errorLabel.setText("no se pudo iniciar sesion"); }
		}
		return false;
	}
	
	public void Desconectar() {
		try {
			DataOutputStream output = new DataOutputStream(Conexion.conexion.getOutputStream());
			
			output.writeUTF(mensajeLogout);
		} catch(Exception ioe) {  }
	}


	public void Cerrar() throws IOException {
		DataOutputStream output = new DataOutputStream(Conexion.conexion.getOutputStream());
        output.writeUTF(mensajeDesconectar);
        output.flush();
        
        Conexion.conexion.close();
	}
	
	public void AplicarHorarios(DefaultTableModel modelo, String[] horarios) {
		for (int c = 1; c < modelo.getColumnCount(); c++) {
			for (int r = 0; r < modelo.getRowCount(); r++) {
				modelo.setValueAt(null, r, c);
			}
		}
		for (int i = 0; i < horarios.length; i++) {
			String[] split = horarios[i].split(",");
			
			int hora = Integer.parseInt(split[1]);
			int dia = -1;
			switch (split[2].charAt(0)) {
				case 'L': dia = 0; break;
				case 'M': dia = 1; break;
				case 'X': dia = 2; break;
				case 'J': dia = 3; break;
				case 'V': dia = 4; break;
			}
			if (dia != -1)
				modelo.setValueAt(split[0], hora, dia + 1);
		}
	}
}
