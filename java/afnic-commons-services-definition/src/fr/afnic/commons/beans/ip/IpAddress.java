/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/beans/ip/IpAddress.java#2 $
 * $Revision: #2 $
 * $Author: ginguene $
 */

package fr.afnic.commons.beans.ip;

import java.io.Serializable;

/**
 * Représente une addresse IP.
 * 
 * @author ginguene
 * 
 */
public abstract class IpAddress implements Serializable {

    private String ip;

    /**
     * Constructeur par défaut
     */
    public IpAddress() {
    }

    /**
     * Constructeur ayant une ip sous forme de<br/>
     * chaine de caractère comme parametre
     * 
     * @param ip
     *            ip au format texte
     */
    public IpAddress(String ip) {
        this.ip = ip;
    }

    public String getIp() {
        return this.ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * Retourne une chaine representant le type d'adresse IP (ex IPV4)
     * 
     * @param ip
     * @return une string représentant le type d'ip
     */
    public abstract String getType();

}
