/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.checkers;

import java.util.regex.Pattern;

import fr.afnic.commons.beans.documents.SimpleDocument;
import fr.afnic.commons.services.exception.invalidformat.InvalidDocumentHandleException;
import fr.afnic.commons.services.exception.invalidformat.InvalidFormatException;

/**
 * Vérifie la validité du format d'un handle.<br/>
 * Classe à remanier en cas de changement de GED.
 * 
 * @author ginguene
 * 
 */
public class SimpleDocumentHandleChecker implements IInternalChecker {

    private static final long serialVersionUID = 1L;

    protected static final String PATTERN_SIMPLE_DOCUMENT_HANDLE_FORMAT_STR = "^Document-\\d+$";
    protected static final Pattern PATTERN_SIMPLE_DOCUMENT_HANDLE_FORMAT = Pattern.compile(SimpleDocumentHandleChecker.PATTERN_SIMPLE_DOCUMENT_HANDLE_FORMAT_STR);

    @Override
    public String check(String documentHandle) throws InvalidFormatException {
        if (documentHandle == null || !SimpleDocumentHandleChecker.PATTERN_SIMPLE_DOCUMENT_HANDLE_FORMAT.matcher(documentHandle).find()) {
            throw new InvalidDocumentHandleException(documentHandle, SimpleDocument.class);
        }
        return documentHandle;
    }

}
