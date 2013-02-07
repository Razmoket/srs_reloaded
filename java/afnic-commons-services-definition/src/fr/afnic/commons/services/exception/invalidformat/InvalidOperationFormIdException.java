package fr.afnic.commons.services.exception.invalidformat;

/**
 * Exception retournée si un parametre désignant un id de formulaire n'a pas le bon format.
 * 
 * @author ginguene
 * 
 */
public class InvalidOperationFormIdException extends InvalidFormatException {

    private static final long serialVersionUID = 1L;

    private String operationFormId;

    public InvalidOperationFormIdException(String operationFormId) {
        super("'" + operationFormId + "' is not a valid operation form id");
        this.operationFormId = operationFormId;
    }

    public String getOperationFormId() {
        return this.operationFormId;
    }

}
