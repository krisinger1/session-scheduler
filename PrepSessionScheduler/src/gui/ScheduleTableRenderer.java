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
		table.setRowHeight(25);
		table.setGridColor(Color.LIGHT_GRAY);
		table.setPreferredSize(new Dimension(250,450));
		table.setMinimumSize(new Dimension(150,500));
		if (column!=0 && value!=null) {
			String valueString = value.toString();
			field.setText(valueString);
			if (Integer.parseInt(valueString)==0) field.setBackground(Color.GREEN);
			else if(Integer.parseInt(valueString)==1) field.setBackground(Color.RED);
			else field.setBackground(Color.pink);
		}
		else {
			field.setText(value.toString());
			field.setBackground(new Color(230, 230, 230));
		}
		return field;
	}

}
