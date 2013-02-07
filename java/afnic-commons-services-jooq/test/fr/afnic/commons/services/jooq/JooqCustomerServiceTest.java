package fr.afnic.commons.services.jooq;

import junit.framework.Test;
import fr.afnic.commons.services.contracts.customer.CustomerServiceContractTest;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.facade.CustomAppServiceFacade;
import fr.afnic.commons.services.mock.MockDictionaryService;
import fr.afnic.commons.services.mock.MockUserService;
import fr.afnic.commons.services.multitld.MultiTldBillingService;
import fr.afnic.commons.services.multitld.MultiTldCustomerContactService;
import fr.afnic.commons.services.multitld.MultiTldCustomerService;
import fr.afnic.commons.services.provider.AppServiceProvider;
import fr.afnic.commons.services.provider.TldServiceProvider;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.utils.sql.pool.SqlDatabaseEnum;

public class JooqCustomerServiceTest extends CustomerServiceContractTest {

    public static Test suite() {
        return new JooqCustomerServiceTest();
    }

    @Override
    public AppServiceFacade createServiceFacadeToTest() throws Exception {
        System.err.println("step1");
        final TldServiceProvider provider = new TldServiceProvider();

        provider.setPostalAddressService(new JooqPostalAddressService(SqlDatabaseEnum.Grc,
                                                                      TldServiceFacade.Fr));

        provider.setIdentityService(new JooqIdentityService(SqlDatabaseEnum.Grc,
                                                            TldServiceFacade.Fr));

        provider.setUserService(new MockUserService());
        provider.setDictionaryService(new MockDictionaryService());

        provider.setCustomerContactService(new JooqCustomerContactService(SqlDatabaseEnum.Grc,
                                                                          TldServiceFacade.Fr));

        provider.setContactDetailsService(new JooqContactDetailsService(SqlDatabaseEnum.Grc,
                                                                        TldServiceFacade.Fr));

        provider.setCustomerService(new JooqCustomerService(SqlDatabaseEnum.Grc,
                                                            TldServiceFacade.Fr));

        CustomAppServiceFacade facade = new CustomAppServiceFacade();
        facade.setProvider(this.getProvider());

        TldServiceFacade.Fr.setServiceProvider(provider);

        System.err.println("step3");
        return facade;
    }

    private AppServiceProvider getProvider() {
        AppServiceProvider provider = new AppServiceProvider();
        provider.setCustomerService(new MultiTldCustomerService());
        provider.setCustomerContactService(new MultiTldCustomerContactService());
        provider.setBillingService(new MultiTldBillingService());

        AppServiceProvider.use(provider);
        return provider;
    }
}
