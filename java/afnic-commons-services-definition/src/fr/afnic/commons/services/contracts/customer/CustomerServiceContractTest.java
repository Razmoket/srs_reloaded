package fr.afnic.commons.services.contracts.customer;

import fr.afnic.commons.test.ServiceFacadeContractTest;

/**
 * Contrat que doit respecter toute implémentation du service ICustomerService
 * 
 * @author ginguene
 * 
 */
public abstract class CustomerServiceContractTest extends ServiceFacadeContractTest {

    @Override
    public Class<?>[] getSubContractTests() {
        return new Class<?>[] {
                               // CustomerServiceCreateCustomerTest.class,
                               //CustomerServiceSearchCustomerTest.class
                               // , CustomerServiceGetCustomerTest.class
                               /* ,CustomerServiceDeleteCustomerTest.class */
                               CustomerServiceUpdateCustomerTest.class
        // ,CustomerServiceStartDomainPortfolioExportTest.class
        };
    }
}
