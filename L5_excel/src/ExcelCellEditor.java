import javax.swing.*;
import java.awt.*;


public class ExcelCellEditor extends DefaultCellEditor{
    public ExcelCellEditor() {
        super(new JTextField());
        //setClickCountToStart(2);//Specifies the number of clicks needed to start editing.
    }

    public Component getTableCellEditorComponent(JTable table,Object value,boolean isSelected,int row,int column){
        //returns editorComponent - the swing component, being edited
        JTextField currentCellText=(JTextField)super.getTableCellEditorComponent(table,value,isSelected,row,column);
        if(value != null ) {
            currentCellText.setText(((CellValue) value).getCellFormula());
        }
        return currentCellText;//if cell is empty
    }
}