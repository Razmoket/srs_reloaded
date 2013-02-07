/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/beans/WhoisContact.java#38 $
 * $Revision: #38 $
 * $Author: ginguene $
 */

package fr.afnic.commons.beans;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fr.afnic.commons.beans.contact.GenericContact;
import fr.afnic.commons.beans.contact.identity.ContactIdentity;
import fr.afnic.commons.beans.customer.Customer;
import fr.afnic.commons.beans.domain.Domain;
import fr.afnic.commons.beans.domain.DomainStatus;
import fr.afnic.commons.beans.operations.qualification.PublicQualificationItemStatus;
import fr.afnic.commons.beans.operations.qualification.PublicReachMedia;
import fr.afnic.commons.beans.operations.qualification.Qualification;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.beans.search.Pagination;
import fr.afnic.commons.beans.search.customer.CustomerSearchCriteria;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.utils.DateUtils;

public abstract class WhoisContact extends GenericContact implements Cloneable {

    private static final long serialVersionUID = 1L;

    private static final transient SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");

    private String handle;
    private ContactType type;

    private QualificationUpdateSource reachSource;
    private QualificationUpdateSource eligibilitySource;

    private Date reachDate;
    private Date eligibilityDate;

    private ContactIdentificationStatus identificationStatus;
    private Date identificationDate;
    private Date identificationExpirationDate;
    private Date reachabilityQualificationDate;

    private Date lastIdentification;

    private String registrarCode;

    private boolean isReferenced;
    private boolean isReferencedAsTechnicalContact;
    private boolean isReferencedAsBillingContact;
    private boolean isReferencedAsAdminContact;

    private boolean isObsolete;

    private List<String> domainsPortfolio = null;

    private PublicQualificationItemStatus eligibilityStatus;
    private PublicQualificationItemStatus reachStatus;

    private PublicReachMedia reachMedia;

    private Boolean disclosureRestriction;

    /** Date de création du contact dans la base de données */
    private Date createDate = null;

    protected UserId userIdCaller;
    protected TldServiceFacade tldCaller;

    protected WhoisContact(ContactIdentity identity, UserId userIdCaller, TldServiceFacade tldCaller) {
        super(identity);
        this.userIdCaller = userIdCaller;
        this.tldCaller = tldCaller;
    }

    public ContactType getType() {
        return this.type;
    }

    public void setType(ContactType type) {
        this.type = type;
    }

