/*
 * $Id: $ $Revision: $ $Author: $
 */

package fr.afnic.commons.services.sql.checker;

import fr.afnic.commons.checkers.CheckerContractTest;
import fr.afnic.commons.checkers.IInternalChecker;

public class SqlNameCheckerTest extends CheckerContractTest {

    @Override
    public String[] getValidValues() {
        return new String[] { "abc", "ABC", "a123", "a_123", "a#123", "a$123", "a12345678901234567890123456789" };
    }

    @Override
    public String[] getInvalidValues() {
        return new String[] { null, "", "    ", "123", "1 2", "#123", "$123", "a12345678901234567890123456789123456" };
    }

    @Override
    public IInternalChecker createChecker() {
        return new SqlNameChecker();
    }

}
