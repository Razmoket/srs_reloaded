/*
 * $Id: $
 * $Revision: $
 */

package fr.afnic.commons.i18n;

import fr.afnic.commons.beans.contact.CustomerContact;
import fr.afnic.commons.beans.contactdetails.Country;
import fr.afnic.commons.beans.contactdetails.PostalAddress;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.utils.LocaleUtils;

/**
 * Singleton permettant d'initialiser les descriptions des objets dans une langue définie par une locale
 * 
 * 
 * 
 * 
 */
public final class DescriptionLocalizer {

    private static final DescriptionLocalizer INSTANCE = new DescriptionLocalizer();

    public static DescriptionLocalizer getInstance() {
        return DescriptionLocalizer.INSTANCE;
    }

    private DescriptionLocalizer() {

    }

    public CustomerContact localize(CustomerContact contact, String locale) throws ServiceException {

        this.localize(contact.getPostalAddress(), locale);
        return contact;
    }

    public PostalAddress localize(PostalAddress postalAddress, String locale) throws ServiceException {
        if (postalAddress != null && postalAddress.getCountry() != null) {
            Country country = postalAddress.getCountry();
            country.setLocale(LocaleUtils.getLocale(locale));
        }
        return postalAddress;
    }

}
