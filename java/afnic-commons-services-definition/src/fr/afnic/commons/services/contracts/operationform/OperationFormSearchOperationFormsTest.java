/**
 * 
 */
package fr.afnic.commons.services.contracts.operationform;

import java.util.List;

import junit.framework.TestCase;

import org.junit.Ignore;
import org.junit.Test;

import fr.afnic.commons.beans.OperationForm;
import fr.afnic.commons.beans.Ticket;
import fr.afnic.commons.beans.WhoisContact;
import fr.afnic.commons.beans.search.form.FormSearchCriteria;
import fr.afnic.commons.beans.validatable.OperationFormId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.exception.invalidformat.InvalidDomainNameException;
import fr.afnic.commons.services.exception.invalidformat.InvalidNichandleException;
import fr.afnic.commons.services.exception.invalidformat.InvalidTicketIdException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.facade.exception.ServiceFacadeException;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.test.generator.ContactGenerator;
import fr.afnic.commons.test.generator.DomainGenerator;
import fr.afnic.commons.test.generator.UserGenerator;
import fr.afnic.commons.test.generator.exception.GeneratorException;

/**
 * @author alaphilippe
 */
public class OperationFormSearchOperationFormsTest {

    private static final String TEST_REGISTRAR_CODE = "TEST";
    private static final String TEST_REGISTRAR_NAME = "AFNIC registry";

    /**
     * Appel du service de recherche avec un objet de critère <code>null</code>. <br/>
     * 1/ preparation de la recherche à effectuer.<br/>
     * 2/ appel du service avec les criteres de recherche.<br/>
     * 3/ validation du contenu rectourné.<br/>
     * 
     * @throws ServiceFacadeException
     * @throws ServiceException
     * @throws DaoException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSearchOperationFormWithNullObject() throws ServiceException, ServiceFacadeException {
        // 1/ preparation de l'objet de critere de recherche.
        FormSearchCriteria criteria = null;

        List<OperationForm> searchOperationForm = this.searchOperationForm(criteria);
        // Appel du service.
        TestCase.assertTrue("Search is not empty with a nulled criteria", searchOperationForm.isEmpty());
    }

    /**
     * Appel du service de recherche avec un objet de recherche instancié dont le contenu est <code>null</code>.
     * 
     * @throws ServiceFacadeException
     * @throws ServiceException
     * @throws DaoException
     */
    @Test
    public void testSearchOperationFormWithNullContent() throws ServiceException, ServiceFacadeException {
        // 1/ preparation de l'objet de critere de recherche.
        FormSearchCriteria criteria = new FormSearchCriteria();
        // 2/ appel du service de recherche.
        List<OperationForm> searchResult = AppServiceFacade.getOperationFormService().searchOperationForms(criteria, UserGenerator.getRootUserId(), TldServiceFacade.Fr);

        // 3/ validation du contenu retourné.
        TestCase.assertTrue("Search is not null with a nulled criteria's content", searchResult.isEmpty());
    }

