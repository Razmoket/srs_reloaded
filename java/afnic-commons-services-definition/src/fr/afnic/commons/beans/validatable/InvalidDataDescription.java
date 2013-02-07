package fr.afnic.commons.beans.validatable;

import java.util.Locale;

import fr.afnic.commons.beans.description.IDescribedInternalObject;
import fr.afnic.commons.services.exception.NotImplementedException;
import fr.afnic.commons.services.exception.ServiceException;

/**
 * 
 * Description d'une donnée invalide
 * 
 * @author ginguene
 * 
 */
public class InvalidDataDescription implements IDescribedInternalObject {

    private static final long serialVersionUID = 1L;

    protected String description;

    public InvalidDataDescription(String description) {
        if (description == null) {
            throw new IllegalArgumentException("description cannot be null");
        }
        this.description = description;
    }

    @Override
    public String getDescription(Locale locale) throws ServiceException {
        throw new NotImplementedException();
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public String getDictionaryKey() {
        return this.getClass().getSimpleName();
    }

    @Override
    public void setLocale(Locale locale) throws ServiceException {
        throw new NotImplementedException();
    }

}
