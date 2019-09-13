import javax.swing.*;
import java.awt.*;

public class Demo extends JFrame {
    public Demo() {
        super("Excel");
        this.setBounds(0,0, 1000, 400);
        this.setContentPane(new View(new ExcelModel()));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Demo app = new Demo();
                app.setVisible(true);
            }
        });
    }

}
