package fr.afnic.commons.legal;

import java.io.Serializable;
import java.util.Date;

import fr.afnic.commons.beans.contact.IndividualAndLegalStructure;
import fr.afnic.utils.DateUtils;

public class SyreliLegalData implements Serializable {

    private static final long serialVersionUID = 1L;

    private IndividualAndLegalStructure requerant;
    private IndividualAndLegalStructure representant;

    private AskedAction askedAction;
    private String numDossier;
    private Date dateCreation;
    private Date answerDate;

    private String tituRights;
    private String tituPosition;
    private String tituLegalProcedures;

    private String violationReasonsPart1;
    private String violationReasonsPart2;
    private String violationReasonsPart3;

    private String domainName;

    public AskedAction getAskedAction() {
        return this.askedAction;
    }

    public void setAskedAction(AskedAction askedAction) {
        this.askedAction = askedAction;
    }

    public String getNumDossier() {
        return this.numDossier;
    }

    public void setNumDossier(String numDossier) {
        this.numDossier = numDossier;
    }

    public Date getDateCreation() {
        return DateUtils.clone(this.dateCreation);
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = DateUtils.clone(dateCreation);
    }

    public IndividualAndLegalStructure getRequerant() {
        return this.requerant;
    }

    public void setRequerant(IndividualAndLegalStructure requerant) {
        this.requerant = requerant;
    }

    public IndividualAndLegalStructure getRepresentant() {
        return this.representant;
    }

    public void setRepresentant(IndividualAndLegalStructure representant) {
        this.representant = representant;
    }

    public String getDomainName() {
        return this.domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public Date getAnswerDate() {
        return DateUtils.clone(this.answerDate);
    }

    public void setAnswerDate(Date answerDate) {
        this.answerDate = DateUtils.clone(answerDate);
    }

    public String getTituRights() {
        return this.tituRights;
    }

    public void setTituRights(String tituRights) {
        this.tituRights = tituRights;
    }

    public String getTituPosition() {
        return this.tituPosition;
    }

    public void setTituPosition(String tituPosition) {
        this.tituPosition = tituPosition;
    }

    public String getTituLegalProcedures() {
        return this.tituLegalProcedures;
    }

    public void setTituLegalProcedures(String tituLegalProcedures) {
        this.tituLegalProcedures = tituLegalProcedures;
    }

    public String getViolationReasonsPart1() {
        return this.violationReasonsPart1;
    }

    public void setViolationReasonsPart1(String violationReasonsPart1) {
        this.violationReasonsPart1 = violationReasonsPart1;
    }

    public String getViolationReasonsPart2() {
        return this.violationReasonsPart2;
    }

    public void setViolationReasonsPart2(String violationReasonsPart2) {
        this.violationReasonsPart2 = violationReasonsPart2;
    }

    public String getViolationReasonsPart3() {
        return this.violationReasonsPart3;
    }

    public void setViolationReasonsPart3(String violationReasonsPart3) {
        this.violationReasonsPart3 = violationReasonsPart3;
    }

}
