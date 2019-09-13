import java.text.SimpleDateFormat;
import java.util.Date;

public class CellValue {
    private Date cellDate;
    private String cellFormula;
    private String errorMessage;
    private Boolean[][]isUsedInFormula;

    public CellValue() {
        cellDate = null;
        cellFormula = "";
        errorMessage = "";
        isUsedInFormula = new Boolean[20][27];
        for(int i = 0; i<20; ++i) {
            for(int j = 0; j<27; ++j){
                isUsedInFormula[i][j] = Boolean.FALSE;
            }
        }
    }

    @Override
    public String toString() {
        if(cellDate!=null) {
            return new SimpleDateFormat("dd.MM.yyyy").format(cellDate);
        }else return "";
    }

    public Date getCellDate() {
        return cellDate;
    }
    public String getCellFormula() {
        return cellFormula;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setCellDate(Date cellDate) {
        this.cellDate = cellDate;
    }

    public void setCellFormula(String cellFormula) {
        this.cellFormula = cellFormula;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public boolean isUsedInformula(int row, int column) {
        return isUsedInFormula[row][column];
    }

    public void makeUsedIn(int row, int column) {
       isUsedInFormula[row][column] = Boolean.TRUE;
    }
    public void makeUnUsedIn(int row, int column) {
        isUsedInFormula[row][column] = Boolean.FALSE;
    }

    public boolean isEmpty(){
        return cellDate==null && cellFormula.length()==0;
    }
}
