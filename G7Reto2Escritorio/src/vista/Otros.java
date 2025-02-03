package vista;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import controlador.Metodos;
import modelo.CenterCellRenderer;

import javax.swing.JComboBox;

import java.awt.Component;

import javax.swing.DefaultComboBoxModel;

public class Otros extends JPanel {

	private static final long serialVersionUID = 1L;

	private JButton volverBoton;
	private JTable table;
	JComboBox<String> profesorCombo;
	JScrollPane horarioPanel;
	
	private Metodos metodos = new Metodos();

	private String[][] horariosCompleto = new String[6][6];
	private DefaultComboBoxModel<String> comboModelo = new DefaultComboBoxModel<String>(new String[] {});
	private DefaultTableModel modelo = new DefaultTableModel(
			new Object[][] {
				{"8:00", null, null, null, null, null},
				{"9:00", null, null, null, null, null},
				{"10:00", null, null, null, null, null},
				{"11:00", null, null, null, null, null},
				{"12:00", null, null, null, null, null},
				{"13:00", null, null, null, null, null},
			},
			new String[] {
				"Horas", "Lunes", "Martes", "Miercoles", "Jueves", "Viernes"
			}
		);
	
	int[] profesores;
	
	public Otros() {
		setBounds(0, 0, 700, 380);
		setLayout(null);
		
		volverBoton = new JButton("Volver");
		volverBoton.setBounds(10, 11, 87, 23);
		add(volverBoton);
		
		JLabel lblNewLabel = new JLabel("OTROS HORARIOS");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(0, 3, 700, 14);
		add(lblNewLabel);
		
		horarioPanel = new JScrollPane();
		horarioPanel.setBounds(10, 77, 680, 292);
		add(horarioPanel);

		table = new JTable() {
			private static final long serialVersionUID = 1L;

			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
				Component c = super.prepareRenderer(renderer, row, column);
				metodos.PrepararRenderer(table, c, horariosCompleto, renderer, row, column);
		        return c;
		    }
		};
		table.setFillsViewportHeight(true);
		table.setEnabled(false);
		table.setRowSelectionAllowed(false);
		table.setModel(modelo);
		table.getColumnModel().getColumn(0).setPreferredWidth(50);
		table.getColumnModel().getColumn(0).setMinWidth(40);
		table.getColumnModel().getColumn(0).setMaxWidth(50);
		horarioPanel.setViewportView(table);

		CenterCellRenderer renderer = new CenterCellRenderer();
		for (int col = 0; col < table.getColumnCount(); col++) {
			table.getColumnModel().getColumn(col).setCellRenderer(renderer);
		}
		
		int altura = Math.max((table.getParent().getParent().getBounds().height - 22) / table.getRowCount(), 20);
		for (int row = 0; row < table.getRowCount(); row++) {
	        table.setRowHeight(altura);
	    }
		
		profesorCombo = new JComboBox<String>();
		profesorCombo.setModel(comboModelo);
		profesorCombo.setBounds(261, 44, 184, 22);
		add(profesorCombo);
	}

	public JButton getVolverBoton() {
		return this.volverBoton;
	}
	
	public JComboBox<String> getProfesorCombo() {
		return this.profesorCombo;
	}

	public JScrollPane getTablaPanel() {
		return this.horarioPanel;
	}
	
	public void setProfesores(Object[][] profesores) {
		this.profesores = new int[profesores.length];
		String[] modeloArray = new String[profesores.length];
		for (int i = 0; i < profesores.length; i++) {
			modeloArray[i] = (String) profesores[i][0];
			this.profesores[i] = (int) profesores[i][1];
		}
		profesorCombo.setModel(new DefaultComboBoxModel<String>(modeloArray));
	}
	
	public void setHorarios(Object[][] horarios) {
		horariosCompleto = metodos.AplicarHorarios(modelo, horarios);
	}
}
