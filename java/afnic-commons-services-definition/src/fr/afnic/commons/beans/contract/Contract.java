/**
 * 
 */
package fr.afnic.commons.beans.contract;

import java.util.Date;
import java.util.List;

import fr.afnic.commons.beans.contact.CustomerContact;
import fr.afnic.commons.beans.contact.identity.ContactIdentity;
import fr.afnic.commons.beans.contactdetails.PostalAddress;
import fr.afnic.commons.beans.customer.Customer;
import fr.afnic.commons.beans.customer.CustomerId;
import fr.afnic.commons.beans.mail.template.EmailTemplate;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.beans.validatable.AbstractValidatable;
import fr.afnic.commons.beans.validatable.CompoundedInvalidDataDescriptionBuilder;
import fr.afnic.commons.beans.validatable.InvalidDataDescription;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.utils.DateUtils;

/**
 * Objet abstrait de modélisation des contrats d'un client permettant de savoir si il est client BA , membres et/ou partenaire.
 * 
 * @author alaphilippe
 * @see Customer
 */
public class Contract extends AbstractValidatable {

    private static final long serialVersionUID = 1L;

    /**
     * type de contrat du client En final car le type de contrat ne change pas dans la durée de vie du contrat
     * */
    private ContractId contractId;

    private ContractTypeOnTld contractType;

    private CustomerId customerId;
    private Date payementDate;
    private int durability;
    private Date createDate;
    private Date updateDate;
    private Date signingDate;
    private int idAccountManager;
    private int createUserId;
    private int updateUserId;
    private ContractStatus contractStatus;
    /*description status?*/
    private String remark;
    private int payementMethodTypeId;
    private int contractTypeTldId;
    private ContactIdentity identity;

    /** le user appelant l'objet */
    private final UserId userIdCaller;
    protected final TldServiceFacade tldCaller;

    private ContractTypeOnTld contractTypeOnTld;

    private PostalAddress postalAddress;

    public Contract(UserId userId, TldServiceFacade tldCaller) {
        super();
        this.userIdCaller = userId;
        this.tldCaller = tldCaller;
    }

    public Contract(ContractTypeOnTld contractType, UserId userId, TldServiceFacade tldCaller) {
        this(userId, tldCaller);
        this.contractType = contractType;
    }

    public void setType(ContractTypeOnTld contractType) {
        this.contractType = contractType;
    }

    public List<CustomerContact> getContacts() {
        throw new RuntimeException("not implemented");
    }

    public CustomerContact getNoc() {
        throw new RuntimeException("not implemented");
    }

    @Override
    public InvalidDataDescription checkInvalidData() {
        CompoundedInvalidDataDescriptionBuilder builder = new CompoundedInvalidDataDescriptionBuilder(this);
        return builder.build();
    }

    public EmailTemplate getConfirmationEMailTemplate() throws ServiceException {
        return this.contractType.getConfirmationEMailTemplate();
    }

    public ContractTypeOnTld getContractType() {
        if (this.contractType == null) {
            this.contractType = ContractTypeMap.getContractTypeByIdOnTld(this.contractTypeTldId, this.userIdCaller, this.tldCaller);
        }
        return this.contractType;
    }

    public void setContractType(ContractTypeOnTld contractType) {
        this.contractType = contractType;
    }

    public ContractId getContractId() {
        return this.contractId;
    }

    public void setContractId(ContractId contractId) {
        this.contractId = contractId;
    }

    public String getContractIdAsString() {
        return this.contractId.getValue();
    }

    public CustomerId getCustomerId() {
        return this.customerId;
    }

    public void setCustomerId(CustomerId customerId) {
        this.customerId = customerId;
    }

    public Date getPayementDate() {
        return DateUtils.clone(this.payementDate);
    }

    public void setPayementDate(Date payementDate) {
        this.payementDate = DateUtils.clone(payementDate);
    }

    public int getDurability() {
        return this.durability;
    }

    public void setDurability(int durability) {
        this.durability = durability;
    }

    public Date getCreateDate() {
        return DateUtils.clone(this.createDate);
    }

    public void setCreateDate(Date createDate) {
        this.createDate = DateUtils.clone(createDate);
    }

    public Date getUpdateDate() {
        return DateUtils.clone(this.updateDate);
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = DateUtils.clone(updateDate);
    }

    public Date getSigningDate() {
        return DateUtils.clone(this.signingDate);
    }

    public void setSigningDate(Date signingDate) {
        this.signingDate = DateUtils.clone(signingDate);
    }

    public int getIdAccountManager() {
        return this.idAccountManager;
    }

    public void setIdAccountManager(int idAccountManager) {
        this.idAccountManager = idAccountManager;
    }

    public int getCreateUserId() {
        return this.createUserId;
    }

    public void setCreateUserId(int idCreateUser) {
        this.createUserId = idCreateUser;
    }

    public int getUpdateUserId() {
        return this.updateUserId;
    }

    public void setUpdateUserId(int idUpdateUser) {
        this.updateUserId = idUpdateUser;
    }

    public ContractStatus getContractStatus() {
        return this.contractStatus;
    }

    public void setContractStatus(ContractStatus contractStatus) {
        this.contractStatus = contractStatus;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getPayementMethodTypeId() {
        return this.payementMethodTypeId;
    }

    public void setPayementMethodTypeId(int idPayementMethodType) {
        this.payementMethodTypeId = idPayementMethodType;
    }

    public int getContractTypeTldId() {
        return this.contractTypeTldId;
    }

    public void setContractTypeTldId(int idContractTypeTld) {
        this.contractTypeTldId = idContractTypeTld;
    }

    public ContactIdentity getIdentity() {
        return this.identity;
    }

    public void setIdentity(ContactIdentity identity) {
        this.identity = identity;
    }

    public ContractTypeOnTld getContractTypeOnTld() {
        return contractTypeOnTld;
    }

    public void setContractTypeOnTld(ContractTypeOnTld contractTypeOnTld) {
        this.contractTypeOnTld = contractTypeOnTld;
    }

    public void setPostalAddress(PostalAddress postalAddress) {
        this.postalAddress = postalAddress;
    }

    public PostalAddress getPostalAddress() {
        return this.postalAddress;
    }

}
