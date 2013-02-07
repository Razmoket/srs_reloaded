package fr.afnic.commons.beans;

import java.io.Serializable;
import java.util.Date;

import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.beans.validatable.NumberId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.utils.DateUtils;

/**
 * 
 * Objet stocké dans la base de donnée.
 * 
 *
 */
public class BusinessObject<NUMBER_ID extends NumberId<?>> implements Serializable {

    protected long objectVersion;

    protected NUMBER_ID id;

    protected String comments;

    protected UserId updateUserId;
    protected UserId createUserId;
    protected UserId lockingUserId;

    protected Date createDate;
    protected Date updateDate;
    protected Date lockingDate;

    /** le user appelant l'objet */
    protected final UserId userIdCaller;
    /** le tld sur lequel on appelle l'objet */
    protected final TldServiceFacade tldCaller;

    protected BusinessObject(UserId userId, TldServiceFacade tld) {
        this.userIdCaller = userId;
        this.tldCaller = tld;
    }

    public boolean isLocked() throws ServiceException {
        return this.lockingUserId != null;
    }

    public void setObjectVersion(long objectVersion) {
        this.objectVersion = objectVersion;
    }

    public void setId(NUMBER_ID id) {
        this.id = id;
    }

    public void setCreateUserId(UserId createUserId) {
        this.createUserId = createUserId;
    }

    public String getComments() {
        return this.comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public UserId getUpdateUserId() {
        return this.updateUserId;
    }

    public void setUpdateUserId(UserId updateUserId) {
        this.updateUserId = updateUserId;
    }

    public Date getCreateDate() {
        return DateUtils.clone(this.createDate);
    }

    public void setCreateDate(Date createDate) {
        this.createDate = DateUtils.clone(createDate);
    }

    public Date getUpdateDate() {
        return DateUtils.clone(this.updateDate);
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = DateUtils.clone(updateDate);
    }

    public long getObjectVersion() {
        return this.objectVersion;
    }

    public NUMBER_ID getId() {
        return this.id;
    }

    public String getIdAsString() {
        if (this.id == null) {
            return "-1";
        } else {
            return this.id.getValue();
        }
    }

    public int getIdAsInt() {
        if (this.id == null) {
            return -1;
        } else {
            return this.id.getIntValue();
        }
    }

    public UserId getCreateUserId() {
        return this.createUserId;
    }

    public boolean hasNoLockingUserId() {
        return !this.hasLockingUserId();

    }

    public boolean hasLockingUserId() {
        return this.lockingUserId != null;

    }

    public UserId getLockingUserId() {
        return this.lockingUserId;
    }

    public void setLockingUserId(UserId lockingUserId) {
        this.lockingUserId = lockingUserId;
    }

    public Date getLockingDate() {
        return DateUtils.clone(this.lockingDate);
    }

    public void setLockingDate(Date lockingDate) {
        this.lockingDate = DateUtils.clone(lockingDate);
    }

}
