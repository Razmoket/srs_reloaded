/*
 * $Id: $
 * $Revision: $
 */

package fr.afnic.commons.services.sql.describer;

import java.io.Serializable;

import com.google.common.collect.Ordering;

import fr.afnic.commons.beans.contactdetails.Region;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class RegionOrdering extends Ordering<Region> implements Serializable {

    /** le user appelant l'objet */
    protected final UserId userIdCaller;
    /** le tld sur lequel on appelle l'objet */
    protected final TldServiceFacade tldCaller;

    public RegionOrdering(UserId userId, TldServiceFacade tld) {
        super();
        this.userIdCaller = userId;
        this.tldCaller = tld;
    }

    @Override
    public int compare(Region arg0, Region arg1) {
        if (arg0.isAllRegionsOfACountry()) {
            return -1;
        }

        if (arg0.isAllRegionsOfACountry()) {
            return -1;
        }

        try {
            return arg0.getDescription(this.userIdCaller, this.tldCaller).compareTo(arg1.getDescription(this.userIdCaller, this.tldCaller));
        } catch (ServiceException e) {
            return -1;
        }
    }
}