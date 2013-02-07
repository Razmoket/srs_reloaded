package fr.afnic.commons.checkers;

/**
 * Classe permettant de tester la classe TicketIdChecker.
 * 
 * @author ginguene
 * 
 */
public class OperationFormIdCheckerTest extends CheckerContractTest {

    @Override
    public String[] getValidValues() {
        return new String[] { "0",
                              "123",
                             "456789" };
    }

    @Override
    public String[] getInvalidValues() {
        return new String[] { null,
                             "",
                             "12a3",
                             "abcde",
                             "-1" };
    }

    @Override
    public IInternalChecker createChecker() {
        return new OperationFormIdChecker();
    }

}
