/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/beans/ContactSnapshot.java#5 $
 * $Revision: #5 $
 * $Author: ginguene $
 */

package fr.afnic.commons.beans;

import java.io.Serializable;
import java.util.Date;

import fr.afnic.utils.DateUtils;

/**
 * Représente le snapshot d'un contact whois. <br/>
 * Il s'agit d'une copie de l'état d'un contact dans une autre table.<br/>
 * On s'en sert pour garder la trace d'un contact à un instant T.<br/>
 * <br/>
 * Par exemple si le garbadge supprime un contact, <br/>
 * on peut garder une référence via un snapshot.
 * 
 * 
 * 
 * @author ginguene
 * 
 */
public class ContactSnapshot implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private WhoisContact contact;
    private Date createDate;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public WhoisContact getContact() {
        return this.contact;
    }

    public void setContact(WhoisContact contact) {
        this.contact = contact;
    }

    public Date getCreateDate() {
        return DateUtils.clone(this.createDate);
    }

    public void setCreateDate(Date createDate) {
        this.createDate = DateUtils.clone(createDate);
    }

}
