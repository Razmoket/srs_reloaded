package fr.afnic.commons.beans.dnssec;

import java.util.Locale;

import fr.afnic.commons.beans.description.IDescribedInternalObject;
import fr.afnic.commons.services.exception.ServiceException;

public enum AlgoHash implements IDescribedInternalObject {

    NON_RENSEIGNE(""),
    RSAMD5("RSAMD5"),
    DH("DH"),
    DSA("DSA"),
    ECC("ECC"),
    RSASHA1("RSASHA1"),
    DSA_NSEC3_SHA1("DSA-NSEC3-SHA1"),
    RSASHA1_NSEC3_SHA1("RSASHA1-NSEC3-SHA1"),
    RSASHA256("RSASHA256"),
    RSASHA512("RSASHA512"),
    ECC_GOST("ECC-GOST"),
    ECDSAP256SHA256("ECDSAP256SHA256"),
    ECDSAP384SHA384("ECDSAP384SHA384"),
    INDIRECT("INDIRECT"),
    PRIVATEDNS("PRIVATEDNS"),
    PRIVATEOID("PRIVATEOID");

    private String description;

    private AlgoHash(String description) {
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
