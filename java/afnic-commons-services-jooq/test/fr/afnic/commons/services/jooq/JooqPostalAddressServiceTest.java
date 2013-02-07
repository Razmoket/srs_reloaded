package fr.afnic.commons.services.jooq;

import junit.framework.Test;
import fr.afnic.commons.services.contracts.postaladdress.PostalAddressServiceContractTest;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.facade.CustomAppServiceFacade;
import fr.afnic.commons.services.mock.MockDictionaryService;
import fr.afnic.commons.services.mock.MockUserService;
import fr.afnic.commons.services.provider.AppServiceProvider;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.utils.sql.pool.SqlDatabaseEnum;

public class JooqPostalAddressServiceTest extends PostalAddressServiceContractTest {

    public static Test suite() {
        return new JooqPostalAddressServiceTest();
    }

    @Override
    public AppServiceFacade createServiceFacadeToTest() throws Exception {

        final AppServiceProvider provider = new AppServiceProvider();
        provider.setPostalAddressService(new JooqPostalAddressService(SqlDatabaseEnum.Grc,
                                                                      TldServiceFacade.Fr));

        provider.setUserService(new MockUserService());
        provider.setDictionaryService(new MockDictionaryService());

        provider.setCustomerContactService(new JooqCustomerContactService(SqlDatabaseEnum.Grc,
                                                                          TldServiceFacade.Fr));

        CustomAppServiceFacade facade = new CustomAppServiceFacade();
        facade.setProvider(provider);
        AppServiceProvider.use(provider);

        AppServiceFacade.use(provider);
        return facade;
    }

}
