import javafx.util.Pair;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;

public class PuzzlePanel extends JPanel {
    private JTable table;
    private  ImageIcon[][] parts;
    private ImageIcon image;
    private int size;
    private JMenuBar menuBar;
    public JMenuBar getMenuBar() {
        return menuBar;
    }

    public PuzzlePanel(int size) {
        this.size = size;
        ImageIcon originalImage = new ImageIcon("гора.jpg");
        image = new ImageIcon(originalImage.getImage().getScaledInstance
                (originalImage.getIconWidth() / 2, originalImage.getIconHeight() / 2, Image.SCALE_DEFAULT));
        menuBar = createMenu();
        try {
            parts = createPuzzle(image, size);
            PuzzleTableModel model = new PuzzleTableModel(parts);
            model.randomizePuzzle();

            table = new JTable(model);
            doTableSetting();
            table.addKeyListener(new ChangeEmptyCellPosition());
            setBorder(BorderFactory.createTitledBorder("Puzzle"));
            add(table, BorderLayout.NORTH);


        } catch (InterruptedException ex) { }
    }

    private ImageIcon getPartOfImage(ImageIcon image, int pix[], int x, int y, int size) throws InterruptedException {
        PixelGrabber pg = new PixelGrabber(image.getImage(), x, y, image.getIconWidth() / size, image.getIconHeight() / size,
                pix, 0, image.getIconWidth() / size);
        pg.grabPixels();
        return new ImageIcon(createImage(new MemoryImageSource(image.getIconWidth() / size, image.getIconHeight() / size,
                pix, 0, image.getIconWidth() / size)));
    }

    private ImageIcon[][] createPuzzle(ImageIcon image, int size) throws InterruptedException {
        ImageIcon[][] parts = new ImageIcon[size][size];
        int x = 0;
        int y = 0;
        int pix[] = new int[image.getIconWidth() * image.getIconHeight() / (size * size)];
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                parts[i][j] = getPartOfImage(image, pix, x, y, size);
                x += image.getIconWidth() / size;
            }
            x = 0;
            y += image.getIconHeight() / size;
        }
        return parts;
    }
    private void replaceImageParts(int x, int y, int i, int j){
        PuzzleTableModel model= (PuzzleTableModel)table.getModel();
        model.setValueAt(model.getValueAt(x, y),i,j);
        model.setValueAt(null,x,y);
        model.setEmptyCellCoordinates(new Pair<>(x,y));
    }
    private class ChangeEmptyCellPosition extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e) {
            PuzzleTableModel model= (PuzzleTableModel)table.getModel();
            int i = model.getEmptyCellCoordinates().getKey();
            int j = model.getEmptyCellCoordinates().getValue();
            if(e.getKeyCode()==KeyEvent.VK_RIGHT) {
                if(model.getEmptyCellCoordinates().getValue()!=0){
                    replaceImageParts(i,j-1,i,j);
                }
            }
            else if(e.getKeyCode()==KeyEvent.VK_LEFT) {
                if(model.getEmptyCellCoordinates().getValue()!=PuzzlePanel.this.parts.length-1){
                    replaceImageParts(i,j+1,i,j);
                }
            }
            else if(e.getKeyCode()==KeyEvent.VK_UP) {
                if(model.getEmptyCellCoordinates().getKey()!=PuzzlePanel.this.parts.length-1){
                    replaceImageParts(i+1,j,i,j);
                }
            }
            else if(e.getKeyCode()==KeyEvent.VK_DOWN) {
                if(model.getEmptyCellCoordinates().getKey()!=0){
                    replaceImageParts(i-1,j,i,j);
                }
            }
            model.fireTableDataChanged();
            if(checkOnWinCombination()) {
                JOptionPane.showMessageDialog(PuzzlePanel.this,"You won!");
            }
        }
    }
    private boolean checkOnWinCombination() {
        for(int i = 0; i<parts.length; ++i) {
            for(int j = 0; j<parts.length; ++j){
                if(i!=parts.length-1||j!=parts.length-1) {
                    if (table.getModel().getValueAt(i, j) != parts[i][j]) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    private JMenuBar createMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu file= new JMenu("File");
        JMenuItem restart = new JMenuItem("Restart");
        restart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(checkOnWinCombination()) {
                    ((PuzzleTableModel) table.getModel()).setImageIconParts(parts);
                     doTableSetting();
                    ((PuzzleTableModel) table.getModel()).randomizePuzzle();
                }
            }
        });
        file.add(restart);
        menuBar.add(file);
        return menuBar;
    }
    private void doTableSetting(){
        table.setRowHeight(image.getIconHeight() / size);
        for (int i = 0; i < size; i++) {
            table.getColumnModel().getColumn(i).setMinWidth(image.getIconWidth() / size);
        }
        table.setRowSelectionAllowed(false);
    }

}


