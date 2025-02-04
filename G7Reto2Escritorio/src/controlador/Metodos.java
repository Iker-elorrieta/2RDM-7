package controlador;

import java.awt.Color;
import java.awt.Component;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.Timestamp;
import java.text.Normalizer;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.swing.JComboBox;
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
	private final String mensajeHorario = "HORARIO";
	private final String mensajeProfesores = "PROFESORES";
	private final String mensajeOtros = "OTROS";
	private final String mensajeReuniones = "REUNIONES";
	
	private ObjectInputStream oinput;
	
	public void CrearInputStream() {
		try {
			oinput = new ObjectInputStream(Conexion.conexion.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
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
				output.writeBoolean(false);
				
				boolean respuesta = input.readBoolean();
				System.out.println("recieved: " + String.valueOf(respuesta));
				
				if (respuesta) {
					Object[] datos = new Object[0];
					if (oinput != null) {
						datos = (Object[]) oinput.readObject();
						System.out.println(datos.length);
						
						oinput.readUTF(); // matriculacion
					}
				} else
					errorLabel.setText("credenciales invalidas");
				
				return respuesta;
			} catch(Exception ioe) { errorLabel.setText("no se pudo iniciar sesion"); ioe.printStackTrace(); }
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
	
	public String[][] AplicarHorarios(DefaultTableModel modelo, Object[][] horarios) {
		String[][] horariosCompleto = new String[modelo.getRowCount()][modelo.getColumnCount()];
		for (int c = 1; c < modelo.getColumnCount(); c++) {
			for (int r = 0; r < modelo.getRowCount(); r++) {
				modelo.setValueAt(null, r, c);
				horariosCompleto[r][c] = "";
			}
		}
		for (int i = 0; i < horarios.length; i++) {
			int hora = Integer.parseInt((String) horarios[i][1]);
			int dia = -1;
			switch (((String) horarios[i][2]).charAt(0)) {
				case 'L': dia = 0; break;
				case 'M': dia = 1; break;
				case 'X': dia = 2; break;
				case 'J': dia = 3; break;
				case 'V': dia = 4; break;
			}
			if (dia != -1) {
				horariosCompleto[hora][dia + 1] = (String) horarios[i][0];
		        modelo.setValueAt(horarios[i][0], hora, dia + 1);
			}
		}
		return horariosCompleto;
	}
	
	public Object[][][] AplicarReuniones(DefaultTableModel modelo, Object[][] reuniones) {
		Object[][][] reunionesCompleto = new String[modelo.getRowCount()][modelo.getColumnCount()][7];
		for (int c = 1; c < modelo.getColumnCount(); c++) {
			for (int r = 0; r < modelo.getRowCount(); r++) {
				modelo.setValueAt(null, r, c);
				for (int x = 0; x < reunionesCompleto[r][c].length; x++) {
					reunionesCompleto[r][c][x] = null;
				}
			}
		}

		Calendar c = Calendar.getInstance();
		for (int x = 0; x < reuniones.length; x++) {
			for (int y = 0; y < reuniones[x].length; y++) {
				System.out.println(y+": "+reuniones[x][y].toString());
			}
			Calendar fecha = (Calendar) c.clone();
			fecha.setTime((Timestamp) reuniones[x][3]);
			int hora = Math.clamp(fecha.get(Calendar.HOUR_OF_DAY) - 8, 0, 5);
			int dia = Math.clamp(fecha.get(Calendar.DAY_OF_WEEK), 1, 5);
			for (int y = 0; y < reuniones[x].length; y++) {
				if (reuniones[x][y].getClass() == Timestamp.class)
					reunionesCompleto[hora][dia][y] = reuniones[x][y].toString();
				else reunionesCompleto[hora][dia][y] = reuniones[x][y];
			}
			modelo.setValueAt((String) reuniones[x][1], hora, dia);
		}

		return reunionesCompleto;
	}
	
	public void PrepararRenderer(JTable tabla, Component c, Object[][] completo, TableCellRenderer renderer, int row, int column) {
        if (c instanceof JComponent) {
            JComponent jc = (JComponent) c;
            Object val = tabla.getValueAt(row, column);
            if (val != null) {
            	String comp = (String) completo[row][column];
            	if (comp != null) {
            		Rectangle rect = tabla.getCellRect(row, column, true);
            		String result = WarpString(comp, val, jc, (int) rect.getWidth(), (int) rect.getHeight());
            		if (result != null)
            			tabla.setValueAt(result, row, column);
            	}
            }
        }
	}
	
	public void PrepararRendererReuniones(JTable tabla, Component c, Object[][][] completo, TableCellRenderer renderer, int row, int column) {
        if (c instanceof JComponent) {
            JComponent jc = (JComponent) c;
            Object val = tabla.getValueAt(row, column);
			jc.setBackground(Color.white);
            if (val != null) {
            	String comp = (String) completo[row][column][1];
            	if (comp != null) {
            		Rectangle rect = tabla.getCellRect(row, column, true);
            		String result = WarpString(comp, val, jc, (int) rect.getWidth(), (int) rect.getHeight());
            		if (result != null)
            			tabla.setValueAt(result, row, column);
            		
            		switch ((String) completo[row][column][0]) {
	            		case "pendiente": jc.setBackground(Color.orange); break;
	            		case "conflicto": jc.setBackground(Color.gray); break;
	            		case "aceptada": jc.setBackground(Color.green); break;
	            		case "denegada": jc.setBackground(Color.red); break;
            		}
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
	
	public Object[][] ObtenerHorarios() {
		Object[][] array = new Object[0][0];
		try {
            DataOutputStream output = new DataOutputStream(Conexion.conexion.getOutputStream());
            output.writeUTF(mensajeHorario);
            output.flush();

            if (oinput != null) array = (Object[][]) oinput.readObject();
        } catch (IOException | ClassNotFoundException ioe) {
        	ioe.printStackTrace();
        }
		return array;
	}
	
	public Object[][] ObtenerProfesores() {
		Object[][] array = new Object[0][0];
		try {
            DataOutputStream output = new DataOutputStream(Conexion.conexion.getOutputStream());
            output.writeUTF(mensajeProfesores);
            output.flush();

            if (oinput != null) array = (Object[][]) oinput.readObject();
        } catch (IOException | ClassNotFoundException ioe) {
        	ioe.printStackTrace();
        }
		return array;
	}
	
	public Object[][] ObtenerOtrosHorarios(int[] profesores, JComboBox<String> combo) {
		Object[][] array = new String[0][0];
		try {
            DataOutputStream output = new DataOutputStream(Conexion.conexion.getOutputStream());
            output.writeUTF(mensajeOtros);
            output.flush();
            
            output.writeInt(profesores[combo.getSelectedIndex()]);
            output.flush();

            if (oinput != null) array = (Object[][]) oinput.readObject();
        } catch (IOException | ClassNotFoundException ioe) {
        	ioe.printStackTrace();
        }
		return array;
	}
	
	public Object[][] ObtenerReuniones() {
		Object[][] array = new Object[0][0];
		try {
            DataOutputStream output = new DataOutputStream(Conexion.conexion.getOutputStream());
            output.writeUTF(mensajeReuniones);
            output.flush();

            if (oinput != null) array = (Object[][]) oinput.readObject();
        } catch (IOException | ClassNotFoundException ioe) {
        	ioe.printStackTrace();
        }
		return array;
	}
}
