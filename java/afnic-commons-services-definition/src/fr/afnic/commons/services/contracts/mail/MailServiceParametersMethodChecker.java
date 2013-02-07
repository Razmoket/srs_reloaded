package fr.afnic.commons.services.contracts.mail;

import java.util.List;

import fr.afnic.commons.beans.contactdetails.EmailAddress;
import fr.afnic.commons.beans.mail.Email;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.checkers.EmailAddressChecker;
import fr.afnic.commons.services.exception.NullArgumentException;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.exception.invalidformat.InvalidFormatException;
import fr.afnic.commons.utils.Preconditions;

/**
 * Classe chargée de valider les parametres d'une méthode du DomainService.<br/>
 * Il est conseillé d'appeler les méthodes de cette classe en début de chaque méthode<br/>
 * d'implémentation de IDomainService.
 * 
 * 
 * @author ginguene
 */
public class MailServiceParametersMethodChecker {

    private final EmailAddressChecker emailAddressChecker = new EmailAddressChecker();

    public void checkSendMailParameters(Email mail, UserId userId) throws ServiceException {
        Preconditions.checkNotNull(mail, "mail");

        if (mail.hasNotToEmailAddresses()) {
            throw new NullArgumentException("mail.to");
        }
        this.checkEmailAddressesParameter(mail.getToEmailAddresses());

        if (mail.hasNotFromAddress()) {
            throw new NullArgumentException("mail.from");
        }
        this.emailAddressChecker.check(mail.getFromEmailAddress().getValue());

        if (mail.hasCcEmailAddresses()) {
            this.checkEmailAddressesParameter(mail.getCcEmailAddresses());
        }

    }

    public void checkEmailAddressesParameter(List<EmailAddress> addresses) throws InvalidFormatException {
        for (EmailAddress address : addresses) {
            this.emailAddressChecker.check(address.getValue());
        }
    }
}
