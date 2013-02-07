/*
 * $Id: $
 * $Revision: $
 */

package fr.afnic.commons.beans.contactdetails;


public class AllRegionsOfACountry extends Region {

    private static final long serialVersionUID = 1L;

    public AllRegionsOfACountry(Country country) {
        super(country.getId(), country);
    }

    @Override
    public boolean isAllRegionsOfACountry() {
        return true;
    }

    @Override
    public int getId() {
        return this.getCountry().getId();
    }

}
