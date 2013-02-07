package fr.afnic.commons.beans.mail;

/**
 * Representation d'un mail avant qu'on lui applique un format (HTML ou Text). A ce stade le mail pourra etre transformer au format de notre choix via
 * un MailFormatter.
 * 
 * @author jginguene
 * 
 */
public class UnformatMail extends GenericEmail {

    private static final long serialVersionUID = -4563376941631150624L;
    private MailContent content;

    public MailContent getContent() {
        return content;
    }

    public void setContent(MailContent content) {
        this.content = content;
    }

}
