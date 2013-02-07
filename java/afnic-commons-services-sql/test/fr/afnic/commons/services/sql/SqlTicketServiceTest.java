package fr.afnic.commons.services.sql;

import junit.framework.Test;
import fr.afnic.commons.services.contracts.ticket.TicketServiceContractTest;
import fr.afnic.commons.services.facade.CustomAppServiceFacade;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.mock.MockUserService;
import fr.afnic.utils.sql.SqlFacade;

/**
 * Test de l'implémentation sql de TicketService
 * 
 * @author ginguene
 * 
 */
public class SqlTicketServiceTest extends TicketServiceContractTest {

    public static Test suite() {
        return new SqlTicketServiceTest();
    }

    @Override
    public AppServiceFacade createServiceFacadeToTest() throws Exception {
        final CustomAppServiceFacade facade = new CustomAppServiceFacade();
        facade.setDictionaryService(new SqlDictionnaryService(SqlFacade.getNicopeFactory()));
        facade.setTicketService(new SqlTicketService(SqlFacade.getNicopeFactory()));
        facade.setWhoisContactService(new SqlWhoisContactService(SqlFacade.getNicopeFactory()));
        facade.setAuthorizationService(new SqlAuthorizationService(SqlFacade.getNicopeFactory()));
        facade.setDomainService(new SqlDomainService(SqlFacade.getNicopeFactory()));
        facade.setUserService(new MockUserService());
        return facade;
    }

}
