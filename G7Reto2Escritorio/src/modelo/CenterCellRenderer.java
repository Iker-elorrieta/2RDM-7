package modelo;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class CenterCellRenderer extends DefaultTableCellRenderer {
	private static final long serialVersionUID = 1L;

	public Component getTableCellRendererComponent(JTable table, Object value,boolean isSelected, boolean hasFocus,int row, int column){
      JLabel label = (JLabel)super.getTableCellRendererComponent(table, value,isSelected, hasFocus,row, column);
      label.setHorizontalAlignment(CENTER);
      return label;
   }
}
