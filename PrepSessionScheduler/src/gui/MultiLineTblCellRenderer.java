package gui;

import java.awt.Component;

import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;

public class MultiLineTblCellRenderer extends JList<String> implements TableCellRenderer{
	@Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        //make multi line where the cell value is String[]
        if (value instanceof String[]) {
		//if (column==1){
        	System.out.println(((String[])value)[1]+" "+((String[])value)[0]);
            setListData((String[])value);
        }
//		if (value instanceof String){
//			String[] data = ((String)value).split(",");
//			setListData(data);
//		}
        
        //cell background color when selected
        if (isSelected) {
            setBackground(UIManager.getColor("Table.selectionBackground"));
        } else {
            setBackground(UIManager.getColor("Table.background"));
        }

        return this;
    }
}

