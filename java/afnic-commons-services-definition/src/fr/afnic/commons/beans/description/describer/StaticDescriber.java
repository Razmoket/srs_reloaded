/*
 * $Id: $ $Revision: $
 */

package fr.afnic.commons.beans.description.describer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import fr.afnic.commons.beans.description.IDescribedExternallyObject;
import fr.afnic.commons.beans.description.IDescriber;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

/**
 * Classe mère facilitant la gestion des descriptions mutlilingue en dur.<br/>
 * Si une locale n'est pas géré, on retrourne la descriptin en anglais.
 * 
 * 
 * 
 */
public abstract class StaticDescriber<DESCRIBED_OBJECT extends IDescribedExternallyObject> implements IDescriber<DESCRIBED_OBJECT> {

    public static final Locale DEFAULT_LOCALE = Locale.ENGLISH;

    private final Map<Locale, HashMap<DESCRIBED_OBJECT, String>> map = new HashMap<Locale, HashMap<DESCRIBED_OBJECT, String>>();

    protected void addDescription(Locale locale, DESCRIBED_OBJECT object, String description) {
        this.getLocaleMap(locale).put(object, description);
    }

    @Override
    public String getDescription(DESCRIBED_OBJECT object, Locale locale, UserId userId, TldServiceFacade tld) throws ServiceException {
        Locale localeToUse = this.getLocaleToUse(locale);
        return this.getLocaleMap(localeToUse).get(object);
    }

    private Locale getLocaleToUse(Locale locale) {
        if (this.hasDescriptionForLocale(locale)) {
            return locale;
        } else {
            return StaticDescriber.DEFAULT_LOCALE;
        }
    }

    private boolean hasDescriptionForLocale(Locale locale) {
        return this.map.containsKey(locale);
    }

    public Set<DESCRIBED_OBJECT> getObjects(Locale locale) throws ServiceException {
        HashMap<DESCRIBED_OBJECT, String> localeMap = this.getLocaleMap(locale);

        Set<DESCRIBED_OBJECT> objects = new HashSet<DESCRIBED_OBJECT>();
        objects.addAll(localeMap.keySet());

        for (DESCRIBED_OBJECT object : objects) {
            object.setLocale(locale);
        }
        return objects;
    }

    private HashMap<DESCRIBED_OBJECT, String> getLocaleMap(Locale locale) {
        if (!this.map.containsKey(locale)) {
            this.map.put(locale, new HashMap<DESCRIBED_OBJECT, String>());
        }
        return this.map.get(locale);
    }

}
