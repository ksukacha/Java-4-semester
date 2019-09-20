import javafx.util.Pair;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.Random;

public class PuzzleTableModel extends AbstractTableModel {
    private ImageIcon[][] parts;
    private Pair<Integer,Integer> emptyCellCoordinates;

    public Pair<Integer, Integer> getEmptyCellCoordinates() {
        return emptyCellCoordinates;
    }

    public void setEmptyCellCoordinates(Pair<Integer, Integer> emptyCellCoordinates) {
        this.emptyCellCoordinates = emptyCellCoordinates;
    }

    private final Object[] columnNames = new Object[]{
            null, null, null
    };

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return ImageIcon.class;
    }

    public PuzzleTableModel(ImageIcon[][] parts) {
        setImageIconParts(parts);
    }
    public void setImageIconParts(ImageIcon[][] parts){
        this.parts = new ImageIcon[parts.length][parts.length];
        for(int i = 0; i< parts.length; ++i){
            for(int j = 0; j<parts.length;++j){
                this.parts[i][j] = parts[i][j];
            }
        }
        emptyCellCoordinates = new Pair<>(parts.length-1, parts.length-1);
    }


    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if(rowIndex == emptyCellCoordinates.getKey()  && columnIndex==emptyCellCoordinates.getValue()) {
            return null;
        }
      return parts[rowIndex][columnIndex];

    }


    @Override
    public int getColumnCount() {
        return parts.length;
    }

    @Override
    public int getRowCount() {
        return parts.length;
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int colIndex) {

        parts[rowIndex][colIndex] = (ImageIcon) value;
    }
    public void randomizePuzzle() {
        int minN = 100;
        int maxN= 200;
        int diff = maxN- minN;
        Random random1 = new Random();
        int nRandom = random1.nextInt(diff + 1);
        nRandom += minN;

        Random ranForDirection = new Random();
        int dirRandom;

        Random ranForNumberOfCells = new Random();
        int cellsNum;
        for(int k = 0; k<nRandom; ++k){
            dirRandom = Math.abs(ranForDirection.nextInt())%2;
            int i = emptyCellCoordinates.getKey();
            int j = emptyCellCoordinates.getValue();
            if(dirRandom == 0) {//horizontal
                if(j>0){
                   cellsNum= Math.abs(ranForNumberOfCells.nextInt())%(j);
                    for(int t=j; t>cellsNum; --t){
                        setValueAt(getValueAt(i, t-1), i,t);
                    }
                    setValueAt(null, i,cellsNum);
                    emptyCellCoordinates = new Pair<>(i, cellsNum);
                }
                else {
                    cellsNum = Math.abs(ranForNumberOfCells.nextInt())%(parts.length-1)+1;
                    for(int t = 1; t<=cellsNum; ++t){
                        setValueAt(getValueAt(i,t), i, t-1);
                    }
                    setValueAt(null,i,cellsNum);
                    emptyCellCoordinates = new Pair<>(i, cellsNum);
                }
            }
            else if(dirRandom==1){//vertical
                if(i>0){
                   cellsNum = Math.abs(ranForNumberOfCells.nextInt())%i;
                    for(int t = i; t>cellsNum; t--){
                        setValueAt(getValueAt(t-1,j), t, j);
                    }
                    setValueAt(null, cellsNum, j);
                    emptyCellCoordinates = new Pair<>(cellsNum, j);
                }
                else{
                   cellsNum =Math.abs(ranForNumberOfCells.nextInt())%(parts.length-1)+1;
                    for(int t = 1; t<=cellsNum; ++t){
                        setValueAt(getValueAt(t,j), t-1,j);
                    }
                    setValueAt(null, cellsNum, j);
                    emptyCellCoordinates = new Pair<>(cellsNum, j);
                }
            }
        }
    }
}
