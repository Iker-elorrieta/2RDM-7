package vista;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.Color;
import java.awt.SystemColor;

public class Horario extends JPanel {

	private static final long serialVersionUID = 1L;

	private JButton volverBoton;
	private JTable table;
	
	private DefaultTableModel modelo = new DefaultTableModel(
			new Object[][] {
				{"1", null, null, null, null, null},
				{"2", null, null, null, null, null},
				{"3", null, null, null, null, null},
				{"4", null, null, null, null, null},
				{"5", null, null, null, null, null},
				{"6", null, null, null, null, null},
			},
			new String[] {
				"Horas", "Lunes", "Martes", "Miercoles", "Jueves", "Viernes"
			}
		);
	
	public Horario() {
		setBounds(0, 0, 700, 380);
		setLayout(null);
		
		volverBoton = new JButton("Volver");
		volverBoton.setBounds(10, 11, 87, 23);
		add(volverBoton);
		
		JLabel lblNewLabel = new JLabel("HORARIO");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(0, 3, 700, 14);
		add(lblNewLabel);
		
		JScrollPane horarioPanel = new JScrollPane();
		horarioPanel.setBounds(10, 45, 680, 324);
		add(horarioPanel);
		
		table = new JTable();
		table.setEnabled(false);
		table.setRowSelectionAllowed(false);
		table.setModel(modelo);
		table.getColumnModel().getColumn(0).setPreferredWidth(50);
		table.getColumnModel().getColumn(0).setMinWidth(40);
		table.getColumnModel().getColumn(0).setMaxWidth(50);
		horarioPanel.setViewportView(table);
	}

	public JButton getVolverBoton() {
		return this.volverBoton;
	}
	
	public void setHorarios(String[] horarios) {
		aplicarHorarios(modelo, horarios);
	}
	
	public static void aplicarHorarios(DefaultTableModel modelo, String[] horarios) {
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
