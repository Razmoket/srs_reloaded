/**
 * 
 */
package fr.afnic.commons.beans.customer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import fr.afnic.commons.beans.contact.CustomerContact;
import fr.afnic.commons.beans.contact.CustomerContactCriteria;
import fr.afnic.commons.beans.contact.GrcContact;
import fr.afnic.commons.beans.contact.identity.ContactIdentity;
import fr.afnic.commons.beans.contact.identity.CorporateEntityIdentity;
import fr.afnic.commons.beans.contact.identity.IndividualIdentity;
import fr.afnic.commons.beans.contactdetails.Country;
import fr.afnic.commons.beans.contract.Contract;
import fr.afnic.commons.beans.contract.ContractTypeEnum;
import fr.afnic.commons.beans.contract.ContractTypeOnTld;
import fr.afnic.commons.beans.customer.benefit.ServiceType;
import fr.afnic.commons.beans.customer.collectiveprocedure.CustomerTag;
import fr.afnic.commons.beans.customer.status.CustomerStatus;
import fr.afnic.commons.beans.customer.status.Termination;
import fr.afnic.commons.beans.domain.DomainPortfolio;
import fr.afnic.commons.beans.history.HistoryEvent;
import fr.afnic.commons.beans.logs.ILogger;
import fr.afnic.commons.beans.profiling.UserAccount;
import fr.afnic.commons.beans.profiling.users.User;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.utils.Preconditions;
import fr.afnic.utils.DateUtils;

public class Customer extends GrcContact implements Serializable {

    private static final long serialVersionUID = 1L;

    protected static final ILogger LOGGER = AppServiceFacade.getLoggerService().getLogger(Customer.class);

    public static Customer createIndividualCustomer(UserId userId, TldServiceFacade tld) {
        return new Customer(new IndividualIdentity(), userId, tld);
    }

    public static Customer createCorporateEntityCustomer(UserId userId, TldServiceFacade tld) {
        return new Customer(new CorporateEntityIdentity(), userId, tld);
    }

    public Customer(ContactIdentity identity, UserId userIdCaller, TldServiceFacade tldCaller) {
        super(identity, userIdCaller, tldCaller);
    }

    private UserAccount account;

    protected CustomerId customerId;

    protected String customerNumber;

    protected String code;

    protected PaymentMethod paymentMethod;

    protected UserId accountManagerId;

    /** Chemin du logo stockés sur le système de fichiers nfs partagé entre les membres du cluster web */
    protected String logoPath;

    /** Nom commercial */
    private String tradeName;

    /** Liste des descriptions de l'annuaire */
    protected HashMap<Country, String> descriptionMap = new HashMap<Country, String>();

    protected CustomerStatus status = CustomerStatus.ACTIVE;

    protected boolean isOnRedList;

    /** Indique si le client est en attente de changement d'option */
    protected boolean isWaitingForContractUpdate;

    protected Termination termination;

    protected HistoryEvent activityTransfert;

    protected String nicopeId;

    protected int accreditationNumber;

    protected String billHeader;

    protected boolean isTest;

    /**
     * liste des prestations offertes par le client.
     */
    protected List<ServiceType> services = new ArrayList<ServiceType>();

    /**
     * liste des procédures collectives.
     */
    protected CustomerTag runningCollectiveProcedure;

    /**
     * Liste des contacts du client.
     */
    protected List<CustomerContact> contacts = null;

    /**
     * Liste des contracts du clients.
     */
    protected List<Contract> contracts = null;

    protected DomainPortfolio portfolio = null;

    public CustomerStatus getStatus() {
        return this.status;
    }

    public void setStatus(CustomerStatus status) {
        this.status = status;
    }

    public String getCustomerIdAsString() {
        if (this.customerId == null) {
            return "";
        } else {
            return this.customerId.getValue();
        }
    }

    public CustomerId getCustomerId() {
        return this.customerId;
    }

    public void setCustomerId(CustomerId customerId) {
        this.customerId = customerId;
    }

    public List<ServiceType> getServices() {
        return this.services;
    }

    public void setServices(List<ServiceType> services) {
        this.services = services;
    }

    public List<CustomerContact> getCustomerContacts() {
        if (this.contacts == null) {
            try {
                CustomerContactCriteria customerContactCriteria = new CustomerContactCriteria();
                customerContactCriteria.setCustomerId(this.customerId);
                this.contacts = AppServiceFacade.getCustomerContactService().seachCustomerContact(customerContactCriteria, userIdCaller,
                                                                                                  tldCaller);
            } catch (ServiceException e) {
                Customer.LOGGER.error("Erreur lors de la récupération des contacts avec customerId:" + this.customerId);
            }
        }
        return this.contacts;
    }

