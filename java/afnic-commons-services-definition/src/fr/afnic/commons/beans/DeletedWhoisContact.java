package fr.afnic.commons.beans;

import fr.afnic.commons.beans.contact.identity.ContactIdentity;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class DeletedWhoisContact extends WhoisContact {

    protected DeletedWhoisContactIdentity identity;

    public DeletedWhoisContact(String handle, UserId userIdCaller, TldServiceFacade tldCaller) {
        super(new DeletedWhoisContactIdentity(), userIdCaller, tldCaller);
        this.setHandle(handle);
    }
}

class DeletedWhoisContactIdentity extends ContactIdentity {

    public DeletedWhoisContactIdentity() {
        super();
    }

    @Override
    public String getName() {
        return "Deleted contact";
    }

    @Override
    public int getId() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setId(int id) {
        // TODO Auto-generated method stub

    }

}