    /**
     * Appel du service de recherche avec un objet de recherche instancié dont le contenu est définit à <code>null</code>.
     * 
     * @throws ServiceFacadeException
     * @throws ServiceException
     * @throws DaoException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSearchOperationFormWithContentSettedToNull() throws ServiceException, ServiceFacadeException {
        // 1/ preparation de l'objet de critere de recherche.
        FormSearchCriteria criteria = new FormSearchCriteria();

        // 2/ appel du service de recherche.
        List<OperationForm> searchResult = AppServiceFacade.getOperationFormService().searchOperationForms(criteria, UserGenerator.getRootUserId(), TldServiceFacade.Fr);

        // 3/ validation du contenu retourné.
        TestCase.assertTrue("Search is not null with a setted to null criteria's content", searchResult.isEmpty());
    }

    private final OperationFormServiceContentValidator validator = new OperationFormServiceContentValidator();

    /**
     * Méthode de mutualisation des appels au service de recherche des formulaires.<br>
     * Cette méthode effectue: <br>
     * <ul>
     * <li>2/ appel du service avec les criteres de recherche.</li>
     * <li>3/ validation simple du contenu rectourné.</li>
     * </ul>
     * Puis retourne la liste des formulaires trouvés pour validation plus poussée par la méthode appelante.<br>
     * 
     * @param criteria
     *            L'objet contenant les critères de recherches.
     * @return la <code>list</code> des <code>OperationForm</code> trouvés avec les critères fournis.
     * @throws ServiceFacadeException
     * @throws ServiceException
     * @throws DaoException
     *             dans la cas où une erreur aurait été rencontrée.
     */
    private List<OperationForm> searchOperationForm(FormSearchCriteria criteria) throws ServiceException, ServiceFacadeException {
        // 2/ appel du service de recherche.
        List<OperationForm> searchResult = AppServiceFacade.getOperationFormService().searchOperationForms(criteria, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        TestCase.assertNotNull("Search returns a null object.", searchResult);
        // 3/ validation du contenu retourné.
        for (OperationForm op : searchResult) {
            this.validator.validateOperationFormContent(op);
        }
        return searchResult;
    }

    /**
     * Methode utilitaire de création de l'objet de criteraire de recherche.
     * 
     * @param id
     * @param domaineName
     * @param ticketId
     * @param adminHandle
     * @param holderHandle
     * @param registrarCode
     * @param registrarName
     * @return l'objet contenant les critères de recherche.
     */
    private FormSearchCriteria getOperationFormSearchCriteria(OperationFormId id, String domaineName, String ticketId, String adminHandle, String holderHandle, String registrarName) {
        FormSearchCriteria criteria = new FormSearchCriteria();

        if (id != null) {
            criteria.setOperationFormId(id.getValue());
        }

        if (domaineName != null) {
            criteria.setDomainName(domaineName);
        }

        if (ticketId != null) {
            criteria.setTicketId(ticketId);
        }

        if (adminHandle != null) {
            criteria.setAdminHandle(adminHandle);
        }

        if (holderHandle != null) {
            criteria.setHolderHandle(holderHandle);
        }

        if (registrarName != null) {
            criteria.setRegistrar(registrarName);
        }

        return criteria;
    }

    /**
     * @see getOperationFormSearchCriteria
     */
    private FormSearchCriteria getOperationFormSearchCriteriaFormId(OperationFormId formId) {
        return this.getOperationFormSearchCriteria(formId, null, null, null, null, null);
    }

    /**
     * @see getOperationFormSearchCriteria
     */
    private FormSearchCriteria getOperationFormSearchCriteriaDomainName(String domainName) {
        return this.getOperationFormSearchCriteria(null, domainName, null, null, null, null);
    }

    /**
     * @see getOperationFormSearchCriteria
     */
    private FormSearchCriteria getOperationFormSearchCriteriaTicketId(String ticketId) {
        return this.getOperationFormSearchCriteria(null, null, ticketId, null, null, null);
    }

    /**
     * @see getOperationFormSearchCriteria
     */
    private FormSearchCriteria getOperationFormSearchCriteriaAdminHandle(String adminHandle) {
        return this.getOperationFormSearchCriteria(null, null, null, adminHandle, null, null);
    }

    /**
     * @see getOperationFormSearchCriteria
     */
    private FormSearchCriteria getOperationFormSearchCriteriaHolderHandle(String holderHandle) {
        return this.getOperationFormSearchCriteria(null, null, null, null, holderHandle, null);
    }

    /**
     * @see getOperationFormSearchCriteria
     */
    private FormSearchCriteria getOperationFormSearchCriteriaRegistrarName(String registrarName) {
        return this.getOperationFormSearchCriteria(null, null, null, null, null, registrarName);
    }

    /**
     * Appel du service de recherche avec un objet de recherche instancié avec un identifiant de formulaire.
     * 
     * @throws GeneratorException
     * @throws DaoException
     * @throws GeneratorException
     * @throws ServiceFacadeException
     * @throws ServiceException
     */
    @Test
    public void testSearchOperationFormWithFormId() throws GeneratorException, ServiceException, ServiceFacadeException {
        String createNewDomain = DomainGenerator.createNewDomain("operation-form-searchid");
        TestCase.assertNotNull("created domain identifier is null", createNewDomain);

        // Récupération du ticket courant sur le domaine pour avoir l'identifiant du formulaire.
        List<Ticket> ticketsWithDomain = AppServiceFacade.getTicketService().getTicketsWithDomain(createNewDomain, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        // Le domaine vient d'etre créé il n'y a donc qu'un seul ticket.
        TestCase.assertEquals("The domain returns more than one ticket.", 1, ticketsWithDomain.size());
        OperationFormId idForm = ticketsWithDomain.get(0).getOperationFormId();

        // 1/ preparation de l'objet de critere de recherche.
        FormSearchCriteria criteria = this.getOperationFormSearchCriteriaFormId(idForm);

        // validation simple du contenu.
        List<OperationForm> searchOperationForm = this.searchOperationForm(criteria);
        TestCase.assertTrue("The search should not be empty.", !searchOperationForm.isEmpty());
        boolean found = false;
        for (OperationForm op : searchOperationForm) {
            found = createNewDomain.equals(op.getDomainName());
            if (found) {
                break;
            }
        }
        TestCase.assertTrue("The search result is not the correct domain name expected [" + createNewDomain + "]", found);
    }

    /**
     * Appel du service de recherche avec un objet de recherche instancié avec le nom de domaine.
     * 
     * @throws GeneratorException
     * @throws DaoException
     * @throws GeneratorException
     * @throws ServiceFacadeException
     * @throws ServiceException
     */
    @Test
    public void testSearchOperationFormWithDomainName() throws GeneratorException, ServiceException, ServiceFacadeException {
        String createNewDomain = DomainGenerator.createNewDomain("operation-form-searchdomain");

        // 1/ preparation de l'objet de critere de recherche.
        FormSearchCriteria criteria = this.getOperationFormSearchCriteriaDomainName(createNewDomain);

        // validation simple du contenu.
        List<OperationForm> searchOperationForm = this.searchOperationForm(criteria);
        TestCase.assertTrue("The search should not be empty.", !searchOperationForm.isEmpty());
        boolean found = false;
        for (OperationForm op : searchOperationForm) {
            found = createNewDomain.equals(op.getDomainName());
            if (found) {
                break;
            }
        }
        TestCase.assertTrue("The search result is not the correct domain name " + createNewDomain, found);
    }

    /**
     * Appel du service de recherche avec un objet de recherche instancié avec le nom de domaine "commencant par".
     * 
     * @throws GeneratorException
     * @throws DaoException
     * @throws GeneratorException
     * @throws ServiceFacadeException
     * @throws ServiceException
     */
    @Test
    public void testSearchOperationFormWithMatchingDomainName() throws GeneratorException, ServiceException, ServiceFacadeException {
        // Le nomd de domaine contient volontairement une majuscule pour vérifier que cela n'empeche pas le service de fonctionner
        String domainName = "testsearchformwithmatchingdomainname.fr";
        fr.afnic.commons.beans.domain.Domain createNewDomain = DomainGenerator.getOrCreateDomain(domainName);

        // recherche normale avec le * à la fin et plus de 5 caracteres
        // 1/ preparation de l'objet de critere de recherche.
        FormSearchCriteria criteria = this.getOperationFormSearchCriteriaDomainName(domainName.substring(0, 20) + "*");

        // validation simple du contenu.
        List<OperationForm> searchOperationForm = this.searchOperationForm(criteria);

        TestCase.assertTrue("The search should not be empty.", !searchOperationForm.isEmpty());
        boolean found = false;
        for (OperationForm op : searchOperationForm) {
            found = domainName.equalsIgnoreCase(op.getDomainName());
            if (found) {
                break;
            }
        }
        TestCase.assertTrue("The search result is not the correct domain name " + createNewDomain.getName(), found);
    }

    /**
     * Appel du service de recherche avec un objet de recherche instancié avec le nom de domaine "commencant par". Erreur attendue si moins de 5
     * caractères.
     * 
     * @throws GeneratorException
     * @throws DaoException
     * @throws GeneratorException
     * @throws ServiceFacadeException
     * @throws ServiceException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSearchOperationFormWithErrorMatchingDomainName() throws GeneratorException, ServiceException, ServiceFacadeException {
        String ndd = "operation-form-searchdomain";
        DomainGenerator.createNewDomain(ndd);

        // recherche normale avec le * à la fin et plus de 5 caracteres
        // 1/ preparation de l'objet de critere de recherche.
        FormSearchCriteria criteriaBis = this.getOperationFormSearchCriteriaDomainName(ndd.substring(0, 3) + "*");

        // validation simple du contenu.
        this.searchOperationForm(criteriaBis);
    }

    /**
     * Appel du service de recherche avec un objet de recherche instancié avec le nom de domaine.
     * 
     * @throws GeneratorException
     * @throws DaoException
     * @throws GeneratorException
     * @throws ServiceFacadeException
     * @throws ServiceException
     */
    @Test
    public void testSearchOperationFormWithDomainNameMultipleResult() throws GeneratorException, ServiceException, ServiceFacadeException {
        String createNewDomain = DomainGenerator.createNewDomain("operation-form-searchmultidomain");
        for (int i = 0; i < 2; i++) {
            AppServiceFacade.getTicketService().createTradeTicket(createNewDomain, ContactGenerator.createCorporateEntityContact().getHandle(),
                                                                  UserGenerator.ROOT_LOGIN, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
            AppServiceFacade.getDomainService().approveTradeDomain(createNewDomain, UserGenerator.ROOT_LOGIN, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        }

        // 1/ preparation de l'objet de critere de recherche.
        FormSearchCriteria criteria = this.getOperationFormSearchCriteriaDomainName(createNewDomain);

        // validation simple du contenu.
        List<OperationForm> searchOperationForm = this.searchOperationForm(criteria);
        TestCase.assertTrue("The search should return more than one result.", searchOperationForm.size() > 1);
        boolean found = false;
        for (OperationForm op : searchOperationForm) {
            found = createNewDomain.equals(op.getDomainName());
            if (found) {
                break;
            }
        }
        TestCase.assertTrue("The search result is not the correct domain name " + createNewDomain, found);
    }

    /**
     * Appel du service de recherche avec un objet de recherche instancié avec un nom de domaine invalide.
     * 
     * @throws ServiceFacadeException
     * @throws ServiceException
     * @throws DaoException
     */
    @Test
    public void testSearchOperationFormWithInvalidDomainName() throws ServiceException, ServiceFacadeException {
        String ndd = "turlututu";

        // 1/ preparation de l'objet de critere de recherche.
        FormSearchCriteria criteria = this.getOperationFormSearchCriteriaDomainName(ndd);

        try {
            this.searchOperationForm(criteria);
            TestCase.fail("No exception thrown");
        } catch (InvalidDomainNameException e) {
            TestCase.assertEquals("'" + ndd + "' is not a valid domain name", e.getMessage());

        }
    }

    /**
     * Appel du service de recherche avec un objet de recherche instancié avec un identifiant de ticket.
     * 
     * @throws GeneratorException
     * @throws ServiceFacadeException
     * @throws ServiceException
     */
    @Test
    public void testSearchOperationFormWithTicketId() throws GeneratorException, ServiceException, ServiceFacadeException {
        String createNewDomain = DomainGenerator.createNewDomain("operation-form-searchticketid");
        TestCase.assertNotNull("created domain identifier is null", createNewDomain);
        // Récupération du ticket courant sur le domaine pour avoir l'identifiant du ticket.
        List<Ticket> ticketsWithDomain = AppServiceFacade.getTicketService().getTicketsWithDomain(createNewDomain, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        // Le domaine vient d'etre créé il n'y a donc qu'un seul ticket.
        TestCase.assertEquals("The domain returns more than one ticket.", 1, ticketsWithDomain.size());
        String ticketId = ticketsWithDomain.get(0).getId();
        TestCase.assertNotNull("OpertationForm identifier returned by ticket is null", ticketId);

        // 1/ preparation de l'objet de critere de recherche.
        FormSearchCriteria criteria = this.getOperationFormSearchCriteriaTicketId(ticketId);

        // validation simple du contenu.
        List<OperationForm> searchOperationForm = this.searchOperationForm(criteria);
        TestCase.assertTrue("The search should not be empty.", !searchOperationForm.isEmpty());
        boolean found = false;
        for (OperationForm op : searchOperationForm) {
            found = createNewDomain.equals(op.getDomainName());
            if (found) {
                break;
            }
        }
        TestCase.assertTrue("The search result is not the correct domain name " + createNewDomain, found);
    }

    /**
     * Appel du service de recherche avec un objet de recherche instancié avec un identifiant de ticket invalide.
     * 
     * @throws GeneratorException
     * @throws ServiceFacadeException
     * @throws ServiceException
     */
    @Test(expected = InvalidTicketIdException.class)
    public void testSearchOperationFormWithInvalidTicketId() throws GeneratorException, ServiceException, ServiceFacadeException {
        String ticketId = "turlututu";

        // 1/ preparation de l'objet de critere de recherche.
        FormSearchCriteria criteria = this.getOperationFormSearchCriteriaTicketId(ticketId);

        // validation simple du contenu.
        this.searchOperationForm(criteria);
    }

    /**
     * Appel du service de recherche avec un objet de recherche instancié avec le nichandle du contact admin.
     * 
     * @throws GeneratorException
     * @throws ServiceException
     */
    @Test
    public void testSearchOperationFormWithAdminHandle() throws GeneratorException, ServiceException {
        WhoisContact admin = ContactGenerator.createCorporateEntityContact();
        DomainGenerator.createNewDomainWithAdmin("operation-form-searchadminhandle", admin);
        String adminHandle = admin.getHandle();
        // 1/ preparation de l'objet de critere de recherche.
        FormSearchCriteria criteria = this.getOperationFormSearchCriteriaAdminHandle(adminHandle);

        // validation simple du contenu.
        List<OperationForm> searchOperationForm = this.searchOperationForm(criteria);
        TestCase.assertTrue("The search should not be empty.", !searchOperationForm.isEmpty());

        boolean found = false;
        for (OperationForm op : searchOperationForm) {
            found = adminHandle.equals(op.getAdminHandle());
            if (found) {
                break;
            }
        }
        TestCase.assertTrue("The search result is not the correct admin handle " + adminHandle, found);
    }

    /**
     * Appel du service de recherche avec un objet de recherche instancié avec le nichandle du contact admin. Ce contact admin doit retourner
     * plusieurs resultats.
     * 
     * @throws GeneratorException
     * @throws ServiceException
     */
    @Test
    public void testSearchOperationFormWithAdminHandleMultipleResult() throws GeneratorException, ServiceException {
        WhoisContact admin = ContactGenerator.createCorporateEntityContact();

        for (int i = 0; i < 2; i++) {
            DomainGenerator.createNewDomainWithAdmin("operation-form-searchadminhandle", admin);
        }

        String adminHandle = admin.getHandle();
        // 1/ preparation de l'objet de critere de recherche.
        FormSearchCriteria criteria = this.getOperationFormSearchCriteriaAdminHandle(adminHandle);

        // validation simple du contenu.
        List<OperationForm> searchOperationForm = this.searchOperationForm(criteria);
        TestCase.assertTrue("The search should return more than one result.", searchOperationForm.size() > 1);
        boolean found = false;
        for (OperationForm op : searchOperationForm) {
            found = adminHandle.equals(op.getAdminHandle());
            if (found) {
                break;
            }
        }
        TestCase.assertTrue("The search result is not the correct admin handle " + adminHandle, found);
    }

    /**
     * Appel du service de recherche avec un objet de recherche instancié avec un nichandle invalide du contact admin.
     * 
     * @throws GeneratorException
     * @throws ServiceFacadeException
     * @throws ServiceException
     */
    @Test(expected = ServiceException.class)
    public void testSearchOperationFormWithInvalidAdminHandle() throws GeneratorException, ServiceException, ServiceFacadeException {
        String adminHandle = "turlututu";
        // 1/ preparation de l'objet de critere de recherche.
        FormSearchCriteria criteria = this.getOperationFormSearchCriteriaAdminHandle(adminHandle);

        // validation simple du contenu.
        this.searchOperationForm(criteria);
    }

    /**
     * Appel du service de recherche avec un objet de recherche instancié avec le nichandle du titulaire.
     * 
     * @throws GeneratorException
     * @throws ServiceException
     */
    @Test
    public void testSearchOperationFormWithHolderHandle() throws GeneratorException, ServiceException {
        WhoisContact holder = ContactGenerator.createCorporateEntityContact();
        String holderHandle = holder.getHandle();
        DomainGenerator.createNewDomainWithHolder("operation-form-searchholderhandle", holder);
        // 1/ preparation de l'objet de critere de recherche.
        FormSearchCriteria criteria = this.getOperationFormSearchCriteriaHolderHandle(holderHandle);

        // validation simple du contenu.
        List<OperationForm> searchOperationForm = this.searchOperationForm(criteria);
        TestCase.assertTrue("The search should not be empty.", !searchOperationForm.isEmpty());

        boolean found = false;
        for (OperationForm op : searchOperationForm) {
            found = holderHandle.equals(op.getHolderHandle());
            if (found) {
                break;
            }
        }
        TestCase.assertTrue("The search result is not the correct holder handle " + holderHandle, found);
    }

    /**
     * Appel du service de recherche avec un objet de recherche instancié avec le nichandle du contact titulaire.<br/>
     * Ce contact titulaire doit retourner plusieurs resultats.
     * 
     * @throws GeneratorException
     * @throws ServiceException
     */
    @Test
    public void testSearchOperationFormWithHolderHandleMultipleResult() throws GeneratorException, ServiceException {
        WhoisContact holder = ContactGenerator.createCorporateEntityContact();

        for (int i = 0; i < 2; i++) {
            DomainGenerator.createNewDomainWithHolder("operation-form-searchholderhandle", holder);
        }

        String holderHandle = holder.getHandle();

        // 1/ preparation de l'objet de critere de recherche.
        FormSearchCriteria criteria = this.getOperationFormSearchCriteriaHolderHandle(holderHandle);

        // validation simple du contenu.
        List<OperationForm> searchOperationForm = this.searchOperationForm(criteria);
        TestCase.assertTrue("The search should return more than one result.", searchOperationForm.size() > 1);

        boolean found = false;
        for (OperationForm op : searchOperationForm) {
            found = holderHandle.equals(op.getHolderHandle());
            if (found) {
                break;
            }
        }
        TestCase.assertTrue("The search result is not the correct holder handle " + holderHandle, found);
    }

    /**
     * Appel du service de recherche avec un objet de recherche instancié avec le nichandle du titulaire invalide.
     * 
     * @throws GeneratorException
     * @throws ServiceFacadeException
     * @throws ServiceException
     */
    @Test
    public void testSearchOperationFormWithInvalidHolderHandle() throws GeneratorException, ServiceException, ServiceFacadeException {
        String holderHandle = "turlututu";
        // 1/ preparation de l'objet de critere de recherche.
        FormSearchCriteria criteria = this.getOperationFormSearchCriteriaHolderHandle(holderHandle);

        // validation simple du contenu.
        try {
            this.searchOperationForm(criteria);
        } catch (InvalidNichandleException e) {
            TestCase.assertEquals("'" + holderHandle + "' is not a valid nicHandle.", e.getMessage());
        }
    }

    /**
     * Appel du service de recherche avec un objet de recherche instancié avec le nom du client BE.
     * 
     * @throws GeneratorException
     * @throws ServiceFacadeException
     * @throws ServiceException
     */
    @Test
    @Ignore("trop de resultat en preprod")
    public void testSearchOperationFormWithRegistrarName() throws GeneratorException, ServiceException, ServiceFacadeException {
        String registrarName = TEST_REGISTRAR_NAME;
        DomainGenerator.createNewDomain("operation-form-searchregistrarname");
        // 1/ preparation de l'objet de critere de recherche.
        FormSearchCriteria criteria = this.getOperationFormSearchCriteriaRegistrarName(registrarName);

        // validation simple du contenu.
        List<OperationForm> searchOperationForm = this.searchOperationForm(criteria);
        TestCase.assertTrue("The search should not be empty.", !searchOperationForm.isEmpty());

        for (OperationForm op : searchOperationForm) {
            TestCase.assertEquals("Bad OperationForm registrarName", registrarName, op.getRegistrarName());
        }
    }

    /**
     * Appel du service de recherche avec un objet de recherche instancié avec le nom du client BE.
     * 
     * @throws GeneratorException
     * @throws ServiceFacadeException
     * @throws ServiceException
     */
    @Test
    @Ignore("trop de resultat en preprod")
    public void testSearchOperationFormWithRegistrarNameMultipleResult() throws GeneratorException, ServiceException, ServiceFacadeException {
        String registrarName = TEST_REGISTRAR_NAME;
        for (int i = 0; i < 5; i++) {
            DomainGenerator.createNewDomain("operation-form-searchregistrarname");
        }
        // 1/ preparation de l'objet de critere de recherche.
        FormSearchCriteria criteria = this.getOperationFormSearchCriteriaRegistrarName(registrarName);

        // validation simple du contenu.
        List<OperationForm> searchOperationForm = this.searchOperationForm(criteria);
        TestCase.assertTrue("The search should return more than one result.", searchOperationForm.size() > 1);

        boolean found = false;
        for (OperationForm op : searchOperationForm) {
            found = registrarName.equals(op.getRegistrarName());
            if (found) {
                break;
            }
        }
        TestCase.assertTrue("The search result is not the correct registrar name " + registrarName, found);
    }

    /**
     * Appel du service de recherche avec un objet de recherche instancié avec un nom du client BE invalide.
     * 
     * @throws GeneratorException
     * @throws ServiceFacadeException
     * @throws ServiceException
     */
    @Test(expected = ServiceException.class)
    public void testSearchOperationFormWithInvalidRegistrarName() throws GeneratorException, ServiceException, ServiceFacadeException {
        String registrarName = "turlututu";
        // 1/ preparation de l'objet de critere de recherche.
        FormSearchCriteria criteria = this.getOperationFormSearchCriteriaRegistrarName(registrarName);

        // validation simple du contenu.
        List<OperationForm> searchOperationForm = this.searchOperationForm(criteria);

        boolean found = false;
        for (OperationForm op : searchOperationForm) {
            found = registrarName.equals(op.getRegistrarCode());
            if (found) {
                break;
            }
        }
        TestCase.assertTrue("The search result is not the correct registrar name " + registrarName, found);
    }

    /**
     * Appel du service de recherche avec un objet de recherche instancié avec le nom de client "commencant par".
     * 
     * @throws GeneratorException
     * @throws ServiceFacadeException
     * @throws ServiceException
     */
    @Test
    public void testSearchOperationFormWithMatchingRegistrarName() throws GeneratorException, ServiceException, ServiceFacadeException {
        String registrarName = TEST_REGISTRAR_NAME;
        DomainGenerator.createNewDomain("operation-form-searchregistrarmatch");

        // recherche normale avec le * à la fin et plus de 5 caracteres
        // 1/ preparation de l'objet de critere de recherche.
        FormSearchCriteria criteria = this.getOperationFormSearchCriteriaRegistrarName(registrarName + "*");

        // validation simple du contenu.
        List<OperationForm> searchOperationForm = this.searchOperationForm(criteria);
        TestCase.assertTrue("The search should not be empty.", !searchOperationForm.isEmpty());
        boolean found = false;
        for (OperationForm op : searchOperationForm) {
            found = registrarName.equals(op.getRegistrarName());
            if (found) {
                break;
            }
        }
        TestCase.assertTrue("The search result is not the correct registrar name " + registrarName, found);
    }

    /**
     * Appel du service de recherche avec un objet de recherche instancié avec le nom de client "commencant par".
     * 
     * @throws GeneratorException
     * @throws ServiceFacadeException
     * @throws ServiceException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSearchOperationFormWithErrorMatchingRegistrarName() throws GeneratorException, ServiceException, ServiceFacadeException {
        String registrarName = TEST_REGISTRAR_NAME;
        DomainGenerator.createNewDomain("operation-form-searchdomain");

        // recherche normale avec le * à la fin et plus de 3 caracteres
        // 1/ preparation de l'objet de critere de recherche.
        FormSearchCriteria criteriaBis = this.getOperationFormSearchCriteriaRegistrarName(registrarName.substring(0, 1) + "*");

        // validation simple du contenu.
        List<OperationForm> searchOperationForm = this.searchOperationForm(criteriaBis);

        boolean found = false;
        for (OperationForm op : searchOperationForm) {
            found = registrarName.equals(op.getRegistrarCode());
            if (found) {
                break;
            }
        }
        TestCase.assertTrue("The search result is not the correct registrar name " + registrarName, found);
    }

    /**
     * Appel du service de recherche de formulaire avec l'objet à trouver.
     * 
     * @throws GeneratorException
     * @throws ServiceFacadeException
     * @throws ServiceException
     */
    @Test
    public void testSearchOperationFormWithAllParam() throws GeneratorException, ServiceException, ServiceFacadeException {

        // 1/ preparation d'appl du service
        String createNewDomain = DomainGenerator.createNewDomain("operation-form-searchallparam");
        TestCase.assertNotNull("created domain identifier is null", createNewDomain);
        // Récupération du ticket courant sur le domaine pour avoir l'identifiant du formulaire.
        List<Ticket> ticketsWithDomain = AppServiceFacade.getTicketService().getTicketsWithDomain(createNewDomain, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        // Le domaine vient d'etre créé il n'y a donc qu'un seul ticket.
        TestCase.assertEquals("The domain returns more than one ticket.", 1, ticketsWithDomain.size());
        OperationFormId idForm = ticketsWithDomain.get(0).getOperationFormId();
        OperationForm operationFormWithId = AppServiceFacade.getOperationFormService().getOperationFormWithId(idForm, UserGenerator.getRootUserId(), TldServiceFacade.Fr);

        FormSearchCriteria searchCriteria = new FormSearchCriteria();
        searchCriteria.setOperationFormId(operationFormWithId.getOperationFormId().getValue());
        searchCriteria.setAdminHandle(operationFormWithId.getAdminHandle());
        searchCriteria.setDomainName(operationFormWithId.getDomainName());
        searchCriteria.setHolderHandle(operationFormWithId.getHolderHandle());
        searchCriteria.setRegistrar(operationFormWithId.getRegistrarName());
        searchCriteria.setTicketId(operationFormWithId.getTicketId());

        // 2/ appel du service
        List<OperationForm> searchOperationForm = this.searchOperationForm(searchCriteria);

        // 3/ validation du retour.
        TestCase.assertNotNull("The search result should not be null.", searchOperationForm);
        TestCase.assertTrue("The search result should not be empty.", !searchOperationForm.isEmpty());
        // la recherche devrait retourner qu'un seul formulaire
        TestCase.assertTrue("The search result should not have more than one entry.", searchOperationForm.size() == 1);
        boolean found = false;
        for (OperationForm op : searchOperationForm) {
            found = createNewDomain.equals(op.getDomainName());
            if (found) {
                break;
            }
        }
        TestCase.assertTrue("The search result is not the correct domain name " + createNewDomain, found);

        OperationForm operationForm = searchOperationForm.get(0);
        // il faut faire une comparaison plus poussée des deux instances.
        TestCase.assertTrue("The search result have to match with the search parameter.", operationForm.equals(operationFormWithId));

    }
}
