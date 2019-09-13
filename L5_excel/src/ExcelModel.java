import javafx.util.Pair;

import javax.swing.table.AbstractTableModel;
import java.text.ParseException;
import java.util.*;

public class ExcelModel extends AbstractTableModel {
    private CellValue[][]data;
    private boolean cellChanged;
    private final String[] columnNames = new String[]{
            " ", "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "O", "P", "Q", "R", "S",
            "T", "U", "V", "W", "X", "Y", "Z"
    };

    public ExcelModel() {
        data = new CellValue[20][27];
        for(int i = 0; i<20; ++i){
            for(int j = 0; j<27; ++j){
                data[i][j] = new CellValue();
            }
        }
        cellChanged = false;
    }

    public CellValue[][] getData() {
        return data;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public int getRowCount() {
        return data.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if(columnIndex!=0) {
            return data[rowIndex][columnIndex];
        }
        else return String.valueOf(rowIndex+1);

    }

    @Override
    public boolean isCellEditable(int row, int column) {
       return column!=0;
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int colIndex) {
        if (value instanceof String && !((String) value).isEmpty()) {
            try {
                Pair<CellValue, CellValue> changedCells = ExcelUtils.changeCellState(this, (String) value, rowIndex, colIndex);
                if (changedCells.getValue()!=null){//значит, использовалась формула =>нужно пометить флажок в ту ячейку, которая исп. в текущей
                  makeCellsUsedInFormula(changedCells.getValue(), rowIndex, colIndex);
                }else{
                    makeCellsUnUsedInFormula(rowIndex,colIndex);
                }
                if(!data[rowIndex][colIndex].getCellFormula().equals("")&&
                    data[rowIndex][colIndex].getCellDate()!=null){//значит, мы перезаписываем уже существующую ячейку
                    cellChanged = true;
                }
                data[rowIndex][colIndex].setErrorMessage("");
                data[rowIndex][colIndex].setCellDate(changedCells.getKey().getCellDate());
                data[rowIndex][colIndex].setCellFormula(changedCells.getKey().getCellFormula());
                if(cellChanged){//нужно пересчитать все ячейки, в формуле которых использовалась текущая ячейка
                     Pair<Integer, Integer> originalIndex = new Pair<>(rowIndex, colIndex);
                     checkOnCycling(rowIndex, colIndex, originalIndex);
                     recount(rowIndex, colIndex);
                }
            } catch (ParseException | ExcelDataParseException|CycleException ex) {
                data[rowIndex][colIndex].setErrorMessage(ex.getMessage());
            }

        }else if(((String) value).isEmpty()){
            data[rowIndex][colIndex].setCellDate(null);
            data[rowIndex][colIndex].setCellFormula("");
            data[rowIndex][colIndex].setErrorMessage("");
        }
    }
    private void makeCellsUsedInFormula(CellValue cell, int rowIndex, int colIndex){
        for(int i = 0; i<20; ++i) {
            for (int j = 0; j < 27; ++j) {
                if (data[i][j] == cell) {
                    data[i][j].makeUsedIn(rowIndex, colIndex);//помечаем флажок для адреса, использующегося в формуле
                }
            }
        }
    }
    private void makeCellsUnUsedInFormula(int rowIndex, int colIndex){
        for(int i = 0; i<20; ++i) {
            for (int j = 0; j < 27; ++j) {
                if (data[i][j].isUsedInformula(rowIndex, colIndex)) {
                    data[i][j].makeUnUsedIn(rowIndex, colIndex);//если ячейка, которая раньше использовалась в (rowIndex,colIndex),
                    //теперь там не используется, т.к. поменяли саму ячейку (i,j)
                }
            }
        }
    }

    private void checkOnCycling(int rowIndex, int colIndex, Pair<Integer, Integer> originalIndex) throws CycleException {
        cellChanged = false;
        Vector<Pair<Integer, Integer>>cellsToUpdate = ExcelUtils.cellsToUpdate(data[rowIndex][colIndex]);
        if (cellsToUpdate != null) {
            for (int i = 0; i < cellsToUpdate.size(); ++i) {
                if (cellsToUpdate.get(i).equals(originalIndex)) {
                    throw new CycleException("#CycleCountingNotAllowed!");
                }
                int iRow = cellsToUpdate.get(i).getKey();
                int iColumn = cellsToUpdate.get(i).getValue();
                checkOnCycling(iRow, iColumn, originalIndex);
            }
        }
    }
    private void recount(int rowIndex, int colIndex)throws ParseException, ExcelDataParseException, CycleException {
        //те ячейки, в которых использовалась текущая ячейка - их нужно обновить

        Vector<Pair<Integer, Integer>> cellsToUpdate = ExcelUtils.cellsToUpdate(data[rowIndex][colIndex]);
        if (cellsToUpdate != null) {
            for (int i = 0; i < cellsToUpdate.size(); ++i) {
                int iRow = cellsToUpdate.get(i).getKey();
                int iColumn = cellsToUpdate.get(i).getValue();

                Pair<CellValue, CellValue> recounted = ExcelUtils.changeCellState(this,
                        data[iRow][iColumn].getCellFormula(),
                        iRow, iColumn);
                data[iRow][iColumn].setCellDate(recounted.getKey().getCellDate());
                data[iRow][iColumn].setCellFormula(recounted.getKey().getCellFormula());
                fireTableCellUpdated(iRow, iColumn);//обновляет сразу после нажатия enter
                recount(iRow, iColumn);//запустить рекурсивный пересчет
            }
            cellChanged = false;
            cellsToUpdate.clear();
        }
    }

}
