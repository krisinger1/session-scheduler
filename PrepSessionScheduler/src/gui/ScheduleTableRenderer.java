package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellRenderer;

import org.omg.CORBA.PRIVATE_MEMBER;

public class ScheduleTableRenderer implements TableCellRenderer {

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		JTextField field = new JTextField();
		field.setBorder(null);
		table.setRowHeight(20);
		table.setGridColor(Color.LIGHT_GRAY);
		table.setPreferredSize(new Dimension(250,350));
		table.setMinimumSize(new Dimension(150,300));

		String valueString = value.toString();
		field.setText(valueString);
		if (column!=0 && value!=null) {
			if (isSelected) {
				if ((Integer)table.getValueAt(row, column)==1) field.setBackground(new Color(250,160,130));
				else field.setBackground(new Color(100,230,90));
			}
			else{
				if (Integer.parseInt(valueString)==0) field.setBackground(new Color(170,250,160));
				else if(Integer.parseInt(valueString)==1) field.setBackground(new Color(250,190,160));
				else field.setBackground(Color.LIGHT_GRAY);
			}
		}
		else {
			field.setBackground(new Color(230, 230, 230));
		}

		return field;
	}

}
