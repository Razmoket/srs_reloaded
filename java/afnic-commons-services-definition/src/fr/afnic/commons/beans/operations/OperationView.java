package fr.afnic.commons.beans.operations;

import com.google.common.base.Joiner;

import fr.afnic.commons.beans.list.IView;
import fr.afnic.commons.beans.list.QualificationResultList;
import fr.afnic.commons.beans.list.ResultList;

public enum OperationView implements IView {

    QualificationsToUpdateInPendingFreeze,
    QualificationsToUpdateInFreeze,
    QualificationsToUpdateInPendingBlock,
    QualificationsToUpdateInBlock,
    QualificationsToUpdateInPendingSuppress,
    QualificationsToUpdateInSuppress,

    ValorizationInPendingFreeze("ID_QUALIFICATION"), // valorisation à geler
    ValorizationInPending("ID_QUALIFICATION"), // valorisation a traiter
    ValorisationInRunning("ID_QUALIFICATION"), //valorisation en cours

    AutoValorization("ID_QUALIFICATION"), // valorisation a traiter
    ReportingPostAuto("ID_QUALIFICATION"), //valorisation en cours
    AutoQualificationPostAuto("ID_QUALIFICATION"), //valorisation en cours
    ManualControlQualificationPostAuto("ID_QUALIFICATION"), //valorisation en cours

    JustificationInPendingFreeze("ID_QUALIFICATION"), // justification à geler
    JustificationInPendingBlock("ID_QUALIFICATION"), // justification à bloquer
    JustificationInPendingSuppress("ID_QUALIFICATION"), // justification à supprimer
    JustificationInPendingResponse("ID_QUALIFICATION"), //justification en attente reponse
    JustificationInReceivedResponse("ID_QUALIFICATION"), //justification répondue

    FailedQualification("ID_QUALIFICATION"),

    HolderPortfolio("DOMAIN_NAME"),

    AllQualifications("ID_QUALIFICATION"),

    None;

    private String identifier = null;

    private OperationView() {

    }

    private OperationView(String identifier) {
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

    public static String listAsString() {
        return Joiner.on(" | ").join(values()).toString();
    }

    @Override
    public ResultList<?> createResultList() {
        return new QualificationResultList(this);
    }

}
