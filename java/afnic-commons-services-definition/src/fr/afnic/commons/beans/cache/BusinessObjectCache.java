package fr.afnic.commons.beans.cache;

import java.io.Serializable;

import fr.afnic.commons.beans.BusinessObject;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.beans.validatable.NumberId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class BusinessObjectCache<ID extends NumberId<VALUE>, VALUE extends BusinessObject<ID>> extends IdentifiedObjectCache<ID, VALUE> implements Serializable {

    @Override
    public void setValue(VALUE value) {
        super.setValue(value);
    }

    @Override
    public void setId(ID id) {
        super.setId(id);
    }

    @Override
    protected ID getIdFromValue() {
        return this.value.getId();
    }

    @Override
    protected VALUE getValueFromId(UserId userId, TldServiceFacade tld) throws ServiceException {
        return this.id.getObjectOwner(userId, tld);
    }

}
