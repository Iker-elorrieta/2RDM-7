package modelo;

import javax.swing.table.DefaultTableModel;

public class TablaNoEditable extends DefaultTableModel {
	private static final long serialVersionUID = 1L;

	public TablaNoEditable(Object[][] data, String[] columnNames) {
        super(data, columnNames);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
}