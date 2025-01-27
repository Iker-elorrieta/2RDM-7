package controlador;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.swing.JLabel;
import javax.swing.JTextField;

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
}
