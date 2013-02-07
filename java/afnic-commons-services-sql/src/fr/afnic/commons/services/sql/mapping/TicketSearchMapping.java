/*
 * $Id: $ $Revision: $ $Author: $
 */

package fr.afnic.commons.services.sql.mapping;

import fr.afnic.commons.beans.search.ticket.TicketSearchCriteria;
import fr.afnic.commons.beans.search.ticket.TicketSearchCriterion;
import fr.afnic.commons.services.sql.table.nicope.AdherentField;
import fr.afnic.commons.services.sql.table.nicope.NomDeDomaineField;
import fr.afnic.commons.services.sql.table.nicope.TicketDescEtatField;
import fr.afnic.commons.services.sql.table.nicope.TicketDescOpeField;
import fr.afnic.commons.services.sql.table.nicope.TicketField;
import fr.afnic.commons.services.sql.table.nicope.TicketHistoField;
import fr.afnic.commons.services.sql.table.whois.Identification2Field;

/**
 * Permet d'associer critère de recherche et colonne de filtre pour la construction d'une requete sql.
 * 
 * @author ginguene
 * 
 */
public class TicketSearchMapping extends AbstractSearchCriteriaMapping<TicketSearchCriteria> {

    public TicketSearchMapping() {
        this.map(TicketSearchCriterion.RegistrarCode, AdherentField.CODE);
        this.map(TicketSearchCriterion.DomainName, NomDeDomaineField.NOM);
        this.map(TicketSearchCriterion.Id, TicketField.ID);
        this.map(TicketSearchCriterion.Status, TicketDescEtatField.GW_STATUS);
        this.map(TicketSearchCriterion.Operation, TicketDescOpeField.GW_OPERATION_NAME);
        this.map(TicketSearchCriterion.EndingCreateDate, TicketHistoField.DATEMAJ);
        this.map(TicketSearchCriterion.BeginningCreateDate, TicketHistoField.DATEMAJ);
        this.map(TicketSearchCriterion.LegalStructureIdentifier, Identification2Field.DATA);
    }

}
