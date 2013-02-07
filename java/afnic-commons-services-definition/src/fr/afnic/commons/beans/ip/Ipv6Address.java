/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/beans/ip/Ipv6Address.java#1 $
 * $Revision: #1 $
 * $Author: alaphilippe $
 */

package fr.afnic.commons.beans.ip;

/**
 * Représente une addresse IPV6.
 * 
 * @author ginguene
 * 
 */
public class Ipv6Address extends IpAddress {

    /**
     * Constructeur prendant en parametre une chaine de caractère représentant l'ip.
     * 
     * @param ip
     *            String représentant l'ip
     */
    public Ipv6Address(String ip) {
        super(ip);
    }

    @Override
    public String getType() {
        return "IPV6";
    }
}
