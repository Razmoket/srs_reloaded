/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/beans/OperationForm.java#28 $
 * $Revision: #28 $
 * $Author: ginguene $
 */
package fr.afnic.commons.beans;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import fr.afnic.commons.beans.application.Version;
import fr.afnic.commons.beans.customer.Customer;
import fr.afnic.commons.beans.domain.Domain;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.beans.search.Pagination;
import fr.afnic.commons.beans.search.customer.CustomerSearchCriteria;
import fr.afnic.commons.beans.validatable.OperationFormId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.utils.DateUtils;
import fr.afnic.utils.ToStringHelper;

/**
 * Représente un formulaire d'opération de l'AFNIC. Un formulaire est un ensemble de données historiquement envoyées par email. Cet ensemble
 * permettant de créer une opération sur un domaine, à savoir un ticket.
 * 
 * Définition d'une priorité dans l'ordre de recherche: 1/ formId 2/ ticketId 3/ domainName 4/ holderHandle 5/ adminHandle 6/ registrarCode 7/
 * registrarName
 * 
 * @author alaphilippe
 */
public class OperationForm implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Identifiant du formulaire. <br>
     * <i>Ce champs est un paramètre de la recherche.</i>
     */
    protected OperationFormId operationFormId;

    protected boolean idIsSet = false;

    /**
     * Nom du domaine lié au formulaire. <br>
     * <i>Ce champs est un paramètre de la recherche.</i>
     */
    protected String domainName;
    protected boolean domainNameIsSet = false;

    /**
     * numéro du ticket <br>
     * <i>Ce champs est un paramètre de la recherche.</i>
     */
    protected String ticketId;
    protected boolean ticketIdIsSet = false;

    /**
     * nichandle du contact admin <br>
     * <i>Ce champs est un paramètre de la recherche.</i>
     */
    protected String adminHandle;
    protected boolean adminHandleIsSet = false;

    /**
     * nichandle du titulaire <br>
     * <i>Ce champs est un paramètre de la recherche.</i>
     */
    protected String holdeHandler;
    protected boolean holderHandlerIsSet = false;

    /**
     * Le formulaire est-il en cours de traitement <br>
     * <i>Ce champs est un paramètre de la recherche.</i>
     */
    protected boolean isPending;

    protected boolean isPendingIsSet = false;

    /**
     * Identifiant du client BE lié au formulaire. <br>
     * <i>Ce champs est un paramètre de la recherche.</i>
     */
    protected String registrarCode;
    protected boolean registrarCodeIsSet = false;

    /**
     * Nom du client BE lié au formulaire. <br>
     * <i>Ce champs est un paramètre de la recherche.</i>
     */
    protected String registrarName;
    protected boolean registrarNameIsSet = false;

    /** Date de création du formulaire. */
    protected Date createDate;
    /** Domaine lié au formulaire. */
    protected Domain domain = null;
    /** Référence vers le client BE. */
    protected Customer registrar = null;
    /** commentaire du le client BE. */
    protected String registrarComment;
    /** quand on le sait courriel ou Epp */
    protected String submitSource;
    /** Si le formulaire est fini ou non */
    protected boolean isArchived;
    /** différence entre trade (isforced=non) et recover (isforced= vrai) */
    protected boolean isForced;
    /** Nom de l'operation lié au formulaire */
    protected TicketOperation operation;
    /** version du formulaire */
    protected Version version;

    /** le user appelant l'objet */
    protected final UserId userIdCaller;
    /** le tld sur lequel on appelle l'objet */
    protected final TldServiceFacade tldCaller;

    public OperationForm(UserId userId, TldServiceFacade tld) {
        this.userIdCaller = userId;
        this.tldCaller = tld;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof OperationForm)) {
            return false;
        }
        OperationForm opToCompare = (OperationForm) obj;
        try {
            if (this.getOperationFormIdAsString().equals(opToCompare.getOperationFormIdAsString())
                && this.adminHandle.equals(opToCompare.getAdminHandle())
                && this.domainName.equals(opToCompare.getDomainName())
                && this.holdeHandler.equals(opToCompare.getHolderHandle())
                && this.registrarCode.equals(opToCompare.getRegistrarCode())
                && this.registrarName.equals(opToCompare.getRegistrarName())) {
                return true;
            }

        } catch (ServiceException e) {
            throw new RuntimeException("equals() failed", e);
        }
        return false;
    }

    @Override
    public int hashCode() {
        if (this.getOperationFormIdAsString() != null) {
            return this.getOperationFormIdAsString().hashCode();
        } else {
            return 31;
        }
    }

    @Override
    public String toString() {
        return new ToStringHelper().addAllObjectAttributes(this).toString();
    }

    public String getOperationFormIdAsString() {
        if (this.operationFormId != null) {
            return this.operationFormId.getValue();
        } else {
            return null;
        }
    }

    public OperationFormId getOperationFormId() {
        return this.operationFormId;
    }

    /**
     * Archive le formulaire
     * 
     * @throws ServiceException
     */
    public void archive() throws ServiceException {
        AppServiceFacade.getOperationFormService().archiveOperationForm(this.operationFormId, this.userIdCaller, this.tldCaller);
    }

    public void setOperationFormId(OperationFormId operationFormId) {
        this.operationFormId = operationFormId;
    }

    /**
     * @return the createDate
     */
    public Date getCreateDate() {
        return DateUtils.clone(this.createDate);
    }

    /**
     * @param createDate
     *            the createDate to set
     */
    public void setCreateDate(Date createDate) {
        this.createDate = DateUtils.clone(createDate);
    }

    /**
     * @return the domain
     * @throws DaoException
     */
    public Domain getDomain() throws ServiceException {
        if (this.domain == null && this.domainName != null) {
            this.domain = AppServiceFacade.getDomainService().getDomainWithName(this.domainName, this.userIdCaller, this.tldCaller);
        }
        return this.domain;
    }

    /**
     * @param domain
     *            the domain to set
     */
    public void setDomain(Domain domain) {
        this.domain = domain;
    }

    /**
     * @return the registrar
     * @throws ServiceException
     */
    public Customer getRegistrar() throws ServiceException {
        if (this.registrar == null) {
            if (this.registrarCode != null) {
                this.registrar = AppServiceFacade.getCustomerService().getCustomerWithCode(this.registrarCode,
                                                                                           this.userIdCaller,
                                                                                           this.tldCaller);
            } else if (this.registrarName != null) {
                //TODO voir si cette partie est vraiment utile
                CustomerSearchCriteria criteria = new CustomerSearchCriteria();
                criteria.setName(this.registrarName);
                this.registrar = AppServiceFacade.getCustomerService().searchCustomer(criteria, Pagination.ONE_ELEMENT_PAGINATION,
                                                                                      this.userIdCaller,
                                                                                      this.tldCaller).getPageResults().get(0);
            }
        }
        return this.registrar;
    }

    /**
     * @param registrar
     *            the registrar to set
     */
    public void setRegistrar(Customer registrar) {
        this.registrar = registrar;
    }

    /**
     * @return the registrarComment
     */
    public String getRegistrarComment() {
        return this.registrarComment;
    }

    /**
     * @param registrarComment
     *            the registrarComment to set
     */
    public void setRegistrarComment(String registrarComment) {
        this.registrarComment = registrarComment;
    }

    /**
     * @return the submitSource
     */
    public String getSubmitSource() {
        return this.submitSource;
    }

    /**
     * @param submitSource
     *            the submitSource to set
     */
    public void setSubmitSource(String submitSource) {
        this.submitSource = submitSource;
    }

    /**
     * @return the isArchived
     */
    public boolean isArchived() {
        return this.isArchived;
    }

    /**
     * @param isArchived
     *            the isArchived to set
     */
    public void setIsArchived(boolean isArchived) {
        this.isArchived = isArchived;
    }

    /**
     * @return the isForced
     */
    public boolean isForced() {
        return this.isForced;
    }

    /**
     * @param isForced
     *            the isForced to set
     */
    public void setIsForced(boolean isForced) {
        this.isForced = isForced;
    }

    /**
     * @return the operation
     */
    public TicketOperation getOperation() {
        return this.operation;
    }

    /**
     * @param operationName
     *            the operationName to set
     */
    public void setOperation(TicketOperation operationName) {
        this.operation = operationName;
    }

    /**
     * @return the version
     */
    public Version getVersion() {
        return this.version;
    }

    /**
     * @param version
     *            the version to set
     */
    public void setVersion(Version version) {
        this.version = version;
    }

    /**
     * @return the domainName
     */
    public String getDomainName() {
        return this.domainName;
    }

    /**
     * @param domainName
     *            the domainName to set
     */
    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    /**
     * @return the registrarCode
     */
    public String getRegistrarCode() {
        return this.registrarCode;
    }

    /**
     * @param registrarCode
     *            the registrarCode to set
     */
    public void setRegistrarCode(String registrarCode) {
        this.registrarCode = registrarCode;
    }

    /**
     * @return the registrarName
     */
    public String getRegistrarName() {
        return this.registrarName;
    }

    /**
     * @param registrarName
     *            the registrarName to set
     */
    public void setRegistrarName(String registrarName) {
        this.registrarName = registrarName;
    }

    /**
     * Indique si une version est associée au formulaire.
     * 
     * @return true si une version est associée au formulaire.
     */
    public boolean hasVersion() {
        return this.version != null;
    }

    /**
     * @return the adminHandle
     * @throws ServiceException
     */
    public String getAdminHandle() throws ServiceException {
        if (this.adminHandle == null) {
            this.adminHandle = this.getDomain().getAdminContactHandle();
        }
        return this.adminHandle;
    }

    /**
     * @param adminHandle
     *            the adminHandle to set
     */
    public void setAdminHandle(String adminHandle) {
        this.adminHandle = adminHandle;
    }

    /**
     * @return the holderHandler
     * @throws ServiceException
     */
    public String getHolderHandle() throws ServiceException {
        if (this.holdeHandler == null && this.getDomain() != null) {
            this.holdeHandler = this.getDomain().getHolderHandle();
        }
        return this.holdeHandler;
    }

    public void setHolderHandle(String holderHandle) {
        this.holdeHandler = holderHandle;
    }

    /**
     * @return the ticketId
     * @throws ServiceException
     */
    public String getTicketId() throws ServiceException {
        if (this.ticketId == null) {
            if (this.domainName != null) {
                Ticket pendingTicketWithDomain = null;
                try {
                    pendingTicketWithDomain = AppServiceFacade.getTicketService().getPendingTicketWithDomain(this.domainName, this.userIdCaller, this.tldCaller);
                } catch (ServiceException e) {
                    // on mange l'exception...
                    pendingTicketWithDomain = null;
                }
                if (pendingTicketWithDomain != null) {
                    this.ticketId = pendingTicketWithDomain.getId();
                } else {
                    List<Ticket> ticketsWithDomain = AppServiceFacade.getTicketService().getTicketsWithDomain(this.domainName, this.userIdCaller, this.tldCaller);
                    if (ticketsWithDomain != null && !ticketsWithDomain.isEmpty()) {
                        for (Ticket tick : ticketsWithDomain) {
                            if (this.getOperationFormId().equals(tick.getOperationFormId())) {
                                this.ticketId = tick.getId();
                                break;
                            }
                        }

                    }
                }
            }
        }
        return this.ticketId;
    }

    /**
     * @param ticketId
     *            the ticketId to set
     */
    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    /**
     * @param isArchived
     *            the isArchived to set
     */
    public void setArchived(boolean isArchived) {
        this.isArchived = isArchived;
    }

    /**
     * @param isForced
     *            the isForced to set
     */
    public void setForced(boolean isForced) {
        this.isForced = isForced;
    }

    /**
     * @return the idIsSet
     */
    public boolean isIdIsSet() {
        return this.idIsSet;
    }

    /**
     * @param idIsSet
     *            the idIsSet to set
     */
    public void setIdIsSet(boolean idIsSet) {
        this.idIsSet = idIsSet;
    }

    /**
     * @return the domainNameIsSet
     */
    public boolean isDomainNameIsSet() {
        return this.domainNameIsSet;
    }

    /**
     * @param domainNameIsSet
     *            the domainNameIsSet to set
     */
    public void setDomainNameIsSet(boolean domainNameIsSet) {
        this.domainNameIsSet = domainNameIsSet;
    }

    /**
     * @return the ticketIdIsSet
     */
    public boolean isTicketIdIsSet() {
        return this.ticketIdIsSet;
    }

    /**
     * @param ticketIdIsSet
     *            the ticketIdIsSet to set
     */
    public void setTicketIdIsSet(boolean ticketIdIsSet) {
        this.ticketIdIsSet = ticketIdIsSet;
    }

    /**
     * @return the adminHandleIsSet
     */
    public boolean isAdminHandleIsSet() {
        return this.adminHandleIsSet;
    }

    /**
     * @param adminHandleIsSet
     *            the adminHandleIsSet to set
     */
    public void setAdminHandleIsSet(boolean adminHandleIsSet) {
        this.adminHandleIsSet = adminHandleIsSet;
    }

    /**
     * @return the holderHandlerIsSet
     */
    public boolean isHolderHandlerIsSet() {
        return this.holderHandlerIsSet;
    }

    /**
     * @param holderHandlerIsSet
     *            the holderHandlerIsSet to set
     */
    public void setHolderHandlerIsSet(boolean holderHandlerIsSet) {
        this.holderHandlerIsSet = holderHandlerIsSet;
    }

    /**
     * @return the isPending
     */
    public boolean isPending() {
        return this.isPending;
    }

    /**
     * @param isPending
     *            the isPending to set
     */
    public void setPending(boolean isPending) {
        this.isPending = isPending;
    }

    /**
     * @return the isPendingIsSet
     */
    public boolean isPendingIsSet() {
        return this.isPendingIsSet;
    }

    /**
     * @param isPendingIsSet
     *            the isPendingIsSet to set
     */
    public void setPendingIsSet(boolean isPendingIsSet) {
        this.isPendingIsSet = isPendingIsSet;
    }

    /**
     * @return the registrarCodeIsSet
     */
    public boolean isRegistrarCodeIsSet() {
        return this.registrarCodeIsSet;
    }

    /**
     * @param registrarCodeIsSet
     *            the registrarCodeIsSet to set
     */
    public void setRegistrarCodeIsSet(boolean registrarCodeIsSet) {
        this.registrarCodeIsSet = registrarCodeIsSet;
    }

    /**
     * @return the registrarNameIsSet
     */
    public boolean isRegistrarNameIsSet() {
        return this.registrarNameIsSet;
    }

    /**
     * @param registrarNameIsSet
     *            the registrarNameIsSet to set
     */
    public void setRegistrarNameIsSet(boolean registrarNameIsSet) {
        this.registrarNameIsSet = registrarNameIsSet;
    }

}
