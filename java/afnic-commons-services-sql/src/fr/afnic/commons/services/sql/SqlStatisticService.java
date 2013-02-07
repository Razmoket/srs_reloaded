/*
 * $Id: SqlStatisticService.java,v 1.6 2010/08/20 10:33:40 ginguene Exp $ $Revision: 1.6 $ $Author: ginguene $
 */

package fr.afnic.commons.services.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fr.afnic.commons.beans.logs.ILogger;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.beans.statistics.DateOfMeasure;
import fr.afnic.commons.beans.statistics.DayOfMeasure;
import fr.afnic.commons.beans.statistics.Measure;
import fr.afnic.commons.beans.statistics.Project;
import fr.afnic.commons.beans.statistics.Statistic;
import fr.afnic.commons.beans.statistics.StatisticsFactory;
import fr.afnic.commons.services.IStatisticService;
import fr.afnic.commons.services.exception.NotFoundException;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.sql.utils.QueryStatementManagement;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.utils.ObjectSerializer;
import fr.afnic.utils.sql.ISqlConnectionFactory;
import fr.afnic.utils.sql.pool.PoolSqlConnectionFactoryFacade;
import fr.afnic.utils.sql.pool.SqlDatabaseEnum;

/**
 * Implementation Sql du IStaticService.
 * 
 * @author ginguene
 * 
 */
public class SqlStatisticService implements IStatisticService {

    protected static final ILogger LOGGER = AppServiceFacade.getLoggerService().getLogger(SqlStatisticService.class);

    private ISqlConnectionFactory sqlConnectionFactory = null;

    private static final String PROJECT_STATS_INSERTION = "insert into nicope.project_stats(" + " nicope.project_stats.id"
                                                          + " ,nicope.project_stats.project" + " ,nicope.project_stats.day" + " ,nicope.project_stats.populated_by"
                                                          + " ,nicope.project_stats.label" + " ,nicope.project_stats.value" + ")" + " values (?,?,?,?,?,?)";

    private static final String PROJECT_STATS_UPDATE_VALUE = "update nicope.project_stats" + " set " + " nicope.project_stats.value = ? " + " where "
                                                             + " nicope.project_stats.project=?" + " and nicope.project_stats.label=?" + " and nicope.project_stats.day=?"
                                                             + " and nicope.project_stats.populated_by=?";

    private static final String PROJECT_STATS_SUM_SELECTION = "select " + " count( nicope.project_stats.value),"
                                                              + " sum( nicope.project_stats.value)" + " from " + " nicope.project_stats " + " where " + " nicope.project_stats.day=?"
                                                              + " and nicope.project_stats.project=?" + " and nicope.project_stats.label=?";

    /** Selectionne toutes les stats d'un projet */
    private static final String PROJECT_STATS_SELECTION_WITH_PROJECT = "select " + " nicope.project_stats.project," + " nicope.project_stats.label,"
                                                                       + " nicope.project_stats.day," + " nicope.project_stats.value," + " nicope.project_stats.populated_by" + " from "
                                                                       + " nicope.project_stats " + " where " + " nicope.project_stats.project=?";

    private static final String PROJECT_STATS_SELECTION_WITH_PROJECT_AND_USER = SqlStatisticService.PROJECT_STATS_SELECTION_WITH_PROJECT
                                                                                + " and nicope.project_stats.populated_by=?";

    /** Selectionne toutes les stats d'un projet entre 2 dates */
    private static final String PROJECT_STATS_SELECTION_WITH_PROJECT_BETWEEN_TWO_DATES = SqlStatisticService.PROJECT_STATS_SELECTION_WITH_PROJECT
                                                                                         + " and nicope.project_stats.day >= ? " + " and nicope.project_stats.day <= ? ";

    /**
     * Constructeur avec un SQLDataAccessor permettant d'obtenir une connection vers la base de donnees
     * 
     * @param sqlConnectionFactory
     *            Factory permettant de créer des connections à la base de données.
     */
    public SqlStatisticService(final ISqlConnectionFactory sqlConnectionFactory) {
        this.sqlConnectionFactory = sqlConnectionFactory;
    }