    public String getHandle() {
        return this.handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public Date getIdentificationDate() {
        return DateUtils.clone(this.identificationDate);
    }

    public void setIdentificationDate(Date identificationDate) {
        this.identificationDate = DateUtils.clone(identificationDate);
    }

    public Date getIdentificationExpirationDate() {
        return DateUtils.clone(this.identificationExpirationDate);
    }

    public void setIdentificationExpirationDate(Date identificationExpirationDate) {
        this.identificationExpirationDate = DateUtils.clone(identificationExpirationDate);
    }

    public Date getReachabilityQualificationDate() {
        return DateUtils.clone(this.reachabilityQualificationDate);
    }

    public void setReachabilityQualificationDate(Date reachabilityQualificationDate) {
        this.reachabilityQualificationDate = DateUtils.clone(reachabilityQualificationDate);
    }

    public ContactIdentificationStatus getIdentificationStatus() {
        return this.identificationStatus;
    }

    public void setIdentificationStatus(ContactIdentificationStatus identificationStatus) {
        this.identificationStatus = identificationStatus;
    }

    /**
     * Retourne un clone de la date de dernière identification.
     * 
     * @return Un clone de la date de dernière identification
     */
    public Date getLastIdentification() {
        return DateUtils.clone(this.lastIdentification);
    }

    /**
     * Retourne la date de dernière identification au format texte
     * 
     * @return date de dernière identification au format texte
     */
    public String getLastIdentificationStr() {
        if (this.lastIdentification == null) {
            return "";
        } else {
            return WhoisContact.DATE_FORMAT.format(this.lastIdentification);
        }
    }

    /**
     * Initialise la date de dernière identification avec un clone du parametre.
     * 
     * @param lastIdentification
     *            Date à cloner pour initialiser la date de dernière identification du ocontact
     */
    public void setLastIdentification(Date lastIdentification) {
        this.lastIdentification = DateUtils.clone(lastIdentification);
    }

    public String getRegistrarCode() {
        return this.registrarCode;
    }

    public void setRegistrarCode(String registrarCode) {
        this.registrarCode = registrarCode;
    }

    /**
     * A revoir lorsque les objets registrar seront supprimés.
     * @return
     * @throws ServiceException 
     */
    public Customer getCustomer() throws ServiceException {
        if (this.registrarCode == null) {
            return null;
        }
        CustomerSearchCriteria criteria = new CustomerSearchCriteria();
        criteria.setCode(this.registrarCode);

        return AppServiceFacade.getCustomerService()
                               .searchCustomer(criteria, Pagination.ONE_ELEMENT_PAGINATION,
                                               this.userIdCaller,
                                               this.tldCaller)
                               .getPageResults()
                               .get(0);
    }

    public boolean isReferenced() {
        return this.isReferenced;
    }

    public void setReferenced(boolean isReferenced) {
        this.isReferenced = isReferenced;
    }

    public boolean isReferencedAsTechnicalContact() {
        return this.isReferencedAsTechnicalContact;
    }

    public void setReferencedAsTechnicalContact(boolean isReferencedAsTechnicalContact) {
        this.isReferencedAsTechnicalContact = isReferencedAsTechnicalContact;
    }

    public boolean isReferencedAsHolderContact() {
        try {
            return !this.getDomainsPortfolio().isEmpty();
        } catch (Exception e) {
            throw new RuntimeException("isReferencedAsHolderContact() failed", e);
        }
    }

    public boolean isReferencedAsBillingContact() {
        return this.isReferencedAsBillingContact;
    }

    public void setReferencedAsBillingContact(boolean isReferencedAsBillingContact) {
        this.isReferencedAsBillingContact = isReferencedAsBillingContact;
    }

    public boolean isReferencedAsAdminContact() {
        return this.isReferencedAsAdminContact;
    }

    public void setReferencedAsAdminContact(boolean isReferencedAsAdminContact) {
        this.isReferencedAsAdminContact = isReferencedAsAdminContact;
    }

    public boolean isObsolete() {
        return this.isObsolete;
    }

    public void setObsolete(boolean isObsolete) {
        this.isObsolete = isObsolete;
    }

    /**
     * Indique si le contact a déja été identifié.<br>
     * pour cela on vérifie si il dispose d'une date de dernière identifiation.
     * 
     * 
     * @return true si le contact a déja été identifié.
     */
    public boolean hasAlreadyBeenIdentified() {
        return this.lastIdentification != null;
    }

    /**
     * Retourne le portefeuille du contact.
     * 
     * @return une liste contenant les nom des domaines du portefeuille du contact.
     * 
     * @throws ServiceException
     *             Si l'accès aux services échoue
     */
    public List<String> getDomainsPortfolio() throws ServiceException {
        if (this.domainsPortfolio == null) {
            this.domainsPortfolio = AppServiceFacade.getDomainService().getDomainNamesWithHolderHandle(this.handle, this.userIdCaller, this.tldCaller);
        }
        return this.domainsPortfolio;
    }

    /**
     * Retourne un clone de la date de création
     * 
     * @return Un clone de la date de création
     */
    @Override
    public Date getCreateDate() {
        return DateUtils.clone(this.createDate);
    }

    /**
     * Initialise la date de création du contact avec un clone du parametre.
     * 
     * @param createDate
     *            Nouvelle date de création du contact
     */
    @Override
    public void setCreateDate(Date createDate) {
        this.createDate = DateUtils.clone(createDate);

    }

    /**
     * Indique si la date de création est initialisée pour le contact.
     * 
     * @return true si la date de création est initialisée pour le contact.
     */
    @Override
    public boolean hasCreateDate() {
        return this.createDate != null;
    }

    /**
     * Indique si le statut d'identification est initialisée pour le contact.
     * 
     * @return true si le statut d'identification est initialisée pour le contact.
     */
    public boolean hasIdentificationStatus() {
        return this.identificationStatus != null;
    }

    public boolean hasEligibilityStatus() {
        return this.eligibilityStatus != null;
    }

    public PublicQualificationItemStatus getEligibilityStatus() {
        return this.eligibilityStatus;
    }

    public void setEligibilityStatus(PublicQualificationItemStatus eligibilityStatus) {
        this.eligibilityStatus = eligibilityStatus;
    }

    public boolean hasReachStatus() {
        return this.reachStatus != null;
    }

    public PublicQualificationItemStatus getReachStatus() {
        return this.reachStatus;
    }

    public void setReachStatus(PublicQualificationItemStatus reachStatus) {
        this.reachStatus = reachStatus;
    }

    public boolean hasReachMedia() {
        return this.reachMedia != null;
    }

    public PublicReachMedia getReachMedia() {
        return this.reachMedia;
    }

    public void setReachMedia(PublicReachMedia reachMedia) {
        this.reachMedia = reachMedia;
    }

    /**
     * Retourne une description des roles (en français uniquement) qu'assume un contact.
     * 
     * TODO Modelisation à revoir.<br/>
     * Il faudrait associer un role à un contact et un portefeuille à ce role.<br/>
     * On pourraits savoir ainsi quels sont les domaines pour lesquels un contact est admin ou commercial.
     * 
     * 
     * 
     * @param contact
     * @return
     * @throws ServiceException
     */
    public String getRolesDescription() {

        ArrayList<String> roles = new ArrayList<String>();

        if (this.isReferencedAsHolderContact()) {
            roles.add("titulaire");
        }

        if (this.isReferencedAsAdminContact()) {
            roles.add("contact admin");
        }

        if (this.isReferencedAsBillingContact()) {
            roles.add("contact commerciale");
        }

        if (this.isReferencedAsTechnicalContact()) {
            roles.add("contact technique");
        }

        if (roles.isEmpty()) {
            roles.add("Aucun");
        }

        return roles.toString();
    }

    public QualificationUpdateSource getReachSource() {
        return this.reachSource;
    }

    public void setReachSource(QualificationUpdateSource reachSource) {
        this.reachSource = reachSource;
    }

    public QualificationUpdateSource getEligibilitySource() {
        return this.eligibilitySource;
    }

    public void setEligibilitySource(QualificationUpdateSource eligibilitySource) {
        this.eligibilitySource = eligibilitySource;
    }

    public Date getReachDate() {
        return DateUtils.clone(this.reachDate);
    }

    public void setReachDate(Date reachDate) {
        this.reachDate = DateUtils.clone(reachDate);
    }

    public Date getEligibilityDate() {
        return DateUtils.clone(this.eligibilityDate);
    }

    public void setEligibilityDate(Date eligibilityDate) {
        this.eligibilityDate = DateUtils.clone(eligibilityDate);
    }

    /**
     * Indique si un contact a un portefeuille contenant uniquement des domaines en redemption (Statut Deleted)
     * 
     * @return true si le portefeuille ne contient que des domaines en statut Deleted
     * @throws ServiceException
     * 
     */
    public boolean hasOnlyDeletedDomains() throws ServiceException {

        for (String domainName : this.getDomainsPortfolio()) {
            Domain domain = AppServiceFacade.getDomainService().getDomainWithName(domainName, this.userIdCaller, this.tldCaller);
            if (domain.getStatus() != DomainStatus.Deleted) {
                return false;
            }
        }

        return true;
    }

    /**
     * Indique si le portefeuille du contact est vide.
     * 
     * @return
     * @throws ServiceException
     */
    public boolean hasEmptyPortfolio() throws ServiceException {
        return this.getDomainsPortfolio() == null
               || this.getDomainsPortfolio().isEmpty();
    }

    /**
     * REtourne une copie du contact.
     * 
     * @return
     * @throws CloneNotSupportedException
     */
    public WhoisContact copy() throws ServiceException {
        try {
            return (WhoisContact) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new ServiceException("copy() failed", e);
        }
    }

    public Qualification getQualificationInProgress() throws ServiceException {
        return AppServiceFacade.getQualificationService().getQualificationInProgress(this.handle, this.userIdCaller, this.tldCaller);
    }

    public List<Domain> getDomains() throws ServiceException {
        return AppServiceFacade.getDomainService().getDomainsWithHolderHandle(this.handle, this.userIdCaller, this.tldCaller);
    }

    public int getDomainsCount() throws ServiceException {
        return AppServiceFacade.getDomainService().getDomainsWithHolderHandleCount(this.handle, this.userIdCaller, this.tldCaller);
    }

    public Boolean isDisclosureRestriction() {
        return this.disclosureRestriction;
    }

    public void setDisclosureRestriction(Boolean disclosureRestriction) {
        this.disclosureRestriction = disclosureRestriction;
    }

}
