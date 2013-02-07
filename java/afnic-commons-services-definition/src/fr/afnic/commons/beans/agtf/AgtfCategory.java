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
public class AgtfCategory implements Serializable, Cloneable {

    protected int id;
    protected String textFr;
    protected String textEn;
    protected Date endDate;
    protected boolean canonicalCheck;
    protected List<AgtfCategoryWord> listCategoryWord;

    protected static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    private static final long serialVersionUID = 1L;

    private static final ILogger LOGGER = AppServiceFacade.getLoggerService().getLogger(AgtfCategory.class);

    /** le user appelant l'objet */
    protected final UserId userIdCaller;
    /** le tld sur lequel on appelle l'objet */
    protected final TldServiceFacade tldCaller;

    public AgtfCategory(UserId userId, TldServiceFacade tld) {
        this.userIdCaller = userId;
        this.tldCaller = tld;
    }

    public int getId() {
        return this.id;
    }

    public String getIdAsString() {
        return Integer.toString(this.id);
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getEndDate() {
        return this.endDate;
    }

    public String getEndDateAsString() {
        if (this.endDate == null) {
            return "";
        } else {
            return DATE_FORMAT.format(this.endDate);
        }
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getTextFr() {
        return this.textFr;
    }

    public void setTextFr(String textFr) {
        this.textFr = textFr;
    }

    public String getTextEn() {
        return this.textEn;
    }

    public void setTextEn(String textEn) {
        this.textEn = textEn;
    }

    public List<AgtfWord> getListWord() throws ServiceException {
        return AppServiceFacade.getAgtfService().getWordFromActiveCategory(this.id, this.userIdCaller, this.tldCaller);
    }

    public boolean isCanonicalCheck() {
        return this.canonicalCheck;
    }

    public void setCanonicalCheck(boolean canonicalCheck) {
        this.canonicalCheck = canonicalCheck;
    }

    public List<AgtfCategoryWord> getListCategoryWord() {
        return this.listCategoryWord;
    }

    public void setListCategoryWord(List<AgtfCategoryWord> listCategoryWord) {
        this.listCategoryWord = listCategoryWord;
    }

    @Override
    public String toString() {
        return this.textFr;
    }

}
