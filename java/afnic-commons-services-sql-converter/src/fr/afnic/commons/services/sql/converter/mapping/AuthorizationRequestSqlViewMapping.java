package fr.afnic.commons.services.sql.converter.mapping;

import fr.afnic.commons.beans.request.AuthorizationRequestView;
import fr.afnic.commons.services.converter.ConstantTypeMapping;

public class AuthorizationRequestSqlViewMapping extends ConstantTypeMapping<String, AuthorizationRequestView> {

    public AuthorizationRequestSqlViewMapping() {
        super(String.class, AuthorizationRequestView.class);
    }

    @Override
    protected void populateMap() {
        this.addMapping("v_auth_req_in_running", AuthorizationRequestView.RunningAuthorizationRequest);
        this.addMapping("v_auth_req_in_waiting", AuthorizationRequestView.WaitingAuthorizationRequest);
        this.addMapping("v_auth_req_in_answered", AuthorizationRequestView.AnsweredAuthorizationRequest);
        this.addMapping("v_auth_req_all", AuthorizationRequestView.AllAuthorizationRequest);
        this.addMapping("v_auth_req_recently_updated", AuthorizationRequestView.RecentlyUpdatedAuthorizationRequest);
        this.addMapping("v_auth_req_in_pending_abord", AuthorizationRequestView.PendingAbordAuthorizationRequest);

    }
}
