/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/beans/contactdetails/Country.java#20 $
 * $Revision: #20 $
 * $Author: barriere $
 */

package fr.afnic.commons.beans.contactdetails;

import java.util.Collections;
import java.util.Locale;
import java.util.Set;

import fr.afnic.commons.beans.description.IDescribedExternallyObject;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.beans.validatable.ObjectValue;
import fr.afnic.commons.checkers.CountryCodeChecker;
import fr.afnic.commons.checkers.IInternalChecker;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.utils.LocaleUtils;

public class Country extends ObjectValue implements IDescribedExternallyObject {

    private static final long serialVersionUID = 1L;

    /** Pays les plus utilisés */
    public static final Country FR = new Country(250, "FR", new UserId(22), TldServiceFacade.Fr);
    public static final Country GB = new Country(826, "GB", new UserId(22), TldServiceFacade.Fr);

    private Set<Region> regions = null;

    private Locale locale = LocaleUtils.getDefaultLanguageLocale();

    /**
     * id dans la table nicope.region.<br/>
     * si l'id vaut -1, il corresptoutes les régions du pays.
     **/
    private final int id;

    /** le user appelant l'objet */
    protected final UserId userIdCaller;
    /** le tld sur lequel on appelle l'objet */
    protected final TldServiceFacade tldCaller;

    public Country(int id, String value, UserId userId, TldServiceFacade tld) {
        this(id, value, null, userId, tld);
    }

    public Country(int id, String value, Set<Region> regions, UserId userId, TldServiceFacade tld) {
        super(value);

        if (value == null) {
            throw new IllegalArgumentException("value cannot be null");
        }
        this.id = id;
        this.regions = regions;
        this.userIdCaller = userId;
        this.tldCaller = tld;
    }

    @Override
    protected IInternalChecker createChecker() {
        return new CountryCodeChecker();
    }

    /**
     * Retourne le nom du pays
     * 
     */
    @Override
    public String getDescription(UserId userId, TldServiceFacade tld) throws ServiceException {
        return this.getDescription(this.locale, userId, tld);
    }

    @Override
    public String getDescription(Locale locale, UserId userId, TldServiceFacade tld) throws ServiceException {
        return AppServiceFacade.getDictionaryService().getDescription(this, locale, userId, tld);
    }

    /**
     * Méthode qui peut disparaitre en faisant le todo de unification avec le modèle de ObjectValue en renommant la méthode getCode() en getValue(), <br/>
     * mais pour le moment trop d'impact notamment sur les RequestStatus
     * 
     */
    @Override
    public String getDictionaryKey() {
        return this.getValue();
    }

    public int getRegionsCount() throws ServiceException {
        return this.getRegions().size();
    }

    public Set<Region> getRegions() throws ServiceException {
        if (this.regions == null) {
            this.regions = AppServiceFacade.getDictionaryService().getRegions(this.getDictionaryKey(), this.userIdCaller, this.tldCaller);
        }

        if (this.regions == null) {
            this.regions = Collections.emptySet();
        }

        return this.regions;
    }

    public boolean isAllRegionOfACountry() {
        return false;
    }

    public int getId() {
        return this.id;
    }

    public void setRegions(Set<Region> regions) {
        this.regions = regions;
    }

    @Override
    public void setLocale(Locale locale) throws ServiceException {
        this.locale = locale;
        for (Region region : this.getRegions()) {
            region.setLocale(locale);
        }
    }

}
