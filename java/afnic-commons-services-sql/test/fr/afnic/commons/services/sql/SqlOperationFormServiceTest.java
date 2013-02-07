/*
 * $Id: $ $Revision: $ $Author: $
 */

package fr.afnic.commons.services.sql;

import junit.framework.Test;
import fr.afnic.commons.services.contracts.operationform.OperationFormServiceContractTest;
import fr.afnic.commons.services.facade.CustomAppServiceFacade;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.mock.MockUserService;
import fr.afnic.utils.sql.SqlFacade;

/**
 * Test de l'implémentation sql de OperationFormService
 * 
 * @author ginguene
 * 
 */
public class SqlOperationFormServiceTest extends OperationFormServiceContractTest {

    public static Test suite() {
        return new SqlOperationFormServiceTest();
    }

    @Override
    public AppServiceFacade createServiceFacadeToTest() throws Exception {
        final CustomAppServiceFacade facade = new CustomAppServiceFacade();
        facade.setDictionaryService(new SqlDictionnaryService(SqlFacade.getNicopeFactory()));
        facade.setTicketService(new SqlTicketService(SqlFacade.getNicopeFactory()));
        facade.setWhoisContactService(new SqlWhoisContactService(SqlFacade.getNicopeFactory()));
        facade.setDomainService(new SqlDomainService(SqlFacade.getNicopeFactory()));
        facade.setOperationFormService(new SqlOperationFormService(SqlFacade.getNicopeFactory()));
        // facade.setMailService(new SmtpMailService());
        facade.setUserService(new MockUserService());
        return facade;
    }

}