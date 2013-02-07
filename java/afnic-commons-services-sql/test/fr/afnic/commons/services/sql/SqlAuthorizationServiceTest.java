/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.services.sql;

import junit.framework.Test;
import fr.afnic.commons.services.contracts.authorization.AuthorizationServiceContractTest;
import fr.afnic.commons.services.facade.CustomAppServiceFacade;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.mock.MockProfileService;
import fr.afnic.utils.sql.SqlFacade;

public class SqlAuthorizationServiceTest extends AuthorizationServiceContractTest {

    public static Test suite() {
        return new SqlAuthorizationServiceTest();
    }

    @Override
    public AppServiceFacade createServiceFacadeToTest() throws Exception {
        CustomAppServiceFacade facade = new CustomAppServiceFacade();
        facade.setDictionaryService(new SqlDictionnaryService(SqlFacade.getNicopeFactory()));
        facade.setTicketService(new SqlTicketService(SqlFacade.getNicopeFactory()));
        facade.setWhoisContactService(new SqlWhoisContactService(SqlFacade.getNicopeFactory()));
        facade.setAuthorizationService(new SqlAuthorizationService(SqlFacade.getNicopeFactory()));
        facade.setRequestService(new SqlRequestService(SqlFacade.getNicopeFactory()));
        facade.setDomainService(new SqlDomainService(SqlFacade.getNicopeFactory()));
        facade.setUserService(new SqlUserService(SqlFacade.getBoaFactory()));
        facade.setProfileService(new MockProfileService());

        //facade.setDocumentService(new MockDocumentService());
        //facade.setUserService(new MockUserService());
        return facade;
    }
}
