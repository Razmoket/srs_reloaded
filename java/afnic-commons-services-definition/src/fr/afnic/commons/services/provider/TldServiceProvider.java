package fr.afnic.commons.services.provider;

import fr.afnic.commons.services.IAccountService;
import fr.afnic.commons.services.IAgtfService;
import fr.afnic.commons.services.IAuthorizationPreliminaryExamService;
import fr.afnic.commons.services.IAuthorizationRequestService;
import fr.afnic.commons.services.IAuthorizationService;
import fr.afnic.commons.services.IBillingService;
import fr.afnic.commons.services.IBusinessRuleService;
import fr.afnic.commons.services.IContactDetailsService;
import fr.afnic.commons.services.IContractService;
import fr.afnic.commons.services.ICustomerContactService;
import fr.afnic.commons.services.ICustomerService;
import fr.afnic.commons.services.IDictionaryService;
import fr.afnic.commons.services.IDocumentService;
import fr.afnic.commons.services.IDomainPortfolioService;
import fr.afnic.commons.services.IDomainService;
import fr.afnic.commons.services.IEmailService;
import fr.afnic.commons.services.IEppService;
import fr.afnic.commons.services.IFaxService;
import fr.afnic.commons.services.IIdentityService;
import fr.afnic.commons.services.ILegalService;
import fr.afnic.commons.services.IModuleService;
import fr.afnic.commons.services.IOldDocumentService;
import fr.afnic.commons.services.IOperationFormService;
import fr.afnic.commons.services.IOperationService;
import fr.afnic.commons.services.IPostalAddressService;
import fr.afnic.commons.services.IProfileService;
import fr.afnic.commons.services.IPublicLegalStructureService;
import fr.afnic.commons.services.IQualificationService;
import fr.afnic.commons.services.IQualityService;
import fr.afnic.commons.services.IRequestService;
import fr.afnic.commons.services.IResultListService;
import fr.afnic.commons.services.IStatisticService;
import fr.afnic.commons.services.ITicketService;
import fr.afnic.commons.services.ITradeService;
import fr.afnic.commons.services.IUserService;
import fr.afnic.commons.services.IWhoisContactService;
import fr.afnic.commons.services.facade.exception.ServiceFacadeException;

public class TldServiceProvider implements IServiceProvider {

    private IModuleService moduleService;

    private IAccountService accountService;

    private IPostalAddressService postalAddressService;

    private IAuthorizationService authorizationService;
    private IAuthorizationRequestService authorizationRequestService;
    private IAuthorizationPreliminaryExamService authorizationPreliminaryExamService;
    private IWhoisContactService whoisContactService;
    private IDictionaryService dictionaryService;

    private IOldDocumentService oldDocumentService;

    private IBillingService billingService;

    private IIdentityService identityService;
    private IContactDetailsService contactDetailsService;

    private IDocumentService documentService;

    private IDomainService domainService;
    private IFaxService faxService;
    private IEmailService emailService;
    private IOperationFormService operationFormService;
    private IEppService eppService;
    private IPublicLegalStructureService publicLegalStructureService;
    private IQualityService qualityService;
    private IRequestService requestService;
    private IStatisticService statisticService;
    private ITicketService ticketService;
    private ITradeService tradeService;
    private IUserService userService;
    private IProfileService profileService;

    private ICustomerService customerService;

    private IContractService contractService;
    private ICustomerContactService customerContactService;
    private IDomainPortfolioService domainPortfolioService;

    private IOperationService operationService;
    private IQualificationService qualificationService;
    private ILegalService legalService;
    private IBusinessRuleService businessRuleService;

    protected IResultListService resultListService;

    protected IAgtfService agtfService;

    @Override
    public IModuleService getModuleService() throws ServiceFacadeException {
        return this.moduleService;
    }

    @Override
    public IWhoisContactService getWhoisContactService() throws ServiceFacadeException {
        return this.whoisContactService;
    }

    @Override
    public IOldDocumentService getOldDocumentService() throws ServiceFacadeException {
        return this.oldDocumentService;
    }

    @Override
    public IDocumentService getDocumentService() throws ServiceFacadeException {
        return this.documentService;
    }

    @Override
    public IDomainService getDomainService() throws ServiceFacadeException {
        return this.domainService;
    }

    @Override
    public IFaxService getFaxService() throws ServiceFacadeException {
        return this.faxService;
    }

    @Override
    public IEmailService getEmailService() throws ServiceFacadeException {
        return this.emailService;
    }

    @Override
    public IOperationFormService getOperationFormService() throws ServiceFacadeException {
        return this.operationFormService;
    }

    @Override
    public IEppService getEppService() throws ServiceFacadeException {
        return this.eppService;
    }

    @Override
    public IPublicLegalStructureService getPublicLegalStructureService() throws ServiceFacadeException {
        return this.publicLegalStructureService;
    }

    @Override
    public IQualityService getQualityService() throws ServiceFacadeException {
        return this.qualityService;
    }

    @Override
    public IRequestService getRequestService() throws ServiceFacadeException {
        return this.requestService;
    }

    @Override
    public IStatisticService getStatisticService() throws ServiceFacadeException {
        return this.statisticService;
    }

    @Override
    public ITicketService getTicketService() throws ServiceFacadeException {
        return this.ticketService;
    }

    @Override
    public ITradeService getTradeService() throws ServiceFacadeException {
        return this.tradeService;
    }

    @Override
    public IUserService getUserService() throws ServiceFacadeException {
        return this.userService;
    }

    @Override
    public IProfileService getProfileService() throws ServiceFacadeException {
        return this.profileService;
    }

