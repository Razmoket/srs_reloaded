package fr.afnic.commons.beans.documents;

import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class DeletedDocument extends Document {

    public DeletedDocument(String handle, UserId userId, TldServiceFacade tld) {
        super(userId, tld);
        this.handle = handle;
        this.title = "Document supprimé";
        this.sender = "Inconnu";
    }

}
