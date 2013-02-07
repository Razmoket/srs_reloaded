package fr.afnic.commons.services.facade;

import java.io.IOException;

import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

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
import fr.afnic.commons.services.ILoggerService;
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
import fr.afnic.commons.services.mock.MockLoggerService;
import fr.afnic.commons.services.provider.AppServiceProvider;

/**
 * Facade permettant d'accéder aux différentes implémentations des services.
 * 
 * 
 */
public class AppServiceFacade {

    public static final String CONFIGURATION_FILE_NAME = "afnic-commons-service-definition.xml";
    public static final String LOG_FILE_NAME = "afnic-commons-service-log.xml";

    private static AppServiceFacade instance = null;

    static AppServiceProvider provider = null;

    /** Le service de log est un service à part, il doit etre disponible pour initialiser les autres service (voir getLoggerService) */
    static ILoggerService staticLoggerService;

    protected AppServiceFacade() {
    }

    public static void use(AppServiceFacade facade) {
        AppServiceFacade.instance = facade;
    }

    public static void use(AppServiceProvider provider) {
        AppServiceFacade.provider = provider;
    }

    /**
     * Service à part qui se charge tout seul
     * 
     * @return
     * @throws ServiceFacadeException
     */
    public static ILoggerService getLoggerService() throws ServiceFacadeException {

        // Le logger est utilisé partout, en son absence la plus part des classes (et donc des autres services),
        // ne peuvent pas etres chargés, il est donc iportant de le charger séparemment et en premier.
        if (AppServiceFacade.staticLoggerService == null) {
            try {
                System.out.println("load " + AppServiceFacade.LOG_FILE_NAME);
                XmlBeanFactory beanFactory = new XmlBeanFactory(new ClassPathResource(AppServiceFacade.LOG_FILE_NAME));
                AppServiceFacade.staticLoggerService = beanFactory.getBean("loggerService", ILoggerService.class);
            } catch (Exception e) {
                // En cas d'erreur au chargement du service de log, on utilise l'implémentation Mock
                e.printStackTrace();
                AppServiceFacade.staticLoggerService = new MockLoggerService();
            }
        }

        return AppServiceFacade.staticLoggerService;
    }

    public static IModuleService getModuleService() throws ServiceFacadeException {
        return provider.getModuleService();
    }

    public static IBillingService getBillingService() throws ServiceFacadeException {
        return provider.getBillingService();
    }

    public static IWhoisContactService getWhoisContactService() throws ServiceFacadeException {
        return provider.getWhoisContactService();
    }

    public static IOldDocumentService getOldDocumentService() throws ServiceFacadeException {
        return provider.getOldDocumentService();
    }

    public static IDocumentService getDocumentService() throws ServiceFacadeException {
        return provider.getDocumentService();
    }

    public static IDomainService getDomainService() throws ServiceFacadeException {
        return provider.getDomainService();
    }

    public static IFaxService getFaxService() throws ServiceFacadeException {
        return provider.getFaxService();
    }

    public static IEmailService getEmailService() throws ServiceFacadeException {
        return provider.getEmailService();
    }

    public static IOperationFormService getOperationFormService() throws ServiceFacadeException {
        return provider.getOperationFormService();
    }

    public static IEppService getEppService() throws ServiceFacadeException {
        return provider.getEppService();
    }

    public static IPublicLegalStructureService getPublicLegalStructureService() throws ServiceFacadeException {
        return provider.getPublicLegalStructureService();
    }

    public static IQualityService getQualityService() throws ServiceFacadeException {
        return provider.getQualityService();
    }

    public static IRequestService getRequestService() throws ServiceFacadeException {
        return provider.getRequestService();
    }

    public static IStatisticService getStatisticService() throws ServiceFacadeException {
        return provider.getStatisticService();
    }

    public static ITicketService getTicketService() throws ServiceFacadeException {
        return provider.getTicketService();
    }

    public static ITradeService getTradeService() throws ServiceFacadeException {
        return provider.getTradeService();
    }

    public static IUserService getUserService() throws ServiceFacadeException {
        return provider.getUserService();
    }

