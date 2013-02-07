package fr.afnic.commons.services.contracts.mail;

import java.util.Date;

import junit.framework.TestCase;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Test;

import fr.afnic.commons.beans.mail.Email;
import fr.afnic.commons.beans.mail.SentEmail;
import fr.afnic.commons.beans.profiling.users.User;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.beans.profiling.users.UserProfile;
import fr.afnic.commons.beans.profiling.users.UserRight;
import fr.afnic.commons.services.exception.NotFoundException;
import fr.afnic.commons.services.exception.NullArgumentException;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.exception.invalidformat.InvalidEmailAddressException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.facade.exception.ServiceFacadeException;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.test.generator.UserGenerator;

/**
 * Valide le comportement de la méthode sendMail d'une implémentation du service IMailService.
 * 
 * @author ginguene
 * 
 */
public class EmailServiceSendMailContractTest {

    @Test(expected = NullArgumentException.class)
    public void testCreateContactWithNullMail() throws Exception {
        User user = this.createUserWithMailSendRight();
        AppServiceFacade.getEmailService().sendEmail(null, user.getId(), TldServiceFacade.Fr);
    }

    @Test(expected = NullArgumentException.class)
    public void testCreateContactWithMailWithoutToAddresses() throws Exception {
        User user = this.createUserWithMailSendRight();
        Email mail = new Email();
        mail.setFromEmailAddress("test@afnic.fr");
        AppServiceFacade.getEmailService().sendEmail(mail, user.getId(), TldServiceFacade.Fr);
    }

    @Test(expected = NullArgumentException.class)
    public void testCreateContactWithMailWithoutFromAddress() throws Exception {
        User user = this.createUserWithMailSendRight();
        Email mail = new Email();
        mail.addToEmailAddress("test@afnic.fr");
        AppServiceFacade.getEmailService().sendEmail(mail, user.getId(), TldServiceFacade.Fr);
    }

    @Test(expected = NullArgumentException.class)
    public void testSendMailWithNullUser() throws Exception {
        Email mail = this.createMailToSend();
        AppServiceFacade.getEmailService().sendEmail(mail, null, TldServiceFacade.Fr);
    }

    /**
     * On ne peut pas envoyer un mail sans un login de user connu.
     */
    @Test(expected = NotFoundException.class)
    public void testSendMailWithUnknownUser() throws Exception {
        Email mail = this.createMailToSend();
        AppServiceFacade.getEmailService().sendEmail(mail, new UserId(900), TldServiceFacade.Fr);
    }

    /**
     * Teste un envoie de mail réussi.
     */
    @Test(expected = InvalidEmailAddressException.class)
    public void testSendMailWithInvalidToAddress() throws Exception {
        User user = this.createUserWithMailSendRight();
        Email mail = this.createMailToSend();
        mail.setToEmailAddresses("vaa");

        AppServiceFacade.getEmailService().sendEmail(mail, user.getId(), TldServiceFacade.Fr);
    }

    /**
     * Teste un envoie de mail réussi.
     */
    @Test
    public void testSendMail() throws Exception {
        User user = this.createUserWithMailSendRight();
        Email mail = this.createMailToSend();

        SentEmail sentMail = AppServiceFacade.getEmailService().sendEmail(mail, user.getId(), TldServiceFacade.Fr);
        TestCase.assertEquals("Bad from in sent mail", sentMail.getFromEmailAddress(), mail.getFromEmailAddress());
        TestCase.assertEquals("Bad to in sent mail", sentMail.getToEmailAddressesAsString(), mail.getToEmailAddressesAsString());
        TestCase.assertEquals("Bad cc in sent mail", sentMail.getCcEmailAddressesAsString(), mail.getCcEmailAddressesAsString());
        TestCase.assertEquals("Bad subject in sent mail", sentMail.getSubject(), mail.getSubject());
        TestCase.assertEquals("Bad content in sent mail", sentMail.getContent(), mail.getContent());

        TestCase.assertTrue("Bad date in sent mail", DateUtils.isSameDay(new Date(), sentMail.getSentDate()));
        TestCase.assertTrue("Blank messageId in sent mail", StringUtils.isNotBlank(sentMail.getMessageId()));
    }

    private User createUserWithMailSendRight() throws ServiceException, ServiceFacadeException {
        User user = new User(UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        user.setLogin("user1");

        UserProfile profil = new UserProfile("Test");
        profil.addRight(UserRight.EmailSend);
        user.setProfile(profil);
        AppServiceFacade.getUserService().addUser(user, UserGenerator.getRootUserId(), TldServiceFacade.Fr);
        return user;
    }

    private Email createMailToSend() throws ServiceException, ServiceFacadeException {
        Email mail = new Email();
        mail.setFromEmailAddress("from@afnic.fr");
        mail.addToEmailAddress("ginguene+to@afnic.fr");
        mail.addCcEmailAddress("ginguene+cc@afnic.fr");
        mail.setSubject("mon sujet de mail");
        mail.setContent("Mon message de mail");
        return mail;
    }

}
