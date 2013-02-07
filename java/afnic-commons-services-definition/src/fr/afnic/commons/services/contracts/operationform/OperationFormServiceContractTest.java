/**
 * 
 */
package fr.afnic.commons.services.contracts.operationform;

import fr.afnic.commons.test.ServiceFacadeContractTest;

/**
 * Initialise la facade à partir de l'implement de IOperationFormService.<br>
 * 
 * @author alaphilippe
 * 
 */
public abstract class OperationFormServiceContractTest extends ServiceFacadeContractTest {

    /**
     * @see fr.afnic.commons.test.ServiceContractTest#getSubContractTests()
     */
    @Override
    public Class<?>[] getSubContractTests() {
        return new Class<?>[] { OperationFormGetOperationFormInitialContentTest.class,
                                OperationFormGetOperationFormsWithDomainTest.class,
                                OperationFormGetOperationFormWithIdTest.class,
                                OperationFormGetOperationFormWithTicketTest.class,
                                OperationFormSearchOperationFormsTest.class,
                                OperationFormCloseOperationFormTest.class, };
    }

}
