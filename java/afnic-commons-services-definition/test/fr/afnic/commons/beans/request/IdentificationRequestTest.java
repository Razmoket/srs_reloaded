/*
 * $Id: //depot/main/java/afnic-commons-services-definition/test/fr/afnic/commons/beans/request/IdentificationRequestTest.java#2 $
 * $Revision: #2 $
 * $Author: alaphilippe $
 */

package fr.afnic.commons.beans.request;

import org.junit.Ignore;
import org.junit.Test;

/**
 * Classe permettant de valider le fonctionnement de la classe identificationRequest.
 * 
 * @author ginguene
 * 
 */
public class IdentificationRequestTest {

    @Test
    @Ignore("Ne doit pas s'appuyer sur la base de donnée ou alors la logiqe métier doit etre déplacée dans un service qui doit eter testé")
    public void testCanDomainsPortfolioBeMehodsWithFrPortfolio() throws Exception {
        testCanDomainsPortfolioBeMehods(".fr");
    }

    public void testCanDomainsPortfolioBeMehods(String extension) throws Exception {

        /*
         * String domainName = DomainGenerator.createNewDomain("test-identification-request", extension); IdentificationRequest identificationRequest
         * = IdentificationGenerator.createdentificationRequestForHolderOfDomain(domainName);
         * TestCase.assertFalse(identificationRequest.canDomainsPortfolioBeBlocked());
         * TestCase.assertFalse(identificationRequest.canDomainsPortfolioBeSuppressed());
         * 
         * domainName = DomainGenerator.createNewDomain("test-identification-request", extension); identificationRequest =
         * IdentificationGenerator.createIdentificationRequestForHolderOfDomainThatCanBeBlocked(domainName);
         * TestCase.assertTrue(identificationRequest.canDomainsPortfolioBeBlocked());
         * TestCase.assertFalse(identificationRequest.canDomainsPortfolioBeSuppressed());
         * 
         * identificationRequest.setStatus(IdentificationRequestStatus.DomainsBlocked);
         * IdentificationDao.getInstance().updateIdentificationRequest(identificationRequest, UserGenerator.ROOT_LOGIN);
         * 
         * identificationRequest = IdentificationDao.getInstance().getIdentificationRequestWithId(identificationRequest.getId());
         * IdentificationGenerator.changeBlockedRequestInRequestThatCanHaveDomainsPortfolioSuppressed(identificationRequest);
         * 
         * identificationRequest = IdentificationDao.getInstance().getIdentificationRequestWithId(identificationRequest.getId());
         * 
         * TestCase.assertFalse(identificationRequest.canDomainsPortfolioBeBlocked());
         * TestCase.assertTrue(identificationRequest.canDomainsPortfolioBeSuppressed());
         */

    }
}
