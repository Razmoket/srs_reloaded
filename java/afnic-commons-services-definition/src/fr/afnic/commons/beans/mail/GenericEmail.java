package fr.afnic.commons.beans.mail;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.common.base.Joiner;

import fr.afnic.commons.beans.contactdetails.EmailAddress;
import fr.afnic.commons.services.exception.invalidformat.InvalidEmailAddressException;
import fr.afnic.commons.utils.Preconditions;

/**
 * Représente un mail
 * 
 * @author ginguene
 * 
 */
public abstract class GenericEmail implements Serializable {

    private static final long serialVersionUID = 1L;

    protected String replyTo;
    protected EmailAddress fromEmailAddress;
    protected String subject;
    protected List<EmailAddress> toEmailAddresses;
    protected List<EmailAddress> ccEmailAddresses;
    protected List<EmailAddress> bccEmailAddresses;
    protected List<MailHeader> headers = new ArrayList<MailHeader>();

    private final List<File> attachements = new ArrayList<File>();

    public EmailAddress getFromEmailAddress() {
        return this.fromEmailAddress;
    }

    public void setFromEmailAddress(String fromEmailAddress) throws InvalidEmailAddressException {
        this.fromEmailAddress = new EmailAddress(fromEmailAddress);
    }

    public void setFromEmailAddress(EmailAddress fromEmailAddress) {
        this.fromEmailAddress = fromEmailAddress;
    }

