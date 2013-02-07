/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/checkers/DocumentHandleChecker.java#5 $
 * $Revision: #5 $
 * $Author: barriere $
 */

package fr.afnic.commons.checkers;

import java.util.regex.Pattern;

import fr.afnic.commons.services.exception.invalidformat.InvalidFormatException;

/**
 * Vérifie la validité du format d'un handle.<br/>
 * Classe à remanier en cas de changement de GED.
 * 
 * 
 * @TODO Créer un service de validité des identifiants de documents.
 * 
 * 
 * @author ginguene
 * 
 */
public final class DocumentHandleChecker implements IInternalChecker {

    private static final long serialVersionUID = 1L;

    protected static final String PATTERN_MAIL_HANDLE_FORMAT_STR = "^MailMessage-\\d+$";
    protected static final Pattern PATTERN_MAIL_HANDLE_FORMAT = Pattern.compile(DocumentHandleChecker.PATTERN_MAIL_HANDLE_FORMAT_STR);

    protected static final String PATTERN_SIMPLE_DOCUMENT_HANDLE_FORMAT_STR = "^Document-\\d+$";
    protected static final Pattern PATTERN_SIMPLE_DOCUMENT_HANDLE_FORMAT = Pattern.compile(DocumentHandleChecker.PATTERN_SIMPLE_DOCUMENT_HANDLE_FORMAT_STR);

    protected static final String PATTERN_GDD_DOCUMENT_HANDLE_FORMAT_STR = "^AF_Document_GDD-\\d+$";
    protected static final Pattern PATTERN_GDD_DOCUMENT_HANDLE_FORMAT = Pattern.compile(DocumentHandleChecker.PATTERN_GDD_DOCUMENT_HANDLE_FORMAT_STR);

    /**
     * Ce constucteur en protected permet de s'assurer que cette classe utilitaire ne sera pas instanciée.
     * 
     */
    private DocumentHandleChecker() {
    }

    public static boolean isValidDocumentHandle(String handle) {
        return DocumentHandleChecker.isValidMailHandle(handle)
                || DocumentHandleChecker.isValidGddDocumentHandle(handle)
                || DocumentHandleChecker.isValidSimpleDocumentHandle(handle);
    }

    public static boolean isValidMailHandle(String handle) {
        if (handle == null) {
            return false;
        }
        return DocumentHandleChecker.PATTERN_MAIL_HANDLE_FORMAT.matcher(handle).find();
    }

    public static boolean isValidGddDocumentHandle(String handle) {
        if (handle == null) {
            return false;
        }
        return DocumentHandleChecker.PATTERN_GDD_DOCUMENT_HANDLE_FORMAT.matcher(handle).find();
    }

    public static boolean isValidSimpleDocumentHandle(String handle) {
        if (handle == null) {
            return false;
        }
        return DocumentHandleChecker.PATTERN_SIMPLE_DOCUMENT_HANDLE_FORMAT.matcher(handle).find();
    }

    @Override
    public String check(String documentHandle) throws InvalidFormatException {
        return documentHandle;

    }

}
