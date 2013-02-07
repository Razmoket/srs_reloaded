/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/beans/documents/Element.java#17 $
 * $Revision: #17 $
 * $Author: barriere $
 */

package fr.afnic.commons.beans.documents;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.IllegalArgumentException;
import fr.afnic.commons.services.exception.NotImplementedException;
import fr.afnic.commons.services.exception.RequestFailedException;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.utils.Preconditions;
import fr.afnic.utils.DateUtils;

/**
 * Element de docushare, en pratique il s'agit d'un folder ou d'un document.
 * 
 * 
 * 
 * @author ginguene
 * 
 */
public class Element implements Serializable {

    private static final long serialVersionUID = 1L;

    protected boolean isLocked = false;
    protected String lockedBy;

    protected String title;
    protected String handle;
    protected String comment;

    protected Date createDate;

    protected Folder folder;

    /** le user appelant l'objet */
    protected final UserId userIdCaller;
    /** le tld sur lequel on appelle l'objet */
    protected final TldServiceFacade tldCaller;

    protected Element(UserId userId, TldServiceFacade tld) {
        this.userIdCaller = userId;
        this.tldCaller = tld;
    }

    public Date getCreateDate() {
        return DateUtils.clone(this.createDate);
    }

    public void setCreateDate(Date createDate) {
        this.createDate = DateUtils.clone(createDate);
    }

    public String getHandle() {
        return this.handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getComment() {
        return StringUtils.defaultString(this.comment, "");
    }

    public void setComment(String comment) throws ServiceException {
        this.comment = comment;
    }

    /**
     * Change le commentaire de l'element dans la ged
     * 
     * @throws RequestFailedException
     */
    public void changeComment(String newComment) throws ServiceException {
        Preconditions.checkNotNull(newComment, "newComment");

        if (this.hasToChangeComment(newComment)) {
            AppServiceFacade.getDocumentService().updateComment(this.handle, newComment, this.userIdCaller, this.tldCaller);
            this.comment = newComment;
        }
    }

    public boolean hasToChangeComment(String newComment) {
        if (newComment == null && this.comment == null) return false;
        if (newComment != null && newComment.equals(this.comment)) return false;
        return true;
    }

    /**
     * Vide le commentaire de l'element dans la GED
     * 
     * @throws RequestFailedException
     */
    public void clearComment() throws ServiceException {
        this.changeComment("");
    }

    /**
     * Change le titre de l'element dans la ged
     * 
     * @param newComment
     * @throws RequestFailedException
     */
    public void changeTitle(String newTitle) throws ServiceException {
        if (newTitle == null) throw new IllegalArgumentException("newTitle cannot be null");

        if (this.hasToChangeTitle(newTitle)) {
            AppServiceFacade.getDocumentService().updateTitle(this.handle, newTitle, this.userIdCaller, this.tldCaller);
            this.title = newTitle;
        }
    }

    public void changeDate(Date newDate, UserId userId, TldServiceFacade tld) throws ServiceException {
        if (newDate == null) throw new IllegalArgumentException("newDate cannot be null");

        AppServiceFacade.getDocumentService().updateReceptionDate(this.handle, newDate, userId, tld);
    }

    public boolean hasToChangeTitle(String newTitle) {
        if (newTitle == null && this.title == null) return false;
        if (newTitle != null && newTitle.equals(this.title)) return false;
        return true;
    }

    public boolean isLocked() {
        return this.isLocked;
    }

    public void setLocked(boolean isLocked) {
        this.isLocked = isLocked;
    }

    public String getLockedBy() {
        return this.lockedBy;
    }

    public void setLockedBy(String lockedBy) {
        this.lockedBy = lockedBy;
    }

    public Folder getFolder() throws ServiceException {
        if (this.folder != null) {
            return this.folder;
        }
        return AppServiceFacade.getDocumentService().getElementFolder(this.handle, this.userIdCaller, this.tldCaller);
    }

    public void setFolder(Folder folder) {
        this.folder = folder;
    }

    /**
     * fonctionne uniquement pour les objets remplis par jooq
     */
    public void populate() throws ServiceException {
        throw new NotImplementedException();
    }

}
