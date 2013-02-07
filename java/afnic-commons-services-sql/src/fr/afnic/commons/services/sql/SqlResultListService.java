package fr.afnic.commons.services.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.afnic.commons.beans.export.ExportView;
import fr.afnic.commons.beans.list.IView;
import fr.afnic.commons.beans.list.QualificationStatResultList;
import fr.afnic.commons.beans.list.ResultList;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.beans.statistics.RawStat;
import fr.afnic.commons.beans.statistics.StatisticsView;
import fr.afnic.commons.services.IResultListService;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.MockAppServiceFacade;
import fr.afnic.commons.services.sql.converter.mapping.SqlViewMapping;
import fr.afnic.commons.services.sql.query.boa.ResultListSqlQueries;
import fr.afnic.commons.services.sql.utils.QueryStatementManagement;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.utils.Preconditions;
import fr.afnic.utils.sql.ISqlConnectionFactory;
import fr.afnic.utils.sql.SqlFacade;
import fr.afnic.utils.sql.pool.PoolSqlConnectionFactoryFacade;
import fr.afnic.utils.sql.pool.SqlDatabaseEnum;

public class SqlResultListService implements IResultListService {

    private static final SqlViewMapping VIEW_MAPPING = new SqlViewMapping();

    private final ISqlConnectionFactory sqlConnectionFactory;

    public SqlResultListService(ISqlConnectionFactory sqlConnectionFactory) {
        this.sqlConnectionFactory = Preconditions.checkNotNull(sqlConnectionFactory, "sqlConnectionFactory");
    }

    public SqlResultListService(SqlDatabaseEnum database, TldServiceFacade tld) throws ServiceException {
        this.sqlConnectionFactory = PoolSqlConnectionFactoryFacade.getSqlPoolConnectionFactory(database, tld);
    }

    public static void main(String[] args) throws Exception {
        MockAppServiceFacade facade = new MockAppServiceFacade();
        facade.use();

        SqlResultListService service = new SqlResultListService(SqlFacade.getBoaFactory());
        facade.setResultListService(service);

        for (IView view : service.getExportViews(new UserId(24), TldServiceFacade.Fr)) {
            service.getResultList(view, null, TldServiceFacade.Fr);
        }
    }

