package fr.afnic.commons.services.sql.converter.mapping;

import java.util.List;

import fr.afnic.commons.beans.export.ExportView;
import fr.afnic.commons.beans.list.IView;
import fr.afnic.commons.beans.operations.OperationView;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.beans.request.AuthorizationRequestView;
import fr.afnic.commons.services.converter.ConstantTypeMapping;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class SqlViewMapping extends ConstantTypeMapping<String, IView> {

    public SqlViewMapping() {
        super(String.class, IView.class);
    }

    @Override
    protected void populateMap() {
        this.populateQualificationViews();
        this.populateAuthorizationRequestViews();
    }

    private void populateQualificationViews() {
        this.addMapping("v_qualif_upd_in_pending_freez", OperationView.QualificationsToUpdateInPendingFreeze);
        this.addMapping("v_qualif_upd_in_freez", OperationView.QualificationsToUpdateInFreeze);
        this.addMapping("v_qualif_upd_in_pending_block", OperationView.QualificationsToUpdateInPendingBlock);
        this.addMapping("v_qualif_upd_in_block", OperationView.QualificationsToUpdateInBlock);
        this.addMapping("v_qualif_upd_in_pending_suppr", OperationView.QualificationsToUpdateInPendingSuppress);
        this.addMapping("v_qualif_upd_in_suppr", OperationView.QualificationsToUpdateInSuppress);

        this.addMapping("v_valo_in_auto_mode", OperationView.AutoValorization);
        this.addMapping("v_valo_from_reporting", OperationView.ReportingPostAuto);
        this.addMapping("v_valo_from_auto", OperationView.AutoQualificationPostAuto);
        this.addMapping("v_valo_from_manual_check", OperationView.ManualControlQualificationPostAuto);

        this.addMapping("v_just_in_pending_freeze", OperationView.JustificationInPendingFreeze);
        this.addMapping("v_just_in_pending_block", OperationView.JustificationInPendingBlock);
        this.addMapping("v_just_in_pending_suppr", OperationView.JustificationInPendingSuppress);

        this.addMapping("v_just_in_pending_resp", OperationView.JustificationInPendingResponse);
        this.addMapping("v_just_in_receiv_resp", OperationView.JustificationInReceivedResponse);

        this.addMapping("v_failed_qualification", OperationView.FailedQualification);

        this.addMapping("v_all_qualification", OperationView.AllQualifications);
    }

    private void populateAuthorizationRequestViews() {
        this.addMapping("v_auth_req_in_running", AuthorizationRequestView.RunningAuthorizationRequest);
        this.addMapping("v_auth_req_in_failed", AuthorizationRequestView.FailedAuthorizationRequest);

        this.addMapping("v_auth_req_in_waiting", AuthorizationRequestView.WaitingAuthorizationRequest);
        this.addMapping("v_auth_req_in_answered", AuthorizationRequestView.AnsweredAuthorizationRequest);
        this.addMapping("v_auth_req_all", AuthorizationRequestView.AllAuthorizationRequest);
        this.addMapping("v_auth_req_in_pending_abord", AuthorizationRequestView.PendingAbordAuthorizationRequest);
        this.addMapping("v_auth_req_recently_updated", AuthorizationRequestView.RecentlyUpdatedAuthorizationRequest);

    }

    @Override
    public String getOtherModelValue(IView commonsValue) {
        if (commonsValue instanceof ExportView) {
            return ((ExportView) commonsValue).getName();
        }

        return super.getOtherModelValue(commonsValue);
    }

    @Override
    public IView getCommonsValue(String otherModelValue, UserId userId, TldServiceFacade tld) {

        if (otherModelValue != null && otherModelValue.toUpperCase().startsWith("EXP_")) {
            try {
                List<ExportView> views = AppServiceFacade.getResultListService().getExportViews(userId, tld);
                for (ExportView view : views) {
                    if (otherModelValue.equalsIgnoreCase(view.getName())) {
                        return view;
                    }
                }
            } catch (ServiceException e) {
                throw new RuntimeException("getCommonsValue() failed", e);
            }
        }
        return super.getCommonsValue(otherModelValue, userId, tld);

    }

}
