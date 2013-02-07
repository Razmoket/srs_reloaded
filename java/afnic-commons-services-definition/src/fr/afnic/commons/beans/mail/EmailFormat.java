package fr.afnic.commons.beans.mail;

/**
 * Format d'un mail
 * 
 * 
 * @author ginguene
 * 
 */
public enum EmailFormat {
    TEXT("text/plain; charset=UTF-8"),
    HTML("text/html; charset=UTF-8");

    private final String encoding;

    private EmailFormat(String encoding) {
        this.encoding = encoding;
    }

    public String getEncoding() {
        return this.encoding;
    }
}
