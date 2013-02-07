/*
 * $Id: $ $Revision: $ $Author: $
 */

package fr.afnic.commons.services.sql.table.nicope;

import fr.afnic.commons.services.sql.query.Nicope;
import fr.afnic.commons.services.sql.query.TableField;

public class TicketInfoField extends TableField<TicketInfoField> {

    /**
     * 
     */
    private static final long serialVersionUID = -8504851738283353056L;
    public static final TicketInfoField IDTICK = new TicketInfoField("IDTICK");
    public static final TicketInfoField REM = new TicketInfoField("REM");
    public static final TicketInfoField NUMFO = new TicketInfoField("NUMFO");
    public static final TicketInfoField MEL = new TicketInfoField("MEL");
    public static final TicketInfoField CONTROLE = new TicketInfoField("CONTROLE");
    public static final TicketInfoField AUTH = new TicketInfoField("AUTH");

    public TicketInfoField(final String name) {
        super(name, Nicope.TICKETINFO_TABLE);
    }

}
