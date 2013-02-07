package fr.afnic.commons.services;

import java.util.List;

import fr.afnic.commons.beans.agtf.AgtfCategory;
import fr.afnic.commons.beans.agtf.AgtfCategoryWord;
import fr.afnic.commons.beans.agtf.AgtfHisto;
import fr.afnic.commons.beans.agtf.AgtfWord;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

public interface IAgtfService {
    public AgtfCategory getCategoryFromCategoryId(int categoryId, UserId userId, TldServiceFacade tld) throws ServiceException;

    public List<AgtfCategory> getCategories(UserId userId, TldServiceFacade tld) throws ServiceException;

    public List<AgtfCategory> getActiveCategories(UserId userId, TldServiceFacade tld) throws ServiceException;

    public List<AgtfWord> getWordFromActiveCategory(int categoryId, UserId userId, TldServiceFacade tld) throws ServiceException;

    public List<AgtfCategoryWord> getActiveCategoriesWordFromWord(int wordId, UserId userId, TldServiceFacade tld) throws ServiceException;

    public List<AgtfCategoryWord> getCategoriesWordFromWord(int wordId, UserId userId, TldServiceFacade tld) throws ServiceException;

    public AgtfWord getWordFromWordId(int wordId, UserId userId, TldServiceFacade tld) throws ServiceException;

    public List<AgtfHisto> getHistoWithIdCategoryWord(Integer idCategoryWord, UserId userId, TldServiceFacade tld) throws ServiceException;

    public boolean addWords(String[] words, List<AgtfCategory> categories, String commen, UserId userId, TldServiceFacade tldt) throws ServiceException;

    public boolean addCategory(String categoryName, String shortName, int canonicalCheck, UserId userId, TldServiceFacade tld) throws ServiceException;

    public boolean deleteCategory(int idCategorie, String comment, UserId userId, TldServiceFacade tld) throws ServiceException;

    public boolean deleteWordInCategory(int idCategory, int idWord, String comment, UserId userId, TldServiceFacade tld) throws ServiceException;

    public boolean addComment(int idCategoryWord, String comment, UserId userId, TldServiceFacade tld) throws ServiceException;

    public boolean checkIfExist(String categoryName, UserId userId, TldServiceFacade tld) throws ServiceException;
}
