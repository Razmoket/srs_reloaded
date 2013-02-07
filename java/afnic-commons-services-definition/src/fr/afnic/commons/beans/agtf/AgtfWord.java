/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/beans/domain/Domain.java#25 $
 * $Revision: #25 $
 * $Author: ginguene $
 */

package fr.afnic.commons.beans.agtf;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.List;

import fr.afnic.commons.beans.logs.ILogger;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;

/***
 * Representation d'un domaine.
 * 
 * @author ginguene
 * 
 */
public class AgtfWord implements Serializable, Cloneable {

    protected int id;
    protected String word;
    protected String wordTld;
    protected int status;
    protected List<AgtfCategoryWord> listCategoryWord;

    protected static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    private static final long serialVersionUID = 1L;

    private static final ILogger LOGGER = AppServiceFacade.getLoggerService().getLogger(AgtfWord.class);

    /** le user appelant l'objet */
    protected final UserId userIdCaller;
    /** le tld sur lequel on appelle l'objet */
    protected final TldServiceFacade tldCaller;

    public AgtfWord(UserId userId, TldServiceFacade tld) {
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

    public String getWord() {
        return this.word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getWordTld() {
        return this.wordTld;
    }

    public void setWordTld(String wordTld) {
        this.wordTld = wordTld;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<AgtfCategoryWord> getListCategoryWord() {
        return this.listCategoryWord;
    }

    public void setListCategoryWord(List<AgtfCategoryWord> listCategoryWord) {
        this.listCategoryWord = listCategoryWord;
    }
}
