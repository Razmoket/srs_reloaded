/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.checkers;

import java.util.regex.Pattern;

import fr.afnic.commons.beans.documents.EmailDocument;
import fr.afnic.commons.services.exception.invalidformat.InvalidDocumentHandleException;
import fr.afnic.commons.services.exception.invalidformat.InvalidFormatException;

/**
 * Vérifie la validité du format d'un handle.<br/>
 * Classe à remanier en cas de changement de GED.
 * 
 * 
 * 
 * 
 * @author ginguene
 * 
 */
public class MailDocumentHandleChecker implements IInternalChecker {

    private static final long serialVersionUID = 1L;

    protected static final String PATTERN_MAIL_HANDLE_FORMAT_STR = "^MailMessage-\\d+$";
    protected static final Pattern PATTERN_MAIL_HANDLE_FORMAT = Pattern.compile(MailDocumentHandleChecker.PATTERN_MAIL_HANDLE_FORMAT_STR);

    @Override
    public String check(String documentHandle) throws InvalidFormatException {
        if (documentHandle == null || !MailDocumentHandleChecker.PATTERN_MAIL_HANDLE_FORMAT.matcher(documentHandle).find()) {
            throw new InvalidDocumentHandleException(documentHandle, EmailDocument.class);
        }
        return documentHandle;
    }

}
