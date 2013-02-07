package fr.afnic.commons.services.jooq;

import java.math.BigInteger;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.jooq.Condition;
import org.jooq.SQLDialect;
import org.jooq.SimpleSelectConditionStep;
import org.jooq.impl.Factory;

import fr.afnic.commons.beans.agtf.AgtfCategory;
import fr.afnic.commons.beans.agtf.AgtfCategoryWord;
import fr.afnic.commons.beans.agtf.AgtfHisto;
import fr.afnic.commons.beans.agtf.AgtfWord;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.IAgtfService;
import fr.afnic.commons.services.exception.ConnectionException;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.jooq.converter.JooqConverterFacade;
import fr.afnic.commons.services.jooq.stub.boa.packages.PkgAgtf;
import fr.afnic.commons.services.jooq.stub.boa.tables.AgtfCategoryTable;
import fr.afnic.commons.services.jooq.stub.boa.tables.AgtfCommentItsCategWordTable;
import fr.afnic.commons.services.jooq.stub.boa.tables.AgtfItsCategoryWordTable;
import fr.afnic.commons.services.jooq.stub.boa.tables.AgtfWordTable;
import fr.afnic.commons.services.jooq.stub.boa.tables.VAgtfWordAndCategoryView;
import fr.afnic.commons.services.jooq.stub.boa.tables.records.AgtfCategoryTableRecord;
import fr.afnic.commons.services.jooq.stub.boa.tables.records.AgtfCommentItsCategWordTableRecord;
import fr.afnic.commons.services.jooq.stub.boa.tables.records.AgtfItsCategoryWordTableRecord;
import fr.afnic.commons.services.jooq.stub.boa.tables.records.AgtfWordTableRecord;
import fr.afnic.commons.services.jooq.stub.boa.tables.records.VAgtfWordAndCategoryViewRecord;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.utils.Preconditions;
import fr.afnic.utils.sql.ISqlConnectionFactory;
import fr.afnic.utils.sql.pool.PoolSqlConnectionFactoryFacade;
import fr.afnic.utils.sql.pool.SqlDatabaseEnum;

public class JooqAgtfService implements IAgtfService {

    private final ISqlConnectionFactory sqlConnectionFactory;

    private static AgtfCategoryTable AGTF_CATEGORY = AgtfCategoryTable.AGTF_CATEGORY;

    private static AgtfWordTable AGTF_WORD = AgtfWordTable.AGTF_WORD;

    private static AgtfItsCategoryWordTable AGTF_ITS_CATEGORY_WORD = AgtfItsCategoryWordTable.AGTF_ITS_CATEGORY_WORD;

    private static AgtfCommentItsCategWordTable AGTF_COMMENT_ITS_CATEG_WORD = AgtfCommentItsCategWordTable.AGTF_COMMENT_ITS_CATEG_WORD;

    private static VAgtfWordAndCategoryView V_AGTF_WORD_AND_CATEGORY = VAgtfWordAndCategoryView.V_AGTF_WORD_AND_CATEGORY;

    public JooqAgtfService(SqlDatabaseEnum database, TldServiceFacade tld) throws ServiceException {
        this.sqlConnectionFactory = PoolSqlConnectionFactoryFacade.getSqlPoolConnectionFactory(database, tld);
    }

    @Override
    public AgtfCategory getCategoryFromCategoryId(int categoryId, UserId userId, TldServiceFacade tld) throws ServiceException {
        return this.getAgtfCategory(0, userId, tld, AGTF_CATEGORY.ID_CATEGORY.equal(Integer.valueOf(categoryId))).get(0);
    }

    private void getCategoryFromListCategoryId(Map<Integer, AgtfCategory> mapCategory, UserId userId, TldServiceFacade tld) throws ServiceException {
        List<AgtfCategory> listCateg = this.getAgtfCategory(0, userId, tld, AGTF_CATEGORY.ID_CATEGORY.in(mapCategory.keySet()));
        for (AgtfCategory categ : listCateg) {
            mapCategory.put(categ.getId(), categ);
        }
    }

    @Override
    public List<AgtfCategory> getCategories(UserId userId, TldServiceFacade tld) throws ServiceException {
        return this.getAgtfCategory(0, userId, tld);
    }

