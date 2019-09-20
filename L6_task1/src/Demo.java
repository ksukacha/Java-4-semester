import javax.swing.*;
import java.awt.*;

public class Demo extends JFrame {
    public Demo() {
        this.setBounds(0,0, 1000, 700);
        PuzzlePanel leftPanel = new PuzzlePanel(3);
        SolutionPanel rightPanel = new SolutionPanel();
        this.setJMenuBar(leftPanel.getMenuBar());
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                leftPanel, rightPanel);
        splitPane.setOneTouchExpandable(true);
        add(splitPane, BorderLayout.CENTER);

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