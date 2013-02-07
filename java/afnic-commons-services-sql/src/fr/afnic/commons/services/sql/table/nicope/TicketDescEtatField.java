/*
 * $Id: $ $Revision: $ $Author: $
 */

package fr.afnic.commons.services.sql.table.nicope;

import fr.afnic.commons.services.sql.query.Nicope;
import fr.afnic.commons.services.sql.query.TableField;

public class TicketDescEtatField extends TableField<TicketDescEtatField> {

    /**
     * 
     */
    private static final long serialVersionUID = -5536130652937916176L;
    public static final TicketDescEtatField ID = new TicketDescEtatField("ID");
    public static final TicketDescEtatField ETAT = new TicketDescEtatField("ETAT");
    public static final TicketDescEtatField GW_STATUS = new TicketDescEtatField("GW_STATUS");

    public TicketDescEtatField(final String name) {
        super(name, Nicope.TICKETDESCETAT_TABLE);
    }

}
