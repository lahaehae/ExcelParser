package kz.ibragimov.excelparser_spring_project.exceptions;

public class CellValidationException extends RuntimeException {

    private int rowIndex;

    public CellValidationException(String message, int rowNumber) {
        super(message);
        this.rowIndex = rowIndex;
    }

    public int getRowNumber(){
        return rowIndex;
    }
}
