/*
 * $Id: $
 * $Revision: $
 */

package fr.afnic.commons.services.sql.describer;

import java.util.HashMap;
import java.util.Locale;
import java.util.Set;

import fr.afnic.commons.beans.description.IDescribedExternallyObject;
import fr.afnic.commons.utils.Preconditions;

public class DescriptionMap<DESCRIBED_OBJECT extends IDescribedExternallyObject> {
    private final HashMap<DESCRIBED_OBJECT, HashMap<String, String>> map = new HashMap<DESCRIBED_OBJECT, HashMap<String, String>>();

    public void addDesc(DESCRIBED_OBJECT object, Locale locale, String desc) {
        this.getDescMap(object).put(this.getLocaleKey(locale), desc);
    }

    public Set<DESCRIBED_OBJECT> getObjects() {
        return this.map.keySet();
    }

    public String getDescription(DESCRIBED_OBJECT object, Locale locale) {
        Preconditions.checkNotNull(object, "object");

        if (this.mapContains(object, locale)) {
            return this.getMapDescription(object, locale);
        } else if (this.mapContains(object, this.getDefaultLocale())) {
            return this.getMapDescription(object, this.getDefaultLocale());
        } else {
            return this.getDefaultDescription(object, locale);
        }
    }

    public String getDefaultDescription(DESCRIBED_OBJECT object, Locale locale) {
        return object.toString();
    }

    private boolean mapContains(DESCRIBED_OBJECT object, Locale locale) {
        return this.getDescMap(object).containsKey(this.getLocaleKey(locale));
    }

    private String getMapDescription(DESCRIBED_OBJECT object, Locale locale) {
        return this.getDescMap(object).get(this.getLocaleKey(locale));
    }

    /**
     * Locale utilisée si il n'existe pas de description pour la locale demandée.
     * 
     * @return
     */
    private Locale getDefaultLocale() {
        return Locale.ENGLISH;
    }

    private HashMap<String, String> getDescMap(DESCRIBED_OBJECT object) {
        if (!this.map.containsKey(object)) {
            this.map.put(object, new HashMap<String, String>());
        }
        return this.map.get(object);
    }

    private String getLocaleKey(Locale locale) {
        return locale.getLanguage();
    }

}