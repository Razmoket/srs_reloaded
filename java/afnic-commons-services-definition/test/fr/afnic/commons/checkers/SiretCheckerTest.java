/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.checkers;

import org.junit.Test;

import fr.afnic.commons.beans.corporateentity.Siren;
import fr.afnic.commons.beans.corporateentity.Siret;
import fr.afnic.commons.services.exception.invalidformat.InvalidSiretException;

public class SiretCheckerTest extends CheckerContractTest {

    @Override
    public String[] getValidValues() {
        return new String[] { "41475756700022",
                              "35152822900088" };
    }

    @Override
    public String[] getInvalidValues() {
        return new String[] { null,
                             "",
                             "123",
                             "12345678901234",
                             "732829320",// siren valid
                             "a2345678901234" };
    }

    @Override
    public IInternalChecker createChecker() {
        return new SiretChecker();
    }

    @Test
    public void testWithSiren() {
        Siret siret = new Siret(new Siren("414757567"), "00022");
        new SiretChecker(siret).check("00022");
    }

    @Test(expected = InvalidSiretException.class)
    public void testWithSirenWithBadSiretValue() {
        Siret siret = new Siret(new Siren("414757567"), "00024");
        new SiretChecker(siret).check("00021");
    }
}
