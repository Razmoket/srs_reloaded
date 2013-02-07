package fr.afnic.commons.checkers;

import java.util.regex.Pattern;

import fr.afnic.commons.services.exception.invalidformat.InvalidFormatException;
import fr.afnic.commons.services.exception.invalidformat.InvalidTicketIdException;

/**
 * Classe permettant de vérifier si un id est valide pour un ticket
 * 
 * @author ginguene
 * 
 */
public final class TicketIdChecker implements IInternalChecker {

    private static final long serialVersionUID = 1L;

    protected static final String PATTERN_TICKET_ID_STR = "^NIC\\d{12}$";
    protected static final Pattern PATTERN_TICKET_ID = Pattern.compile(PATTERN_TICKET_ID_STR);

    @Override
    public String check(String ticketId) throws InvalidFormatException {
        if (ticketId == null
               || !PATTERN_TICKET_ID.matcher(ticketId).find()) {
            throw new InvalidTicketIdException(ticketId);
        }
        return ticketId;

    }

}
