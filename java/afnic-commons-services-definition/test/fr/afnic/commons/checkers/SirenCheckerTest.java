/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.checkers;

public class SirenCheckerTest extends CheckerContractTest {

    @Override
    public String[] getValidValues() {
        return new String[] { "732829320",
                               "414757567" };
    }

    @Override
    public String[] getInvalidValues() {
        return new String[] { null,
                             "",
                             "123",
                             "123456789",
                             "a23456789",
                             "000000000" };
    }

    @Override
    public IInternalChecker createChecker() {
        return new SirenChecker();
    }

}
