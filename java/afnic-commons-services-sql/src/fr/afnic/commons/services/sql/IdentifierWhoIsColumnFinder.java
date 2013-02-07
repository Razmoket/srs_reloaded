/*
 * $Id: IdentifierWhoIsColumnFinder.java,v 1.1 2010/09/06 13:11:43 ginguene Exp $ $Revision: 1.1 $ $Author: ginguene $
 */

package fr.afnic.commons.services.sql;

import fr.afnic.commons.beans.corporateentity.CorporateEntityIdentifier;
import fr.afnic.commons.beans.corporateentity.Siren;
import fr.afnic.commons.beans.corporateentity.Siret;
import fr.afnic.commons.beans.corporateentity.TradeMark;
import fr.afnic.commons.services.exception.ServiceException;

/**
 * Permet d'associé la valeur de la colonne type de la table Whois.identification2.
 * 
 * 
 * @author ginguene
 * 
 */
public final class IdentifierWhoIsColumnFinder {

    /**
     * Constructeur privé car il s'agit d'une classe utilitaire.
     */
    private IdentifierWhoIsColumnFinder() {
    }

    /**
     * @param identifier
     *            Identifiant dont on souhaite avoir la valeur correspondante dans la colonne Whois.identification2.type
     * 
     * @return
     * @throws ServiceException
     *             Si l'on ne connait pas la valeur que doit prendre la colonne pour un identifiant.<br/>
     *             Cela peut arriver si l'on gère de nouveaux identifiants (ex Waldec);
     */
    public static String getColumnContent(final CorporateEntityIdentifier identifier) throws ServiceException {

        if (identifier instanceof Siren) {
            return "SIRENE";
        }

        if (identifier instanceof Siret) {
            return "SIRENE";
        }

        if (identifier instanceof TradeMark) {
            return "MARQUE";
        }

        throw new ServiceException("No Whois.identification2.type value corresponding to " + identifier.getClass().getSimpleName());
    }

}
