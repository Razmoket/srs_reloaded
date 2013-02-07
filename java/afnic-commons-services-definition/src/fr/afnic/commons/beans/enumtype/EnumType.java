package fr.afnic.commons.beans.enumtype;

import java.util.Locale;

import org.apache.commons.lang.StringUtils;

import fr.afnic.commons.beans.description.IDescribedExternallyObject;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.beans.validatable.InvalidDataDescription;
import fr.afnic.commons.beans.validatable.InvalidEnumTypeDescription;
import fr.afnic.commons.beans.validatable.ObjectValue;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.utils.LocaleUtils;

public class EnumType extends ObjectValue implements IDescribedExternallyObject {

    private static final long serialVersionUID = 1L;

    protected boolean isInvalid;

    protected Locale locale = LocaleUtils.getDefaultLanguageLocale();;

    protected EnumType(UserId userId, TldServiceFacade tld) {
        super("");
        this.isInvalid = (this instanceof IInvalidEnumType);
    }

    protected EnumType(String value) {
        super(value);
        if (value == null) {
            throw new IllegalArgumentException("value cannot be null.");
        }
        this.isInvalid = (this instanceof IInvalidEnumType);
    }

    @Override
    public String getName() {
        if (StringUtils.isBlank(this.value)) {
            // Au premier appel de getName si l'on est passé par le constructeur par défaut.
            return this.getClass().getSimpleName();
        }
        return this.value;
    }

    @Override
    public String getDescription(UserId userId, TldServiceFacade tld) throws ServiceException {
        return AppServiceFacade.getDictionaryService().getDescription(this, this.locale, userId, tld);
    }

    @Override
    public String getDescription(Locale locale, UserId userId, TldServiceFacade tld) throws ServiceException {
        return AppServiceFacade.getDictionaryService().getDescription(this, locale, userId, tld);
    }

    public boolean isInvalid() {
        return this.isInvalid;
    }

    @Override
    public InvalidDataDescription checkInvalidData() {
        if (this instanceof IInvalidEnumType) {
            IInvalidEnumType invalidEnumType = (IInvalidEnumType) this;
            return new InvalidEnumTypeDescription(invalidEnumType);
        } else {
            return null;
        }
    }

    @Override
    public String getDictionaryKey() {
        return this.value;
    }

    @Override
    public void setLocale(Locale locale) throws ServiceException {
        this.locale = locale;
    }
}
