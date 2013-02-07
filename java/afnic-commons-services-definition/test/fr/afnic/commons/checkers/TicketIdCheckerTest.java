package fr.afnic.commons.checkers;

/**
 * Classe permettant de tester la classe TicketIdChecker.
 * 
 * @author ginguene
 * 
 */
public class TicketIdCheckerTest extends CheckerContractTest {

    @Override
    public String[] getValidValues() {
        return new String[] { "NIC000000000000",
                             "NIC123456789012" };
    }

    @Override
    public String[] getInvalidValues() {
        return new String[] { null,
                             "",
                             "123",
                             "123456789012345",
                             "abcdefghijklmno",
                             "ABC000000000000",
                             "NIC00000000000a" };
    }

    @Override
    public IInternalChecker createChecker() {
        return new TicketIdChecker();
    }

}
