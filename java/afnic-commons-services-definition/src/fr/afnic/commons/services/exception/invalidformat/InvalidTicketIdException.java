/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/services/exception/invalidformat/InvalidTicketIdException.java#2 $
 * $Revision: #2 $
 * $Author: ginguene $
 */

package fr.afnic.commons.services.exception.invalidformat;

/**
 * Exception retourée si un parametre désignant un nom de domaine n'a pas le bon format.
 * 
 * @author ginguene
 * 
 */
public class InvalidTicketIdException extends InvalidFormatException {

    private static final long serialVersionUID = 1L;

    private String ticketId;

    public InvalidTicketIdException(String ticketId) {
        super("'" + ticketId + "' is not a valid ticket id");
        this.ticketId = ticketId;
    }

    public String getTicketId() {
        return this.ticketId;
    }

}
