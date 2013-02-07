/*
 * $Id: $ $Revision: $ $Author: $
 */

package fr.afnic.commons.services.sql.table.nicope;

import fr.afnic.commons.services.sql.query.Nicope;
import fr.afnic.commons.services.sql.query.TableField;

public class TicketHistoField extends TableField<TicketHistoField> {

    /**
     * 
     */
    private static final long serialVersionUID = -825338723652858655L;
    public static final TicketHistoField IDTICK = new TicketHistoField("IDTICK");
    public static final TicketHistoField NUM = new TicketHistoField("NUM");
    public static final TicketHistoField DATEMAJ = new TicketHistoField("DATEMAJ");
    public static final TicketHistoField USERMAJ = new TicketHistoField("USERMAJ");
    public static final TicketHistoField REMHISTO = new TicketHistoField("REMHISTO");
    public static final TicketHistoField IDETAT = new TicketHistoField("IDETAT");

    public TicketHistoField(final String name) {
        super(name, Nicope.TICKETHISTO_TABLE);
    }

}
