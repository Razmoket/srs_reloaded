/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/beans/statistics/Statistic.java#1 $
 * $Revision: #1 $
 * $Author: alaphilippe $
 */

package fr.afnic.commons.beans.statistics;

import java.util.Locale;

import fr.afnic.commons.beans.description.IDescribedInternalObject;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.utils.LocaleUtils;

public class Period implements IDescribedInternalObject {
    private Locale locale = LocaleUtils.getDefaultLanguageLocale();
    private final String id;
    private final String value;

    public Period(String id, String value) {
        this.id = id;
        this.value = value;
    }

    @Override
    public String getDescription() throws ServiceException {
        return this.getDescription(this.locale);
    }

    @Override
    public String getDescription(Locale locale) throws ServiceException {
        // TODO Auto-generated method stub
        return this.value;
    }

    @Override
    public void setLocale(Locale locale) throws ServiceException {
        this.locale = locale;
    }

    @Override
    public String getDictionaryKey() {
        return this.id;
    }

}
