package kz.ibragimov.excelparser_spring_project.exceptions;

public class FieldValidationException extends RuntimeException{
    private String fieldName;

    public FieldValidationException(String message, String fieldName){
        super(message);
        this.fieldName = fieldName;
    }

    public String getFieldName(){
        return fieldName;
    }

}
