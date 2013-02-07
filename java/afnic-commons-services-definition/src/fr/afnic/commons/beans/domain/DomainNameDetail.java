package fr.afnic.commons.beans.domain;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

public class DomainNameDetail implements Serializable {

	private static final long serialVersionUID = 1L;

	/**nom avec les accent: ex: test-àéù.fr*/
    protected String utf8;

    /**nom sans accent commencant par xn--. ex:xn--test--0wb2r4n.fr*/
    protected String ldh;

    /**nom sans accent: ex: test-aeu.fr, permet de repérer des familles de domaine*/
    protected String asciiEquivalent;

    public String getUtf8() {
        return this.utf8;
    }

    public void setUtf8(String utf8) {
        this.utf8 = utf8;
    }

    public String getLdh() {
        return this.ldh;
    }

    public void setLdh(String ldh) {
        this.ldh = ldh;
    }

    public String getAsciiEquivalent() {
        return this.asciiEquivalent;
    }

    public void setAsciiEquivalent(String asciiEquivalent) {
        this.asciiEquivalent = asciiEquivalent;
    }

    public boolean isNotAsciiName() {
        return !this.isAsciiName();
    }

    public boolean isAsciiName() {
        return StringUtils.equalsIgnoreCase(this.ldh, this.utf8);
    }

}
