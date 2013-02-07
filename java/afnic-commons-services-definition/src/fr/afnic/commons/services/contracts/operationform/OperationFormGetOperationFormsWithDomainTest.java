/**
 * 
 */
package fr.afnic.commons.services.contracts.operationform;

import java.util.List;

import junit.framework.TestCase;

import org.junit.Test;

import fr.afnic.commons.beans.OperationForm;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.facade.exception.ServiceFacadeException;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.test.generator.DomainGenerator;
import fr.afnic.commons.test.generator.UserGenerator;
import fr.afnic.commons.test.generator.exception.GeneratorException;

/**
 * @author alaphilippe
 * 
 */
public class OperationFormGetOperationFormsWithDomainTest {

    private final OperationFormServiceContentValidator validator = new OperationFormServiceContentValidator();

    /**
     * Test l'accès au contenu d'un formulaire avec un identifiant de domain.<br/>
     * 1) Création d'un domaine qui n'existe pas en base avec un format valide permettant de récupérer.<br/>
     * 2) Appel du getOperationFormsWithDomain du DAO pour récupérer le bean.<br/>
     * 3) valider le contenu du bean.
     * 
     * @throws GeneratorException
     * @throws ServiceFacadeException
     * @throws ServiceException
     */
    @Test
    public void testGetOperationFormsWithDomain() throws GeneratorException, ServiceException, ServiceFacadeException {

        // 1) Création d'un domaine qu n'existe pas en base avec un format valide permettant de récupérer.
        String createNewDomain = DomainGenerator.createNewDomain("operation-form-getformwithid");
        TestCase.assertNotNull("created domain identifier is null", createNewDomain);

        // 2) Appel du getOperationFormsWithDomain du DAO pour récupérer le bean.
        List<OperationForm> operationFormsWithDomain = AppServiceFacade.getOperationFormService().getOperationFormsWithDomain(createNewDomain, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        TestCase.assertNotNull("GetOperationFormWithId returns a null Object.", operationFormsWithDomain);

        // 3) valider le contenu du bean.
        for (OperationForm operationForm : operationFormsWithDomain) {
            this.validator.validateOperationFormContent(operationForm);
        }

    }
}
