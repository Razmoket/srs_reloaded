/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.beans.contactdetails;

import java.util.Locale;

import fr.afnic.commons.beans.description.IDescribedExternallyObject;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.beans.validatable.ObjectValue;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.utils.Preconditions;
import fr.afnic.utils.LocaleUtils;

/**
 * Region d'un pays.<br/>
 * Données relié à la table nicope.region, le code correspond à la colonne id et la description à la colonne nom_fr.
 * 
 * 
 * 
 * 
 */
public class Region extends ObjectValue implements IDescribedExternallyObject {

    private static final long serialVersionUID = 1L;

    private Country country;
    private Locale locale = LocaleUtils.getDefaultLanguageLocale();

    /**
     * id dans la table nicope.region.<br/>
     * si l'id vaut -1, il correspond à toutes les régions du pays.
     **/
    private final int id;

    public Region(int id, Country country) {
        super(Integer.toString(id));
        this.id = id;
        this.country = Preconditions.checkNotNull(country, "country");
    }

    /**
     * Utilisé pour une compatibilité avec wicket qui pour les converter a besoin d'un constructeur avec le parametre value pour les objects values.<br/>
     * TODO Il s'agit donc d'une dépendance au framework web dans la partie commons, cela ne devrait pas exister et devra etre supprimer.
     * 
     * @param value
     */
    public Region(String value) {
        super(value);
        this.id = Integer.parseInt(value);
    }

    @Override
    public String getDescription(UserId userId, TldServiceFacade tld) throws ServiceException {
        return this.getDescription(this.locale, userId, tld);
    }

    @Override
    public String getDescription(Locale locale, UserId userId, TldServiceFacade tld) throws ServiceException {
        return AppServiceFacade.getDictionaryService().getDescription(this, locale, userId, tld);
    }

    @Override
    public String getDictionaryKey() {
        return this.value;
    }

    public Country getCountry() {
        return this.country;
    }

    @Override
    public int hashCode() {
        return this.id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!super.equals(obj)) return false;
        if (this.getClass() != obj.getClass()) return false;
        Region other = (Region) obj;
        return this.getId() == other.getId();
    }

    public int getId() {
        return this.id;
    }

    public boolean isAllRegionsOfACountry() {
        return false;
    }

    @Override
    public void setLocale(Locale locale) throws ServiceException {
        this.locale = locale;
    }

}
