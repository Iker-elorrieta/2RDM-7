package controlador;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import javax.swing.JComboBox;

import modelo.Conexion;

public class InterfazControl {

	private final String mensajeHorario = "HORARIO";
	private final String mensajeProfesores = "PROFESORES";
	private final String mensajeOtros = "OTROS";

	private ObjectInputStream ois;
	
	public void CrearInputStream() {
		try {
			ois = new ObjectInputStream(Conexion.conexion.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String[] ObtenerHorarios() {
		String[] array = new String[0];
		try {
            DataOutputStream output = new DataOutputStream(Conexion.conexion.getOutputStream());
            output.writeUTF(mensajeHorario);
            output.flush();

            if (ois != null) array = (String[]) ois.readObject();
        } catch (IOException | ClassNotFoundException ioe) {
        	ioe.printStackTrace();
        }
		return array;
	}
	
	public String[] ObtenerProfesores() {
		String[] array = new String[0];
		try {
            DataOutputStream output = new DataOutputStream(Conexion.conexion.getOutputStream());
            output.writeUTF(mensajeProfesores);
            output.flush();

            if (ois != null) array = (String[]) ois.readObject();
        } catch (IOException | ClassNotFoundException ioe) {
        	ioe.printStackTrace();
        }
		return array;
	}
	
	public String[] ObtenerOtrosHorarios(int[] profesores, JComboBox<String> combo) {
		String[] array = new String[0];
		try {
            DataOutputStream output = new DataOutputStream(Conexion.conexion.getOutputStream());
            output.writeUTF(mensajeOtros);
            output.flush();
            
            output.writeInt(profesores[combo.getSelectedIndex()]);
            output.flush();

            if (ois != null) array = (String[]) ois.readObject();
        } catch (IOException | ClassNotFoundException ioe) {
        	ioe.printStackTrace();
        }
		return array;
	}
}
