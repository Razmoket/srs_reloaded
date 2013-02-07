package fr.afnic.commons.services.multitld;

import java.util.List;

import fr.afnic.commons.beans.agtf.AgtfCategory;
import fr.afnic.commons.beans.agtf.AgtfCategoryWord;
import fr.afnic.commons.beans.agtf.AgtfHisto;
import fr.afnic.commons.beans.agtf.AgtfWord;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.IAgtfService;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class MultiTldAgtfService implements IAgtfService {

    protected MultiTldAgtfService() {
        super();
    }

    @Override
    public List<AgtfCategory> getCategories(UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getAgtfService().getCategories(userId, tld);
    }

    @Override
    public AgtfCategory getCategoryFromCategoryId(int categoryId, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getAgtfService().getCategoryFromCategoryId(categoryId, userId, tld);
    }

    @Override
    public List<AgtfCategory> getActiveCategories(UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getAgtfService().getActiveCategories(userId, tld);
    }

    @Override
    public List<AgtfWord> getWordFromActiveCategory(int categoryId, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getAgtfService().getWordFromActiveCategory(categoryId, userId, tld);
    }

    @Override
    public List<AgtfCategoryWord> getActiveCategoriesWordFromWord(int wordId, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getAgtfService().getActiveCategoriesWordFromWord(wordId, userId, tld);
    }

    @Override
    public List<AgtfCategoryWord> getCategoriesWordFromWord(int wordId, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getAgtfService().getCategoriesWordFromWord(wordId, userId, tld);
    }

    @Override
    public AgtfWord getWordFromWordId(int wordId, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getAgtfService().getWordFromWordId(wordId, userId, tld);
    }

    @Override
    public List<AgtfHisto> getHistoWithIdCategoryWord(Integer idCategoryWord, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getAgtfService().getHistoWithIdCategoryWord(idCategoryWord, userId, tld);
    }

    @Override
    public boolean addWords(String[] words, List<AgtfCategory> categories, String comment, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getAgtfService().addWords(words, categories, comment, userId, tld);
    }

    @Override
    public boolean addCategory(String categoryName, String categoryShortName, int canonicalCheck, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getAgtfService().addCategory(categoryName, categoryShortName, canonicalCheck, userId, tld);
    }

    @Override
    public boolean deleteCategory(int idCategorie, String comment, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getAgtfService().deleteCategory(idCategorie, comment, userId, tld);
    }

    @Override
    public boolean deleteWordInCategory(int idCategory, int idWord, String comment, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getAgtfService().deleteWordInCategory(idCategory, idWord, comment, userId, tld);
    }

    @Override
    public boolean addComment(int idCategoryWord, String comment, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getAgtfService().addComment(idCategoryWord, comment, userId, tld);
    }

    @Override
    public boolean checkIfExist(String categoryName, UserId userId, TldServiceFacade tld) throws ServiceException {
        return tld.getServiceProvider().getAgtfService().checkIfExist(categoryName, userId, tld);
    }

}
