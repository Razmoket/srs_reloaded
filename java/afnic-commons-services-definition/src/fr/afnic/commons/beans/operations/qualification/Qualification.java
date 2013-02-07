package fr.afnic.commons.beans.operations.qualification;

import java.util.Map;

import fr.afnic.commons.beans.ContactSnapshot;
import fr.afnic.commons.beans.CorporateEntityWhoisContact;
import fr.afnic.commons.beans.WhoisContact;
import fr.afnic.commons.beans.boarequest.TopLevelOperation;
import fr.afnic.commons.beans.boarequest.TopLevelOperationStatus;
import fr.afnic.commons.beans.contactdetails.EmailAddress;
import fr.afnic.commons.beans.customer.Customer;
import fr.afnic.commons.beans.customer.CustomerId;
import fr.afnic.commons.beans.mail.Email;
import fr.afnic.commons.beans.operations.Operation;
import fr.afnic.commons.beans.operations.OperationConfiguration;
import fr.afnic.commons.beans.operations.OperationId;
import fr.afnic.commons.beans.operations.OperationType;
import fr.afnic.commons.beans.operations.contact.IContactOperation;
import fr.afnic.commons.beans.operations.qualification.operation.QualificationAddEligibility;
import fr.afnic.commons.beans.operations.qualification.operation.QualificationAddReachability;
import fr.afnic.commons.beans.operations.qualification.operation.TopLevelOperationStatusUpdate;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.beans.validatable.InvalidDataException;
import fr.afnic.commons.services.exception.NotFoundException;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class Qualification extends TopLevelOperation implements IContactOperation, IQualificationOperation, Cloneable {

    private EmailAddress initiatorEmailAddress;
    private PortfolioStatus portfolioStatus;
    private ReachStatus reachStatus;
    private EligibilityStatus eligibilityStatus;
    private QualificationSource source;

    private CustomerId customerId;
    private boolean incoherent;

    private String holderNicHandle;
    private String holderSnapshotId;

    private int qualificationObjectVersion;

    private QualificationId qualificationId;

    private String domainNameInitializedFrom;

    private String qualificationComment;

    public Qualification(UserId userId, TldServiceFacade tld) {
        super(OperationConfiguration.create()
                                    .setType(OperationType.Qualification)
                                    .setCreateUserId(userId), userId, tld);
    }

    public void setHolderNicHandle(String holderNicHandle) {
        this.holderNicHandle = holderNicHandle;
    }

    public String getHolderNicHandle() {
        return this.holderNicHandle;
    }

    public void setHolderSnapshotId(String holderSnapshotId) {
        this.holderSnapshotId = holderSnapshotId;
    }

    public QualificationSource getSource() {
        return this.source;
    }

    public void setSource(QualificationSource source) {
        this.source = source;
    }

    public boolean isExternalSource() {
        return this.source == QualificationSource.Reporting || this.source == QualificationSource.Plaint;
    }

    public EligibilityStatus getEligibilityStatus() {
        return this.eligibilityStatus;
    }

    public void setEligibilityStatus(EligibilityStatus eligibilityStatus) {
        this.eligibilityStatus = eligibilityStatus;
    }

    public ReachStatus getReachStatus() {
        return this.reachStatus;
    }

    public void setReachStatus(ReachStatus reachStatus) {
        this.reachStatus = reachStatus;
    }

    public PortfolioStatus getPortfolioStatus() {
        return this.portfolioStatus;
    }

    public void setPortfolioStatus(PortfolioStatus portfolioStatus) {
        this.portfolioStatus = portfolioStatus;
    }

    public boolean isReachable() {
        return this.reachStatus == ReachStatus.Email || this.reachStatus == ReachStatus.Phone;
    }

    public boolean isEligible() {
        return this.eligibilityStatus == EligibilityStatus.Active;
    }

    public EmailAddress getHolderEmailAddress() throws ServiceException {
        return this.getHolder().getFirstEmailAddress();
    }

    public EmailAddress getRegistrarNocEmailAddress() throws ServiceException {
        throw new RuntimeException("Not implemented");
        /*return this.getQualification().getCustomer()
                   .getContacts(CustomerContactRole.NOC)
                   .get(0)
                   .getFirstEmailAddress();*/
    }

    public EmailAddress getInitiatorEmailAddress() {
        return this.initiatorEmailAddress;
    }

    public void setInitiatorEmailAddress(EmailAddress initiatorEmailAddress) {
        this.initiatorEmailAddress = initiatorEmailAddress;
    }

    /**
     * Crée et execute une opération pour la qualification
     */
    public Qualification execute(QualificationOperationType qualificationOperationType) throws ServiceException {
        return this.execute(qualificationOperationType, null);
    }

    /**
     * Crée et execute une opération pour la qualification
     */
    public Qualification execute(QualificationOperationType qualificationOperationType, Map<OperationType, String> comment) throws ServiceException {
        return this.execute(qualificationOperationType, comment, null);
    }

    /**
     * Crée et execute une opération pour la qualification
     */
    public Qualification execute(QualificationOperationType qualificationOperationType, Map<OperationType, String> comment, Map<OperationType, Email> email) throws ServiceException {
        Operation operation = qualificationOperationType.createOperation(this, comment, email, this.userIdCaller, this.tldCaller);

        operation.execute();
        return AppServiceFacade.getQualificationService().getQualification(this.getId(), this.userIdCaller, this.tldCaller);
    }

    public WhoisContact getHolder() throws ServiceException {
        if (this.holderNicHandle != null) {
            try {
                return AppServiceFacade.getWhoisContactService().getContactWithHandle(this.holderNicHandle, this.userIdCaller, this.tldCaller);
            } catch (NotFoundException e) {
                if (this.hasHolderSnapshot()) {
                    return this.getHolderSnapshot().getContact();
                } else {
                    throw new NotFoundException("No contact or snapshot found for handle " + this.holderNicHandle, CorporateEntityWhoisContact.class);
                }
            }
        } else {
            throw new ServiceException("No contactHolderHandle defined");
        }
    }

    public boolean hasHolderSnapshot() throws ServiceException {
        return this.holderSnapshotId != null;
    }

    public ContactSnapshot getHolderSnapshot() throws ServiceException {
        if (this.holderSnapshotId != null && !"0".equals(this.holderSnapshotId)) {
            return AppServiceFacade.getWhoisContactService().getSnapshot(this.holderSnapshotId, this.userIdCaller, this.tldCaller);
        } else {
            return null;
        }
    }

    public PublicQualificationStatus computePublicQualificationStatus() throws ServiceException {
        if (this.topLevelStatus == null) {
            return null;
        } else {
            return this.topLevelStatus.getPublicQualificationStatus();
        }
    }

    public PublicQualificationItemStatus computePublicReachStatus() {
        return ReachStatusToPublicQualificationItemStatusConverter.convert(this.reachStatus);

    }

    public PublicQualificationItemStatus computePublicEligibilityStatus() {
        return EligibilityStatusToPublicQualificationItemStatusConverter.convert(this.eligibilityStatus);

    }

    public PublicReachMedia computePublicReachMedia() {
        return ReachStatusToPublicReachMediaConverter.convert(this.reachStatus);
    }

    public boolean isIncoherent() {
        return this.incoherent;
    }

    public void setIncoherent(boolean incoherent) {
        this.incoherent = incoherent;
    }

    public CustomerId getCustomerId() {
        return this.customerId;
    }

    public Customer getCustomer() throws ServiceException {
        try {
            return this.customerId.getObjectOwner(this.userIdCaller, this.tldCaller);
        } catch (InvalidDataException e) {
            return AppServiceFacade.getWhoisContactService().getContactWithHandle(this.holderNicHandle, this.userIdCaller, this.tldCaller).getCustomer();
        }
    }

    public void setCustomerId(CustomerId customerId) {
        this.customerId = customerId;
    }

    public int getQualificationObjectVersion() {
        return this.qualificationObjectVersion;
    }

    public void setQualificationObjectVersion(int qualificationObjectVersion) {
        this.qualificationObjectVersion = qualificationObjectVersion;
    }

    public String getHolderSnapshotId() {
        return this.holderSnapshotId;
    }

    @Override
    public String getNicHandle() {
        return this.getHolderNicHandle();
    }

    @Override
    public Qualification getQualification() {
        return this;
    }

    @Override
    public Qualification copy() throws ServiceException {
        try {
            return (Qualification) super.clone();
        } catch (Exception e) {
            throw new ServiceException("copy() failed", e);
        }
    }

    public QualificationId getQualificationId() {
        return this.qualificationId;
    }

    public String getQualificationIdAsString() {
        if (this.qualificationId == null) {
            return "-1";
        } else {
            return this.qualificationId.getValue();
        }
    }

    public void setQualificationId(QualificationId qualificationId) {
        this.qualificationId = qualificationId;
    }

    public void updateEligibility(EligibilityStatus newValue, String comment) throws ServiceException {

        OperationConfiguration conf = OperationConfiguration.create();
        conf = conf.setParentId(this.getId());
        conf = conf.setComment(comment);
        conf = conf.setCreateUserId(this.userIdCaller);

        OperationId id = this.createAndAddSimpleOperation(new QualificationAddEligibility(conf,
                                                                                          this.getEligibilityStatus(),
                                                                                          newValue, this.userIdCaller, this.tldCaller));

        QualificationAddEligibility operation = AppServiceFacade.getOperationService().getOperation(id, QualificationAddEligibility.class, this.userIdCaller, this.tldCaller);
        operation.execute();
    }

    public void updateTopLevelStatut(TopLevelOperationStatus newValue, String comment) throws ServiceException {

        OperationConfiguration conf = OperationConfiguration.create();
        conf = conf.setParentId(this.getId());
        conf = conf.setComment(comment);
        conf = conf.setCreateUserId(this.userIdCaller);

        OperationId id = this.createAndAddSimpleOperation(new TopLevelOperationStatusUpdate(conf,
                                                                                            this.getTopLevelStatus(),
                                                                                            newValue, this.userIdCaller, this.tldCaller));

        TopLevelOperationStatusUpdate operation = AppServiceFacade.getOperationService().getOperation(id, TopLevelOperationStatusUpdate.class, this.userIdCaller, this.tldCaller);
        operation.execute();
    }

    public void updateReachability(ReachStatus newValue, String comment) throws ServiceException {

        OperationConfiguration conf = OperationConfiguration.create();
        conf = conf.setParentId(this.getId());
        conf = conf.setComment(comment);
        conf = conf.setCreateUserId(this.userIdCaller);

        OperationId id = this.createAndAddSimpleOperation(new QualificationAddReachability(conf,
                                                                                           this.getReachStatus(),
                                                                                           newValue, this.userIdCaller, this.tldCaller));

        QualificationAddReachability operation = AppServiceFacade.getOperationService().getOperation(id, QualificationAddReachability.class, this.userIdCaller, this.tldCaller);
        operation.execute();
    }

    @Override
    public void populate() throws ServiceException {
        AppServiceFacade.getQualificationService().populateQualification(this, this.userIdCaller, this.tldCaller);
    }

    @Override
    public String getDisplayName() {
        return this.getClass().getSimpleName() + " " + this.getQualificationIdAsString();
    }

    public String getDomainNameInitializedFrom() {
        return this.domainNameInitializedFrom;
    }

    public void setDomainNameInitializedFrom(String domainNameInitializedFrom) {
        this.domainNameInitializedFrom = domainNameInitializedFrom;
    }

    public String getQualificationComment() {
        return this.qualificationComment;
    }

    public void setQualificationComment(String qualificationComment) {
        this.qualificationComment = qualificationComment;
    }
}