    public static IProfileService getProfileService() throws ServiceFacadeException {
        return provider.getProfileService();
    }

    public static IAgtfService getAgtfService() throws ServiceFacadeException {
        return provider.getAgtfService();
    }

    public static IAuthorizationService getAuthorizationService() throws ServiceFacadeException {
        return provider.getAuthorizationService();
    }

    public static IDictionaryService getDictionaryService() {
        return provider.getDictionaryService();
    }

    public static IApplicationService getApplicationService() {
        return provider.getApplicationService();
    }

    public static ICustomerService getCustomerService() {
        return provider.getCustomerService();
    }

    public static IContractService getContractService() {
        return provider.getContractService();
    }

    public static ICustomerContactService getCustomerContactService() {
        return provider.getCustomerContactService();
    }

    public static IDomainPortfolioService getDomainPortfolioService() {
        return provider.getDomainPortfolioService();
    }

    public static IAccountService getAccountService() {
        return provider.getAccountService();
    }

    public static IOperationService getOperationService() {
        return provider.getOperationService();
    }

    public static IQualificationService getQualificationService() {
        return provider.getQualificationService();
    }

    public static ILegalService getLegalService() {
        return provider.getLegalService();
    }

    public static IBusinessRuleService getBusinessRuleService() {
        return provider.getBusinessRuleService();
    }

    public static IResultListService getResultListService() {
        return provider.getResultListService();
    }

    public static IAuthorizationRequestService getAuthorizationRequestService() {
        return provider.getAuthorizationRequestService();
    }

    public static IAuthorizationPreliminaryExamService getAuthorizationPreliminaryExamService() {
        return provider.getAuthorizationPreliminaryExamService();
    }

    public static IPostalAddressService getPostalAddressService() {
        return provider.getPostalAddressService();
    }

    public static IIdentityService getIdentityService() {
        return provider.getIdentityService();
    }

    public static IContactDetailsService getContactDetailsService() {
        return provider.getContactDetailsService();
    }

    /**
     * Retourne une instance d'une facade.<br/>
     * Si la facade n'a pas encore ete initialisée, elle est initialisée via spring depuis <br/>
     * le fichier CONFIGURATION_FILE_NAME qui doit se trouver dans le classpath.
     * 
     * @return
     * @throws ServiceFacadeInitializationException
     */
    protected static AppServiceFacade getInstance() throws ServiceFacadeInitializationException {
        if (AppServiceFacade.instance == null) {
            loadInstanceFromConfigurationFile();
        } else {
            System.err.println("already loaded");
        }
        return AppServiceFacade.instance;
    }

    /***
     * Charge une instance de ServiceFacade depuis le fichier CONFIGURATION_FILE_NAME.<br/>
     * Le fichier doit etre dans le classpath.
     * 
     * @return
     */
    public static void loadInstanceFromConfigurationFile() throws ServiceFacadeInitializationException {

        ClassPathResource classPathResource = null;
        try {
            System.out.println("load " + AppServiceFacade.CONFIGURATION_FILE_NAME);
            classPathResource = new ClassPathResource(AppServiceFacade.CONFIGURATION_FILE_NAME);
            XmlBeanFactory beanFactory = new XmlBeanFactory(classPathResource);
            AppServiceFacade facade = beanFactory.getBean("facade", AppServiceFacade.class);
            AppServiceFacade.use(facade);
            AppServiceProvider.use(provider);
        } catch (Exception e) {
            String fileName = "";
            if (classPathResource != null) {
                try {
                    fileName = "[" + classPathResource.getFile().getAbsolutePath() + "]";
                } catch (IOException e1) {
                    fileName = "[" + AppServiceFacade.CONFIGURATION_FILE_NAME + " not found" + "]";
                }
            }
            throw new ServiceFacadeInitializationException("loadInstanceFromConfigurationFile() failed " + fileName, e);
        }
    }

    public static void resetConfiguration() {
        AppServiceFacade.instance = null;
    }

    public void setProvider(AppServiceProvider provider) {
        AppServiceFacade.provider = provider;
    }

}
