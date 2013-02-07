/*
 * $Id: //depot/main/java/afnic-commons-services-definition/test/fr/afnic/commons/beans/DomainNameCheckerTest.java#5 $
 * $Revision: #5 $
 * $Author: ginguene $
 */

package fr.afnic.commons.checkers;

/**
 * Classe permettant de tester la classe DomainNameChecker
 * 
 * @author ginguene
 * 
 */
public class EmailAddressCheckerTest extends CheckerContractTest {

    @Override
    public String[] getValidValues() {
        return new String[] { "bou@ba.com",
                             "bou@ba.fr",
                             "bou.ba@ba.com",
                             "bou+ba@ba.fr",
                             "123@ba.fr",
                             "bou@123.fr",
                             "niclog-dev+preprod+domainmanager+capmg.fr@nic.fr" };
    }

    @Override
    public String[] getInvalidValues() {
        return new String[] { null,
                             "",
                             "bou",
                             "bou@ba",
                             "bou@.fr",
                             "@ba.fr" };
    }

    @Override
    public IInternalChecker createChecker() {
        return new EmailAddressChecker();
    }
}
