package fr.afnic.commons.beans.operations.qualification.operation.email;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import fr.afnic.commons.beans.WhoisContact;
import fr.afnic.commons.beans.application.env.Environnement;
import fr.afnic.commons.beans.contactdetails.EmailAddress;
import fr.afnic.commons.beans.operations.OperationType;
import fr.afnic.commons.beans.operations.qualification.PublicQualificationItemStatus;
import fr.afnic.commons.beans.operations.qualification.PublicReachMedia;
import fr.afnic.commons.beans.operations.qualification.Qualification;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.utils.Preconditions;
import fr.afnic.utils.DateUtils;

public abstract class QualificationEmailTemplate extends ParameterizedEmailTemplate<Qualification> {

    protected String fromEmailAddressToUse;
    private final QualificationEmailTemplateDestinary destinary;
    protected final OperationType type;
    protected final String separator;

    public String getSeparator() {
        return this.separator;
    }

    protected QualificationEmailTemplate(OperationType type, QualificationEmailTemplateDestinary destinary, UserId userId, TldServiceFacade tld) {
        this(type, destinary, "\n", userId, tld);
    }

    protected QualificationEmailTemplate(OperationType type, QualificationEmailTemplateDestinary destinary, String separator, UserId userId, TldServiceFacade tld) {
        super(Qualification.class, userId, tld);
        this.destinary = Preconditions.checkNotNull(destinary, "destinary");
        this.type = type;
        this.separator = separator;

        String fromEmailAddressTmp = "justification";
        if (!AppServiceFacade.getApplicationService().isEnv(Environnement.Prod)) {
            fromEmailAddressTmp += "-" + AppServiceFacade.getApplicationService().getCurrentVersion().getEnvironnement().getName().toLowerCase();
        }
        fromEmailAddressTmp += "@afnic.fr";

        this.fromEmailAddressToUse = fromEmailAddressTmp;
    }

    public void setQualification(Qualification parameter) {
        this.parameter = parameter;
    }

    public String getHolderNicHandle() throws ServiceException {
        return this.getQualification().getHolderNicHandle();
    }

    public String getDomainNameInitializedFrom() throws ServiceException {
        return this.getQualification().getDomainNameInitializedFrom();
    }

    protected String getCustomerName() throws ServiceException {
        return this.getQualification().getCustomerId().getObjectOwner(this.userIdCaller, this.tldCaller).getName();
    }

    @Override
    public EmailAddress buildFromEmailAddress() throws ServiceException {
        return new EmailAddress(this.fromEmailAddressToUse);
    }

    protected String getFrStartEmail() {
        return "Bonjour," + this.separator + this.separator;
    }

    protected String getEnStartEmail() {
        return "Dear Sir or Madam," + this.separator + this.separator;
    }

    protected String getFrEndEmail() {
        return this.separator + this.separator + "Cordialement." + this.separator + this.separator;
    }

    protected String getEnEndEmail() {
        return this.separator + this.separator + "Yours sincerely," + this.separator + this.separator;
    }

    protected String getSeparatorEmailContent() {
        return this.separator + "======================================" + this.separator + this.separator;
    }

    protected String getDomainPortfolioContentList() throws ServiceException {
        List<String> domains = AppServiceFacade.getDomainService().getDomainNamesWithHolderHandle(this.getHolderNicHandle(), this.userIdCaller, this.tldCaller);
        StringBuilder stringBuilder = new StringBuilder();

        for (String domain : domains) {
            stringBuilder.append(domain + this.separator);
        }

        return stringBuilder.toString();
    }

    protected String getHolderName() throws ServiceException {
        WhoisContact contactWithHandle = AppServiceFacade.getWhoisContactService().getContactWithHandle(this.getHolderNicHandle(), this.userIdCaller, this.tldCaller);
        return contactWithHandle.getName();
    }

    protected String getQualificationCreateDate() throws ServiceException {
        return DateUtils.toDayFormat(this.getQualification().getCreateDate());
    }

    protected PublicQualificationItemStatus getPublicEligibilityStatus() throws ServiceException {
        return this.getQualification().computePublicEligibilityStatus();
    }

    protected PublicQualificationItemStatus getPublicReachStatus() throws ServiceException {
        return this.getQualification().computePublicReachStatus();
    }

    protected PublicReachMedia getPublicReachMedia() throws ServiceException {
        return this.getQualification().computePublicReachMedia();
    }

    protected Qualification getQualification() throws ServiceException {
        return AppServiceFacade.getOperationService().getOperation(this.parameter.getId(), Qualification.class, this.userIdCaller, this.tldCaller);
    }

    @Override
    public List<EmailAddress> buildCcEmailAddress() throws ServiceException {
        return Collections.emptyList();
    }

    @Override
    public List<EmailAddress> buildBccEmailAddress() throws ServiceException {
        return Collections.emptyList();
    }

    @Override
    public List<EmailAddress> buildToEmailAddress() throws ServiceException {
        return Arrays.asList(this.destinary.getEmailAddress(this.getQualification()));
    }

    @Override
    public final String buildContent() throws ServiceException {
        return new StringBuilder().append(this.getFrStartEmail())
                                  .append(this.buildFrContent())
                                  .append(this.getFrEndEmail())
                                  .append(this.getSeparatorEmailContent())
                                  .append(this.getEnStartEmail())
                                  .append(this.buildEnContent())
                                  .append(this.getEnEndEmail())
                                  .append(this.buildEndEmail())
                                  .toString();

    }

    protected abstract String buildFrContent() throws ServiceException;

    protected abstract String buildEnContent() throws ServiceException;

    protected String buildEndEmail() throws ServiceException {
        return "";
    }

    @Override
    public final String buildSubject() throws ServiceException {
        return "[Q_" + this.getQualification().getQualificationId().getValue() + "] " + this.buildEndSubject();
    }

    public OperationType getType() {
        return this.type;
    }

    protected abstract String buildEndSubject() throws ServiceException;

    public QualificationEmailTemplateDestinary getDestinary() {
        return this.destinary;
    }

}
