/**
 * 
 */
package fr.afnic.commons.services.contracts.operationform;

import java.util.List;

import junit.framework.TestCase;

import org.junit.Test;

import fr.afnic.commons.beans.OperationForm;
import fr.afnic.commons.beans.OperationFormVersion;
import fr.afnic.commons.beans.Ticket;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.facade.exception.ServiceFacadeException;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.test.generator.DomainGenerator;
import fr.afnic.commons.test.generator.UserGenerator;
import fr.afnic.commons.test.generator.exception.GeneratorException;

/**
 * @author alaphilippe
 */
public class OperationFormGetOperationFormWithTicketTest {

    private final OperationFormServiceContentValidator validator = new OperationFormServiceContentValidator();

    /**
     * Test l'accès au contenu d'un formulaire avec un identifiant de ticket.<br/>
     * 1) Création d'un domaine qui n'existe pas en base avec un format valide permettant de récupérer.<br/>
     * 2) Récupération du ticket courant sur le domaine.<br/>
     * 3) Appel du getOperationFormWithTicket du DAO pour récupérer le bean.<br/>
     * 4) valider le contenu du bean.
     * 
     * @throws DaoException
     * @throws GeneratorException
     * @throws ServiceFacadeException
     * @throws ServiceException
     */
    @Test
    public void testGetOperationFormWithTicket() throws GeneratorException, ServiceException, ServiceFacadeException {

        // 1) Création d'un domaine qu n'existe pas en base avec un format valide permettant de récupérer.
        String createNewDomain = DomainGenerator.createNewDomain("operation-form-getformwithid");
        TestCase.assertNotNull("created domain identifier is null", createNewDomain);

        // 2) Récupération du ticket courant sur le domaine pour avoir l'identifiant du ticket.
        List<Ticket> ticketsWithDomain = AppServiceFacade.getTicketService().getTicketsWithDomain(createNewDomain, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        // Le domaine vient d'etre créé il n'y a donc qu'un seul ticket.
        TestCase.assertEquals("The domain returns more than one ticket.", 1, ticketsWithDomain.size());
        String ticketId = ticketsWithDomain.get(0).getId();
        TestCase.assertNotNull("OpertationForm identifier returned by ticket is null", ticketId);

        // 3) Appel du getOperationFormWithTicket du DAO pour récupérer le bean.
        OperationForm operationFormWithId = AppServiceFacade.getOperationFormService().getOperationFormWithTicket(ticketId, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        TestCase.assertNotNull("GetOperationFormWithId returns a null Object.", operationFormWithId);

        // 4) valider le contenu du bean.
        this.validator.validateOperationFormContent(operationFormWithId);

        // Les formulaires créés doivent l'etre dans la dernière version
        TestCase.assertEquals("Operation form version ", OperationFormVersion.LAST, operationFormWithId.getVersion());

    }
}
