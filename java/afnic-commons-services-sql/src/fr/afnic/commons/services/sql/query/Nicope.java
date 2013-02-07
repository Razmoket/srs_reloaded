/*
 * $Id: $ $Revision: $ $Author: $
 */

package fr.afnic.commons.services.sql.query;

import fr.afnic.commons.services.sql.table.nicope.AdherentField;
import fr.afnic.commons.services.sql.table.nicope.NomDeDomaineField;
import fr.afnic.commons.services.sql.table.nicope.TicketDescEtatField;
import fr.afnic.commons.services.sql.table.nicope.TicketDescOpeField;
import fr.afnic.commons.services.sql.table.nicope.TicketField;
import fr.afnic.commons.services.sql.table.nicope.TicketHistoField;
import fr.afnic.commons.services.sql.table.nicope.TicketInfoField;

/**
 * 
 * Description des tables du schéma nicope.
 * 
 * 
 * 
 */
public final class Nicope extends TableSpace {

    private static final long serialVersionUID = 1L;

    private static final String TABLE_SPACE = "nicope";

    private static final Nicope INSTANCE = new Nicope();

    public static final Table<TicketField> TICKET_TABLE = Nicope.INSTANCE.createTable("ticket");
    public static final Table<TicketHistoField> TICKETHISTO_TABLE = Nicope.INSTANCE.createTable("tickethisto");
    public static final Table<NomDeDomaineField> NOMDEDOMAINE_TABLE = Nicope.INSTANCE.createTable("nomdedomaine");
    public static final Table<TicketDescEtatField> TICKETDESCETAT_TABLE = Nicope.INSTANCE.createTable("ticketdescetat");
    public static final Table<TicketDescOpeField> TICKETDESCOPE_TABLE = Nicope.INSTANCE.createTable("ticketdescoper");
    public static final Table<TicketInfoField> TICKETINFO_TABLE = Nicope.INSTANCE.createTable("ticketinfo");
    public static final Table<AdherentField> ADHERENT_TABLE = Nicope.INSTANCE.createTable("adherent");

    private Nicope() {
        super(Nicope.TABLE_SPACE);
    }

}
