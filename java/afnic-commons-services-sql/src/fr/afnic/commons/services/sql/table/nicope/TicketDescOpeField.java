/*
 * $Id: $ $Revision: $ $Author: $
 */

package fr.afnic.commons.services.sql.table.nicope;

import fr.afnic.commons.services.sql.query.Nicope;
import fr.afnic.commons.services.sql.query.TableField;

public class TicketDescOpeField extends TableField<TicketDescOpeField> {

    /**
     * 
     */
    private static final long serialVersionUID = -8017220870988828044L;
    public static final TicketDescOpeField ID = new TicketDescOpeField("ID");
    public static final TicketDescOpeField OPERATION = new TicketDescOpeField("OPERATION");
    public static final TicketDescOpeField GENRE = new TicketDescOpeField("GENRE");
    public static final TicketDescOpeField DIFFUSION = new TicketDescOpeField("DIFFUSION");
    public static final TicketDescOpeField SHORT_ID = new TicketDescOpeField("SHORT_ID");
    public static final TicketDescOpeField GW_OPERATION_NAME = new TicketDescOpeField("GW_OPERATION_NAME");

    public TicketDescOpeField(final String name) {
        super(name, Nicope.TICKETDESCOPE_TABLE);
    }

}
