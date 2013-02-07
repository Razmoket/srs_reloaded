/*
 * $Id: $ $Revision: $ $Author: $
 */

package fr.afnic.commons.services.sql.join;

import fr.afnic.commons.services.sql.table.nicope.AdherentField;
import fr.afnic.commons.services.sql.table.nicope.NomDeDomaineField;
import fr.afnic.commons.services.sql.table.nicope.TicketDescEtatField;
import fr.afnic.commons.services.sql.table.nicope.TicketDescOpeField;
import fr.afnic.commons.services.sql.table.nicope.TicketField;
import fr.afnic.commons.services.sql.table.nicope.TicketHistoField;
import fr.afnic.commons.services.sql.table.nicope.TicketInfoField;

/**
 * Classe à générer en fonction des contraintes entre tables
 * 
 * @author ginguene
 * 
 */
public class ConstraintJoinMap extends JoinMap {

    public ConstraintJoinMap() {
        this.addJoin(NomDeDomaineField.ID, TicketField.IDNOMDOM);
        this.addJoin(TicketField.IDETAT, TicketDescEtatField.ID);
        this.addJoin(TicketField.IDOPE, TicketDescOpeField.ID);
        this.addJoin(TicketField.ID, TicketInfoField.IDTICK);
        this.addJoin(AdherentField.ID, TicketField.IDADHER);
        this.addJoin(TicketField.ID, TicketHistoField.IDTICK);
    }
}
