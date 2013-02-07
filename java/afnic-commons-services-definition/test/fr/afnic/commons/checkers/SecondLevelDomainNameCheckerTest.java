/*
 * $Id: //depot/main/java/afnic-commons-services-definition/test/fr/afnic/commons/checkers/DomainNameCheckerTest.java#1 $
 * $Revision: #1 $
 * $Author: ginguene $
 */

package fr.afnic.commons.checkers;

/**
 * Classe permettant de tester la classe SecondLevelDomainNameChecker
 * 
 * @author ginguene
 * 
 */
public class SecondLevelDomainNameCheckerTest extends CheckerContractTest {

    @Override
    public String[] getValidValues() {
        return new String[] { "tint.tm.fr",
                             "tint.gouv.fr",
                             "tint.com.fr",
                             "tint.asso.re",
                             "tint.tm.re",
                             "tint-bou.tm.fr" };
    }

    @Override
    public String[] getInvalidValues() {
        return new String[] { null,
                             "",
                             "fdsfsd",
                             "a.fr",
                             "a.gouv.fr",
                             "testdu060111galettedesrois.fr" };
    }

    @Override
    public IInternalChecker createChecker() {
        return new SecondLevelDomainNameChecker();
    }

}
