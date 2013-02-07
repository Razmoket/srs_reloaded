/*
 * $Id: ContactPortfolioToCsv.java,v 1.1 2010/08/11 10:04:40 ginguene Exp $
 * $Revision: 1.1 $
 * $Author: ginguene $
 */

package fr.afnic.commons.export.csv;

import java.util.List;

import fr.afnic.commons.beans.domain.Domain;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;

/**
 * Assure la convertion d'une liste de domaine au format Csv.
 * 
 * 
 * @author ginguene
 * 
 */
public final class ContactPortfolioToCsv {

    /**
     * Constucteur privé pour empecher l'instanciation
     * 
     */
    private ContactPortfolioToCsv() {
    }

    /**
     * Converti une liste de domaine au format CSV.
     * 
     * @param stats
     *            Liste de Statistiques à convertir
     * 
     * @return Une chaine de caratère au format CSV.
     * @throws DaoException
     */
    public static String format(List<String> domains, UserId userId, TldServiceFacade tld) throws ServiceException {
        StringBuffer buffer = new StringBuffer();

        buffer.append("Nom, date anniversaire, date de création, status, opération en cours\n");
        for (String domainName : domains) {
            Domain domain = AppServiceFacade.getDomainService().getDomainWithName(domainName, userId, tld);

            buffer.append(domainName + ","
                          + domain.getAnniversaryDateStr() + ","
                          + domain.getCreateDateStr() + ","
                          + domain.getStatus() + "\n");
        }

        return buffer.toString();
    }

}
