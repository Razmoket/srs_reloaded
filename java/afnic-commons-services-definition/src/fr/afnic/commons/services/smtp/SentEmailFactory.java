/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/services/smtp/SentEmailFactory.java#5 $
 * $Revision: #5 $
 * $Author: ginguene $
 */

package fr.afnic.commons.services.smtp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeMultipart;

import fr.afnic.commons.beans.contactdetails.EmailAddress;
import fr.afnic.commons.beans.mail.SentEmail;
import fr.afnic.commons.beans.mail.reception.ConverterException;
import fr.afnic.commons.beans.mail.reception.EmailBoxException;
import fr.afnic.commons.services.exception.invalidformat.InvalidEmailAddressException;

/**
 * Classe utilisaire permettant de convertir un javax.mail.Message<br/>
 * issu de la librairie javaMail en SentMail de l'afnic-commons
 * 
 * @author ginguene
 * 
 */
public final class SentEmailFactory {

    private Message message = null;
    private SentEmail mail = null;

    /**
     * Constructeur privé empechant l'instanciation
     */
    private SentEmailFactory(Message message) {
        this.message = message;
    }

    /**
     * Converti un javax.mail.Message en <br>
     * fr.afnic.commons.beans.mail.SentMail
     * 
     * @param message
     * @return
     * @throws ConverterException
     */
    public static SentEmail createSentMail(Message message) throws ConverterException {
        return new SentEmailFactory(message).createSentMail();
    }

    private SentEmail createSentMail() throws ConverterException {
        try {
            this.mail = new SentEmail();
            this.populateEmail();
            return this.mail;
        } catch (Exception e) {
            throw new ConverterException("Error while converting Message to Mail", e);
        }
    }

    private void populateEmail() throws EmailBoxException {
        try {
            this.populateSubject();
            this.populateFromEmailAddress();
            this.populateToAddresses();
            this.populateCcAddresses();
            this.populateReplyTo();
            this.populateMessageId();
            this.populateContent();
            this.populateReceivedDate();
            this.populateSentDate();
            this.populateAttachements();
        } catch (Exception e) {
            throw new EmailBoxException("Error while populateMailFromMessage()", e);
        }
    }

    private void populateSubject() throws MessagingException {
        this.mail.setSubject(this.message.getSubject());
    }

    private void populateFromEmailAddress() throws MessagingException, InvalidEmailAddressException {
        this.mail.setFromEmailAddress(this.message.getFrom()[0].toString());
    }

    private void populateToAddresses() throws MessagingException, InvalidEmailAddressException {
        this.mail.setToEmailAddresses(this.getEmailAddressesFromRecipient(Message.RecipientType.TO));
    }

    private void populateCcAddresses() throws MessagingException, InvalidEmailAddressException {
        this.mail.setCcEmailAddresses(this.getEmailAddressesFromRecipient(Message.RecipientType.CC));
    }

    private List<EmailAddress> getEmailAddressesFromRecipient(Message.RecipientType recipient) throws MessagingException, InvalidEmailAddressException {
        List<EmailAddress> emailAddresses = new ArrayList<EmailAddress>();

        Address[] addresses = this.message.getRecipients(recipient);
        if (addresses != null) {
            for (Address address : addresses) {
                emailAddresses.add(new EmailAddress(address.toString()));
            }
        }
        return emailAddresses;
    }

    private void populateReplyTo() throws MessagingException {
        String[] allInReplyTo = this.message.getHeader("In-Reply-To");
        if (allInReplyTo != null) {
            this.mail.setReplyTo(allInReplyTo[0]);
        }
    }

    private void populateMessageId() throws MessagingException {
        String[] allMessageID = this.message.getHeader("Message-ID");
        if (allMessageID != null) {
            this.mail.setMessageId(allMessageID[0]);
        }
    }

    private void populateSentDate() throws MessagingException {
        this.mail.setSentDate(this.message.getSentDate());
    }

    private void populateReceivedDate() throws MessagingException {
        this.mail.setReceivedDate(this.message.getReceivedDate());
    }

    private void populateContent() throws MessagingException, IOException {
        Object content = this.message.getContent();

        // Gestion des mails multi part avec des pièces jointes
        while (content != null && content instanceof MimeMultipart) {
            MimeMultipart multi = (MimeMultipart) content;
            content = multi.getBodyPart(0).getContent();
        }

        if (content != null) {
            this.mail.setContent(content.toString());
        }
    }

    private void populateAttachements() throws MessagingException, IOException {
        Object content = this.message.getContent();
        if (content != null && content instanceof Multipart) {
            Multipart multipart = (Multipart) this.message.getContent();

            for (int i = 0; i < multipart.getCount(); i++) {
                BodyPart part = multipart.getBodyPart(i);

                String contentType = part.getContentType();

                if (part.getFileName() != null) {
                    // Retrieve the file name
                    String fileName = part.getFileName();

                    File file = new File(fileName);
                    this.write(file, part.getInputStream());
                    this.mail.addAttachement(file);
                }
            }
        }

    }

    private void write(File file, InputStream inputStream) throws IOException {
        OutputStream out = new FileOutputStream(file.getName());
        byte[] buf = new byte[1024];
        int len;
        while ((len = inputStream.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        out.close();
        inputStream.close();
    }

}
