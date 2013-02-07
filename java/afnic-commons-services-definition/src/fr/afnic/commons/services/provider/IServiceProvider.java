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

public interface IServiceProvider {

    public IModuleService getModuleService() throws ServiceFacadeException;

    public IBillingService getBillingService() throws ServiceFacadeException;

    public IWhoisContactService getWhoisContactService() throws ServiceFacadeException;

    public IOldDocumentService getOldDocumentService() throws ServiceFacadeException;

    public IDocumentService getDocumentService() throws ServiceFacadeException;

    public IIdentityService getIdentityService() throws ServiceFacadeException;

    public IDomainService getDomainService() throws ServiceFacadeException;

    public IFaxService getFaxService() throws ServiceFacadeException;

    public IEmailService getEmailService() throws ServiceFacadeException;

    public IOperationFormService getOperationFormService() throws ServiceFacadeException;

    public IEppService getEppService() throws ServiceFacadeException;

    public IPublicLegalStructureService getPublicLegalStructureService() throws ServiceFacadeException;

    public IQualityService getQualityService() throws ServiceFacadeException;

    public IRequestService getRequestService() throws ServiceFacadeException;

    public IStatisticService getStatisticService() throws ServiceFacadeException;

    public ITicketService getTicketService() throws ServiceFacadeException;

    public ITradeService getTradeService() throws ServiceFacadeException;

    public IUserService getUserService() throws ServiceFacadeException;

    public IProfileService getProfileService() throws ServiceFacadeException;

    public IAuthorizationService getAuthorizationService() throws ServiceFacadeException;

    public IDictionaryService getDictionaryService();

    public ICustomerService getCustomerService();

    public IContractService getContractService();

    public ICustomerContactService getCustomerContactService();

    public IDomainPortfolioService getDomainPortfolioService();

    public IAccountService getAccountService();

    public IOperationService getOperationService();

    public IQualificationService getQualificationService();

    public ILegalService getLegalService();

    public IBusinessRuleService getBusinessRuleService();

    public IResultListService getResultListService();

    public IAuthorizationRequestService getAuthorizationRequestService();

    public IAuthorizationPreliminaryExamService getAuthorizationPreliminaryExamService();

    public IPostalAddressService getPostalAddressService();

    public IAgtfService getAgtfService();

    public IContactDetailsService getContactDetailsService();

}
