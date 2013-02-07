/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.beans.description;

import java.io.Serializable;
import java.util.Locale;

import fr.afnic.commons.services.exception.ServiceException;

/**
 * Objet contenant une description.<br/>
 * utilisé pour tagguer les enumération du packages beans.
 * 
 * 
 * TODO unification avec le modèle de ObjectValue en renommant la méthode getCode() en getValue(), <br/>
 * mais pour le moment trop d'impact notamment sur les RequestStatus
 * 
 * @author ginguene
 * 
 */
public interface IDescribedObject extends Serializable {

    /**
     * Définit la locale dans laquelle on veut que getDescription retourne le texte
     */
    public void setLocale(Locale locale) throws ServiceException;

    /**
     * retourne l'identifiant qui est utilisé pour obtenir la description
     * 
     * @return
     */
    public String getDictionaryKey();

}
