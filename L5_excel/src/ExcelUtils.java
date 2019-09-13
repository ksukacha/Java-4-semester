import javafx.util.Pair;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExcelUtils {
    private static boolean isDate(String strInput)  {
        return Pattern.matches("(((0[1-9]|[12][0-9]|30)\\.(04|06|09|11))|" +
                "((0[1-9]|[12][0-9]|30|31)\\.(01|03|05|07|08|10|12))|" +
                "((0[1-9]|[12][0-9])\\.02))\\.[1-9]\\d{3}", strInput);
    }
    private static boolean isDateAndNumberFormula(String input) {
        return Pattern.matches("=(((0[1-9]|[12][0-9]|30)\\.(04|06|09|11))|" +
                "((0[1-9]|[12][0-9]|30|31)\\.(01|03|05|07|08|10|12))|" +
                "((0[1-9]|[12][0-9])\\.02))\\.[1-9]\\d{3}[+-]\\d+", input);
    }
    private static boolean isAdressAndNumberFormula(String input) {
        return Pattern.matches("=[A-Z]\\d{1,2}[+-]\\d+", input);
    }
    private static boolean isMinMaxFormula(String input) {
        return Pattern.matches("=(MIN|MAX)\\((((0[1-9]|[12][0-9]|30)\\.(04|06|09|11))|"+
                "((0[1-9]|[12][0-9]|30|31)\\.(01|03|05|07|08|10|12))|"+
                "((0[1-9]|[12][0-9])\\.02))\\.[1-9]\\d{3},[A-Z]\\d{1,2}\\)", input);
    }

    public static Pair<CellValue,CellValue> changeCellState(
            ExcelModel model, String strInput, int rowIndex, int colIndex) throws ParseException, ExcelDataParseException,CycleException {
        CellValue cell = new CellValue();
        if(strInput.length()==0){
            cell.setCellFormula("");
            cell.setCellDate(null);
            cell.setErrorMessage("");
        }
        if(isDate(strInput)){
            DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
            Date date = df.parse(strInput);
            cell.setCellDate(date);
            cell.setCellFormula(strInput);
        }
        else if(isDateAndNumberFormula(strInput)){
            Date firstSlag = new SimpleDateFormat("dd.MM.yyyy").parse(strInput.substring(1,11));
            char operator = strInput.charAt(11);
            int days = Integer.parseInt(strInput.substring(12));
            changeDate(cell, firstSlag, operator, days);
            cell.setCellFormula(strInput);
        }
        else if(isAdressAndNumberFormula(strInput)){
            Pattern p = Pattern.compile("[A-Z]\\d{1,2}");
            Matcher m = p.matcher(strInput);
            String adress;
            if(m.find()) {
                adress = strInput.substring(m.start(), m.end());
                char letter = adress.charAt(0);
                int row = Integer.parseInt(adress.substring(1));
                char operator = strInput.charAt(adress.length() + 1);
                int days = Integer.parseInt(strInput.substring(adress.length() + 2));
                CellValue firstSlag = getCell(model, letter, row);
                if(rowIndex == row-1 && colIndex ==((int)letter-64) ){
                    throw new CycleException("#CycleCountingNotAllowed!");
                }
                if(model.getData()[rowIndex][colIndex].isUsedInformula((row-1),(int)letter-64)){
                    throw new CycleException("#CycleCountingNotAllowed!");
                }
                if(model.getData()[row-1][(int)letter-64].getErrorMessage().length()!=0){
                    throw new ExcelDataParseException("#CellUnadressable!");
                }
                if(firstSlag.isEmpty()) {
                    throw new ExcelDataParseException("#EmptyCellAddress!");
                }
                else{
                    changeDate(cell, firstSlag.getCellDate(), operator, days);
                    cell.setCellFormula(strInput);
                    return new Pair<>(cell, firstSlag);
                }
            }
        }
        else if(isMinMaxFormula(strInput)){
            Pattern p = Pattern.compile("[A-Z]\\d{1,2}");
            Matcher m = p.matcher(strInput);
            if(m.find()) {
                String adress = strInput.substring(m.start(), m.end());
                char letter = adress.charAt(0);
                int row = Integer.parseInt(adress.substring(1));
                CellValue secondOperand = getCell(model, letter, row);
                if(rowIndex == row-1 && colIndex ==((int)letter-64)){
                    throw new CycleException("#CycleCountingNotAllowed!");
                }
                if(secondOperand.isEmpty()) {
                    throw new ExcelDataParseException("#EmptyCellAddress!");
                }else{
                    Date firstOperand = new SimpleDateFormat("dd.MM.yyyy").parse(strInput.substring(5, strInput.length() -
                            adress.length() - 1));
                    if (strInput.substring(1, 4).equals("MIN")) {
                        cell.setCellDate(new Date(Math.min(firstOperand.getTime(), secondOperand.getCellDate().getTime())));
                    } else {
                        cell.setCellDate(new Date(Math.max(firstOperand.getTime(), secondOperand.getCellDate().getTime())));
                    }
                    cell.setCellFormula(strInput);
                    return new Pair<>(cell, secondOperand);
                }
            }
        }
        else{
            throw new ExcelDataParseException("#DataParseError!");
        }
        return new Pair<>(cell, null);
    }

    private static CellValue getCell(ExcelModel model, char letter, int number)throws ExcelDataParseException{
        if(number==0) {
            throw new ExcelDataParseException("#CellUnaddressable!");

        }else {
            return model.getData()[number - 1][(int) letter - 64];
        }
    }
    private static void changeDate(CellValue cell, Date date, char operator, int days ) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        if(operator == '+'){
            c.add(Calendar.DATE, days);
            cell.setCellDate(c.getTime());
        }else{
            c.add(Calendar.DATE, -days);
            cell.setCellDate(c.getTime());
        }

    }

    //нужно для перезаписи тех ячеек, в которых используется
    //ячейка cell, если ячейка cell изменяется
    public static Vector<Pair<Integer, Integer>> cellsToUpdate(CellValue cell){
        Vector<Pair<Integer, Integer>> cellsToUpdate = new Vector<>();
        for(int i = 0; i<20;++i){
            for(int j = 0; j<27; ++j){
                if(cell.isUsedInformula(i,j)){
                    cellsToUpdate.addElement(new Pair<>(i,j));
                }
            }
        }
        if(cellsToUpdate.size()!=0) {//нужно, чтобы остановиться в рекурсии
            return cellsToUpdate;
        }else{
            return null;
        }
    }
}
