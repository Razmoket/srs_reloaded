/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/services/IStatisticService.java#2 $
 * $Revision: #2 $
 * $Author: barriere $
 */

package fr.afnic.commons.services;

import java.util.Date;
import java.util.List;

import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.beans.statistics.DateOfMeasure;
import fr.afnic.commons.beans.statistics.Measure;
import fr.afnic.commons.beans.statistics.Project;
import fr.afnic.commons.beans.statistics.Statistic;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

/***
 * Permet d'accédé aux statistiques de projet
 * 
 * @author ginguene
 * 
 */
public interface IStatisticService {

    /**
     * Met à jour une statistique
     * 
     * 
     * @param id
     *            Id de la stat à mettre à jour
     * @param newValue
     *            Nouvelle valeur de la statistique
     * 
     * @throws DaoException
     *             Si un problème intervient durant l'opération
     */
    // public void updateStatisticValue(int id, float newValue) throws DaoException;

    /**
     * 
     * Ajoute ou incremente de 1 une mesure de statistique dans la base de donnée. <br/>
     * Si il s'agit d'un ajout, la valeur attribuée est celle de la mesure passée en parametre. <br/>
     * Dans le cas d'un incrément la valeur de la mesure est ignorée.
     * 
     * 
     * @param measure
     *            mesure à ajouter dans les bases
     * 
     * @return La mesure qui a été mise en base.
     * 
     * 
     * @throws ServiceException
     *             Si un problème intervient durant l'opération
     */
    public Measure createOrIncrementMeasure(Measure measure, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Ajoute ou met à jour une measure avec une nouvelle valeur.
     * 
     * @param measure
     *            measure à ajouter ou à mettre à jour dans les bases.
     * 
     * @return La mesure qui a été mise en base.
     * 
     * @throws ServiceException
     *             Si un problème intervient durant l'opération.
     */
    public Measure createOrUpdateMeasure(Measure measure, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Crée une mesure si elle n'est pas déja présente.
     * 
     * @param measure
     *            measure à ajouter dans les bases.
     * 
     * @return true si la measure n'était pas présente et qu'elle vient d'etre inserée.
     * 
     * @throws ServiceException
     *             Si un problème intervient durant l'opération.
     */
    public boolean createMeasureIfNotExist(Measure measure, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Retourne la mesure d'une statistique prise à un moment donné pour tous les utilisateurs
     * 
     * @param date
     *            Date où a été réalisée la mesure.
     * @param statistic
     *            Statistique Dont on veut récuperer la mesure
     * 
     * 
     * @return La mesure correspondante aux parametres de la fonction.
     * 
     * @throws ServiceException
     *             Si un problème intervient durant l'opération
     */
    public Measure getMeasure(DateOfMeasure date, Statistic statistic, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Retourne la mesure d'une statistique prise à un moment donné pour un utilisateur
     * 
     * @param date
     *            Date où a été réalisée la mesure.
     * 
     * @param statistic
     *            Statistique Dont on veut récuperer la mesure
     * 
     * @param userLogin
     *            login de l'utilisateur Dont on veut récuperer la mesure
     * 
     * 
     * @return La mesure correspondante aux parametres de la fonction.
     * 
     * @throws ServiceException
     *             Si un problème intervient durant l'opération
     */
    public Measure getMeasure(DateOfMeasure date, Statistic statistic, String userLogin, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Retourne la liste de toutes les mesures effectuées sur un projet.
     * 
     * @param project
     *            Projet dont on veut les mesures.
     * 
     * @return Une liste de mesure associées au projet.
     * 
     * @throws ServiceException
     *             Si un problème intervient durant l'opération
     */
    public List<Measure> getMeasuresWithProject(Project project, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Retourne la lsite des mesure priser sur un projet pour un utilisateur.
     * 
     * @param project
     *            Projet concerné par les mesure.
     * 
     * @param userLogin
     *            Login de l'utilisateur dont on veut les mesures.
     * 
     * @return Une liste de mesure correspondant aux critère (vide si aucun ne correspond).
     * 
     * @throws ServiceException
     *             Si un problème intervient durant l'opération.
     */
    public List<Measure> getMeasuresWithProjectAndUser(Project project, String userLogin, UserId userId, TldServiceFacade tld) throws ServiceException;

    /**
     * Retourne toutes les mesures effectuées sur un projet entre 2 dates.<br/>
     * l'interval inclue les bornes.
     * 
     * @param project
     *            Projet dont on veut les mesures.
     * 
     * @param startDate
     *            Date de début incluse.
     * 
     * @param endDate
     *            Date de fin incluse.
     * 
     * @return Toutes les mesures effectuées sur un projet entre 2 dates
     * 
     * @throws ServiceException
     *             Si un problème intervient durant l'opération.
     */
    public List<Measure> getMeasuresWithProjectBetweenTwoDates(Project project, Date startDate, Date endDate, UserId userId, TldServiceFacade tld) throws ServiceException;
}
