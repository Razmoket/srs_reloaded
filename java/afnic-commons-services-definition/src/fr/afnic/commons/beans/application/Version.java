/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/beans/application/Version.java#12 $
 * $Revision: #12 $
 * $Author: ginguene $
 */

package fr.afnic.commons.beans.application;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.afnic.commons.beans.application.env.Environnement;
import fr.afnic.commons.services.exception.invalidformat.InvalidFormatException;

/**
 * Représente une version sur 3 digit.<br/>
 * le séparateur par défaut est le .
 * 
 * 
 * @see fr.afnic.commons.beans.application.VersionTest;
 * @author ginguene
 * 
 */
public class Version implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Pattern VERSION_PATTERN = Pattern.compile("(\\d*)\\.(\\d*)\\.(\\d*)");

    private int major;
    private int minor;
    private int patch;

    /** Numéro de submit dans le gestionnaire de source */
    private int submitNumber;

    /** L'application concernée par la version */
    private Application application;

    /** Environnement concerné par la version */
    private Environnement environnement;

    /**
     * Constructeur prenant en parametre les 3 digit de la version.
     * 
     * @param major
     * @param minor
     * @param patch
     */
    public Version(int major, int minor, int patch, int submitNumber, Application application, Environnement environnement) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
        this.submitNumber = submitNumber;
        this.application = application;
        this.environnement = environnement;
    }

    public Version(int major, int minor, int patch, int submitNumber) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
        this.submitNumber = submitNumber;
    }

    public String getDisplayName() {
        String displayName = this.major + "." + this.minor + "." + this.patch;

        if (this.application != null) {
            displayName = this.application.getName() + " " + displayName;
        }

        if (this.environnement == null || this.environnement != Environnement.Prod) {
            displayName += "." + this.submitNumber;
        }

        if (this.environnement != null && this.environnement != Environnement.Prod) {
            displayName += " [" + this.environnement.getName() + "]";
        }

        return displayName;
    }

    public String getApplicationName() {
        return this.application.getName();
    }

    /**
     * Constructeur prenant en parametre les 3 digit de la version.
     * 
     * @param major
     * @param minor
     * @param patch
     */
    public Version(int major, int minor, int patch) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
    }

    /**
     * Constructeur par copie.
     * 
     * @param version
     * @throws InvalidFormatException
     */
    public Version(String version) throws InvalidFormatException {
        try {
            Matcher matcher = VERSION_PATTERN.matcher(version);
            if (matcher.matches()) {
                this.major = Integer.parseInt(matcher.group(1));
                this.minor = Integer.parseInt(matcher.group(2));
                this.patch = Integer.parseInt(matcher.group(3));
            } else {
                throw new InvalidFormatException(version, Version.class, "A.B.C");
            }
        } catch (Exception e) {
            throw new InvalidFormatException(version, Version.class, "A.B.C", e);
        }

    }

    public int getMajor() {
        return this.major;
    }

    public int getMinor() {
        return this.minor;
    }

    public int getPatch() {
        return this.patch;
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Version) {
            Version version = (Version) obj;
            return this.major == version.getMajor() && this.minor == version.getMinor() && this.patch == version.getPatch();
        } else {
            return false;
        }
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        assert false : "hashCode not designed";
        return -1;
    }

    /**
     * retroune 0 si le 2 versions sont les même 1 si this > version -1 si this < version
     * 
     * @param version
     * @return
     */
    public int compareTo(Version version) {
        if (this.major > version.getMajor()) return 1;
        if (this.major < version.getMajor()) return -1;
        if (this.minor > version.getMinor()) return 1;
        if (this.minor < version.getMinor()) return -1;
        if (this.patch > version.getPatch()) return 1;
        if (this.patch < version.getPatch()) return -1;
        if (this.submitNumber > version.getSubmitNumber()) return 1;
        if (this.submitNumber < version.getSubmitNumber()) return -1;
        return 0;
    }

    public String getDigitString() {
        return this.major + "." + this.minor + "." + this.patch;
    }

    public Application getApplication() {
        return this.application;
    }

    public Environnement getEnvironnement() {
        return this.environnement;
    }

    public int getSubmitNumber() {
        return this.submitNumber;
    }

    public void setSubmitNumber(int submitNumber) {
        this.submitNumber = submitNumber;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public void setEnvironnement(Environnement environnement) {
        this.environnement = environnement;
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return super.toString();
    }

}
