package fr.afnic.commons.beans.dnssec;

import java.util.Locale;

import fr.afnic.commons.beans.description.IDescribedInternalObject;
import fr.afnic.commons.services.exception.ServiceException;

public enum DigestHash implements IDescribedInternalObject {

    RESERVED("Reserved"),
    SHA_1("SHA-1"),
    SHA_256("SHA-256"),
    GOST_R_34_11_94("GOST R 34.11-94"),
    SHA_384("SHA-384");

    private String description;

    private DigestHash(String description) {
        this.description = description;
    }

    @Override
    public void setLocale(Locale locale) throws ServiceException {
    }

    @Override
    public String getDictionaryKey() {
        return this.toString();
    }

    @Override
    public String getDescription() throws ServiceException {
        return this.description;
    }

    @Override
    public String getDescription(Locale locale) throws ServiceException {
        return this.description;
    }
}
