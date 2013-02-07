/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/beans/ip/Ipv4Address.java#1 $
 * $Revision: #1 $
 * $Author: alaphilippe $
 */

package fr.afnic.commons.beans.ip;

/**
 * Représente une addresse IPV4.
 * 
 * @author ginguene
 * 
 */
public class Ipv4Address extends IpAddress {

    /**
     * Constructeur ayant une ip sous forme de<br/>
     * chaine de caractère comme parametre.
     * 
     * @param ip
     *            ip au format text
     */
    public Ipv4Address(String ip) {
        super(ip);
    }

    @Override
    public String getType() {
        return "IPV4";
    }
}
