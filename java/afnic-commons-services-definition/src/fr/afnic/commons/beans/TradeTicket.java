/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/beans/TradeTicket.java#14 $
 * $Revision: #14 $
 * $Author: ginguene $
 */

package fr.afnic.commons.beans;

import fr.afnic.commons.beans.customer.Customer;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;

/**
 * Ticket dont les informations concernent une operation de Trade.
 * 
 * @author ginguene
 * 
 */
public class TradeTicket extends Ticket {

    private static final long serialVersionUID = 1L;

    private String outgoingRegistrarCode;
    private String outgoingHolderHandle;
    private String incomingRegistrarCode;
    private String incomingHolderHandle;

    private Customer outgoingRegistrar;
    private WhoisContact outgoingHolder;
    private Customer incomingRegistrar;
    private WhoisContact incomingHolder;

    public TradeTicket(UserId userId, TldServiceFacade tld) {
        super(userId, tld);
    }

    public String getOutgoingRegistrarCode() {
        if (this.outgoingRegistrarCode == null && this.outgoingRegistrar != null) {
            this.outgoingRegistrarCode = this.outgoingRegistrar.getAccountLogin();
        }
        return this.outgoingRegistrarCode;
    }

    public void setOutgoingRegistrarCode(String outgoingRegistrarCode) {
        this.outgoingRegistrarCode = outgoingRegistrarCode;
    }

    public String getOutgoingHolderHandle() {
        return this.outgoingHolderHandle;
    }

    public void setOutgoingHolderHandle(String outgoingHolderHandle) {
        this.outgoingHolderHandle = outgoingHolderHandle;
    }

    public String getIncomingRegistrarCode() {
        if (this.incomingRegistrarCode == null && this.incomingRegistrar != null) {
            this.incomingRegistrarCode = this.incomingRegistrar.getAccountLogin();
        }
        return this.incomingRegistrarCode;
    }

    public void setIncomingRegistrarCode(String incomingRegistrarCode) {
        this.incomingRegistrarCode = incomingRegistrarCode;
    }

    public String getIncomingHolderHandle() {
        return this.incomingHolderHandle;
    }

    public void setIncomingHolderHandle(String incomingHolderHandle) {
        this.incomingHolderHandle = incomingHolderHandle;
    }

    /**
     * Retourne le bureau d'enregistrement sortant.
     * 
     * @return
     * @throws ServiceException
     */
    public Customer getOutgoingRegistrar() throws ServiceException {

        if (this.outgoingRegistrarCode == null) {
            this.outgoingRegistrarCode = this.registrarCode;
        }

        if (this.outgoingRegistrar == null && this.outgoingRegistrarCode != null) {
            this.outgoingRegistrar = AppServiceFacade.getCustomerService().getCustomerWithCode(this.outgoingRegistrarCode, this.userIdCaller, this.tldCaller);
        }
        return this.outgoingRegistrar;
    }

    public void setOutgoingRegistrar(Customer outgoingRegistrar) {
        this.outgoingRegistrar = outgoingRegistrar;
    }

    /**
     * Retourne le titulaire sortant.
     * 
     * @return
     * @throws ServiceException
     */
    public WhoisContact getOutgoingHolder() throws ServiceException {
        if (this.outgoingHolder == null && this.outgoingHolderHandle != null) {
            this.outgoingHolder = AppServiceFacade.getWhoisContactService().getContactWithHandle(this.outgoingHolderHandle, this.userIdCaller, this.tldCaller);
        }

        return this.outgoingHolder;
    }

    public void setOutgoingHolder(WhoisContact outgoingHolder) {
        this.outgoingHolder = outgoingHolder;
    }

    /**
     * Retourne le bureau d'enregistrement entrant.
     * 
     * @return
     * @throws ServiceException
     */
    public Customer getIncomingRegistrar() throws ServiceException {
        if (this.incomingRegistrarCode == null) {
            this.incomingRegistrarCode = this.registrarCode;
        }

        if (this.incomingRegistrar == null && this.incomingRegistrarCode != null) {
            this.incomingRegistrar = AppServiceFacade.getCustomerService().getCustomerWithCode(this.incomingRegistrarCode, this.userIdCaller, this.tldCaller);
        }

        return this.incomingRegistrar;
    }

    public void setIncomingRegistrar(Customer incomingRegistrar) {
        this.incomingRegistrar = incomingRegistrar;
    }

    /**
     * Retourne le titulaire entrant.
     * 
     * @return
     * @throws ServiceException
     */
    public WhoisContact getIncomingHolder() throws ServiceException {
        if (this.incomingHolder == null && this.incomingHolderHandle != null) {
            this.incomingHolder = AppServiceFacade.getWhoisContactService().getContactWithHandle(this.incomingHolderHandle, this.userIdCaller, this.tldCaller);
        }
        return this.incomingHolder;
    }

    public void setIncomingHolder(WhoisContact incomingHolder) {
        this.incomingHolder = incomingHolder;
    }

    /**
     * Indique si il y a un bureau d'enregistrement sortant.<br/>
     * Dans le cas contraire cela signifie que lors du trade il n'y a pas de changement de bureau d'enregistrement.
     * 
     * @return
     * @throws ServiceException
     */
    public boolean hasOutgoingRegistrar() throws ServiceException {
        return this.outgoingRegistrarCode != null;
    }

    /**
     * Indique si il y a un bureau d'enregistrement entrant.<br/>
     * Dans le cas contraire cela signifie que lors du trade il n'y a pas de changement de bureau d'enregistrement.
     * 
     * @return
     * @throws ServiceException
     */
    public boolean hasIncomingRegistrar() throws ServiceException {
        return this.incomingRegistrarCode != null;
    }

    /**
     * Indique si il y a un titulaire sortant * @return
     * 
     * @throws ServiceException
     */
    public boolean hasOutgoingHolder() throws ServiceException {
        return this.outgoingHolderHandle != null;
    }

    /**
     * Indique si il y a un titulaire entrant.
     * 
     * @return
     * @throws ServiceException
     */
    public boolean hasIncomingHolder() throws ServiceException {
        return this.incomingHolderHandle != null;
    }

}
