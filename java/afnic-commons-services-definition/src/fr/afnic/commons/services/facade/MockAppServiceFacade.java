/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.services.facade;

import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.mock.MockApplicationService;
import fr.afnic.commons.services.mock.MockAuthorizationPreliminaryExamService;
import fr.afnic.commons.services.mock.MockAuthorizationRequestService;
import fr.afnic.commons.services.mock.MockAuthorizationService;
import fr.afnic.commons.services.mock.MockCustomerContactService;
import fr.afnic.commons.services.mock.MockCustomerService;
import fr.afnic.commons.services.mock.MockDictionaryService;
import fr.afnic.commons.services.mock.MockDocumentService;
import fr.afnic.commons.services.mock.MockDomainPortfolioService;
import fr.afnic.commons.services.mock.MockDomainService;
import fr.afnic.commons.services.mock.MockEmailService;
import fr.afnic.commons.services.mock.MockEppService;
import fr.afnic.commons.services.mock.MockFaxService;
import fr.afnic.commons.services.mock.MockLoggerService;
import fr.afnic.commons.services.mock.MockOldDocumentService;
import fr.afnic.commons.services.mock.MockOperationFormService;
import fr.afnic.commons.services.mock.MockOperationService;
import fr.afnic.commons.services.mock.MockProfileService;
import fr.afnic.commons.services.mock.MockQualificationService;
import fr.afnic.commons.services.mock.MockQualityService;
import fr.afnic.commons.services.mock.MockRequestService;
import fr.afnic.commons.services.mock.MockStatisticService;
import fr.afnic.commons.services.mock.MockTicketService;
import fr.afnic.commons.services.mock.MockTradeService;
import fr.afnic.commons.services.mock.MockUserService;
import fr.afnic.commons.services.mock.MockWhoisContactService;

public class MockAppServiceFacade extends CustomAppServiceFacade {

    @edu.umd.cs.findbugs.annotations.SuppressWarnings(value = "ST_WRITE_TO_STATIC_FROM_INSTANCE_METHOD", justification = "staticLoggerService doit etre static.")
    public MockAppServiceFacade() throws ServiceException {
        AppServiceFacade.staticLoggerService = new MockLoggerService();
        this.setWhoisContactService(new MockWhoisContactService());
        this.setDomainService(new MockDomainService());
        this.setUserService(new MockUserService());
        this.setTicketService(new MockTicketService());
        this.setEmailService(new MockEmailService());
        this.setDictionaryService(new MockDictionaryService());
        this.setOldDocumentService(new MockOldDocumentService());
        this.setFaxService(new MockFaxService());
        this.setAuthorizationService(new MockAuthorizationService());
        this.setAuthorizationRequestService(new MockAuthorizationRequestService());
        this.setAuthorizationPreliminaryExamService(new MockAuthorizationPreliminaryExamService());

        this.setTradeService(new MockTradeService());
        this.setOperationFormService(new MockOperationFormService());
        this.setRequestService(new MockRequestService());
        this.setEppService(new MockEppService());
        this.setStatisticService(new MockStatisticService());
        this.setApplicationService(new MockApplicationService());
        this.setCustomerContactService(new MockCustomerContactService());
        this.setCustomerService(new MockCustomerService());
        this.setProfileService(new MockProfileService());

        this.setOperationService(new MockOperationService());
        this.setQualificationService(new MockQualificationService());
        this.setDomainPortfolioService(new MockDomainPortfolioService());
        this.setDocumentService(new MockDocumentService());

        this.setQualityService(new MockQualityService());
    }
}
