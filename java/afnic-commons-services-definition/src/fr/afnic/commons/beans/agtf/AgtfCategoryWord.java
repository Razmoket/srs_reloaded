/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/beans/domain/Domain.java#25 $
 * $Revision: #25 $
 * $Author: ginguene $
 */

package fr.afnic.commons.beans.agtf;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import fr.afnic.commons.beans.logs.ILogger;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;

/***
 * Representation d'un domaine.
 * 
 * @author ginguene
 * 
 */
public class AgtfCategoryWord implements Serializable, Cloneable {

    protected int categoryWordId;
    protected int categoryId;
    protected transient AgtfCategory agtfCategory = null;
    protected int wordId;
    protected transient AgtfWord agtfWord = null;
    protected Date createDate;
    protected Date deleteDate;
    protected List<AgtfHisto> listHisto;

    protected static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    private static final long serialVersionUID = 1L;

    private static final ILogger LOGGER = AppServiceFacade.getLoggerService().getLogger(AgtfCategoryWord.class);

    /** le user appelant l'objet */
    protected final UserId userIdCaller;
    /** le tld sur lequel on appelle l'objet */
    protected final TldServiceFacade tldCaller;

    public AgtfCategoryWord(UserId userId, TldServiceFacade tld) {
        this.userIdCaller = userId;
        this.tldCaller = tld;
    }

    public int getCategoryWordId() {
        return this.categoryWordId;
    }

    public void setCategoryWordId(int categoryWordId) {
        this.categoryWordId = categoryWordId;
    }

    public Date getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getDeleteDate() {
        return this.deleteDate;
    }

    public void setDeleteDate(Date deleteDate) {
        this.deleteDate = deleteDate;
    }

    public List<AgtfHisto> getListHisto() {
        return this.listHisto;
    }

    public void setListHisto(List<AgtfHisto> listHisto) {
        this.listHisto = listHisto;
    }

    public int getCategoryId() {
        return this.categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getWordId() {
        return this.wordId;
    }

    public void setWordId(int wordId) {
        this.wordId = wordId;
    }

    public AgtfWord getWord() throws ServiceException {
        if (this.agtfWord == null) {
            this.agtfWord = AppServiceFacade.getAgtfService().getWordFromWordId(this.wordId, this.userIdCaller, this.tldCaller);
        }
        return this.agtfWord;
    }

    public AgtfCategory getCategory() throws ServiceException {
        if (this.agtfCategory == null) {
            this.agtfCategory = AppServiceFacade.getAgtfService().getCategoryFromCategoryId(this.categoryId, this.userIdCaller, this.tldCaller);
        }
        return this.agtfCategory;
    }

    public void setAgtfCategory(AgtfCategory agtfCategory) {
        this.agtfCategory = agtfCategory;
    }

}