    public SqlStatisticService(SqlDatabaseEnum database, TldServiceFacade tld) throws ServiceException {
        this.sqlConnectionFactory = PoolSqlConnectionFactoryFacade.getSqlPoolConnectionFactory(database, tld);
    }

    /** {@inheritDoc} */
    @Override
    public Measure createOrIncrementMeasure(final Measure measure, UserId userId, TldServiceFacade tld) throws ServiceException {
        this.checkMeasureForCreate(measure);

        try {
            final Measure existingMeasure = AppServiceFacade.getStatisticService().getMeasure(measure.getDate(), measure.getStatistic(),
                                                                                              measure.getUserLogin(), userId, tld);
            final float newValue = existingMeasure.getValue() + 1;
            return this.updateMeasureValue(measure, newValue);
        } catch (final NotFoundException e) {
            // Mesure inexistante, il s'agit d'un ajout
            return this.createMeasure(measure);
        }

    }

    /** {@inheritDoc} */
    @Override
    public boolean createMeasureIfNotExist(final Measure measure, UserId userId, TldServiceFacade tld) throws ServiceException {
        this.checkMeasureForCreate(measure);

        try {
            AppServiceFacade.getStatisticService().getMeasure(measure.getDate(), measure.getStatistic(), measure.getUserLogin(), userId, tld);
            return false;
        } catch (final NotFoundException e) {
            // Mesure inexistante
            this.createMeasure(measure);
            return true;
        }
    }

    /** {@inheritDoc} */
    @Override
    public Measure createOrUpdateMeasure(final Measure measure, UserId userId, TldServiceFacade tld) throws ServiceException {
        this.checkMeasureForCreate(measure);
        try {
            AppServiceFacade.getStatisticService().getMeasure(measure.getDate(), measure.getStatistic(), measure.getUserLogin(), userId, tld);
            return this.updateMeasureValue(measure, measure.getValue());
        } catch (final NotFoundException e) {
            // Mesure inexistante, il s'agit d'un ajout
            return this.createMeasure(measure);
        }
    }

    /**
     * Méthode chargée de controler la mesure à inserer en base.<br/>
     * Si cette dernière n'est pas valide, une exception est déclenchée.
     * 
     * @param measure
     *            Mesure à tester.
     * 
     * @throws IllegalArgumentException
     *             Si la mesure n'est pas valide.
     */
    private void checkMeasureForCreate(final Measure measure) throws IllegalArgumentException {
        if (measure == null) {
            throw new IllegalArgumentException("measure cannot be null");
        }

        if (measure.isMeasureForAllUsers()) {
            throw new IllegalArgumentException("measure.userLogin cannot be null");
        }
    }

