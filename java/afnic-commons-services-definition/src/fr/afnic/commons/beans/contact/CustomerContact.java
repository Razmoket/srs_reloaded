/**
 * 
 */
package fr.afnic.commons.beans.contact;

import java.util.ArrayList;
import java.util.List;

import fr.afnic.commons.beans.contact.identity.ContactIdentity;
import fr.afnic.commons.beans.contact.identity.CorporateEntityIdentity;
import fr.afnic.commons.beans.contact.identity.IndividualIdentity;
import fr.afnic.commons.beans.contactdetails.Region;
import fr.afnic.commons.beans.customer.Customer;
import fr.afnic.commons.beans.customer.CustomerId;
import fr.afnic.commons.beans.profiling.EppAccount;
import fr.afnic.commons.beans.profiling.GenericAccount;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;

/**
 * Objet de représentation d'un contact de client.
 * 
 * 
 */
public class CustomerContact extends GrcContact {

    private static final long serialVersionUID = -9218925096944797706L;

    public static CustomerContact createIndividualCustomer(UserId userId, TldServiceFacade tld) {
        return new CustomerContact(new IndividualIdentity(), userId, tld);
    }

    public static CustomerContact createCorporateEntityCustomer(UserId userId, TldServiceFacade tld) {
        return new CustomerContact(new CorporateEntityIdentity(), userId, tld);
    }

    private CustomerContactId contactId;

    private CustomerId customerId;

    private final List<CustomerContactRole> roles = new ArrayList<CustomerContactRole>();

    private CustomerContactStatus status;

    /** région d'activité du contact -> lorsque c'est un commercial. */
    private Region region;

    private EppAccount eppAccount;
    private GenericAccount appAccount;

    private String nicopeId;

    private boolean isCompanyRepresentative;

    public CustomerContact(ContactIdentity identity, UserId userId, TldServiceFacade tld) {
        super(identity, userId, tld);
    }

    public boolean isCompanyRepresentative() {
        return this.isCompanyRepresentative;
    }

    public void setCompanyRepresentative(boolean isCompanyRepresentative) {
        this.isCompanyRepresentative = isCompanyRepresentative;
    }

    public CustomerContactId getContactId() {
        return this.contactId;
    }

    public String getContactIdAsString() {
        return this.contactId.getValue();
    }

    public void setContactId(CustomerContactId contactId) {
        this.contactId = contactId;
    }

    public boolean hasContactId() {
        return this.contactId != null;
    }

    public Region getActivityRegion() {
        return this.region;
    }

    public void setActivityRegion(Region region) {
        this.region = region;
    }

    public boolean hasActivityRegion() {
        return this.region != null;
    }

    public EppAccount getEppAccount() {
        return this.eppAccount;
    }

    public void setEppAccount(EppAccount account) {
        this.eppAccount = account;
    }

    public boolean hasEppAccount() {
        return this.eppAccount != null;
    }

    public GenericAccount getAppAccount() {
        return this.appAccount;
    }

    public void setAppAccount(GenericAccount appAccount) {
        this.appAccount = appAccount;
    }

    public CustomerId getCustomerId() {
        return this.customerId;
    }

    public void setCustomerId(CustomerId customerId) {
        this.customerId = customerId;
    }

    public Customer getCustomer() throws ServiceException {
        return AppServiceFacade.getCustomerService().getCustomerWithId(this.customerId,
                                                                       this.userIdCaller,
                                                                       this.tldCaller);
    }

    public String getNicopeId() {
        return this.nicopeId;
    }

    public void setNicopeId(String nicopeId) {
        this.nicopeId = nicopeId;
    }

    public CustomerContactStatus getStatus() {
        return this.status;
    }

    public void setStatus(CustomerContactStatus status) {
        this.status = status;
    }

    public List<CustomerContactRole> getRoles() {
        return this.roles;
    }

}
