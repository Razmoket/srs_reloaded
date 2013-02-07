package fr.afnic.commons.beans.mail;

import fr.afnic.utils.ToStringHelper;

/**
 * 
 * Mail spécialisé avec un format
 * 
 * @author ginguene
 * 
 */
public class Email extends GenericEmail {

    private static final long serialVersionUID = 1L;

    protected EmailFormat format = EmailFormat.TEXT;
    protected String content;

    public EmailFormat getFormat() {
        return this.format;
    }

    public void setFormat(EmailFormat format) {
        this.format = format;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return new ToStringHelper().add("format", this.format)
                                   .add("to", this.getToEmailAddressesAsString())
                                   .add("from", this.getFromEmailAddress())
                                   .add("cc", this.getCcEmailAddressesAsString())
                                   .add("subject", this.getSubject())
                                   .toString();
    }
}
