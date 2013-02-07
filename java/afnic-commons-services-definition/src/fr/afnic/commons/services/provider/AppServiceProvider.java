package fr.afnic.commons.services.provider;

import fr.afnic.commons.services.IAccountService;
import fr.afnic.commons.services.IAgtfService;
import fr.afnic.commons.services.IApplicationService;
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
import fr.afnic.commons.services.facade.exception.ServiceFacadeInitializationException;
import fr.afnic.commons.services.facade.exception.ServiceNotInitializedException;

public class AppServiceProvider implements IServiceProvider {

    public static final String CONFIGURATION_FILE_NAME = "afnic-commons-service-definition.xml";
    public static final String LOG_FILE_NAME = "afnic-commons-service-log.xml";

    private static AppServiceProvider instance = null;

    protected IModuleService moduleService;
    protected IAccountService accountService;

    protected IPostalAddressService postalAddressService;

    protected IAuthorizationService authorizationService;
    protected IAuthorizationRequestService authorizationRequestService;
    protected IAuthorizationPreliminaryExamService authorizationPreliminaryExamService;
    protected IWhoisContactService whoisContactService;
    protected IDictionaryService dictionaryService;

    protected IBillingService billingService;

    protected IOldDocumentService oldDocumentService;

    protected IDocumentService documentService;
    protected IIdentityService identityService;
    protected IContactDetailsService contactDetailsService;

    protected IDomainService domainService;
    protected IFaxService faxService;
    protected IEmailService emailService;
    protected IOperationFormService operationFormService;
    protected IEppService eppService;
    protected IPublicLegalStructureService publicLegalStructureService;
    protected IQualityService qualityService;
    protected IRequestService requestService;
    protected IStatisticService statisticService;
    protected ITicketService ticketService;
    protected ITradeService tradeService;
    protected IUserService userService;
    protected IProfileService profileService;
    protected IAgtfService agtfService;

    protected ICustomerService customerService;

    protected IContractService contractService;
    protected ICustomerContactService customerContactService;
    protected IDomainPortfolioService domainPortfolioService;

    protected IApplicationService applicationService;

    protected IOperationService operationService;
    protected IQualificationService qualificationService;
    protected ILegalService legalService;
    protected IBusinessRuleService businessRuleService;

    protected IResultListService resultListService;

    public AppServiceProvider() {
    }

    public static void use(AppServiceProvider facade) {
        AppServiceProvider.instance = facade;
    }

    @Override
    public IModuleService getModuleService() throws ServiceFacadeException {
        return getService(AppServiceProvider.getInstance().moduleService, "moduleService");
    }

    @Override
    public IBillingService getBillingService() throws ServiceFacadeException {
        return getService(AppServiceProvider.getInstance().billingService, "billingService");
    }

    @Override
    public IWhoisContactService getWhoisContactService() throws ServiceFacadeException {
        return getService(AppServiceProvider.getInstance().whoisContactService, "whoisContactService");
    }

    @Override
    public IOldDocumentService getOldDocumentService() throws ServiceFacadeException {
        return getService(AppServiceProvider.getInstance().oldDocumentService, "oldDocumentService");
    }

    @Override
    public IDocumentService getDocumentService() throws ServiceFacadeException {
        return getService(AppServiceProvider.getInstance().documentService, "documentService");
    }

    @Override
    public IDomainService getDomainService() throws ServiceFacadeException {
        return getService(AppServiceProvider.getInstance().domainService, "domainService");
    }

    @Override
    public IFaxService getFaxService() throws ServiceFacadeException {
        return getService(AppServiceProvider.getInstance().faxService, "faxService");
    }

    @Override
    public IEmailService getEmailService() throws ServiceFacadeException {
        return getService(AppServiceProvider.getInstance().emailService, "emailService");
    }

    @Override
    public IOperationFormService getOperationFormService() throws ServiceFacadeException {
        return getService(AppServiceProvider.getInstance().operationFormService, "operationFormService");
    }

    @Override
    public IEppService getEppService() throws ServiceFacadeException {
        return getService(AppServiceProvider.getInstance().eppService, "eppService");
    }

    @Override
    public IPublicLegalStructureService getPublicLegalStructureService() throws ServiceFacadeException {
        return getService(AppServiceProvider.getInstance().publicLegalStructureService, "publicLegalStructureService");
    }

    @Override
    public IQualityService getQualityService() throws ServiceFacadeException {
        return getService(AppServiceProvider.getInstance().qualityService, "qualityService");
    }

