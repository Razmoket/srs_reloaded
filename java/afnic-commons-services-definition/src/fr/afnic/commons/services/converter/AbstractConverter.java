package fr.afnic.commons.services.converter;

import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.utils.ToStringHelper;

public abstract class AbstractConverter<IN, OUT> {

    protected final Class<IN> inClass;

    protected final Class<OUT> outClass;

    protected AbstractConverter(Class<IN> inClass, Class<OUT> outClass) {
        this.inClass = inClass;
        this.outClass = outClass;
    }

    public abstract OUT convert(IN toConvert, UserId userId, TldServiceFacade tld) throws ServiceException;

    public Class<IN> getInClass() {
        return this.inClass;
    }

    public Class<OUT> getOutClass() {
        return this.outClass;
    }

    @Override
    public String toString() {
        return new ToStringHelper().addAllObjectAttributes(this).toString();
    }
}