    @Override
    public List<ExportView> getExportViews(UserId userId, TldServiceFacade tld) throws ServiceException {
        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);
        String sql = ResultListSqlQueries.GET_ALL_EXPORT_VIEW_NAME;
        PreparedStatement preparedStatement = queryStatementManagement.initializeStatement(sql);
        return queryStatementManagement.executeStatementList(ExportView.class, preparedStatement, userId, tld);

    }

    @Override
    public ResultList<?> getResultList(IView view, UserId userId, TldServiceFacade tld) throws ServiceException {
        Preconditions.checkNotNull(view.getIdentifier(), "view.identifier");
        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);
        String sql = ResultListSqlQueries.BEGIN_RESULT_QUERY + VIEW_MAPPING.getOtherModelValue(view);

        PreparedStatement preparedStatement = queryStatementManagement.initializeStatement(sql);
        ResultList<?> executeStatement = queryStatementManagement.executeStatement(ResultList.class, preparedStatement, userId, tld);
        return executeStatement != null ? executeStatement : view.createResultList();
    }

    @Override
    public int getResultListCount(IView view, UserId userId, TldServiceFacade tld) throws ServiceException {
        Preconditions.checkNotNull(view.getIdentifier(), "view.identifier");
        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);
        String sql = ResultListSqlQueries.BEGIN_GET_COUNT_RESULT_QUERY + VIEW_MAPPING.getOtherModelValue(view);
        PreparedStatement preparedStatement = queryStatementManagement.initializeStatement(sql);
        return queryStatementManagement.executeStatement(preparedStatement);
    }

    @Override
    public QualificationStatResultList getResultList(StatisticsView view, Date date, UserId userId, TldServiceFacade tld) throws ServiceException {
        Preconditions.checkNotNull(view.getIdentifier(), "view.identifier");
        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);
        String sql = null;
        if (view.equals(StatisticsView.BeWhois)) {
            sql = ResultListSqlQueries.RESULT_QUERY_STATS2;
        } else {
            sql = ResultListSqlQueries.RESULT_QUERY_STATS;
        }
        PreparedStatement preparedStatement = queryStatementManagement.initializeStatement(sql);
        try {
            preparedStatement.setString(1, view.getIdentifier());
            preparedStatement.setDate(2, new java.sql.Date(date.getTime()));
        } catch (SQLException e) {
            throw new ServiceException("getResultList(" + view.getIdentifier() + " , " + date + ") failed", e);
        }
        QualificationStatResultList executeStatement = queryStatementManagement.executeStatement(QualificationStatResultList.class, preparedStatement, userId, tld);
        return executeStatement != null ? executeStatement : new QualificationStatResultList(view);
    }

    @Override
    public List<Date> getListDate(StatisticsView view, UserId userId, TldServiceFacade tld) throws ServiceException {
        Preconditions.checkNotNull(view.getIdentifier(), "view.identifier");
        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);
        String sql = ResultListSqlQueries.RESULT_QUERY_DATE_STATS;
        PreparedStatement preparedStatement = queryStatementManagement.initializeStatement(sql);
        try {
            preparedStatement.setString(1, view.getIdentifier());
        } catch (SQLException e) {
            throw new ServiceException("getListDate(" + view.getIdentifier() + ") failed", e);
        }
        List<Date> executeStatement = queryStatementManagement.executeStatementList(Date.class, preparedStatement, userId, tld);
        return executeStatement;
    }

    @Override
    public Date getLastDate(StatisticsView view, UserId userId, TldServiceFacade tld) throws ServiceException {
        Preconditions.checkNotNull(view.getIdentifier(), "view.identifier");
        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);
        String sql = ResultListSqlQueries.RESULT_QUERY_LAST_DATE_STATS;
        PreparedStatement preparedStatement = queryStatementManagement.initializeStatement(sql);
        try {
            preparedStatement.setString(1, view.getIdentifier());
        } catch (SQLException e) {
            throw new ServiceException("getListDate(" + view.getIdentifier() + ") failed", e);
        }
        Date executeStatement = queryStatementManagement.executeStatementAsDate(preparedStatement);
        return executeStatement;
    }

    @Override
    public String getPeriodicity(StatisticsView view, UserId userId, TldServiceFacade tld) throws ServiceException {
        Preconditions.checkNotNull(view.getIdentifier(), "view.identifier");
        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);
        String sql = ResultListSqlQueries.RESULT_QUERY_PERIODICITY_STATS;
        PreparedStatement preparedStatement = queryStatementManagement.initializeStatement(sql);
        try {
            preparedStatement.setString(1, view.getIdentifier());
        } catch (SQLException e) {
            throw new ServiceException("getPeriodicity(" + view.getIdentifier() + ") failed", e);
        }
        String result = queryStatementManagement.executeStatementAsString(preparedStatement);
        return result;
    }

    @Override
    public void createValoTag(Date date, Date dateEnd, UserId userId, TldServiceFacade tld) throws ServiceException {
        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);
        String sql = ResultListSqlQueries.COMPUTE_VALO_TAG_TOTAL;
        PreparedStatement preparedStatement = queryStatementManagement.initializeStatement(sql);
        try {
            preparedStatement.setDate(1, new java.sql.Date(date.getTime()));
            preparedStatement.setDate(2, new java.sql.Date(dateEnd.getTime()));
        } catch (SQLException e) {
            throw new ServiceException("createValoTag(" + date + ", " + dateEnd + ") failed", e);
        }
        List<RawStat> listTotal = queryStatementManagement.executeStatementList(RawStat.class, preparedStatement, userId, tld);

        sql = ResultListSqlQueries.COMPUTE_VALO_TAG_NB;
        queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);
        preparedStatement = queryStatementManagement.initializeStatement(sql);
        try {
            preparedStatement.setDate(1, new java.sql.Date(date.getTime()));
            preparedStatement.setDate(2, new java.sql.Date(dateEnd.getTime()));
        } catch (SQLException e) {
            throw new ServiceException("createValoTag(" + date + ", " + dateEnd + ") failed", e);
        }
        List<RawStat> listNb = queryStatementManagement.executeStatementList(RawStat.class, preparedStatement, userId, tld);
        Map<String, Integer> mapNb = new HashMap<String, Integer>();
        for (RawStat item : listNb) {
            mapNb.put(item.getLimit(), item.getNb());
        }

        for (RawStat item : listTotal) {
            try {
                Integer nb = mapNb.get(item.getLimit());
                if (nb == null) {
                    nb = 0;
                }
                queryStatementManagement.executeUpdate(ResultListSqlQueries.INSERT_QUALIFICATION_STAT, StatisticsView.ValoTag.getIdentifier(), date, item.getLimit(), nb,
                                                       item.getNb());
            } catch (SQLException e) {
                throw new ServiceException("createValoTag(" + date + ", " + dateEnd + ") failed", e);
            }
        }
    }

    @Override
    public void createJustifBlock(Date date, Date dateEnd, UserId userId, TldServiceFacade tld) throws ServiceException {
        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);
        String sql = ResultListSqlQueries.COMPUTE_JUSTIF_BLOCK_TOTAL;
        PreparedStatement preparedStatement = queryStatementManagement.initializeStatement(sql);
        try {
            preparedStatement.setDate(1, new java.sql.Date(date.getTime()));
            preparedStatement.setDate(2, new java.sql.Date(dateEnd.getTime()));
        } catch (SQLException e) {
            throw new ServiceException("createJustifBlock(" + date + ", " + dateEnd + ") failed", e);
        }
        List<RawStat> listTotal = queryStatementManagement.executeStatementList(RawStat.class, preparedStatement, userId, tld);

        sql = ResultListSqlQueries.COMPUTE_JUSTIF_BLOCK_NB;
        queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);
        preparedStatement = queryStatementManagement.initializeStatement(sql);
        try {
            preparedStatement.setDate(1, new java.sql.Date(date.getTime()));
            preparedStatement.setDate(2, new java.sql.Date(dateEnd.getTime()));
            preparedStatement.setDate(3, new java.sql.Date(date.getTime()));
            preparedStatement.setDate(4, new java.sql.Date(dateEnd.getTime()));
        } catch (SQLException e) {
            throw new ServiceException("createJustifBlock(" + date + ", " + dateEnd + ") failed", e);
        }
        List<RawStat> listNb = queryStatementManagement.executeStatementList(RawStat.class, preparedStatement, userId, tld);
        Map<String, Integer> mapNb = new HashMap<String, Integer>();
        for (RawStat item : listNb) {
            mapNb.put(item.getLimit(), item.getNb());
        }

        for (RawStat item : listTotal) {
            try {
                Integer nb = mapNb.get(item.getLimit());
                if (nb == null) {
                    nb = 0;
                }
                queryStatementManagement.executeUpdate(ResultListSqlQueries.INSERT_QUALIFICATION_STAT, StatisticsView.JustifBlock.getIdentifier(), date, item.getLimit(), nb,
                                                       item.getNb());
            } catch (SQLException e) {
                throw new ServiceException("createJustifBlock(" + date + ", " + dateEnd + ") failed", e);
            }
        }
    }

    @Override
    public void createJustifDelete(Date date, Date dateEnd, UserId userId, TldServiceFacade tld) throws ServiceException {
        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);

        String sql = ResultListSqlQueries.COMPUTE_JUSTIF_DELETE_TOTAL;
        PreparedStatement preparedStatement = queryStatementManagement.initializeStatement(sql);

        try {
            try {
                preparedStatement.setDate(1, new java.sql.Date(date.getTime()));
                preparedStatement.setDate(2, new java.sql.Date(dateEnd.getTime()));
            } catch (SQLException e) {
                throw new ServiceException("createJustifDelete(" + date + ", " + dateEnd + ") failed", e);
            }
            List<RawStat> listTotal = queryStatementManagement.executeStatementList(RawStat.class, preparedStatement, userId, tld);

            sql = ResultListSqlQueries.COMPUTE_JUSTIF_DELETE_NB;
            queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);
            preparedStatement = queryStatementManagement.initializeStatement(sql);
            try {
                preparedStatement.setDate(1, new java.sql.Date(date.getTime()));
                preparedStatement.setDate(2, new java.sql.Date(dateEnd.getTime()));
            } catch (SQLException e) {
                throw new ServiceException("createJustifDelete(" + date + ", " + dateEnd + ") failed", e);
            }
            List<RawStat> listNb = queryStatementManagement.executeStatementList(RawStat.class, preparedStatement, userId, tld);
            Map<String, Integer> mapNb = new HashMap<String, Integer>();
            for (RawStat item : listNb) {
                mapNb.put(item.getLimit(), item.getNb());
            }

            for (RawStat item : listTotal) {
                try {
                    Integer nb = mapNb.get(item.getLimit());
                    if (nb == null) {
                        nb = 0;
                    }
                    queryStatementManagement.executeUpdate(ResultListSqlQueries.INSERT_QUALIFICATION_STAT, StatisticsView.JustifDelete.getIdentifier(), date, item.getLimit(), nb,
                                                           item.getNb());
                } catch (SQLException e) {
                    throw new ServiceException("createJustifDelete(" + date + ", " + dateEnd + ") failed", e);
                }
            }
        } finally {
            queryStatementManagement.closeConnection();
        }
    }

    @Override
    public void createBeWhois(Date date, Date dateEnd, UserId userId, TldServiceFacade tld) throws ServiceException {

        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);
        String sql = ResultListSqlQueries.COMPUTE_BE_WHOIS_TOTAL;
        PreparedStatement preparedStatement = queryStatementManagement.initializeStatement(sql);
        List<RawStat> listTotal = queryStatementManagement.executeStatementList(RawStat.class, preparedStatement, userId, tld);

        sql = ResultListSqlQueries.COMPUTE_BE_WHOIS_NB;
        queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);
        preparedStatement = queryStatementManagement.initializeStatement(sql);
        try {
            preparedStatement.setDate(1, new java.sql.Date(date.getTime()));
            preparedStatement.setDate(2, new java.sql.Date(dateEnd.getTime()));
        } catch (SQLException e) {
            throw new ServiceException("createBeWhois(" + date + ", " + dateEnd + ") failed", e);
        }
        List<RawStat> listNb = queryStatementManagement.executeStatementList(RawStat.class, preparedStatement, userId, tld);
        Map<String, Integer> mapNb = new HashMap<String, Integer>();
        for (RawStat item : listNb) {
            mapNb.put(item.getLimit(), item.getNb());
        }

        for (RawStat item : listTotal) {
            try {
                Integer nb = mapNb.get(item.getLimit());
                if (nb == null) {
                    nb = 0;
                }
                queryStatementManagement.executeUpdate(ResultListSqlQueries.INSERT_QUALIFICATION_STAT, StatisticsView.BeWhois.getIdentifier(), date, item.getLimit(), nb,
                                                       item.getNb());
            } catch (SQLException e) {
                throw new ServiceException("createBeWhois(" + date + ", " + dateEnd + ") failed", e);
            }
        }

    }
}
