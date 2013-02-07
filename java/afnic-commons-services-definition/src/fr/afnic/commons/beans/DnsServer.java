/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/beans/DnsServer.java#2 $
 * $Revision: #2 $
 * $Author: ginguene $
 */

package fr.afnic.commons.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import fr.afnic.commons.beans.ip.IpAddress;

/**
 * Serveur Dns identifié par un nom et possédant une liste d'addresse ip
 * 
 * 
 * @author ginguene
 * 
 */
public class DnsServer implements Serializable {

    private String name;
    private List<IpAddress> addresses;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<IpAddress> getAddresses() {
        return this.addresses;
    }

    public void setAddresses(List<IpAddress> addresses) {
        this.addresses = addresses;
    }

    /**
     * Ajoute une addresse au serveur DNS.
     * 
     * @param address
     *            Addresse à ajouter.
     */
    public void addAddress(IpAddress address) {
        if (this.addresses == null) {
            this.addresses = new ArrayList<IpAddress>();
        }
        this.addresses.add(address);
    }
}
