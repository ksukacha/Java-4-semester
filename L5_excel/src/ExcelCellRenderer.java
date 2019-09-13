import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class ExcelCellRenderer extends JTextField
        implements TableCellRenderer {
    public ExcelCellRenderer() {
        setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
        setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
        ExcelModel model = (ExcelModel)table.getModel();
        CellValue cell = model.getData()[row][column];
        if(column == 0){
            setText(model.getValueAt(row,column).toString());
        }
        else {
           if (cell.getErrorMessage().length() == 0) {
                if (hasFocus &&!cell.getCellFormula().equals("")) {
                    setText(cell.getCellFormula());
                    setForeground(table.getSelectionForeground());
                    setBackground(table.getSelectionBackground());
                } else {
                    setText(cell.toString());
                    setForeground(table.getForeground());
                    setBackground(table.getBackground());
                }
            } else {
                setText(cell.getErrorMessage());
            }
          /*  if(hasFocus &&!cell.getCellFormula().equals("")){
                    setText(cell.getCellFormula());
                    setForeground(table.getSelectionForeground());
                    setBackground(table.getSelectionBackground());
            }else if(!hasFocus &&cell.getErrorMessage().length() == 0){
                setText(cell.toString());
                setForeground(table.getForeground());
                setBackground(table.getBackground());
            }else if(!hasFocus &&cell.getErrorMessage().length() != 0){
                setText(cell.getErrorMessage());
            }*/

        }
        setBorder(hasFocus ?
                BorderFactory.createLineBorder(UIManager.getColor("Table.selectionForeground"), 1) :
                BorderFactory.createEmptyBorder(2,2,2,2));
        if(column==0){
            setBackground(new Color(242,242,242));
        }
        return this;
    }
}
