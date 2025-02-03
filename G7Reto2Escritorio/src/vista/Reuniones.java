package vista;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import controlador.Metodos;
import modelo.CenterCellRenderer;
import modelo.TablaNoEditable;

public class Reuniones extends JPanel {

	private static final long serialVersionUID = 1L;

	private JButton volverBoton;
	private JTable table;

	private Metodos metodos = new Metodos();
	
	private Object[][][] reunionesCompleto = new Object[6][6][7];
	private DefaultTableModel modelo = new TablaNoEditable(
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
	
	public Reuniones() {
		setBounds(0, 0, 700, 380);
		setLayout(null);
		
		volverBoton = new JButton("Volver");
		volverBoton.setBounds(10, 11, 87, 23);
		add(volverBoton);
		
		JLabel infoLabel = new JLabel("haz doble click en una reunion para ver mas informacion");
		infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
		infoLabel.setBounds(0, 22, 700, 14);
		add(infoLabel);
		
		JScrollPane horarioPanel = new JScrollPane();
		horarioPanel.setBounds(10, 45, 680, 324);
		add(horarioPanel);
		
		table = new JTable() {
			private static final long serialVersionUID = 1L;

			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
				Component c = super.prepareRenderer(renderer, row, column);
				metodos.PrepararRendererReuniones(table, c, reunionesCompleto, renderer, row, column);
		        return c;
		    }
		};
		table.setFillsViewportHeight(true);
		table.setRowSelectionAllowed(false);
		table.setModel(modelo);
		table.getColumnModel().getColumn(0).setPreferredWidth(50);
		table.getColumnModel().getColumn(0).setMinWidth(40);
		table.getColumnModel().getColumn(0).setMaxWidth(50);
		horarioPanel.setViewportView(table);
		
		JLabel lblNewLabel = new JLabel("REUNIONES");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(0, 0, 700, 14);
		add(lblNewLabel);
		
		table.addMouseListener(new java.awt.event.MouseAdapter() {
		    @Override
		    public void mouseClicked(java.awt.event.MouseEvent evt) {
		    	int row = -1, col = -1;
		        row = table.getSelectedRow();
		        col = table.getSelectedColumn();
		        
		        if (evt.getClickCount() > 1 && row != -1 && col != -1) {
		        	Object value = table.getValueAt(row, col);
		        	if (value != null) {
		        		// mostrar info reunion
		        	}
		        }
		    }
		});

		CenterCellRenderer renderer = new CenterCellRenderer();
		for (int col = 0; col < table.getColumnCount(); col++) {
			table.getColumnModel().getColumn(col).setCellRenderer(renderer);
		}
		
		int altura = Math.max((table.getParent().getParent().getBounds().height - 22) / table.getRowCount(), 20);
		for (int row = 0; row < table.getRowCount(); row++) {
	        table.setRowHeight(altura);
	    }
	}

	public JButton getVolverBoton() {
		return this.volverBoton;
	}
	
	public void setReuniones(Object[][] reuniones) {
		reunionesCompleto = metodos.AplicarReuniones(modelo, reuniones);
	}
}
