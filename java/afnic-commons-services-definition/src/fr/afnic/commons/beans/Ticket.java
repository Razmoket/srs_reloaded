/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/beans/Ticket.java#37 $
 * $Revision: #37 $
 * $Author: ginguene $
 */

package fr.afnic.commons.beans;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fr.afnic.commons.beans.customer.Customer;
import fr.afnic.commons.beans.customer.CustomerId;
import fr.afnic.commons.beans.domain.Domain;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.beans.validatable.OperationFormId;
import fr.afnic.commons.buisnessrules.TicketStatusChangeRule;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;

/**
 * Représente un ticket d'opération afnic. <br/>
 * Ce dernier décrit une opération effectuée sur un nom de domaine telle q'une création, un trade ou une suppresion
 * 
 * @author ginguene
 * 
 */
public class Ticket implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    protected String domainName;
    protected String id;
    protected OperationFormId operationFormId; // numéro du formaulaire

    protected CustomerId registrarId;
    protected String registrarCode;
    protected String registrarName;
    protected String originalRequesterName;

    protected Domain domain = null;
    protected Customer registrar = null;
    protected OperationForm operationForm = null;

    protected TicketOperation operation;
    protected TicketStatus status;
    protected Date createDate;

    protected String comment;

    protected String domainNicopeId;

    public String getDomainNicopeId() {
        return this.domainNicopeId;
    }

    public void setDomainNicopeId(String domainNicopeId) {
        this.domainNicopeId = domainNicopeId;
    }

    /** le user appelant l'objet */
    protected final UserId userIdCaller;
    /** le tld sur lequel on appelle l'objet */
    protected final TldServiceFacade tldCaller;

    public Ticket(UserId userId, TldServiceFacade tld) {
        this.userIdCaller = userId;
        this.tldCaller = tld;
    }

    public String getComment() {
        return this.comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    protected List<TicketHistoryEvent> history = null;

    public TicketStatus getStatus() {
        return this.status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    public String getStatusDescription() throws ServiceException {
        if (this.status == null) {
            return "";
        } else {
            return this.status.getDescription(this.userIdCaller, this.tldCaller);
        }
    }

    public void setStatusFromString(String statusString) {
        if (statusString == null) {
            this.status = null;
        } else {
            try {
                this.status = TicketStatus.valueOf(statusString);
            } catch (Exception e) {
                throw new IllegalArgumentException("Invalid ticket status:" + statusString);
            }
        }

    }

    public TicketOperation getOperation() {
        return this.operation;
    }

    public void setOperationFromString(String operationCodeStr) {
        if (operationCodeStr == null) {
            this.operation = null;
        } else {
            try {
                this.operation = TicketOperation.valueOf(operationCodeStr);
            } catch (Exception e) {
                throw new IllegalArgumentException("Invalid ticket operation:" + operationCodeStr);
            }
        }
    }

    public void setOperation(TicketOperation operation) {
        this.operation = operation;
    }

    public String getOperationDescription() throws ServiceException {
        if (this.operation == null) {
            return "";
        } else {
            return this.operation.getDescription(this.userIdCaller, this.tldCaller);
        }
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDomainName() {
        return this.domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public Date getCreateDate() {
        if (this.createDate != null) {
            return (Date) this.createDate.clone();
        } else {
            return null;
        }
    }

    /**
     * Dernière personne ayant effectué des modification Si il n'y en a pas retourne ""
     * 
     * @return
     * @throws ServiceException
     */
    public String getLastUpdater() throws ServiceException {
        TicketHistoryEvent lastEntry = this.getLastHistoryEntry();
        if (lastEntry != null) {
            return lastEntry.getUser();
        } else {
            return "";
        }
    }

    public String getLastUpdateDateStr() throws ServiceException {
        Date lastUpdateDate = this.getLastUpdateDate();
        if (lastUpdateDate == null) {
            return "";
        } else {
            return Ticket.DATE_FORMAT.format(lastUpdateDate);
        }
    }

    /**
     * Retourne la date de la dernière entrée effectuée dans l'historique
     * 
     * @return
     * @throws ServiceException
     */
    public Date getLastUpdateDate() throws ServiceException {
        TicketHistoryEvent lastEntry = this.getLastHistoryEntry();
        if (lastEntry != null) {
            return lastEntry.getDate();
        } else {
            return null;
        }
    }

    /**
     * Retourne la dernière entrée dans l'historique du ticket
     * 
     * @return
     * @throws ServiceException
     */
    private TicketHistoryEvent getLastHistoryEntry() throws ServiceException {
        TicketHistoryEvent lastTicketHistoryEvent = null;
        if (this.getHistory() != null && this.history.size() > 0) {
            for (TicketHistoryEvent event : this.history) {
                if (lastTicketHistoryEvent == null
                    || event.getDate().after(lastTicketHistoryEvent.getDate())) {
                    lastTicketHistoryEvent = event;
                }
            }
        }
        return lastTicketHistoryEvent;
    }

    /**
     * retourne une chaine de caractère correspondant à la date de creation. On peut definir le format de cette date via la methode setDateFormat
     * 
     * @return
     * @throws ServiceException
     * @Deprecated la logique d'affichage ne doit pas se trouver ici mais dans les gui
     */
    @Deprecated
    public String getCreateDateStr() throws ServiceException {
        if (this.getCreateDate() == null) {
            return "";
        } else {
            return Ticket.DATE_FORMAT.format(this.getCreateDate());
        }
    }

    public void setCreateDate(Date createDate) {
        if (createDate != null) {
            this.createDate = (Date) createDate.clone();
        } else {
            this.createDate = null;
        }
    }

    public String getRegistrarCode() {
        return this.registrarCode;
    }

    public List<String> getEMails() throws ServiceException {
        return AppServiceFacade.getTicketService().getEmail(this.id, this.userIdCaller, this.tldCaller);
    }

    public void setRegistrarCode(String registrarCode) {
        this.registrarCode = registrarCode;
    }

    public String getRegistrarName() throws ServiceException {
        if (this.registrarName == null && this.registrarCode != null) {
            if (this.getRegistrar() != null) {
                this.registrarName = this.getRegistrar().getName();
            }
        }
        return this.registrarName;
    }

    public void setRegistrarName(String registrarName) {
        this.registrarName = registrarName;
    }

    @Override
    public String toString() {
        return "Ticket: id[" + this.id + "]";
    }

    public boolean addHistoryEntry(TicketHistoryEvent element) {
        if (this.history == null) {
            this.history = new ArrayList<TicketHistoryEvent>();
        }
        return this.history.add(element);
    }

    public List<TicketHistoryEvent> getHistory() throws ServiceException {
        if (this.history == null) {
            this.history = AppServiceFacade.getTicketService()
                                           .getHistoryEventsWithTicketId(this.id, this.userIdCaller, this.tldCaller);

        }
        return this.history;
    }

    public void setHistory(List<TicketHistoryEvent> history) {
        this.history = history;
        if (this.history != null && this.history.size() > 0) {
            this.setCreateDate(this.history.get(0).getDate());
        }
    }

    /**
     * Récupère l'objet qui correspond au nom de domaine du ticket
     * 
     * @return
     * @throws ServiceException
     */
    public Domain getDomain() throws ServiceException {
        if (this.domain == null) {
            this.domain = AppServiceFacade.getDomainService()
                                          .getDomainWithName(this.domainName, this.userIdCaller, this.tldCaller);
        }
        return this.domain;

    }

    /**
     * Récupère l'objet qui correspond au handle du be associé au ticket
     * 
     * @return {@link Registrar}
     * @throws ServiceException
     *             Si l'operation échoue.
     */
    public Customer getRegistrar() throws ServiceException {
        if (this.registrar == null) {

            if (this.registrarId != null) {
                this.registrar = AppServiceFacade.getCustomerService().getCustomerWithId(this.registrarId, this.userIdCaller, this.tldCaller);
            } else if (this.registrarCode != null) {
                this.registrar = AppServiceFacade.getCustomerService().getCustomerWithCode(this.registrarCode, this.userIdCaller, this.tldCaller);
            }
        }
        return this.registrar;
    }

    public void setRegistrarId(CustomerId registrarId) {
        this.registrarId = registrarId;
    }

    public OperationFormId getOperationFormId() {
        return this.operationFormId;
    }

    public void setOperationFormId(OperationFormId operationFormId) {
        this.operationFormId = operationFormId;
    }

    /**
     * Indique si le ticket est lié à un formulaire
     * 
     * @return {@link boolean}
     */
    public boolean hasOperationForm() {
        return this.operationFormId != null;
    }

    public String getOriginalRequesterName() {
        return this.originalRequesterName;
    }

    public void setOriginalRequesterName(String originalRequesterName) {
        this.originalRequesterName = originalRequesterName;
    }

    public OperationForm getOperationForm() throws ServiceException {
        if (this.hasOperationForm()) {
            return AppServiceFacade.getOperationFormService().getOperationFormWithId(this.operationFormId, this.userIdCaller, this.tldCaller);
        } else {
            return null;
        }

    }

    /**
     * Indique si un commentaire est disponible pour le ticket.
     * 
     * @return {@link boolean}
     */
    public boolean hasComment() {
        return this.comment != null
               && !this.comment.isEmpty();
    }

    /**
     * Indique si un nom de requeteur est disponible pour le ticket.
     * 
     * @return {@link boolean}
     */
    public boolean hasOriginalRequesterName() {
        return this.originalRequesterName != null
               && !this.originalRequesterName.isEmpty();
    }

    /**
     * Indique si le ticket est en statut final
     * 
     * @return {@link boolean}
     */
    public boolean hasFinalStatus() {
        return this.status.isFinal();
    }

    /**
     * Indique si le ticket est n'est pas en statut final
     * 
     * @return {@link boolean}
     */
    public boolean hasNotFinalStatus() {
        return !this.hasFinalStatus();
    }

    /**
     * Retourne une copie du ticket.
     * 
     * @return
     * @throws CloneNotSupportedException
     */
    public Ticket copy() throws CloneNotSupportedException {
        return (Ticket) super.clone();
    }

    public boolean hasNextStatus() {
        return !this.hasNotNextStatus();
    }

    public boolean hasNotNextStatus() {
        return this.getNextAllowedStatus().isEmpty();
    }

    /**
     * Retourne la liste des nouveaux status que peut prendre le ticket.
     * 
     * @return
     */
    public List<TicketStatus> getNextAllowedStatus() {
        return TicketStatusChangeRule.getAllowedNewStatusForTicketStatus(this.getOperation(), this.getStatus());
    }

}
