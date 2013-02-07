/*
 * $Id: //depot/main/java/afnic-commons-services-definition/test/fr/afnic/commons/checkers/FirstLevelDomainNameCheckerTest.java#7 $
 * $Revision: #7 $
 * $Author: barriere $
 */

package fr.afnic.commons.checkers;

/**
 * Classe permettant de tester la classe DomainNameChecker
 * 
 * @author ginguene
 * 
 */
public class FirstLevelDomainNameCheckerTest extends CheckerContractTest {

    @Override
    public String[] getValidValues() {
        return new String[] { "a123.fr",
                             "a-123.fr",
                             "ab.tm.fr",
                             "ab.tm.fr",
                             "ab.gouv.fr",
                             "ab.asso.fr",
                             "ab.asso.re",
                             "tint-bou.re",
                             "123456.fr",
                             "0z.fr",
                             "toto.pm",
                             "toto.paris",
                             "nomdedomaine-1315793290490.fr" };
    }

    @Override
    public String[] getInvalidValues() {
        return new String[] { null,
                             "",
                             "fdsfsd",
                             "a.fr",
                             "a.gouv.fr" };
    }

    @Override
    public IInternalChecker createChecker() {
        return new FirstLevelDomainNameChecker();
    }

}
