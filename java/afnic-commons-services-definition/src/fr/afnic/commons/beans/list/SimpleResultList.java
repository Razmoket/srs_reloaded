package fr.afnic.commons.beans.list;

import org.apache.commons.lang.NotImplementedException;

import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

/**
 * ResultList qui n'est pas lié à une vue.
 * Utilisée surtout dans la couche graphique pour convertir des liste d'objet en resultlist quand il n'existe pas encore
 * de méthode directe pour récupérer des resultList.
 * 
 *
 */
public class SimpleResultList extends ResultList<IView> {

    /** le user appelant l'objet */
    protected final UserId userIdCaller;
    /** le tld sur lequel on appelle l'objet */
    protected final TldServiceFacade tldCaller;

    public SimpleResultList(UserId userId, TldServiceFacade tld) {
        this.userIdCaller = userId;
        this.tldCaller = tld;

    }

    @Override
    public CharSequence createStream() throws ServiceException {
        throw new NotImplementedException();
    }
}