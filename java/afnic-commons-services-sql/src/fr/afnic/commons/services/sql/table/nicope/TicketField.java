/*
 * $Id: $ $Revision: $ $Author: $
 */

package fr.afnic.commons.services.sql.table.nicope;

import fr.afnic.commons.services.sql.query.Nicope;
import fr.afnic.commons.services.sql.query.TableField;

public class TicketField extends TableField<TicketField> {

    private static final long serialVersionUID = -1710829452546284901L;

    public static final TicketField ID = new TicketField("ID");
    public static final TicketField IDNOMDOM = new TicketField("IDNOMDOM");
    public static final TicketField NOMORG = new TicketField("NOMORG");
    public static final TicketField OPE = new TicketField("OPE");
    public static final TicketField IDADHER = new TicketField("IDADHER");
    public static final TicketField LASHISTO = new TicketField("LASHISTO");
    public static final TicketField SEED = new TicketField("SEED");
    public static final TicketField IDETAT = new TicketField("IDETAT");
    public static final TicketField TYPE = new TicketField("TYPE");
    public static final TicketField IDOPE = new TicketField("IDOPE");
    public static final TicketField IDEPRECEDENT = new TicketField("IDEPRECEDENT");

    private TicketField(final String name) {
        super(name, Nicope.TICKET_TABLE);
    }

}
