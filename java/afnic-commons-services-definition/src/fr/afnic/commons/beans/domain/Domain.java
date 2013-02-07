/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/beans/domain/Domain.java#30 $
 * $Revision: #30 $
 * $Author: ginguene $
 */

package fr.afnic.commons.beans.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.afnic.commons.beans.DSServer;
import fr.afnic.commons.beans.DnsServer;
import fr.afnic.commons.beans.Ticket;
import fr.afnic.commons.beans.WhoisContact;
import fr.afnic.commons.beans.customer.Customer;
import fr.afnic.commons.beans.documents.GddDocument;
import fr.afnic.commons.beans.logs.ILogger;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.beans.request.AuthorizationRequest;
import fr.afnic.commons.beans.request.TradeRequest;
import fr.afnic.commons.beans.search.Pagination;
import fr.afnic.commons.beans.search.customer.CustomerSearchCriteria;
import fr.afnic.commons.services.exception.NotFoundException;
import fr.afnic.commons.services.exception.RequestFailedException;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.utils.DateUtils;

/***
 * Representation d'un domaine.
 * 
 * @author ginguene
 * 
 */
public class Domain implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;

    private static final ILogger LOGGER = AppServiceFacade.getLoggerService().getLogger(Domain.class);

    private DomainStatus status;
    private String registrarCode;
    private String registrarName;

    private DomainNameDetail nameDetail;

    private String name;
    private String extension;
    private String authInfo;
    private List<Ticket> tickets = null;

    private List<GddDocument> documents = null;

    private String holderHandle;
    private String holderName = null;
    private WhoisContact holder = null;

    private Date createDate;
    private Date lastUpdate;
    private Date deleteDate;
    private Date anniversaryDate;

    private List<DnsServer> dnsServers;

    private List<String> technicalContactHandles;
    private String adminContactHandle;

    private List<WhoisContact> technicalContacts;
    private WhoisContact adminContact;

    private List<AuthorizationRequest> authorizationRequests = null;
    private List<TradeRequest> tradeRequests = null;

    private List<DSServer> dsServer;

    private boolean isOnHold;

    /** le user appelant l'objet */
    protected final UserId userIdCaller;
    /** le tld sur lequel on appelle l'objet */
    protected final TldServiceFacade tldCaller;

    public Domain(UserId userId, TldServiceFacade tld) {
        this.userIdCaller = userId;
        this.tldCaller = tld;
    }

    public static Domain createFreeDomain(String domainName, UserId userId, TldServiceFacade tld) throws ServiceException {
        Domain domain = new Domain(userId, tld);
        domain.setName(domainName);
        domain.setStatus(DomainStatus.Free);

        DomainNameDetail detail = AppServiceFacade.getQualityService().normalizeDomainName(domainName, userId, tld);
        domain.setNameDetail(detail);
        return domain;
    }

    public Customer getRegistry() throws ServiceException {
        return AppServiceFacade.getCustomerService().getRegistry(this.userIdCaller, this.tldCaller);
    }

    /**
     * Récupère la liste de tous les tickets liés au domaines.
     * 
     * @return
     * @throws ServiceException
     */
    public List<Ticket> getTickets() throws ServiceException {
        if (this.tickets == null) {
            this.tickets = AppServiceFacade.getTicketService()
                                           .getTicketsWithDomain(this.name, this.userIdCaller, this.tldCaller);

        }
        return this.tickets;
    }

    /**
     * Retourne tous les documents 'en cours' de la GED liés au domaine.
     * 
     * @return
     * @throws ServiceException
     */
    public List<GddDocument> getDocument() throws ServiceException {
        if (this.documents == null) {
            this.documents = AppServiceFacade.getOldDocumentService()
                                             .getAttachedDocumentsToDomain(this.name, this.userIdCaller, this.tldCaller);

        }
        return this.documents;
    }

    /**
     * Initialise le name et l'extension du domaine a partir d'une chaine formatte <name>.<extension>
     * 
     * @param fullName
     */
    public void setFullName(String fullName) {
        if (fullName == null) throw new IllegalArgumentException("Argument full name is null ");

        Pattern pattern = Pattern.compile("^(.*)\\.(.*?)$");
        Matcher matcher = pattern.matcher(fullName);

        // si recherche fructueuse
        if (matcher.matches()) {
            if (matcher.groupCount() != 3) {
                this.name = matcher.group(1);
                this.extension = matcher.group(2);
            } else {
                throw new IllegalArgumentException("Argument full name format should be <name>.<extension> [" + fullName
                                                   + "]");
            }
        } else {
            throw new IllegalArgumentException("Argument full name format should be <name>.<extension> [" + fullName + "]");
        }
    }

    /**
     * Recupère un objet contact à partir du holderNichandle
     * 
     * @return Contact
     * @throws ServiceException
     */
    public WhoisContact getHolder() throws ServiceException {
        System.err.println("==>" + this.holderHandle);
        if (this.holder == null) {
            this.holder = AppServiceFacade.getWhoisContactService().getContactWithHandle(this.holderHandle, this.userIdCaller, this.tldCaller);
        }
        return this.holder;
    }

    public DomainStatus getStatus() {
        return this.status;
    }

    public String getStatusAsString() {
        if (this.status != null) {
            return this.status.toString();
        } else {
            return "";
        }
    }

    public void setStatus(DomainStatus status) {
        this.status = status;
    }

    public String getName() {
        return this.name;
    }

    public String getExtension() {
        return this.extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHolderHandle(String holderHandle) {
        this.holderHandle = holderHandle;
    }

    public String getHolderHandle() {
        return this.holderHandle;
    }

    public Date getLastUpdate() {
        return DateUtils.clone(this.lastUpdate);
    }

    /**
     * retourne la date de dernière mise à jour en format texte.
     * 
     * @return La date de dernière mise à jour en format texte.
     */
    public String getLastUpdateStr() {
        return DateUtils.toDayFormat(this.lastUpdate);
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = DateUtils.clone(lastUpdate);
    }

    public Date getAnniversaryDate() {
        return DateUtils.clone(this.anniversaryDate);
    }

    /**
     * retourne la date anniversaire du domaine en format texte.
     * 
     * @return La date de dernière mise à jour en format texte.
     */
    public String getAnniversaryDateStr() {
        return DateUtils.toDayFormat(this.anniversaryDate);
    }

    public void setAnniversaryDate(Date anniversaryDate) {
        this.anniversaryDate = DateUtils.clone(anniversaryDate);
    }

    public Date getDeleteDate() {
        return DateUtils.clone(this.deleteDate);
    }

    /**
     * retourne une chaine de caractère correspondant à la date de suppression. <br/>
     * On peut definir le format de cette date via la methode setDateFormat.
     * 
     * @return La date de suppresion en format texte
     */
    public String getDeleteDateStr() {
        return DateUtils.toDayFormat(this.deleteDate);
    }

    public void setDeleteDate(Date deleteDate) {
        this.deleteDate = DateUtils.clone(deleteDate);
    }

    public String getAuthInfo() {
        return this.authInfo;
    }

    public void setAuthInfo(String authInfo) {
        this.authInfo = authInfo;
    }

    public String getRegistrarCode() {
        return this.registrarCode;
    }

    public void setRegistrarCode(String registrarCode) {
        this.registrarCode = registrarCode;
    }

    public String getRegistrarName() {
        return this.registrarName;
    }

    public void setRegistrarName(String registrarName) {
        this.registrarName = registrarName;
    }

    public Date getCreateDate() throws ServiceException {
        if (this.hasNoCreateDate()
            && this.hasTicket()) {
            this.createDate = this.getFirstTicketCreateDate();
        }

        if (this.createDate != null) {
            return (Date) this.createDate.clone();
        } else {
            return null;
        }
    }

    /**
     * Indique si l'on dispose d'une date de création pour le domaine. Ce n'est pas le cas par exemple si le domaine n'est pas attribué.
     * 
     * @return
     */
    private boolean hasNoCreateDate() {
        return this.createDate == null;
    }

    /**
     * Indique si il existe des tickets liés au domaine
     * 
     * @return
     * @throws ServiceException
     */
    private boolean hasTicket() throws ServiceException {
        return this.getTickets() != null
               && this.getTickets().size() > 0;
    }

    /**
     * Retourne la date de création du ticket le plus vieux lié au domaine.
     * 
     * @return
     * @throws ServiceException
     */
    private Date getFirstTicketCreateDate() throws ServiceException {
        return this.getTickets().get(0).getCreateDate();
    }

    /**
     * retourne une chaine de caractère correspondant à la date de creation.
     * 
     * @return
     * @throws ServiceException
     */
    public String getCreateDateStr() throws ServiceException {
        return DateUtils.toDayFormat(this.getCreateDate());
    }

    public void setCreateDate(Date createDate) {
        this.createDate = DateUtils.clone(createDate);
    }

    /**
     * Retourne la liste des serveurs dns du domaine.
     * 
     * @return
     * @throws ServiceException
     */
    public List<DnsServer> getDnsServers() throws ServiceException {
        return this.dnsServers = AppServiceFacade.getDomainService()
                                                 .getDnsServersWithDomain(this.name, this.userIdCaller, this.tldCaller);
    }

    public void setDnsServers(List<DnsServer> servers) {
        this.dnsServers = servers;
    }

    /**
     * Ajoute un serveur dns au domaine.
     * 
     * @param server
     * 
     */
    public void addDnsServer(DnsServer server) {
        if (this.dnsServers == null) {
            this.dnsServers = new ArrayList<DnsServer>();
        }
        this.dnsServers.add(server);
    }

    /**
     * Ajoute un ticket au domaine.
     * 
     * @param ticket
     */
    public void addTicket(Ticket ticket) {
        if (this.tickets == null) {
            this.tickets = new ArrayList<Ticket>();
        }
        this.tickets.add(ticket);
    }

    public List<String> getTechnicalContactHandle() {
        return this.technicalContactHandles;
    }

    public void setTechnicalContactHandles(List<String> technicalContactHandles) {
        this.technicalContactHandles = technicalContactHandles;
    }

    /**
     * Ajoute un contact technique au domaine.
     * 
     * @param technicalContactHandle
     */
    public void addTechnicalContactHandle(String technicalContactHandle) {
        if (this.technicalContactHandles == null) {
            this.technicalContactHandles = new ArrayList<String>();
        }
        this.technicalContactHandles.add(technicalContactHandle);
    }

    public String getAdminContactHandle() {
        return this.adminContactHandle;
    }

    public void setAdminContactHandle(String adminContactHandle) {
        this.adminContactHandle = adminContactHandle;
    }

    /**
     * Retourne la liste d'objet correspondant aux handle des contact techniques du domaine
     * 
     * @return
     * @throws ServiceException
     */
    @SuppressWarnings("rawtypes")
    public List<WhoisContact> getTechnicalContacts() throws ServiceException {
        if (this.technicalContactHandles == null) {
            this.technicalContactHandles = AppServiceFacade.getWhoisContactService().getDomainContactHandles(this.name, DomainContactType.TECH, this.userIdCaller, this.tldCaller);
        }

        if (this.technicalContacts == null) {
            this.technicalContacts = new ArrayList<WhoisContact>();

            try {

                for (String technicalContactHanlde : this.technicalContactHandles) {
                    WhoisContact technicalContact = AppServiceFacade.getWhoisContactService()
                                                                    .getContactWithHandle(technicalContactHanlde, this.userIdCaller, this.tldCaller);
                    if (technicalContact != null) {
                        this.technicalContacts.add(technicalContact);
                    } else {
                        Domain.LOGGER.error("No contact found with handle " + technicalContactHanlde);
                    }
                }
            } catch (RequestFailedException e) {
                Domain.LOGGER.error(e.getMessage(), e);
            }
        }

        return this.technicalContacts;
    }

    /**
     * Retourne l'objet correspondant au handle du contact admin du domaine
     * 
     * @return
     * @throws ServiceException
     */
    public WhoisContact getAdminContact() throws ServiceException {
        if (this.adminContact == null) {
            if (this.adminContactHandle == null) {
                this.adminContactHandle = AppServiceFacade.getWhoisContactService().getDomainContactHandles(this.name, DomainContactType.ADMIN, this.userIdCaller, this.tldCaller).get(0);
            }

            this.adminContact = AppServiceFacade.getWhoisContactService().getContactWithHandle(this.adminContactHandle, this.userIdCaller, this.tldCaller);
        }
        return this.adminContact;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "Domain: name[" + this.name + "]";
    }

    /**
     * Retourne le nom du titulaire du domaine.
     * 
     * @return
     * @throws ServiceException
     */
    public String getHolderName() throws ServiceException {
        if (this.holderName == null && this.holder != null) {
            this.holderName = this.getHolder().getName();
        }
        return this.holderName;
    }

    public void setHolderName(String holderName) {
        this.holderName = holderName;
    }

    public List<AuthorizationRequest> getAuthorizationRequests() throws ServiceException {
        if (this.authorizationRequests == null) {
            this.authorizationRequests = AppServiceFacade.getAuthorizationRequestService()
                                                         .getAuthorizationRequestsWithDomain(this.getName(), this.userIdCaller, this.tldCaller);
        }
        return this.authorizationRequests;
    }

    public List<TradeRequest> getTradeRequests() throws ServiceException {
        if (this.tradeRequests == null) {
            this.tradeRequests = AppServiceFacade.getTradeService()
                                                 .getTradeRequestsWithDomain(this.getName(), this.userIdCaller, this.tldCaller);
        }
        return this.tradeRequests;
    }

    public boolean hasDnsServers() {
        return this.dnsServers != null
               && this.dnsServers.size() > 0;
    }

    public boolean hasAuthorizationRequests() throws ServiceException {
        return this.getAuthorizationRequests() != null
               && this.getAuthorizationRequests().size() > 0;
    }

    public boolean hasTradeRequests() throws ServiceException {
        return this.getTradeRequests() != null
               && this.getTradeRequests().size() > 0;
    }

    public boolean hasTickets() throws ServiceException {
        return this.getTickets() != null
               && this.getTickets().size() > 0;
    }

    public boolean hasDocuments() throws ServiceException {
        return this.getDocument() != null
               && this.getDocument().size() > 0;
    }

    public boolean isOnHold() {
        return this.isOnHold;
    }

    public void setOnHold(boolean isOnHold) {
        this.isOnHold = isOnHold;
    }

    /**
     * Si une opération est en cours sur un domaine, retourne une description de cette opération, sinon retourne ""
     * 
     * @return une description de l'operation en cours pour le domaine
     * @throws ServiceException
     *             Si l'opération échoue
     */
    public String getCurrentOperationDescription() throws ServiceException {
        try {
            Ticket ticket = AppServiceFacade.getTicketService().getPendingTicketWithDomain(this.name, this.userIdCaller, this.tldCaller);
            return ticket.getOperation().getDescription(this.userIdCaller, this.tldCaller);
        } catch (NotFoundException e) {
            return "";
        }
    }

    public boolean hasHolder() {
        return this.holderHandle != null;
    }

    public boolean hasTechnicalsContact() {
        return this.getTechnicalContactHandle() != null && this.getTechnicalContactHandle().size() > 0;
    }

    public Customer getCustomer() throws ServiceException {
        CustomerSearchCriteria criteria = new CustomerSearchCriteria();
        criteria.setCode(this.registrarCode);

        List<Customer> customerList = AppServiceFacade.getCustomerService()
                                                      .searchCustomer(criteria, Pagination.ONE_ELEMENT_PAGINATION,
                                                                      this.userIdCaller,
                                                                      this.tldCaller)
                                                      .getPageResults();

        if ((customerList != null) && (customerList.size() > 0)) {
            return customerList.get(0);
        } else {
            return null;
        }
    }

    public DomainNameDetail getNameDetail() {
        return this.nameDetail;
    }

    public void setNameDetail(DomainNameDetail detail) {
        this.nameDetail = detail;
    }

    public List<DSServer> getDsServer() throws ServiceException {
        if (this.dsServer == null) {
            this.dsServer = AppServiceFacade.getDomainService()
                                            .getDSServerWithDomain(this.name, this.userIdCaller, this.tldCaller);
        }
        return this.dsServer;
    }

    public void setDsServer(List<DSServer> dsServer) {
        this.dsServer = dsServer;
    }

    public void addDsServer(DSServer dsServer) {
        if (this.dsServer == null) {
            this.dsServer = new ArrayList<DSServer>();
        }
        this.dsServer.add(dsServer);
    }

}
