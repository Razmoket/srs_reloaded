/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.beans.description;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import fr.afnic.commons.beans.logs.ILogger;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;

/**
 * Permet de stocker et de récupérer des IDescriber
 * 
 * @author ginguene
 * 
 */
public class DescriberMap {

    private final Map<Class<? extends IDescribedExternallyObject>, IDescriber<?>> map = new HashMap<Class<? extends IDescribedExternallyObject>, IDescriber<?>>();

    private static final ILogger LOGGER = AppServiceFacade.getLoggerService().getLogger(DescriberMap.class);

    public <D extends IDescribedExternallyObject> void put(Class<D> clazz, IDescriber<D> describer) {
        this.map.put(clazz, describer);
    }

    @SuppressWarnings("unchecked")
    public <D extends IDescribedExternallyObject> IDescriber<D> get(Class<D> clazz) {
        return (IDescriber<D>) this.map.get(clazz);
    }

    public <D extends IDescribedExternallyObject> boolean contains(Class<D> clazz) {
        return this.map.containsKey(clazz);
    }

    @SuppressWarnings("unchecked")
    public <D extends IDescribedExternallyObject> String getDescription(D object, Locale locale, UserId userId, TldServiceFacade tld) throws ServiceException {

        if (this.map.containsKey(object.getClass())) {
            IDescriber<D> describer = (IDescriber<D>) this.map.get(object.getClass());
            return describer.getDescription(object, locale, userId, tld);
        } else {
            // Si on n'a pas trouvé de describer, on regarde si la classe de l'objet n'hérite pas d'une des classe stockée dans la map
            for (Class<?> mapClass : this.map.keySet()) {
                if (mapClass.isAssignableFrom(object.getClass())) {
                    // Si on trouve une association on la sauvegarde pour ne pas refaire le travail la fois suivante
                    IDescriber<D> describer = (IDescriber<D>) this.map.get(mapClass);
                    this.map.put(object.getClass(), describer);
                    return describer.getDescription(object, locale, userId, tld);
                }
            }
            DescriberMap.LOGGER.warn("no describer for " + object.getClass().getSimpleName());
            return "[" + object.getDictionaryKey() + "]";
        }
    }
}
