package gui;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;

public class MultiLineTblCellRenderer implements TableCellRenderer{
	JList<String> data;
	JLabel name;

	MultiLineTblCellRenderer(){
		data = new JList<String>();
	}

	@Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        //make multi-line where the cell value is String
		if (column==1){
			if (value instanceof String){
				String[] dataArray = ((String)value).split(",");
				// set height of row based on how many lines printed in row
				// setting rowheight in renderer is wrong according to stack overflow. Fix this?
				// or is it only wrong to do it here if changing dynamically when someone resizes column?
		        table.setRowHeight(row,(table.getFont().getSize()+8)*dataArray.length);
				data.setListData(dataArray);
			}

	        //cell background color when selected
	        if (isSelected) {
	            data.setBackground(UIManager.getColor("Table.selectionBackground"));
	        } else {
	            data.setBackground(UIManager.getColor("Table.background"));
	        }
	        return data;
		}
		else {
			name = new JLabel((String)value);
			return name;
		}
    }
}

///////////////////////////////////  OLD renderer  //////////////////////////////////////////////////////////
//public class MultiLineTblCellRenderer extends JList<String> implements TableCellRenderer{
//	@Override
//    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
//        //make multi line where the cell value is String
//		if (column==1 && value instanceof String){
//			String[] data = ((String)value).split(",");
//			// set height of row based on how many lines printed in row
//	        table.setRowHeight((table.getFont().getSize()+8)*(data.length>1 ? data.length:8));
//	      //  table.setRowHeight((table.getFont().getSize()+8)*5);
//
//			setListData(data);
//		}
//
////		if (value instanceof ArrayList<?>){
////			System.out.println(" multilinerenderer in instanceof");
////			Object[] data =((ArrayList<Session>) value).toArray();
////		}
////
//        //cell background color when selected
//        if (isSelected) {
//            setBackground(UIManager.getColor("Table.selectionBackground"));
//        } else {
//            setBackground(UIManager.getColor("Table.background"));
//        }
//
//
//        return this;
//    }
//}

