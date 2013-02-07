/*
 * $Id: $
 * $Revision: $
 */

package fr.afnic.commons.beans.request;

import java.io.Serializable;
import java.util.Date;

import fr.afnic.commons.beans.AuthorizationOperation;
import fr.afnic.commons.beans.customer.Customer;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.utils.DateUtils;
import fr.afnic.utils.ToStringHelper;

public class AuthorizationPreliminaryExam implements Cloneable, Serializable {

    private int id;

    private String motivation;
    private Date createDate;

    private String registrarCode;
    private String holderHandle;
    private String domainName;

    private AuthorizationPreliminaryExamStatus status;

    public int getId() {
        return this.id;
    }

    public String getIdAsString() {
        return Integer.toString(this.id);
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMotivation() {
        return this.motivation;
    }

    public void setMotivation(String motivation) {
        this.motivation = motivation;
    }

    public Date getCreateDate() {
        return DateUtils.clone(this.createDate);
    }

    public void setCreateDate(Date createDate) {
        this.createDate = DateUtils.clone(createDate);
    }

    public String getRegistrarCode() {
        return this.registrarCode;
    }

    public void setRegistrarCode(String registrarCode) {
        this.registrarCode = registrarCode;
    }

    public String getHolderHandle() {
        return this.holderHandle;
    }

    public void setHolderHandle(String holderHandle) {
        this.holderHandle = holderHandle;
    }

    public String getDomainName() {
        return this.domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public AuthorizationPreliminaryExamStatus getStatus() {
        return this.status;
    }

    public void setStatus(AuthorizationPreliminaryExamStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return new ToStringHelper().addAllObjectAttributes(this).toString();
    }

    public AuthorizationPreliminaryExam copy() {
        try {
            return (AuthorizationPreliminaryExam) this.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("copy failed", e);
        }
    }

    public AuthorizationRequest createAuthorizationRequest(UserId userId, TldServiceFacade tld) throws ServiceException {
        AuthorizationRequest request = new AuthorizationRequest(userId, tld);
        request.setRequestedDomainName(this.domainName);
        request.setRequestedHolderHandle(this.holderHandle);

        Customer customer = AppServiceFacade.getCustomerService().getCustomerWithCode(this.registrarCode, userId, tld);
        request.setRegistrarCode(customer.getAccountLogin());

        request.setMotivation(this.motivation);
        request.setAuthorizationPreliminaryExamId(this.id);
        request.setStatus(AuthorizationRequestStatus.Running);
        request.setOperation(AuthorizationOperation.CreateDomain);

        return request;
    }
}
