/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.services.contracts.request;

import org.junit.Before;
import org.junit.Test;

import fr.afnic.commons.beans.documents.GddDocument;
import fr.afnic.commons.beans.request.TradeRequest;
import fr.afnic.commons.services.exception.NotFoundException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.test.generator.UserGenerator;

public class RequestServiceTest {

    @Before
    public void initServiceFacade() {

    }

    /**
     * On lie un document à une requete et on cherche à voir si la méthode <br/>
     * getRequestLinkedToDocumentWithHandle retourne bien la requete
     * 
     * @throws Exception
     */
    @Test
    public void testLinkDocumentAndGetRequestLinkedToDocumentWithHandle() throws Exception {
        TradeRequest request = new TradeRequest(UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        request.setId(123);

        GddDocument document = new GddDocument(UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        document.setHandle("DOC-123");

        AppServiceFacade.getRequestService().linkDocumentToRequest(document, request, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        AppServiceFacade.getRequestService().getLinkedRequestToDocumentWithHandle(document.getHandle(), UserGenerator.getRootUserId(), TldServiceFacade.Fr);

    }

    /**
     * On cherche à récupérer la requete liée à un document alors qu'il n'y en a aucune.
     * 
     * @throws Exception
     */
    @Test(expected = NotFoundException.class)
    public void testGetLinkedRequestToDocumentWithHandleWithNoLinkedRequest() throws Exception {
        GddDocument document = new GddDocument(UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        document.setHandle("DOC-123");
        AppServiceFacade.getRequestService().getLinkedRequestToDocumentWithHandle(document.getHandle(), UserGenerator.getRootUserId(), TldServiceFacade.Fr);
    }

}
