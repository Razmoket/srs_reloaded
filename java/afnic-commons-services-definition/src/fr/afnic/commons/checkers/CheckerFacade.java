package fr.afnic.commons.checkers;

import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.invalidformat.InvalidFormatException;
import fr.afnic.commons.services.tld.TldServiceFacade;

/**
 * Facade permettant d'accéder aux différents checker
 * 
 * @author ginguene
 * 
 * 
 */
public final class CheckerFacade {

    private CheckerFacade() {

    }

    private static final GddDocumentHandleChecker GDD_DOCUMENT_HANDLE_CHECKER = new GddDocumentHandleChecker();
    private static final MailDocumentHandleChecker MAIL_DOCUMENT_HANDLE_CHECKER = new MailDocumentHandleChecker();
    private static final SimpleDocumentHandleChecker SIMPKE_DOCUMENT_HANDLE_CHECKER = new SimpleDocumentHandleChecker();
    private static final EmailAddressChecker EMAIL_ADDRESS_CHECKER = new EmailAddressChecker();
    private static final DomainNameChecker DOMAIN_NAME_CHECKER = new DomainNameChecker();
    private static final FirstLevelDomainNameChecker FIRST_LEVEL_DOMAIN_NAME_CHECKER = new FirstLevelDomainNameChecker();
    private static final SecondLevelDomainNameChecker SECOND_LEVEL_DOMAIN_NAME_CHECKER = new SecondLevelDomainNameChecker();
    private static final TicketIdChecker TICKET_ID_CHECKER = new TicketIdChecker();

    public static String checkGddDocumentHandle(String documentHandle) throws InvalidFormatException {
        return CheckerFacade.GDD_DOCUMENT_HANDLE_CHECKER.check(documentHandle);
    }

    public static String checkMailDocumentHandle(String documentHandle) throws InvalidFormatException {
        return CheckerFacade.MAIL_DOCUMENT_HANDLE_CHECKER.check(documentHandle);
    }

    public static String checkSimpleDocumentHandle(String documentHandle) throws InvalidFormatException {
        return CheckerFacade.SIMPKE_DOCUMENT_HANDLE_CHECKER.check(documentHandle);
    }

    public static String checkEmailAddress(String emailAddress) throws InvalidFormatException {
        return CheckerFacade.EMAIL_ADDRESS_CHECKER.check(emailAddress);
    }

    public static String checkDomainName(String domainName, UserId userId, TldServiceFacade tld) throws InvalidFormatException {
        return CheckerFacade.DOMAIN_NAME_CHECKER.check(domainName, userId, tld);
    }

    public static String checkSecondLevelDomainName(String domainName) throws InvalidFormatException {
        return CheckerFacade.SECOND_LEVEL_DOMAIN_NAME_CHECKER.check(domainName);
    }

    public static String checkFirstLevelDomainName(String domainName) throws InvalidFormatException {
        return CheckerFacade.FIRST_LEVEL_DOMAIN_NAME_CHECKER.check(domainName);
    }

    public static String checkTicketId(String ticketId) throws InvalidFormatException {
        return CheckerFacade.TICKET_ID_CHECKER.check(ticketId);
    }

}
