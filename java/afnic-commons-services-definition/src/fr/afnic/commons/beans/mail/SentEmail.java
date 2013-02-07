package fr.afnic.commons.beans.mail;

import java.util.Date;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * Représente un mail qui a été envoyé et dispose donc d'une date d'envoie, de recéption, un id et le replyTo, <br/>
 * si le mail est envoyé en réponse à un autre (messageId du premier mail)
 * 
 * 
 * 
 * @author ginguene
 * 
 */
public class SentEmail extends Email {

    private static final long serialVersionUID = 1L;

    private String replyTo;
    private String messageId;
    private Date sentDate;
    private Date receivedDate;

    /**
     * Constructeur par défaut
     * 
     */
    public SentEmail() {
    }

    /**
     * Constucteur qui copie les attrributs du mail, puis ajoute le messageId et la date de reception du mail
     * 
     * @param mail
     *            mail servant de modèle
     * @param messageId
     *            id du mail
     * 
     * @param sentDate
     *            date de reception du mail
     */
    public SentEmail(Email mail, String messageId, Date sentDate) {
        this.fromEmailAddress = mail.getFromEmailAddress();
        this.subject = mail.getSubject();
        this.content = mail.getContent();
        this.ccEmailAddresses = mail.getCcEmailAddresses();
        this.toEmailAddresses = mail.getToEmailAddresses();
        this.format = mail.getFormat();

        this.messageId = messageId;

        if (sentDate != null) {
            this.sentDate = (Date) sentDate.clone();
        }

        this.replyTo = mail.getReplyTo();
    }

    public Date getSentDate() {
        if (this.sentDate != null) {
            return (Date) this.sentDate.clone();
        } else {
            return null;
        }
    }

    /**
     * Initialise la date avec un clone du parametre
     * 
     * @param sentDate
     *            date de reception du mail
     */
    public void setSentDate(Date sentDate) {
        if (sentDate != null) {
            this.sentDate = (Date) sentDate.clone();
        }
    }

    public String getMessageId() {
        return this.messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public boolean hasReplyTo() {
        return StringUtils.isNotBlank(this.replyTo);
    }

    @Override
    public String getReplyTo() {
        return this.replyTo;
    }

    @Override
    public void setReplyTo(String replyTo) {
        this.replyTo = replyTo;
    }

    /**
     * Retourne un clone de la date de reception ou null<br/>
     * si il n'y en a pas
     * 
     * @return clone de la date de reception ou null
     */
    public Date getReceivedDate() {
        if (this.receivedDate != null) {
            return (Date) this.receivedDate.clone();
        } else {
            return null;
        }
    }

    public void setReceivedDate(Date receivedDate) {
        if (receivedDate != null) {
            this.receivedDate = (Date) receivedDate.clone();
        } else {
            this.receivedDate = null;
        }
    }

}