    @Override
    public IRequestService getRequestService() throws ServiceFacadeException {
        return getService(AppServiceProvider.getInstance().requestService, "requestService");
    }

    @Override
    public IStatisticService getStatisticService() throws ServiceFacadeException {
        return getService(AppServiceProvider.getInstance().statisticService, "statisticService");
    }

    @Override
    public ITicketService getTicketService() throws ServiceFacadeException {
        return getService(AppServiceProvider.getInstance().ticketService, "ticketService");
    }

    @Override
    public ITradeService getTradeService() throws ServiceFacadeException {
        return getService(AppServiceProvider.getInstance().tradeService, "tradeService");
    }

    @Override
    public IUserService getUserService() throws ServiceFacadeException {
        return getService(AppServiceProvider.getInstance().userService, "userService");
    }

    @Override
    public IProfileService getProfileService() throws ServiceFacadeException {
        return getService(AppServiceProvider.getInstance().profileService, "profileService");
    }

    @Override
    public IAuthorizationService getAuthorizationService() throws ServiceFacadeException {
        return getService(AppServiceProvider.getInstance().authorizationService, "authorizationService");
    }

    @Override
    public IDictionaryService getDictionaryService() {
        return getService(AppServiceProvider.getInstance().dictionaryService, "dictionaryService");
    }

    public IApplicationService getApplicationService() {
        return getService(AppServiceProvider.getInstance().applicationService, "applicationService");
    }

    @Override
    public ICustomerService getCustomerService() {
        return getService(AppServiceProvider.getInstance().customerService, "customerService");
    }

    @Override
    public IContractService getContractService() {
        return getService(AppServiceProvider.getInstance().contractService, "contractService");
    }

    @Override
    public ICustomerContactService getCustomerContactService() {
        return getService(AppServiceProvider.getInstance().customerContactService, "customerContactService");
    }

    @Override
    public IDomainPortfolioService getDomainPortfolioService() {
        return getService(AppServiceProvider.getInstance().domainPortfolioService, "domainPortfolioService");
    }

    @Override
    public IAccountService getAccountService() {
        return getService(AppServiceProvider.getInstance().accountService, "accountService");
    }

    @Override
    public IOperationService getOperationService() {
        return getService(AppServiceProvider.getInstance().operationService, "operationService");
    }

    @Override
    public IQualificationService getQualificationService() {
        return getService(AppServiceProvider.getInstance().qualificationService, "qualificationService");
    }

    @Override
    public ILegalService getLegalService() {
        return getService(AppServiceProvider.getInstance().legalService, "legalService");
    }

    @Override
    public IBusinessRuleService getBusinessRuleService() {
        return getService(AppServiceProvider.getInstance().businessRuleService, "businessRuleService");
    }

    @Override
    public IResultListService getResultListService() {
        return getService(AppServiceProvider.getInstance().resultListService, "resultListService");
    }

    @Override
    public IAuthorizationRequestService getAuthorizationRequestService() {
        return getService(AppServiceProvider.getInstance().authorizationRequestService, "authorizationRequestService");
    }

    @Override
    public IAuthorizationPreliminaryExamService getAuthorizationPreliminaryExamService() {
        return getService(AppServiceProvider.getInstance().authorizationPreliminaryExamService, "authorizationPreliminaryExamService");
    }

    @Override
    public IPostalAddressService getPostalAddressService() {
        return getService(AppServiceProvider.getInstance().postalAddressService, "postalAddressService");
    }

    private static <SERVICE> SERVICE getService(SERVICE service, String serviceName) {
        if (service == null) {
            throw new ServiceNotInitializedException(serviceName);
        } else {
            return service;
        }
    }

    /**
     * Retourne une instance d'une facade.<br/>
     * Si la facade n'a pas encore ete initialisée, elle est initialisée via spring depuis <br/>
     * le fichier CONFIGURATION_FILE_NAME qui doit se trouver dans le classpath.
     * 
     * @return
     * @throws ServiceFacadeInitializationException
     */
    protected static AppServiceProvider getInstance() throws ServiceFacadeInitializationException {
        return AppServiceProvider.instance;
    }

    @Deprecated
    public static AppServiceProvider getInstancePublic() throws ServiceFacadeInitializationException {
        return AppServiceProvider.instance;
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

    public void setApplicationService(IApplicationService applicationService) {
        this.applicationService = applicationService;
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

    public void setBillingService(IBillingService billingService) {
        this.billingService = billingService;
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

    @Override
    public IIdentityService getIdentityService() throws ServiceFacadeException {
        return this.identityService;
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

}
