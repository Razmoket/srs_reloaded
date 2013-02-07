package fr.afnic.commons.beans.cache;

import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

/**
 * Permet de stocker un objet soit via son id, soit via une instance de l'objet.
 * Le cache permet de pouvoir récupérer l'id ou une instance et de conserver en interne la cohérence entre les 2.
 * 
 * @author ginguene
 *
 */
public abstract class IdentifiedObjectCache<ID, VALUE> {

    protected ID id = null;
    protected VALUE value = null;

    public void setValue(VALUE value) {
        this.value = value;
        this.id = null;
    }

    public ID getId() {
        if (this.id != null) {
            return this.id;
        } else if (this.value != null) {
            this.id = this.getIdFromValue();
        }

        return this.id;
    }

    public VALUE getValue(UserId userId, TldServiceFacade tld) throws ServiceException {

        if (this.value != null) {
            return this.value;
        } else if (this.id != null) {
            this.value = this.getValueFromId(userId, tld);
        }

        return this.value;
    }

    public void setId(ID id) {
        if (this.value == null || this.getIdFromValue() != id) {
            this.id = id;
            this.value = null;
        }
    }

    public boolean isSet() {
        return this.id != null || this.value != null;
    }

    /**
     * Méthode à implémenter chargée de retourner l'id à partir de la value.
     * N'est appelé que si this.value n'est pas null.
     * Ne doit utiliser que le this.value et jamais le getValue();
     */
    protected abstract ID getIdFromValue();

    /**
     * Méthode à implémenter chargée de retourner la value à partir de l'id.
     * N'est appelé que si la this.id n'est pas null.
     * Ne doit utiliser que le this.id et jamais le getId();
     * @throws ServiceException 
     */
    protected abstract VALUE getValueFromId(UserId userId, TldServiceFacade tld) throws ServiceException;

}