    @Override
    public List<AgtfCategory> getActiveCategories(UserId userId, TldServiceFacade tld) throws ServiceException {
        return this.getAgtfCategory(0, userId, tld, AGTF_CATEGORY.END_DATE.greaterThan(new java.sql.Date(Calendar.getInstance().getTimeInMillis())));
    }

    @Override
    public AgtfWord getWordFromWordId(int wordId, UserId userId, TldServiceFacade tld) throws ServiceException {
        return this.getAgtfWord(0, userId, tld, AGTF_WORD.ID_WORD.equal(Integer.valueOf(wordId)));
    }

    @Override
    public List<AgtfWord> getWordFromActiveCategory(int categoryId, UserId userId, TldServiceFacade tld) throws ServiceException {
        List<AgtfWord> listWord = this.getAgtfWordAndActiveCategory(0, userId, tld, V_AGTF_WORD_AND_CATEGORY.DELETE_DATE.equal((java.sql.Date) null),
                                                                    V_AGTF_WORD_AND_CATEGORY.ID_CATEGORY.equal(Integer.valueOf(categoryId)));
        Map<Integer, AgtfCategory> mapCategory = new HashMap<Integer, AgtfCategory>();
        Map<Integer, List<AgtfHisto>> mapComment = new HashMap<Integer, List<AgtfHisto>>();
        for (AgtfWord word : listWord) {
            mapCategory.put(word.getListCategoryWord().get(0).getCategoryId(), null);
            mapComment.put(word.getListCategoryWord().get(0).getCategoryWordId(), null);
        }
        this.getCategoryFromListCategoryId(mapCategory, userId, tld);
        this.getHistoFromListIdCategoryWord(mapComment, userId, tld);
        for (AgtfWord word : listWord) {
            AgtfCategoryWord categWord = word.getListCategoryWord().get(0);
            categWord.setAgtfCategory(mapCategory.get(categWord.getCategoryId()));
            categWord.setListHisto(mapComment.get(categWord.getCategoryWordId()));
        }
        return listWord;
    }

    @Override
    public List<AgtfCategoryWord> getActiveCategoriesWordFromWord(int wordId, UserId userId, TldServiceFacade tld) throws ServiceException {
        return this.getAgtfCategoryWord(0, userId, tld, AGTF_ITS_CATEGORY_WORD.ID_WORD.equal(Integer.valueOf(wordId)), AGTF_ITS_CATEGORY_WORD.DELETE_DATE.equal((java.sql.Date) null));
    }

    @Override
    public List<AgtfCategoryWord> getCategoriesWordFromWord(int wordId, UserId userId, TldServiceFacade tld) throws ServiceException {
        return this.getAgtfCategoryWord(0, userId, tld, AGTF_ITS_CATEGORY_WORD.ID_WORD.equal(Integer.valueOf(wordId)));
    }

    @Override
    public List<AgtfHisto> getHistoWithIdCategoryWord(Integer idCategoryWord, UserId userId, TldServiceFacade tld) throws ServiceException {
        return this.getAgtfCommentFromId(0, userId, tld, AGTF_COMMENT_ITS_CATEG_WORD.ID_CATEGORY_WORD.equal(idCategoryWord));
    }

    private void getHistoFromListIdCategoryWord(Map<Integer, List<AgtfHisto>> mapHisto, UserId userId, TldServiceFacade tld) throws ServiceException {
        List<AgtfHisto> listHisto = this.getAgtfCommentFromId(0, userId, tld, AGTF_COMMENT_ITS_CATEG_WORD.ID_CATEGORY_WORD.in(mapHisto.keySet()));
        for (AgtfHisto histo : listHisto) {
            List<AgtfHisto> listHistoTmp = mapHisto.get(histo.getCategoryWordId());
            if (listHistoTmp == null) {
                listHistoTmp = new ArrayList<AgtfHisto>();
            }
            listHistoTmp.add(histo);
            mapHisto.put(histo.getCategoryWordId(), listHistoTmp);
        }
    }

