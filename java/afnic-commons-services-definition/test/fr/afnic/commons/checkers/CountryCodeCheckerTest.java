/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.checkers;

public class CountryCodeCheckerTest extends CheckerContractTest {

    @Override
    public String[] getValidValues() {
        return new String[] { "AB",
                              "AA" };
    }

    @Override
    public String[] getInvalidValues() {
        return new String[] { null,
                             "",
                             "ABC",
                             "ab",
                             "A-",
                             "1B" };
    }

    @Override
    public IInternalChecker createChecker() {
        return new CountryCodeChecker();
    }

}
