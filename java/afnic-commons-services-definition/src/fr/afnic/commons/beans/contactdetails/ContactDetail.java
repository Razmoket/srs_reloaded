package fr.afnic.commons.beans.contactdetails;

import fr.afnic.commons.beans.validatable.ObjectValue;

public class ContactDetail extends ObjectValue {

    private static final long serialVersionUID = 1L;

    public final static int EMAIL = 1;
    public final static int PHONE = 2;
    public final static int FAX = 3;
    public final static int URL = 4;

    public ContactDetail() {
        super();
    }

    public ContactDetail(String value) {
        super(value);
    }
}
