package fr.afnic.commons.services.filter;

import fr.afnic.commons.beans.WhoisContact;

/**
 * Retourne l'identifiant d'un contact, c'est à dire son nichandle
 * 
 * @author ginguene
 * 
 */
public class ContactIdentifier implements IObjectIdentifier<WhoisContact> {

    @Override
    public String getId(WhoisContact contact) {
        return contact.getHandle();
    }

}
