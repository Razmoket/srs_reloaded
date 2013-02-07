package fr.afnic.commons.services.sql;

import junit.framework.Test;
import fr.afnic.commons.services.contracts.user.UserServiceContractTest;
import fr.afnic.commons.services.facade.MockAppServiceFacade;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.utils.sql.SqlFacade;

public class SqlUserServiceTest extends UserServiceContractTest {

    public static Test suite() {
        return new SqlUserServiceTest();
    }

    @Override
    public AppServiceFacade createServiceFacadeToTest() throws Exception {
        MockAppServiceFacade facade = new MockAppServiceFacade();
        facade.setUserService(new SqlUserService(SqlFacade.getBoaFactory()));
        facade.setProfileService(new SqlProfileService(SqlFacade.getBoaFactory()));
        return facade;
    }

}