    public void setCustomerContacts(List<CustomerContact> customerContacts) {
        this.contacts = Preconditions.checkNotNull(customerContacts, "customerContacts");
    }

    public void addContact(CustomerContact contact) {
        Preconditions.checkNotNull(contact, "contact");
        this.contacts.add(contact);
    }

    public boolean hasContacts() {
        return getCustomerContacts() != null && !this.contacts.isEmpty();
    }

    public List<Contract> getContracts() {
        if (this.contracts == null) {
            try {
                this.contracts = AppServiceFacade.getContractService().getContractWithCustomerId(getCustomerId(), userIdCaller, tldCaller);
            } catch (ServiceException e) {
                Customer.LOGGER.error("Erreur lors de la récupération des contrats avec customerId:" + this.customerId);
            }
        }
        return this.contracts;
    }

    public void setContracts(List<Contract> customerContracts) {
        this.contracts = customerContracts;
    }

    public String getCustomerNumber() {
        return this.customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    public PaymentMethod getPaymentMethod() {
        return this.paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public UserId getAccountManagerId() {
        return this.accountManagerId;
    }

    public User getAccountManager() throws ServiceException {
        return this.accountManagerId.getObjectOwner(this.userIdCaller, this.tldCaller);
    }

    public String getAccountManagerLogin() throws ServiceException {
        if (this.hasAccountManager()) {
            return this.getAccountManager().getLogin();
        } else {
            return " ";
        }
    }

    public void setAccountManagerId(UserId accountManagerId) {
        this.accountManagerId = accountManagerId;
    }

    public boolean hasAccountManager() {
        return this.accountManagerId != null;
    }

    @Override
    public String getRemark() {
        return this.remark;
    }

    public void setComment(String comment) {
        this.remark = comment;
    }

    public List<Class<CustomerStatus>> getNextAllowedStatus() {
        return this.status.getNextAllowedStatus(this);
    }

    public <S extends CustomerStatus> boolean isAllowedNextStatus(Class<S> statusClass) {
        return this.status.getNextAllowedStatus(this).contains(statusClass);
    }

    public boolean hasCustomerId() {
        return this.customerId != null;
    }

    public boolean hasCustomerNumber() {
        return this.customerNumber != null;
    }

    public boolean hasPaymentMethod() {
        return this.paymentMethod != null;
    }

    public boolean hasComment() {
        return this.remark != null;
    }

    public boolean hasStatus() {
        return this.status != null;
    }

    public boolean hasServices() {
        return this.services != null && !this.services.isEmpty();
    }

    public boolean hasCustomerContracts() {
        return this.getContracts() != null && !this.contracts.isEmpty();
    }

    @Override
    public long getObjectVersion() {
        return this.objectVersion;
    }

    @Override
    public void setObjectVersion(int objectVersion) {
        this.objectVersion = objectVersion;
    }

    @Override
    public UserId getUpdateUserId() {
        return this.updateUserId;
    }

    @Override
    public void setUpdateUserId(UserId updateUserId) {
        this.updateUserId = updateUserId;
    }

    @Override
    public UserId getCreateUserId() {
        return this.createUserId;
    }

    @Override
    public void setCreateUserId(UserId createUserId) {
        this.createUserId = createUserId;
    }

    @Override
    public Date getCreateDate() {
        return DateUtils.clone(this.createDate);
    }

    @Override
    public void setCreateDate(Date createDate) {
        this.createDate = DateUtils.clone(createDate);
    }

    @Override
    public Date getUpdateDate() {
        return DateUtils.clone(this.updateDate);
    }

    @Override
    public void setUpdateDate(Date updateDate) {
        this.updateDate = DateUtils.clone(updateDate);
    }

    public boolean isOnRedList() {
        return this.isOnRedList;
    }

    public void setOnRedList(boolean isOnRedList) {
        this.isOnRedList = isOnRedList;
    }

    public CustomerTag getRunningCollectiveProcedure() {
        return this.runningCollectiveProcedure;
    }

    public void setRunningCollectiveProcedures(CustomerTag runningCollectiveProcedure) {
        this.runningCollectiveProcedure = runningCollectiveProcedure;
    }

    public boolean hasNotRunningCollectiveProcedure() {
        return !this.hasRunningCollectiveProcedure();
    }

    public boolean hasRunningCollectiveProcedure() {
        return this.runningCollectiveProcedure != null;
    }

    public boolean isWaitingForContractUpdate() {
        return this.isWaitingForContractUpdate;
    }

    public void setWaitingForContractUpdate(boolean isWaitingForContractUpdate) {
        this.isWaitingForContractUpdate = isWaitingForContractUpdate;
    }

    public boolean hasService(ServiceType type) {
        return this.services != null && this.services.contains(type);
    }

    public void removeService(ServiceType type) {
        if (this.services != null) {
            this.services.remove(type);
        }
    }

    public void addService(ServiceType type) {
        if (this.services == null) {
            this.services = new ArrayList<ServiceType>();
        }
        this.services.add(type);
    }

    public boolean hasNotContractTypeOnTld(ContractTypeOnTld type) {
        return !this.hasContractTypeOnTld(type);
    }

    public boolean hasContractTypeOnTld(ContractTypeOnTld type) {
        for (Contract contract : this.getContracts()) {
            if (contract.getContractType().equals(type)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasContractType(ContractTypeEnum type) {
        for (Contract contract : this.getContracts()) {
            if (contract.getContractType().getTypeContract() == type) {
                return true;
            }
        }
        return false;
    }

    public void removeContract(ContractTypeOnTld type) {
        for (Contract contract : this.contracts) {
            if (contract.getContractType().equals(type)) {
                this.contracts.remove(contract);
                return;
            }
        }
    }

    public boolean hasNotTermination() {
        return !this.hasTermination();
    }

    public boolean hasTermination() {
        return this.termination != null;
    }

    public Termination getTermination() {
        return this.termination;
    }

    public void setTermination(Termination termination) {
        this.termination = termination;
    }

    public boolean isNotRegistrar() {
        return !this.isRegistrar();
    }

    public boolean isRegistrar() {
        return this.hasContractType(ContractTypeEnum.Registrar);
    }

    public boolean hasEmptyPortfolio() {
        return this.portfolio == null || this.portfolio.isEmpty();
    }

    public boolean hasNotAcivityTransfert() {
        return !this.hasActivityTransfert();
    }

    public boolean hasActivityTransfert() {
        return this.activityTransfert != null;
    }

    public HistoryEvent getActivityTransfert() {
        return this.activityTransfert;
    }

    public void setActivityTransfert(HistoryEvent activityTransfert) {
        this.activityTransfert = activityTransfert;
    }

    /**
     * Indique si le customer peut effectuer un transfert d'activité
     * 
     * @return
     */
    public boolean canTransfertActivity() {
        return this.hasNotAcivityTransfert() && this.getStatus() == CustomerStatus.ACTIVE
               && this.isCorporateEntity();
    }

    /**
     * Indique si le client peut changer d'option (option1/option2)
     * 
     * @return
     */
    public boolean canUpdateContract() {
        return this.hasNotAcivityTransfert() && this.getStatus() == CustomerStatus.ACTIVE && this.isRegistrar();
    }

    public void addDescription(Country countryCode, String description) {
        this.descriptionMap.put(countryCode, description);
    }

    public String getDescription(Country countryCode) {
        if (this.descriptionMap.containsKey(countryCode)) {
            return this.descriptionMap.get(countryCode);
        } else {
            return "";
        }
    }

    public String getTradeName() {
        return this.tradeName;
    }

    public void setTradeName(String tradeName) {
        this.tradeName = tradeName;
    }

    public String getLogoPath() {
        return this.logoPath;
    }

    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }

    public String getNicopeId() {
        return this.nicopeId;
    }

    public void setNicopeId(String nicopeId) {
        this.nicopeId = nicopeId;
    }

    public String getAccountLogin() {
        return this.account.getLogin();
    }

    public UserAccount getAccount() {
        return this.account;
    }

    public void setAccount(UserAccount account) {
        this.account = account;
    }

    public boolean hasAccount() {
        return this.account != null;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getAccreditationNumber() {
        return this.accreditationNumber;
    }

    public void setAccreditationNumber(int accreditationNumber) {
        this.accreditationNumber = accreditationNumber;
    }

    public String getBillHeader() {
        return this.billHeader;
    }

    public void setBillHeader(String billHeader) {
        this.billHeader = billHeader;
    }

    public boolean isTest() {
        return this.isTest;
    }

    public void setTest(boolean isTest) {
        this.isTest = isTest;
    }

}
