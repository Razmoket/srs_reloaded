/*
 * $Id: TicketGenerator.java,v 1.5 2010/07/20 15:48:17 ginguene Exp $
 * $Revision: 1.5 $
 * $Author: ginguene $
 */

package fr.afnic.commons.test.generator;

/**
 * Permet de générer des tickets utilisés pour les tests.
 * 
 * @author ginguene
 * 
 */
public final class TicketGenerator {

    /** En dur car c'est un cas interressant avec un commentaire et que l'on ne sait pas en faire via la gateway **/
    public static final String VALID_TICKET_ID = "NIC000000047150";

    public static final String UNKNOWN_TICKET_ID = "NIC100000000000";

    public static final String INVALID_TICKET_ID = "ba";

    /**
     * Constructeur privé pour empecher l'instanciation de cette classe utilitaire.
     */
    private TicketGenerator() {
    }

    public static String getValidTicketId() {
        return VALID_TICKET_ID;
    }

    public static String getUnknownTicketId() {
        return UNKNOWN_TICKET_ID;
    }

}
