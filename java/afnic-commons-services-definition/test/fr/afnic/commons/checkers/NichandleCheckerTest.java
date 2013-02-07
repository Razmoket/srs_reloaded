/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.checkers;

public class NichandleCheckerTest extends CheckerContractTest {

    @Override
    public String[] getValidValues() {
        return new String[] { "ABC123-FRNIC",
                              "ABC1-FRNIC",
                             "A1-FRNIC" };
    }

    @Override
    public String[] getInvalidValues() {
        return new String[] { null,
                             "",
                             "ABC0-FRNIC",
                             "123-FRNIC",
                             "AB123-BUNIC" };
    }

    @Override
    public IInternalChecker createChecker() {
        return new NichandleChecker();
    }

}
