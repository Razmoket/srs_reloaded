package fr.afnic.commons.checkers;

import junit.framework.TestCase;

import org.junit.Test;

import fr.afnic.commons.services.exception.invalidformat.InvalidFormatException;

/**
 * Contrat de tests pour les checker.
 * 
 * 
 * 
 * @author ginguene
 * 
 */
public abstract class CheckerContractTest {

    public abstract String[] getValidValues();

    public abstract String[] getInvalidValues();

    public abstract IInternalChecker createChecker();

    @Test
    public void testCheckWithValidValues() throws InvalidFormatException {
        for (String validValue : getValidValues()) {
            createChecker().check(validValue);
        }
    }

    @Test
    public void testCheckWithInvalidValues() throws InvalidFormatException {

        for (String invalidValue : this.getInvalidValues()) {
            try {
                createChecker().check(invalidValue);
                TestCase.fail("'" + invalidValue + "' should be considered as an invalid value");
            } catch (InvalidFormatException e) {
                // Exception attendue
            }
        }
    }

}
