package fr.afnic.commons.beans.contact.identity;

import java.io.Serializable;

public abstract class ContactIdentity implements Serializable {

    public abstract String getName();

    public abstract int getId();

    public abstract void setId(int id);

}