    /**
     * Si maxResult <= 0 , pas de clause limit()
     */
    private List<AgtfCategory> getAgtfCategory(int maxResult, UserId userId, TldServiceFacade tld, Condition... conditions) throws ServiceException {
        //Preconditions.checkNotEmpty(conditions, "conditions");

        Factory factory = this.createFactory();
        try {
            SimpleSelectConditionStep<AgtfCategoryTableRecord> select = factory.selectFrom(AGTF_CATEGORY)
                                                                               .where(conditions);

            org.jooq.Result<AgtfCategoryTableRecord> result = null;
            if (maxResult > 0) {
                result = select.limit(maxResult).fetch();
            } else {
                result = select.fetch();
            }

            return JooqConverterFacade.convertIterator(result.iterator(), AgtfCategory.class, userId, tld);
        } catch (Exception e) {
            throw new ServiceException("getAgtfCategory(" + Arrays.toString(conditions) + ") failed", e);
        } finally {
            this.closeFactory(factory);
        }
    }

    /**
     * Si maxResult <= 0 , pas de clause limit()
     */
    private AgtfWord getAgtfWord(int maxResult, UserId userId, TldServiceFacade tld, Condition... conditions) throws ServiceException {
        //Preconditions.checkNotEmpty(conditions, "conditions");

        Factory factory = this.createFactory();
        try {
            SimpleSelectConditionStep<AgtfWordTableRecord> select = factory.selectFrom(AGTF_WORD)
                                                                           .where(conditions);

            org.jooq.Result<AgtfWordTableRecord> result = null;
            if (maxResult > 0) {
                result = select.limit(maxResult).fetch();
            } else {
                result = select.fetch();
            }

            return JooqConverterFacade.convert(result.get(0), AgtfWord.class, userId, tld);
        } catch (Exception e) {
            throw new ServiceException("getAgtfWord(" + Arrays.toString(conditions) + ") failed", e);
        } finally {
            this.closeFactory(factory);
        }
    }

    /**
     * Si maxResult <= 0 , pas de clause limit()
     */
    private List<AgtfCategoryWord> getAgtfCategoryWord(int maxResult, UserId userId, TldServiceFacade tld, Condition... conditions) throws ServiceException {
        //Preconditions.checkNotEmpty(conditions, "conditions");

        Factory factory = this.createFactory();
        try {
            SimpleSelectConditionStep<AgtfItsCategoryWordTableRecord> select = factory.selectFrom(AGTF_ITS_CATEGORY_WORD)
                                                                                      .where(conditions);

            org.jooq.Result<AgtfItsCategoryWordTableRecord> result = null;
            if (maxResult > 0) {
                result = select.limit(maxResult).fetch();
            } else {
                result = select.fetch();
            }

            return JooqConverterFacade.convertIterator(result.iterator(), AgtfCategoryWord.class, userId, tld);
        } catch (Exception e) {
            throw new ServiceException("getAgtfCategoryWord(" + Arrays.toString(conditions) + ") failed", e);
        } finally {
            this.closeFactory(factory);
        }
    }

    /**
     * Si maxResult <= 0 , pas de clause limit()
     */
    private List<AgtfWord> getAgtfWordAndActiveCategory(int maxResult, UserId userId, TldServiceFacade tld, Condition... conditions) throws ServiceException {
        Preconditions.checkNotEmpty(conditions, "conditions");

        Factory factory = this.createFactory();
        try {
            SimpleSelectConditionStep<VAgtfWordAndCategoryViewRecord> select = factory.selectFrom(V_AGTF_WORD_AND_CATEGORY)
                                                                                      .where(conditions);

            org.jooq.Result<VAgtfWordAndCategoryViewRecord> result = null;
            if (maxResult > 0) {
                result = select.limit(maxResult).fetch();
            } else {
                result = select.fetch();
            }
            return JooqConverterFacade.convertIterator(result.iterator(), AgtfWord.class, userId, tld);
        } catch (Exception e) {
            throw new ServiceException("getAgtfWordAndActiveCategory(" + Arrays.toString(conditions) + ") failed", e);
        } finally {
            this.closeFactory(factory);
        }
    }

    //V_AGTF_COMMENT_FROM_WORD

