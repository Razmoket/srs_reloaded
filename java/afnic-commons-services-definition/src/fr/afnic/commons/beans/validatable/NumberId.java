/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/beans/validatable/NumberId.java#26 $ $Revision: #26 $ $Author:
 * ginguene $
 */

package fr.afnic.commons.beans.validatable;

import java.io.Serializable;

import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.NotFoundException;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

/**
 * Id représenté par un nombre dont on souhaite pouvoir faire facilement référence sous forme de nombre ou de String
 * 
 * @author ginguene
 * 
 */
public abstract class NumberId<OBJECT extends Object> extends ObjectValue implements Serializable {

    private static final long serialVersionUID = 1L;

    private int intValue = -1;

    public NumberId() {
    }

    public NumberId(String id) {
        super(id);
    }

    public NumberId(int id) {
        super(Long.toString(id));
        this.intValue = id;
    }

    @Override
    public InvalidDataDescription checkInvalidData() {
        try {
            this.intValue = Integer.parseInt(this.value);
            if (this.intValue <= 0) {
                return new InvalidNumberIdDescription(this.value, InvalidNumberIdDescription.Error.SMALLER_THAN_ZERO);
            }

            return null;
        } catch (Exception e) {
            return new InvalidNumberIdDescription(this.value, InvalidNumberIdDescription.Error.NOT_NUMBER);
        }
    }

    public int getIntValue() {
        this.validate();
        return this.intValue;

    }

    @Override
    public int hashCode() {
        try {
            return this.getIntValue();
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        return this.getIntValue() == ((NumberId<?>) obj).getIntValue();
    }

    public boolean isNotExisting(UserId userId, TldServiceFacade tld) throws ServiceException {
        return !this.isExisting(userId, tld);
    }

    /**
     * Indique si un objet est trouvé dans la base pour cet identifiant.
     * @throws ServiceException 
     */
    public boolean isExisting(UserId userId, TldServiceFacade tld) throws ServiceException {
        try {
            this.getObjectOwner(userId, tld);
            return true;
        } catch (NotFoundException e) {
            return false;
        }
    }

    /**
     * 
     */
    public abstract OBJECT getObjectOwner(UserId userId, TldServiceFacade tld) throws ServiceException;

}
