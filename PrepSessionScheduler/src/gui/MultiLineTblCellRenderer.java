package gui;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;

import com.mysql.fabric.xmlrpc.base.Data;

import model.Session;

public class MultiLineTblCellRenderer extends JList<String> implements TableCellRenderer{
	@Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        //make multi line where the cell value is String[]
//        if (value instanceof String[]) {
//		//if (column==1){
//        	//System.out.println(((String[])value)[1]+" "+((String[])value)[0]);
//            setListData((String[])value);
//        }
		if (value instanceof String){
			String[] data = ((String)value).split(",");
			// set height of row based on how many lines printed in row
	        table.setRowHeight((table.getFont().getSize()+8)*4);
			setListData(data);
		}
//		if (value instanceof ArrayList<?>){
//			System.out.println(" multilinerenderer in instanceof");
//			Object[] data =((ArrayList<Session>) value).toArray();
//		}
//
        //cell background color when selected
        if (isSelected) {
            setBackground(UIManager.getColor("Table.selectionBackground"));
        } else {
            setBackground(UIManager.getColor("Table.background"));
        }


        return this;
    }
}

