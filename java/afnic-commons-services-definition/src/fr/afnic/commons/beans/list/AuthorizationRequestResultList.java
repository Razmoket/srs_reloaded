package fr.afnic.commons.beans.list;

import org.apache.commons.lang.NotImplementedException;

import fr.afnic.commons.beans.request.AuthorizationRequestView;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.utils.Preconditions;

public class AuthorizationRequestResultList extends ResultList<AuthorizationRequestView> {

    public AuthorizationRequestResultList(AuthorizationRequestView view) {
        this.view = Preconditions.checkNotNull(view, "view");
    }

    @Override
    public CharSequence createStream() throws ServiceException {
        throw new NotImplementedException();
    }

}
