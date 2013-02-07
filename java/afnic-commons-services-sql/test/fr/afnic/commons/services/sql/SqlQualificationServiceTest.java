package fr.afnic.commons.services.sql;

import junit.framework.Test;
import fr.afnic.commons.services.contracts.qualification.QualificationServiceContractTest;
import fr.afnic.commons.services.facade.MockAppServiceFacade;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.utils.sql.SqlFacade;

public class SqlQualificationServiceTest extends QualificationServiceContractTest {

    public static Test suite() {
        return new SqlQualificationServiceTest();
    }

    @Override
    public AppServiceFacade createServiceFacadeToTest() throws Exception {
        MockAppServiceFacade facade = new MockAppServiceFacade();
        facade.setQualificationService(new SqlQualificationService(SqlFacade.getBoaFactory()));
        facade.setOperationService(new SqlOperationService(SqlFacade.getBoaFactory()));
        facade.setWhoisContactService(new SqlWhoisContactService(SqlFacade.getNicopeFactory()));
        facade.setUserService(new SqlUserService(SqlFacade.getBoaFactory()));
        return facade;
    }
}
