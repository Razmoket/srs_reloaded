/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/checkers/DomainNameChecker.java#8 $
 * $Revision: #8 $
 * $Author: ginguene $
 */

package fr.afnic.commons.checkers;

import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.invalidformat.InvalidFormatException;
import fr.afnic.commons.services.tld.TldServiceFacade;

/***
 * Classe permettant de savoir si un nom de domaine est valide ou non. <br/>
 * 
 * 
 * TODO Cette classe doit disparaitre a terme au profit d'un service centralisé qui permettra de gérér plus facilement d'autres extensions.
 * 
 * @author ginguene
 * 
 */
public final class DomainNameAreaChecker implements IExternalChecker {

    private static final long serialVersionUID = 1L;

    public static void main(String[] args) {
        new DomainNameAreaChecker().check("nomdedomaine-1315793290490.fr", new UserId(22), TldServiceFacade.Fr);
    }

    @Override
    public String check(String listDomainName, UserId userId, TldServiceFacade tld) throws InvalidFormatException {
        if (listDomainName != null && listDomainName.length() > 0) {
            DomainNameChecker checker = new DomainNameChecker();
            listDomainName = listDomainName.replaceAll("\\\r\\\n", "\\\n");
            String[] listDomain = listDomainName.split("\\\n");
            for (String domain : listDomain) {
                checker.check(domain, userId, tld);
            }
        }
        return listDomainName;
    }
}