    public String getSubject() {
        return this.subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public List<EmailAddress> getToEmailAddresses() {
        return this.toEmailAddresses;
    }

    public List<MailHeader> getHeaders() {
        return this.headers;
    }

    public void setToEmailAddresses(List<EmailAddress> toEmailAddresses) {
        this.toEmailAddresses = toEmailAddresses;
    }

    public void setToEmailAddressesFromStrList(List<String> toEmailAddresses) {
        this.clearToEmailAddresses();
        if (toEmailAddresses != null) {
            for (String toEmailAddress : toEmailAddresses) {
                this.toEmailAddresses.add(new EmailAddress(toEmailAddress));
            }
        }
    }

    public void setToEmailAddresses(String... toEmailAddresses) throws InvalidEmailAddressException {
        this.clearToEmailAddresses();
        if (toEmailAddresses != null) {
            for (String toEmailAddress : toEmailAddresses) {
                this.toEmailAddresses.add(new EmailAddress(toEmailAddress));
            }
        }
    }

    public void setCcEmailAddresses(List<EmailAddress> ccEmailAddresses) {
        this.ccEmailAddresses = ccEmailAddresses;
    }

    public void setCcEmailAddresses(String... ccEmailAddresses) throws InvalidEmailAddressException {
        this.clearCcEmailAddresses();

        if (ccEmailAddresses != null) {
            for (String ccEmailAddress : ccEmailAddresses) {
                this.ccEmailAddresses.add(new EmailAddress(ccEmailAddress));
            }
        }
    }

    public void setCcEmailAddressesFromStrList(List<String> ccEmailAddresses) throws InvalidEmailAddressException {
        this.clearCcEmailAddresses();

        if (ccEmailAddresses != null) {
            for (String ccEmailAddress : ccEmailAddresses) {
                this.ccEmailAddresses.add(new EmailAddress(ccEmailAddress));
            }
        }
    }

    public void setCcEmailAddresses(EmailAddress... ccEmailAddresses) throws InvalidEmailAddressException {
        this.clearCcEmailAddresses();

        if (ccEmailAddresses != null) {
            for (EmailAddress ccEmailAddress : ccEmailAddresses) {
                this.ccEmailAddresses.add(ccEmailAddress);
            }
        }
    }

    public void setBccEmailAddresses(String... bccEmailAddresses) throws InvalidEmailAddressException {
        this.clearBccEmailAddresses();
        if (bccEmailAddresses != null) {
            for (String bccEmailAddress : bccEmailAddresses) {
                this.bccEmailAddresses.add(new EmailAddress(bccEmailAddress));
            }
        }
    }

    public void setBccEmailAddresses(EmailAddress... bccEmailAddresses) throws InvalidEmailAddressException {
        this.clearBccEmailAddresses();
        if (bccEmailAddresses != null) {
            for (EmailAddress bccEmailAddress : bccEmailAddresses) {
                this.bccEmailAddresses.add(bccEmailAddress);
            }
        }
    }

    public void setBccEmailAddressesFromStrList(List<String> bccEmailAddresses) throws InvalidEmailAddressException {
        this.clearCcEmailAddresses();

        if (bccEmailAddresses != null) {
            for (String bccEmailAddress : bccEmailAddresses) {
                this.bccEmailAddresses.add(new EmailAddress(bccEmailAddress));
            }
        }
    }

    public void setBccEmailAddresses(List<EmailAddress> bccEmailAddresses) throws InvalidEmailAddressException {
        this.bccEmailAddresses = bccEmailAddresses;
    }

    private void clearToEmailAddresses() {
        if (this.toEmailAddresses == null) {
            this.toEmailAddresses = new ArrayList<EmailAddress>();
        } else {
            this.toEmailAddresses.clear();
        }
    }

    public List<EmailAddress> getCcEmailAddresses() {
        return this.ccEmailAddresses;
    }

    private void clearCcEmailAddresses() {
        if (this.ccEmailAddresses == null) {
            this.ccEmailAddresses = new ArrayList<EmailAddress>();
        } else {
            this.ccEmailAddresses.clear();
        }
    }

    private void clearBccEmailAddresses() {
        if (this.bccEmailAddresses == null) {
            this.bccEmailAddresses = new ArrayList<EmailAddress>();
        } else {
            this.bccEmailAddresses.clear();
        }
    }

    public void addHeaderWithNameAndValue(String name, String value) {
        this.headers.add(new MailHeader(name, value));
    }

    public void addToEmailAddress(EmailAddress toEmailAddress) {
        if (this.toEmailAddresses == null) {
            this.toEmailAddresses = new ArrayList<EmailAddress>();
        }
        this.toEmailAddresses.add(toEmailAddress);
    }

    public void addToEmailAddress(String toEmailAddress) throws InvalidEmailAddressException {
        if (this.toEmailAddresses == null) {
            this.toEmailAddresses = new ArrayList<EmailAddress>();
        }
        this.toEmailAddresses.add(new EmailAddress(toEmailAddress));
    }

    public void addCcEmailAddress(EmailAddress ccEmailAddress) {
        if (this.ccEmailAddresses == null) {
            this.ccEmailAddresses = new ArrayList<EmailAddress>();
        }
        this.ccEmailAddresses.add(ccEmailAddress);
    }

    public void addCcEmailAddress(String ccEmailAddress) throws InvalidEmailAddressException {
        if (this.ccEmailAddresses == null) {
            this.ccEmailAddresses = new ArrayList<EmailAddress>();
        }
        this.ccEmailAddresses.add(new EmailAddress(ccEmailAddress));
    }

    public boolean hasNotFromAddress() {
        return !this.hasFromAddress();
    }

    public boolean hasFromAddress() {
        return this.fromEmailAddress != null;
    }

    public boolean hasNotToEmailAddresses() {
        return !this.hasToEmailAddresses();
    }

    public boolean hasToEmailAddresses() {
        return (this.getToEmailAddresses() != null && this.getToEmailAddresses().size() > 0);
    }

    public boolean hasNotCcEmailAddresses() {
        return !this.hasCcEmailAddresses();
    }

    public boolean hasCcEmailAddresses() {
        return (this.getCcEmailAddresses() != null && this.getCcEmailAddresses().size() > 0);
    }

    public void setReplyTo(String replyTo) {
        this.addHeaderWithNameAndValue("In-Reply-To", replyTo);
    }

    public String getReplyTo() {
        for (MailHeader header : this.headers) {
            if (header.getName().equals("In-Reply-To")) {
                return header.getValue();
            }
        }
        return null;
    }

    /**
     * Retourne la liste des addresses email de destination sous la forme d'une chaine de caractère où chaque addresse est séparée par un ','
     * 
     * @return
     */
    public String getToEmailAddressesAsString() {
        return Joiner.on(",").skipNulls().join(this.getToEmailAddressesAsStringList());
    }

    /**
     * Retourne la liste des addresses email en copie sous la forme d'une chaine de caractère où chaque addresse est séparée par un ','
     * 
     * @return
     */
    public String getCcEmailAddressesAsString() {
        return Joiner.on(",").skipNulls().join(this.getCcEmailAddressesAsStringList());
    }

    public String getBccEmailAddressesAsString() {
        return Joiner.on(",").skipNulls().join(this.getBccEmailAddressesAsStringList());
    }

    public List<String> getToEmailAddressesAsStringList() {
        return this.getEmailAddressesAsString(this.toEmailAddresses);
    }

    public List<String> getCcEmailAddressesAsStringList() {
        return this.getEmailAddressesAsString(this.ccEmailAddresses);
    }

    public List<String> getBccEmailAddressesAsStringList() {
        return this.getEmailAddressesAsString(this.bccEmailAddresses);
    }

    private List<String> getEmailAddressesAsString(List<EmailAddress> emailAddresses) {
        List<String> emailAddressesAsString = new ArrayList<String>();
        if (emailAddresses != null) {
            for (EmailAddress emailAddress : emailAddresses) {
                emailAddressesAsString.add(emailAddress.getValue());
            }
        }
        return emailAddressesAsString;
    }

    public boolean hasAttachements() {
        return this.attachements != null && !this.attachements.isEmpty();
    }

    public void addAttachement(File attachement) {
        Preconditions.checkNotNull(attachement, "attachement");
        this.attachements.add(attachement);
    }

    public void addAttachement(String fileName) {
        Preconditions.checkNotNull(fileName, "fileName");
        this.attachements.add(new File(fileName));
    }

    public List<File> getAttachements() {
        return Collections.unmodifiableList(this.attachements);
    }

}
