/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.services.smtp;

import java.io.File;
import java.util.List;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import fr.afnic.commons.beans.mail.Email;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.utils.Preconditions;
import fr.afnic.utils.ObjectSerializer;

/**
 * Transforme un objet Mail de l'afnic-commons en MimeMessage de l'api javamail.
 * 
 * @author ginguene
 * 
 */
public final class MimeMessageFactory {

    private final static MimeMessageFactory INSTANCE = new MimeMessageFactory();

    public static MimeMessage createMimeMessage(Email mail, UserId userId) throws ServiceException {
        return INSTANCE.createMimeMessageImpl(mail, userId);
    }

    private MimeMessageFactory() {

    }

    private MimeMessage createMimeMessageImpl(Email email, UserId userId) throws ServiceException {
        Preconditions.checkNotNull(email, "email");

        try {

            Session session = Session.getDefaultInstance(System.getProperties(), null);
            session.setDebug(true);
            MimeMessage mimeMessage = new MimeMessage(session);

            mimeMessage.setFrom(new InternetAddress(email.getFromEmailAddress().getValue()));
            mimeMessage.setRecipients(Message.RecipientType.TO, this.getInternetAddressFromStringList(email.getToEmailAddressesAsStringList()));
            mimeMessage.setRecipients(Message.RecipientType.CC, this.getInternetAddressFromStringList(email.getCcEmailAddressesAsStringList()));
            mimeMessage.setRecipients(Message.RecipientType.BCC, this.getInternetAddressFromStringList(email.getBccEmailAddressesAsStringList()));

            mimeMessage.setSubject(email.getSubject());

            mimeMessage.setHeader("Content-Type", email.getFormat().getEncoding());
            mimeMessage.setSentDate(new java.util.Date());

            // Tout mail emis via l'API possède ce tag
            mimeMessage.setHeader("X-afnic-commons", "true");
            mimeMessage.setHeader("X-userId", userId.getValue());

            if (email.hasAttachements()) {
                this.populateWithAttachements(mimeMessage, email);
            } else {
                this.populateWithoutAttachement(mimeMessage, email);
            }

            return mimeMessage;
        } catch (Exception e) {
            throw new ServiceException("createMimeMessage() failed with email:\n " + ObjectSerializer.toYaml(email), e);
        }
    }

    private void populateWithoutAttachement(MimeMessage mimeMessage, Email email) throws MessagingException {
        mimeMessage.setContent(email.getContent(), email.getFormat().getEncoding());
    }

    private void populateWithAttachements(MimeMessage mimeMessage, Email email) throws MessagingException {
        Multipart multipart = new MimeMultipart();

        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(email.getContent(), email.getFormat().getEncoding());
        multipart.addBodyPart(messageBodyPart);

        // Part two is attachment
        for (File attachement : email.getAttachements()) {
            MimeBodyPart fileBodyPart = new MimeBodyPart();
            DataSource source = new FileDataSource(attachement);
            fileBodyPart.setDataHandler(new DataHandler(source));
            fileBodyPart.setFileName(attachement.getAbsolutePath());
            multipart.addBodyPart(fileBodyPart);
        }

        // Put parts in message
        mimeMessage.setContent(multipart);
    }

    private InternetAddress[] getInternetAddressFromStringList(List<String> addressList) throws AddressException {
        InternetAddress[] internetAddresses = new InternetAddress[addressList.size()];
        for (int i = 0; i < internetAddresses.length; i++) {
            internetAddresses[i] = new InternetAddress(addressList.get(i));
        }
        return internetAddresses;
    }

}
