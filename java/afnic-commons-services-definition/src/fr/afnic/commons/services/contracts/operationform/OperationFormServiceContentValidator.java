/**
 * 
 */
package fr.afnic.commons.services.contracts.operationform;

import junit.framework.TestCase;
import fr.afnic.commons.beans.OperationForm;

/**
 * @author alaphilippe
 * 
 */
public class OperationFormServiceContentValidator {

    /**
     * Methode simple de mutualisation des tests sur le contenu d'un formulaire.
     * 
     * @param operationForm
     *            le formulaire a valider.
     */
    public void validateOperationFormContent(OperationForm operationForm) {
        TestCase.assertNotNull("Operation form is null", operationForm);
        TestCase.assertNotNull("Operation form Creation date is null", operationForm.getCreateDate());
        TestCase.assertNotNull("Operation form domain name is null", operationForm.getDomainName());
        TestCase.assertNotNull("Operation form identifier is null", operationForm.getOperationFormIdAsString());
        // TestCase.assertNotNull("",operationForm.getIsArchived());
        // TestCase.assertNotNull("",operationForm.getIsForced());
        TestCase.assertNotNull("Operation form name is null", operationForm.getOperation());
        TestCase.assertNotNull("Operation form registrar name is null", operationForm.getRegistrarName());
        // TestCase.assertNotNull("Operation form registrar comment is null", operationForm.getRegistrarComment());
        // TestCase.assertNotNull("Operation form submit source is null", operationForm.getSubmitSource());
        // TestCase.assertNotNull("Operation form version is null", operationForm.getVersion());

    }
}
