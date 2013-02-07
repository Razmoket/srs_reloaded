package fr.afnic.commons.services.exception.invalidformat;

import fr.afnic.commons.beans.documents.Document;

/**
 * Exception levée lorqu'un handle de document n'ayant pas le bon format est utilisé.
 * 
 * @author ginguene
 * 
 */
public class InvalidDocumentHandleException extends InvalidFormatException {

    private static final long serialVersionUID = 1L;

    private String documentHandle;
    private Class<?> documentClass;

    public <E extends Document> InvalidDocumentHandleException(String documentHandle, Class<E> documentClass) {
        super("'" + documentHandle + "' is not a valid " + documentClass.getSimpleName() + " handle");
        this.documentHandle = documentHandle;
        this.documentClass = documentClass;
    }

    public Class<?> getDocumentClass() {
        return documentClass;
    }

    public String getDocumentHandle() {
        return this.documentHandle;
    }

}
