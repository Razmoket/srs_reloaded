/*
 * $Id: $
 * $Revision: $
 */

package fr.afnic.commons.checkers;

import java.util.regex.Pattern;

import fr.afnic.commons.beans.request.reserveddomains.SpecialDomain;

/**
 * Checker un peu particulier car n'implementant pas IChecker. Il permet de savoir si l'on a à faire à un domaines reservé.
 * 
 * 
 * 
 */
public final class SpecialDomainChecker {

    private static final String[] SPECIAL_DOMAIN_PATTERNS = { "\\.gouv\\.fr$",
                                                             "\\.tm\\.fr$",
                                                             "\\.asso\\.fr$",
                                                             "\\.gouv\\.fr$",
                                                             "^cc-.*\\.fr$",
                                                             "^cg-.*\\.fr$",
                                                             "^cr-.*\\.fr$",
                                                             "^mairie-.*\\.fr$",
                                                             "^ville-.*\\.fr$",
                                                             "^agglo-.*\\.fr$" };

    public static SpecialDomain getReservedDomainNameMotivation(String domainName) {
        for (String specialDomainPattern : SpecialDomainChecker.SPECIAL_DOMAIN_PATTERNS) {
            Pattern pattern = Pattern.compile(specialDomainPattern);
            if (pattern.matcher(domainName).find()) {
                specialDomainPattern = specialDomainPattern.replaceAll("\\\\", "")
                                                           .replaceAll("\\$$", "")
                                                           .replaceAll("^\\^", "")
                                                           .replaceAll("\\.\\*", "*");
                return new SpecialDomain(specialDomainPattern);
            }
        }

        return null;
    }

    private SpecialDomainChecker() {

    }

}
