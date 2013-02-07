package fr.afnic.commons.beans.request;

import com.google.common.base.Joiner;

import fr.afnic.commons.beans.list.AuthorizationRequestResultList;
import fr.afnic.commons.beans.list.IView;
import fr.afnic.commons.beans.list.ResultList;

public enum AuthorizationRequestView implements IView {

    RunningAuthorizationRequest("ID_AUTHORIZATIONREQUEST"),
    WaitingAuthorizationRequest("ID_AUTHORIZATIONREQUEST"),
    AnsweredAuthorizationRequest("ID_AUTHORIZATIONREQUEST"),
    PendingAbordAuthorizationRequest("ID_AUTHORIZATIONREQUEST"),
    AllAuthorizationRequest("ID_AUTHORIZATIONREQUEST"),
    RecentlyUpdatedAuthorizationRequest("ID_AUTHORIZATIONREQUEST"),
    FailedAuthorizationRequest("ID_AUTHORIZATIONREQUEST");

    private String identifier = null;

    private AuthorizationRequestView(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public String getIdentifier() {
        if (this.identifier != null) {
            return this.identifier;
        } else {
            throw new IllegalArgumentException("no identifierColumnName defined for view " + this);
        }
    }

    @Override
    public ResultList<?> createResultList() {
        return new AuthorizationRequestResultList(this);
    }

    public static String listAsString() {
        return Joiner.on(" | ").join(values()).toString();
    }

}
