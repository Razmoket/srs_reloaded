package fr.afnic.commons.beans.mail;

/**
 * Header d'un mail
 * 
 * @author ginguene
 * 
 */
public class MailHeader {

    private final String name;
    private final String value;

    public MailHeader(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
