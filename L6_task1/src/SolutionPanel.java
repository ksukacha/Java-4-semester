import javax.swing.*;
import java.awt.*;

public class SolutionPanel extends JPanel {
    public SolutionPanel() {
        ImageIcon originalImage = new ImageIcon("гора.jpg");
        ImageIcon image = new ImageIcon(originalImage.getImage().getScaledInstance
                (originalImage.getIconWidth() / 2, originalImage.getIconHeight() / 2, Image.SCALE_DEFAULT));
        JLabel solutionImage = new JLabel(image);
        setBorder(BorderFactory.createTitledBorder("Solution to puzzle"));
        add(solutionImage, BorderLayout.CENTER);
    }
}
