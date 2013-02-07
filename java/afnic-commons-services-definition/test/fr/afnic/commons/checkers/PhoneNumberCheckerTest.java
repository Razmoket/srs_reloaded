/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.checkers;

public class PhoneNumberCheckerTest extends CheckerContractTest {

    @Override
    public String[] getValidValues() {
        return new String[] { "010203040506",
                              "01 02 03 04",
                             "+(033) 01 02 03",
                             "+49 (0) 68 94 93 96 850" };
    }

    @Override
    public String[] getInvalidValues() {
        return new String[] { null,
                             "",
                             "avs",
                             "0102d03",
                             "01.02.03.04.05",
                             "+49 (0) 68 94-93 96 850" };
    }

    @Override
    public IInternalChecker createChecker() {
        return new PhoneNumberChecker();
    }

}