    /**
     * Si maxResult <= 0 , pas de clause limit()
     */
    private List<AgtfHisto> getAgtfCommentFromId(int maxResult, UserId userId, TldServiceFacade tld, Condition... conditions) throws ServiceException {
        Preconditions.checkNotEmpty(conditions, "conditions");

        Factory factory = this.createFactory();
        try {
            SimpleSelectConditionStep<AgtfCommentItsCategWordTableRecord> select = factory.selectFrom(AGTF_COMMENT_ITS_CATEG_WORD)
                                                                                          .where(conditions);

            org.jooq.Result<AgtfCommentItsCategWordTableRecord> result = null;
            if (maxResult > 0) {
                result = select.limit(maxResult).fetch();
            } else {
                result = select.fetch();
            }

            return JooqConverterFacade.convertIterator(result.iterator(), AgtfHisto.class, userId, tld);
        } catch (Exception e) {
            throw new ServiceException("getAgtfCommentFromWord(" + Arrays.toString(conditions) + ") failed", e);
        } finally {
            this.closeFactory(factory);
        }
    }

    @Override
    public boolean addWords(String[] words, List<AgtfCategory> categories, String commen, UserId userId, TldServiceFacade tldt) throws ServiceException {
        Factory factory = this.createFactory();
        try {
            for (String word : words) {
                for (AgtfCategory category : categories) {
                    BigInteger idWord = PkgAgtf.createWordInCategory(factory, category.getId(), StringUtils.deleteWhitespace(word), tldt.name(), commen, userId.getIntValue());
                }
            }
        } catch (Exception e) {
            throw new ServiceException("addWords() failed", e);
        } finally {
            this.closeFactory(factory);
        }

        return false;
    }

    @Override
    public boolean addCategory(String categoryName, String shortName, int canonicalCheck, UserId userId, TldServiceFacade tld) throws ServiceException {
        Factory factory = this.createFactory();
        try {
            PkgAgtf.addCategory(factory, categoryName, categoryName, shortName, canonicalCheck);
        } catch (Exception e) {
            throw new ServiceException("addCategory(" + categoryName + canonicalCheck + ") failed", e);
        } finally {
            this.closeFactory(factory);
        }
        return false;
    }

    @Override
    public boolean deleteCategory(int idCategorie, String comment, UserId userId, TldServiceFacade tld) throws ServiceException {
        Factory factory = this.createFactory();
        try {
            PkgAgtf.deleteCategory(factory, idCategorie, "", userId.getIntValue());
        } catch (Exception e) {
            throw new ServiceException("deleteCategory(" + idCategorie + ") failed", e);
        } finally {
            this.closeFactory(factory);
        }
        return true;
    }

    @Override
    public boolean deleteWordInCategory(int idCategory, int idWord, String comment, UserId userId, TldServiceFacade tld) throws ServiceException {
        Factory factory = this.createFactory();
        try {
            PkgAgtf.deleteWordInCategory(factory, idCategory, idWord, comment, userId.getIntValue());
        } catch (Exception e) {
            throw new ServiceException("deleteWordInCategory(" + idCategory + "," + idWord + ") failed", e);
        } finally {
            this.closeFactory(factory);
        }
        return true;
    }

    @Override
    public boolean addComment(int idCategoryWord, String comment, UserId userId, TldServiceFacade tld) throws ServiceException {
        Factory factory = this.createFactory();
        try {
            PkgAgtf.addComment(factory, idCategoryWord, comment, userId.getIntValue());
        } catch (Exception e) {
            throw new ServiceException("addComment(" + idCategoryWord + "," + comment + ") failed", e);
        } finally {
            this.closeFactory(factory);
        }
        return true;
    }

    protected Factory createFactory() throws ConnectionException {
        Connection connection = this.sqlConnectionFactory.createConnection();
        return new Factory(connection, SQLDialect.ORACLE);
    }

    protected void closeFactory(Factory factory) throws ConnectionException {
        this.sqlConnectionFactory.closeConnection(factory.getConnection());
    }

    @Override
    public boolean checkIfExist(String categoryName, UserId userId, TldServiceFacade tld) throws ServiceException {
        Factory factory = this.createFactory();
        try {
            SimpleSelectConditionStep<AgtfCategoryTableRecord> select = factory.selectFrom(AGTF_CATEGORY).where(AgtfCategoryTable.AGTF_CATEGORY.VALUE_FR.equal(categoryName));

            org.jooq.Result<AgtfCategoryTableRecord> result = select.fetch();
            if (result.size() > 0) {
                return true;
            }

            return false;
        } catch (Exception e) {
            throw new ServiceException("checkIfExist(" + categoryName + ") failed", e);
        } finally {
            this.closeFactory(factory);
        }
    }

}