    @Override
    public IAuthorizationService getAuthorizationService() throws ServiceFacadeException {
        return this.authorizationService;
    }

    @Override
    public IDictionaryService getDictionaryService() {
        return this.dictionaryService;
    }

    @Override
    public ICustomerService getCustomerService() {
        return this.customerService;
    }

    @Override
    public IContractService getContractService() {
        return this.contractService;
    }

    @Override
    public ICustomerContactService getCustomerContactService() {
        return this.customerContactService;
    }

    @Override
    public IDomainPortfolioService getDomainPortfolioService() {
        return this.domainPortfolioService;
    }

    @Override
    public IAccountService getAccountService() {
        return this.accountService;
    }

    @Override
    public IOperationService getOperationService() {
        return this.operationService;
    }

    @Override
    public IQualificationService getQualificationService() {
        return this.qualificationService;
    }

    @Override
    public ILegalService getLegalService() {
        return this.legalService;
    }

    @Override
    public IBusinessRuleService getBusinessRuleService() {
        return this.businessRuleService;
    }

    @Override
    public IResultListService getResultListService() {
        return this.resultListService;
    }

    @Override
    public IAuthorizationRequestService getAuthorizationRequestService() {
        return this.authorizationRequestService;
    }

    @Override
    public IAuthorizationPreliminaryExamService getAuthorizationPreliminaryExamService() {
        return this.authorizationPreliminaryExamService;
    }

    @Override
    public IPostalAddressService getPostalAddressService() {
        return this.postalAddressService;
    }

    @Override
    public IIdentityService getIdentityService() {
        return this.identityService;
    }

    public void setPostalAddressService(IPostalAddressService postalAddressService) {
        this.postalAddressService = postalAddressService;
    }

    public void setAuthorizationService(IAuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    public void setAuthorizationRequestService(IAuthorizationRequestService authorizationRequestService) {
        this.authorizationRequestService = authorizationRequestService;
    }

    public void setAuthorizationPreliminaryExamService(IAuthorizationPreliminaryExamService authorizationPreliminaryExamService) {
        this.authorizationPreliminaryExamService = authorizationPreliminaryExamService;
    }

    public void setWhoisContactService(IWhoisContactService whoisContactService) {
        this.whoisContactService = whoisContactService;
    }

    public void setDictionaryService(IDictionaryService dictionaryService) {
        this.dictionaryService = dictionaryService;
    }

    public void setOldDocumentService(IOldDocumentService oldDocumentService) {
        this.oldDocumentService = oldDocumentService;
    }

    public void setDocumentService(IDocumentService documentService) {
        this.documentService = documentService;
    }

    public void setDomainService(IDomainService domainService) {
        this.domainService = domainService;
    }

    public void setFaxService(IFaxService faxService) {
        this.faxService = faxService;
    }

    public void setEmailService(IEmailService mailService) {
        this.emailService = mailService;
    }

    public void setOperationFormService(IOperationFormService operationFormService) {
        this.operationFormService = operationFormService;
    }

    public void setEppService(IEppService eppService) {
        this.eppService = eppService;
    }

    public void setPublicLegalStructureService(IPublicLegalStructureService publicLegalStructureService) {
        this.publicLegalStructureService = publicLegalStructureService;
    }

    public void setQualityService(IQualityService qualityService) {
        this.qualityService = qualityService;
    }

    public void setRequestService(IRequestService requestService) {
        this.requestService = requestService;
    }

    public void setStatisticService(IStatisticService statisticService) {
        this.statisticService = statisticService;
    }

    public void setTicketService(ITicketService ticketService) {
        this.ticketService = ticketService;
    }

    public void setTradeService(ITradeService tradeService) {
        this.tradeService = tradeService;
    }

    public void setUserService(IUserService userService) {
        this.userService = userService;
    }

    public void setProfileService(IProfileService profileService) {
        this.profileService = profileService;
    }

    public void setCustomerService(ICustomerService customerService) {
        this.customerService = customerService;
    }

    public void setContractService(IContractService contractService) {
        this.contractService = contractService;
    }

    public void setCustomerContactService(ICustomerContactService customerContactService) {
        this.customerContactService = customerContactService;
    }

    public void setDomainPortfolioService(IDomainPortfolioService domainPortfolioService) {
        this.domainPortfolioService = domainPortfolioService;
    }

    public void setAccountService(IAccountService accountService) {
        this.accountService = accountService;
    }

    public void setOperationService(IOperationService operationService) {
        this.operationService = operationService;
    }

    public void setQualificationService(IQualificationService qualificationService) {
        this.qualificationService = qualificationService;
    }

    public void setResultListService(IResultListService resultListService) {
        this.resultListService = resultListService;
    }

    public void setLegalService(ILegalService legalService) {
        this.legalService = legalService;
    }

    public void setBusinessRuleService(IBusinessRuleService businessRuleService) {
        this.businessRuleService = businessRuleService;
    }

    @Override
    public IAgtfService getAgtfService() {
        return this.agtfService;
    }

    public void setAgtfService(IAgtfService agtfService) {
        this.agtfService = agtfService;
    }

    public void setIdentityService(IIdentityService identityService) {
        this.identityService = identityService;
    }

    @Override
    public IContactDetailsService getContactDetailsService() {
        return this.contactDetailsService;
    }

    public void setContactDetailsService(IContactDetailsService contactDetailsService) {
        this.contactDetailsService = contactDetailsService;
    }

    @Override
    public IBillingService getBillingService() throws ServiceFacadeException {
        return this.billingService;
    }

    public void setBillingService(IBillingService billingService) {
        this.billingService = billingService;
    }

}
