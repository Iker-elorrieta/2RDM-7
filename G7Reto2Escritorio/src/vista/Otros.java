package vista;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

public class Otros extends JPanel {

	private static final long serialVersionUID = 1L;

	private JButton volverBoton;
	private JTable table;
	JComboBox<String> profesorCombo;
	
	private DefaultComboBoxModel<String> comboModelo = new DefaultComboBoxModel<String>(new String[] {});
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
	
	int[] profesores;
	
	public Otros() {
		setBounds(0, 0, 600, 380);
		setLayout(null);
		
		volverBoton = new JButton("Volver");
		volverBoton.setBounds(10, 11, 87, 23);
		add(volverBoton);
		
		JLabel lblNewLabel = new JLabel("OTROS HORARIOS");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(0, 3, 600, 14);
		add(lblNewLabel);
		
		JScrollPane horarioPanel = new JScrollPane();
		horarioPanel.setBounds(10, 77, 580, 292);
		add(horarioPanel);
		
		table = new JTable();
		table.setFillsViewportHeight(true);
		table.setRowSelectionAllowed(false);
		table.setModel(modelo);
		table.getColumnModel().getColumn(0).setPreferredWidth(50);
		table.getColumnModel().getColumn(0).setMinWidth(40);
		table.getColumnModel().getColumn(0).setMaxWidth(50);
		horarioPanel.setViewportView(table);
		
		profesorCombo = new JComboBox<String>();
		profesorCombo.setModel(comboModelo);
		profesorCombo.setBounds(210, 44, 184, 22);
		add(profesorCombo);
	}

	public JButton getVolverBoton() {
		return this.volverBoton;
	}
	
	public JComboBox<String> getProfesorCombo() {
		return this.profesorCombo;
	}
	
	public int[] getProfesores() {
		return this.profesores;
	}
	
	public void setProfesores(String[] profesores) {
		this.profesores = new int[profesores.length];
		String[] modeloArray = new String[profesores.length];
		for (int i = 0; i < profesores.length; i++) {
			String[] split = profesores[i].split(",");
			modeloArray[i] = split[0];
			this.profesores[i] = Integer.parseInt(split[1]);
		}
		profesorCombo.setModel(new DefaultComboBoxModel<String>(modeloArray));
	}
	
	public void setHorarios(String[] horarios) {
		System.out.println(horarios.length);
		Horario.aplicarHorarios(modelo, horarios);
	}
}