    /**
     * Met à jour la valeur d'une mesure avec une nouvelle valeur.
     * 
     * @param measure
     *            Mesure à mettre à jour.
     * 
     * @param newValue
     *            Nouvelle valeur.
     * 
     * @return La Mesure après l'update.
     * 
     * @throws ServiceException
     *             Si l'opération échoue.
     */
    private Measure updateMeasureValue(final Measure measure, final float newValue) throws ServiceException {

        if (SqlStatisticService.LOGGER.isTraceEnabled()) {
            SqlStatisticService.LOGGER.trace("Execute " + SqlStatisticService.PROJECT_STATS_UPDATE_VALUE + " with project=" + measure.getProject()
                                             + "; label:" + measure.getLabel() + "; populatedBy:" + measure.getUserLogin() + "; value:" + measure.getValue());
        }

        PreparedStatement preparedStatement = null;
        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);
        try {

            preparedStatement = queryStatementManagement.initializeStatement(SqlStatisticService.PROJECT_STATS_UPDATE_VALUE);
            int i = 1;
            preparedStatement.setFloat(i++, newValue);
            preparedStatement.setString(i++, measure.getProject().getName());
            preparedStatement.setString(i++, measure.getLabel());
            preparedStatement.setDate(i++, new java.sql.Date(measure.getStartingDate().getTime()));
            preparedStatement.setString(i++, measure.getUserLogin());

            preparedStatement.executeUpdate();

        } catch (final Exception e) {
            throw new ServiceException("createMeasure() failed with:\n" + ObjectSerializer.toXml(measure), e);
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (final Exception e) {
                    SqlStatisticService.LOGGER.error("close statement failed", e);
                }
            }
            queryStatementManagement.closeConnection();
        }

        return new Measure(measure.getStatistic(), measure.getDate(), newValue, measure.getUserLogin());
    }

    /**
     * Méthode chargée d'ajouter une mesure dans la base de données.
     * 
     * @param measure
     *            Mesure à ajouter
     * 
     * @return La mesure ajoutée
     * 
     * @throws ServiceException
     *             Si l'opération échoue.
     */
    private Measure createMeasure(final Measure measure) throws ServiceException {
        if (measure == null) {
            throw new IllegalArgumentException("measure cannot be null");
        }

        if (SqlStatisticService.LOGGER.isTraceEnabled()) {
            SqlStatisticService.LOGGER.trace("Execute " + SqlStatisticService.PROJECT_STATS_INSERTION + " with project=" + measure.getProject()
                                             + "; label:" + measure.getLabel() + "; populatedBy:" + measure.getUserLogin() + "; value:" + measure.getValue());
        }

        PreparedStatement preparedStatement = null;
        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);
        try {
            final int nextId = this.getNextStatisticId();

            preparedStatement = queryStatementManagement.initializeStatement(SqlStatisticService.PROJECT_STATS_INSERTION);
            int i = 1;
            preparedStatement.setInt(i++, nextId);
            preparedStatement.setString(i++, measure.getProject().getName());
            preparedStatement.setDate(i++, new java.sql.Date(measure.getStartingDate().getTime()));
            preparedStatement.setString(i++, measure.getUserLogin());
            preparedStatement.setString(i++, measure.getLabel());
            preparedStatement.setFloat(i++, measure.getValue());

            preparedStatement.executeUpdate();

        } catch (final Exception e) {
            throw new ServiceException("createMeasure() failed with:\n" + ObjectSerializer.toXml(measure), e);
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (final Exception e) {
                    SqlStatisticService.LOGGER.error("close statement failed", e);
                }
            }
            queryStatementManagement.closeConnection();
        }

        return measure;
    }

    /** {@inheritDoc} */
    @Override
    public Measure getMeasure(final DateOfMeasure date, final Statistic statistic, UserId userId, TldServiceFacade tld) throws ServiceException {
        return this.getMeasure(date, statistic, null, userId, tld);
    }

    /** {@inheritDoc} */
    @Override
    public Measure getMeasure(final DateOfMeasure date, final Statistic statistic, final String userLogin, UserId userId, TldServiceFacade tld) throws ServiceException {

        String sql = SqlStatisticService.PROJECT_STATS_SUM_SELECTION;

        if (userLogin != null && !userLogin.isEmpty()) {
            sql += " and nicope.project_stats.populated_by=?";
        }

        final ResultSetProcessor<Measure> processor = new ResultSetProcessor<Measure>() {
            @Override
            public void populateResultFromResultSet(final ResultSet resultSet) throws SQLException, ServiceException {
                if (resultSet.next()) {
                    final int count = resultSet.getInt(1);

                    if (count == 0) {
                        throw new NotFoundException("No measure found for date:" + date.toString() + ",statistic:" + statistic.toString()
                                                    + "; userLogin:" + userLogin);
                    } else {
                        final float value = resultSet.getFloat(2);
                        this.result = new Measure(statistic, date, value, userLogin);
                    }
                }
            }
        };

        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);
        try {
            if (SqlStatisticService.LOGGER.isTraceEnabled()) {
                SqlStatisticService.LOGGER.trace("Execute " + sql + " with stat " + statistic.toString() + "; userLogin:" + userLogin);
            }
            queryStatementManagement.executeQuery(processor, sql, date.getStartingDate(), statistic.getProject().getName(), statistic.getLabel(),
                                                  userLogin);
        } catch (final SQLException e) {
            throw new ServiceException("getMeasure(" + date.toString() + ", " + statistic.toString() + "," + userLogin + ") failed", e);
        }

        return processor.getResult();
    }

    /** {@inheritDoc} */
    @Override
    public List<Measure> getMeasuresWithProjectBetweenTwoDates(final Project project, final Date startDate, final Date endDate, UserId userId, TldServiceFacade tld)
                                                                                                                                                                    throws ServiceException {
        return this.getMeasures(SqlStatisticService.PROJECT_STATS_SELECTION_WITH_PROJECT_BETWEEN_TWO_DATES, project.getName(), startDate, endDate);
    }

    /** {@inheritDoc} */
    @Override
    public List<Measure> getMeasuresWithProject(final Project project, UserId userId, TldServiceFacade tld) throws ServiceException {
        return this.getMeasures(SqlStatisticService.PROJECT_STATS_SELECTION_WITH_PROJECT, project.getName());
    }

    /** {@inheritDoc} */
    @Override
    public List<Measure> getMeasuresWithProjectAndUser(final Project project, final String userLogin, UserId userId, TldServiceFacade tld) throws ServiceException {
        return this.getMeasures(SqlStatisticService.PROJECT_STATS_SELECTION_WITH_PROJECT_AND_USER, project.getName(), userLogin);
    }

    /**
     * Retourne toutes les mesures correspondant à une requete sql dont on passe les parametres.
     * 
     * 
     * @param sql
     *            requete sql permettant de récupéréer les mesures.
     * 
     * 
     * @return La liste des mesures correspondant à la recherche.
     * 
     * @throws ServiceException
     *             Si l'opération échoue.
     */
    private List<Measure> getMeasures(final String sql, final Object... params) throws ServiceException {
        final ResultSetProcessor<List<Measure>> processor = this.getResultSetProcessorForList();
        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);
        try {
            if (SqlStatisticService.LOGGER.isTraceEnabled()) {
                String paramsStr = "";
                for (final Object obj : params) {
                    paramsStr += obj.toString() + ",";
                }
                SqlStatisticService.LOGGER.trace("Execute " + sql + "with " + paramsStr);
            }
            queryStatementManagement.executeQuery(processor, sql, params);
        } catch (final SQLException e) {
            throw new ServiceException("getMeasure() failed", e);
        }
        return processor.getResult();

    }

    /**
     * Crée un ResultSetProcessor permettant de créer une liste de Measure pour un projet.
     * 
     * 
     * 
     * @return ResultSetProcessor permettant de créer une liste de Measure.
     */
    private ResultSetProcessor<List<Measure>> getResultSetProcessorForList() {
        return new ResultSetProcessor<List<Measure>>() {
            @Override
            public void populateResultFromResultSet(final ResultSet resultSet) throws SQLException, ServiceException {
                this.result = new ArrayList<Measure>();

                while (resultSet.next()) {
                    int ind = 1;
                    final Project project = new Project(resultSet.getString(ind++));
                    final String label = resultSet.getString(ind++);
                    final Date date = new Date(resultSet.getDate(ind++).getTime());
                    final float value = resultSet.getFloat(ind++);
                    final String userLogin = resultSet.getString(ind++);

                    final Statistic statistic = StatisticsFactory.createStatistic(project, label);

                    final Measure measure = new Measure(statistic, new DayOfMeasure(date), value, userLogin);

                    this.result.add(measure);
                }
            }
        };

    }

    /**
     * Retourne le prochain Id à utiliser pour inserer une ligne dans la table PROJECT_STATS en base.
     * 
     * @return Le prochain Id à utiliser pour inserer une statistique en base.
     * @throws ServiceException
     *             Si l'opération échoue
     */
    private int getNextStatisticId() throws ServiceException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        QueryStatementManagement queryStatementManagement = new QueryStatementManagement(this.sqlConnectionFactory);
        try {
            final String sql = "SELECT SEQ_PROJECT_STATS.nextval from dual";
            if (SqlStatisticService.LOGGER.isTraceEnabled()) {
                SqlStatisticService.LOGGER.trace("Execute " + sql);
            }

            preparedStatement = queryStatementManagement.initializeStatement(sql);
            resultSet = preparedStatement.executeQuery();
            int nextId = -1;
            if (resultSet.next()) {
                nextId = resultSet.getInt(1);
            }
            return nextId;
        } catch (final SQLException e) {
            throw new ServiceException("getNextStatisticId() failed", e);
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (final Exception e) {
                    SqlStatisticService.LOGGER.error("close resultSet failed", e);
                }
            }

            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (final Exception e) {
                    SqlStatisticService.LOGGER.error("close statement failed", e);
                }
            }
            queryStatementManagement.closeConnection();
        }

    }

}
