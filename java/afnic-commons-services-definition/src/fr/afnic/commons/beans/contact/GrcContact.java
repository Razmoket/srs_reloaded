/*
 * $Id: $ $Revision: $ $Author: $
 */

package fr.afnic.commons.beans.contact;

import fr.afnic.commons.beans.cache.BusinessObjectCache;
import fr.afnic.commons.beans.contact.identity.ContactIdentity;
import fr.afnic.commons.beans.contactdetails.PostalAddress;
import fr.afnic.commons.beans.contactdetails.PostalAddressId;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

/**
 * Contact stocké dans la base GRC.
 * 
 * 
 *
 */
public class GrcContact extends GenericContact {

    private static final long serialVersionUID = 1L;

    protected int objectVersion;
    protected UserId updateUserId;
    protected UserId createUserId;
    protected String remark;

    protected UserId userIdCaller;
    protected TldServiceFacade tldCaller;

    protected int individualId;

    protected int corporateId;

    private final BusinessObjectCache<PostalAddressId, PostalAddress> postalAddressCache = new BusinessObjectCache<PostalAddressId, PostalAddress>();

    public GrcContact(ContactIdentity identity, UserId userIdCaller, TldServiceFacade tldCaller) {
        super(identity);
        this.userIdCaller = userIdCaller;
        this.tldCaller = tldCaller;
    }

    public long getObjectVersion() {
        return this.objectVersion;
    }

    public void setObjectVersion(int objectVersion) {
        this.objectVersion = objectVersion;
    }

    public UserId getUpdateUserId() {
        return this.updateUserId;
    }

    public void setUpdateUserId(UserId updateUserId) {
        this.updateUserId = updateUserId;
    }

    public UserId getCreateUserId() {
        return this.createUserId;
    }

    public void setCreateUserId(UserId createUserId) {
        this.createUserId = createUserId;
    }

    @Override
    public PostalAddress getPostalAddress() throws ServiceException {
        return this.postalAddressCache.getValue(this.userIdCaller, this.tldCaller);
    }

    @Override
    public void setPostalAddress(PostalAddress postalAddress) {
        this.postalAddressCache.setValue(postalAddress);
    }

    public void setPostalAddressId(PostalAddressId postalAddressId) {
        this.postalAddressCache.setId(postalAddressId);
    }

    public PostalAddressId getPostalAddressId() throws ServiceException {
        return this.postalAddressCache.getId();
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getIndividualId() {
        return this.individualId;
    }

    public void setIndividualId(int individualId) {
        this.individualId = individualId;
    }

    public int getCorporateId() {
        return this.corporateId;
    }

    public void setCorporateId(int corporateId) {
        this.corporateId = corporateId;
    }

}
