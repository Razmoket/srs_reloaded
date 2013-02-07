/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.buisnessrules;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import fr.afnic.commons.beans.TicketOperation;
import fr.afnic.commons.beans.Ticket;
import fr.afnic.commons.beans.TicketStatus;

/**
 * Définit les changements de status de tickets autorisés par l'outil.<br/>
 * Les transitions de tickets décrites ici sont un sous-ensemble des transitions décrite dans nicope.TicketTransit.<br/>
 * Certaines transitions décrites dans cette table sont obsolète (même si on ne sait pas forcément lesquelles).<br/>
 * A terme il faudrait faire un peu le ménage dans cette table, et à ce moment là, relier cette classe à cette table.<br/>
 * <br/>
 * Classe peu découpé car on gère peu de transitions, si on est amené à en ajouter de nombreuse,<br/>
 * il faudra la redécouper pour qu'elle ne devienne pas une usine à gaz au fur et à mesure qu'elle grandit.<br/>
 * Il n'est pas à ce jour prévu de rajouter des transitions.
 * 
 * 
 * 
 * @author ginguene
 * 
 */
public final class TicketStatusChangeRule {

    /**
     * Structure qui permet d'avoir pour une opération, la liste des transistions autorisées. ex:
     * transitions.get(Operation.CheckExtraLegalSettlement).get(TicketStatus.PendingExtraLegalSettlement) retourne la liste<br/>
     * des nouveaux status possibles pour un ticket de CheckExtraLegalSettlement en statut PendingExtraLegalSettlement.
     * */
    private HashMap<TicketOperation, HashMap<TicketStatus, ArrayList<TicketStatus>>> transitions = new HashMap<TicketOperation, HashMap<TicketStatus, ArrayList<TicketStatus>>>();
    private static TicketStatusChangeRule instance = new TicketStatusChangeRule();

    public static List<TicketStatus> getAllowedNewStatusForTicket(Ticket ticket) {
        return TicketStatusChangeRule.getAllowedNewStatusForTicketStatus(ticket.getOperation(), ticket.getStatus());
    }

    public static List<TicketStatus> getAllowedNewStatusForTicketStatus(TicketOperation operation, TicketStatus initStatus) {
        if (TicketStatusChangeRule.instance.transitions.get(operation) != null && TicketStatusChangeRule.instance.transitions.get(operation).get(initStatus) != null) {
            return TicketStatusChangeRule.instance.transitions.get(operation).get(initStatus);
        } else {
            return Collections.emptyList();
        }
    }

    private TicketStatusChangeRule() {
        this.addTransitions(TicketOperation.CheckExtraLegalSettlement, TicketStatus.PendingExtraLegalSettlement, TicketStatus.Closed);

        this.addTransitions(TicketOperation.TransferDomain, TicketStatus.PendingRegistrarReject, TicketStatus.Cancelled);
        this.addTransitions(TicketOperation.TransferDomain, TicketStatus.PendingRegistrarApprove, TicketStatus.Cancelled);

        this.addTransitions(TicketOperation.TransferDomain, TicketStatus.PendingRegistrarReject, TicketStatus.PendingCheckover);
        this.addTransitions(TicketOperation.TransferDomain, TicketStatus.PendingHolderEMail, TicketStatus.PendingCheckover);

        this.addTransitions(TicketOperation.TradeDomain, TicketStatus.PendingHolderFax, TicketStatus.Cancelled);
        this.addTransitions(TicketOperation.TradeDomain, TicketStatus.PendingHolderEMail, TicketStatus.Cancelled);

    }

    private void addTransitions(TicketOperation operation, TicketStatus initStatus, TicketStatus newStatus) {
        if (this.transitions.get(operation) == null) {
            this.transitions.put(operation, new HashMap<TicketStatus, ArrayList<TicketStatus>>());
        }

        if (this.transitions.get(operation).get(initStatus) == null) {
            this.transitions.get(operation).put(initStatus, new ArrayList<TicketStatus>());
        }

        this.transitions.get(operation).get(initStatus).add(newStatus);
    }
}
