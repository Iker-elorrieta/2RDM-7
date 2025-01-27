package vista;

import javax.swing.JPanel;

import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import controlador.Metodos;

import javax.swing.JScrollPane;
import javax.swing.JTable;

public class Horario extends JPanel {

	private static final long serialVersionUID = 1L;

	private JButton volverBoton;
	private JTable table;
	
	private Metodos metodos = new Metodos();
	
	private String[][] horariosCompleto = new String[6][6];
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
		
		table = new JTable() {
			private static final long serialVersionUID = 1L;

			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
		        Component c = super.prepareRenderer(renderer, row, column);
		        if (c instanceof JComponent) {
		            JComponent jc = (JComponent) c;
		            Object val = getValueAt(row, column);
		            if (val != null) {
		            	String completo = horariosCompleto[row][column];
		            	jc.setToolTipText(completo);
		            } else 
		            	jc.setToolTipText("-");
		        }
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
		
		int altura = Math.max((table.getParent().getParent().getBounds().height - 22) / table.getRowCount(), 20);
		for (int row = 0; row < table.getRowCount(); row++) {
	        table.setRowHeight(altura);
	    }
	}

	public JButton getVolverBoton() {
		return this.volverBoton;
	}
	
	public void setHorarios(String[] horarios) {
		horariosCompleto = metodos.AplicarHorarios(modelo, horarios);
	}
}
