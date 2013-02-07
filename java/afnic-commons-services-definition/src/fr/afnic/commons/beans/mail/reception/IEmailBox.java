package fr.afnic.commons.beans.mail.reception;

import java.util.List;

import fr.afnic.commons.beans.mail.SentEmail;

/**
 * Interface décrivant une boite de réception<br/>
 * Permet de consulter et d'effacer des mails contenues<br/>
 * dans une boite mail.
 * 
 * 
 * @author ginguene
 * 
 */
public interface IEmailBox {

    public int getUnreadEmailCount() throws EmailBoxException;

    public List<SentEmail> getUnreadEmails() throws EmailBoxException;

    public int getEmailCount() throws EmailBoxException;

    public List<SentEmail> getEmails() throws EmailBoxException;

    public void deleteEmail(SentEmail mail) throws EmailBoxException;

    public void deleteEmail(String mailId) throws EmailBoxException;

    public void setAsReadMail(SentEmail mail) throws EmailBoxException;

    public void setAsReadMail(String mailId) throws EmailBoxException;

}
