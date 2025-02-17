package controlador;

import java.awt.Component;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.text.Normalizer;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

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
	
	public String[][] AplicarHorarios(DefaultTableModel modelo, String[] horarios) {
		String[][] horariosCompleto = new String[modelo.getRowCount()][modelo.getColumnCount()];
		for (int c = 1; c < modelo.getColumnCount(); c++) {
			for (int r = 0; r < modelo.getRowCount(); r++) {
				modelo.setValueAt(null, r, c);
				horariosCompleto[r][c] = "";
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
			if (dia != -1) {
				horariosCompleto[hora][dia + 1] = split[0];
		        modelo.setValueAt(split[0], hora, dia + 1);
			}
		}
		return horariosCompleto;
	}
	
	public void PrepararRenderer(JTable tabla, Component c, String[][] horariosComepleto, TableCellRenderer renderer, int row, int column) {
        if (c instanceof JComponent) {
            JComponent jc = (JComponent) c;
            Object val = tabla.getValueAt(row, column);
            if (val != null) {
            	String completo = horariosComepleto[row][column];
            	if (completo != null) {
            		Rectangle rect = tabla.getCellRect(row, column, true);
            		String result = WarpString(completo, val, jc, (int) rect.getWidth(), (int) rect.getHeight());
            		if (result != null)
            			tabla.setValueAt(result, row, column);
            	}
            }
        }
	}
	
	public String WarpString(String input, Object val, JComponent componente, int ancho, int alto) {
		Graphics g = componente.getGraphics();
    	if (g != null && val != null && val.toString().contentEquals(input)) {
    		String resultado = "";
        	FontMetrics met = g.getFontMetrics();
        	String[] split = input.split(" ");
        	int anchoTotal = 0;
        	int lineas = 1;
        	for (String palabra : split) {
            	int width = met.stringWidth(palabra+" ");
            	anchoTotal += width;
            	if (anchoTotal < (ancho - 10)) {
            		resultado += " "+palabra;
            	} else {
            		resultado += "<br>"+palabra;
            		anchoTotal = width;
            		lineas++;
            	}
        	}
        	if (lineas * met.getHeight() > (alto - 10)) {
		        Set<String> ignorado = new HashSet<>(Arrays.asList("de", "a", "e", "y"));
				String corta = Arrays.stream(input.split(" "))
		                .filter(word -> !ignorado.contains(word.toLowerCase()))
		                .map(word -> Normalizer.normalize(word, Normalizer.Form.NFKD).replaceAll("\\B.|\\P{L}", "").toUpperCase())
		                .collect(Collectors.joining(""));
				return corta;
        	}
        	return "<html>"+resultado.substring(1)+"</html>";
    	}
		return null;
	}
}
