import javax.swing.*;
import java.awt.*;

public class View extends JPanel {
    private JTable table;
    public View(ExcelModel model) {
        setLayout(new BorderLayout());
        table = new JTable(model);
        table.setDefaultRenderer(Object.class, new ExcelCellRenderer());
        table.setDefaultEditor(Object.class, new ExcelCellEditor());

        table.setRowSelectionAllowed(false);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        for (int i = 1; i < 26; i++) {
            table.getColumnModel().getColumn(i).setMinWidth(150);
        }
        for(int i = 0; i<20;++i){
            table.setRowHeight(20);
        }
        table.getColumnModel().getColumn(0).setMaxWidth(20);
        add(new JScrollPane(table));
    }
}
