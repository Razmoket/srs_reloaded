package fr.afnic.commons.services.filter;

import fr.afnic.commons.beans.WhoisContact;

public class ContactResultFilter extends ResultFilter<WhoisContact> {

    public ContactResultFilter() {
        super(new ContactIdentifier());
    }

}
