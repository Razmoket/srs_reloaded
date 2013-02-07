/*
 * $Id: MeasureTestCase.java,v 1.2 2010/08/20 07:36:54 ginguene Exp $
 * $Revision: 1.2 $
 * $Author: ginguene $
 */

package fr.afnic.commons.test;

import junit.framework.TestCase;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.beans.statistics.DateOfMeasure;
import fr.afnic.commons.beans.statistics.Statistic;
import fr.afnic.commons.services.exception.NotFoundException;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;

/**
 * Permet de faire des assert sur des statistiques.<br/>
 * Lancer la création du StatisticTestCaseen début de tests,<br/>
 * effectuer l'action qui doit changer (ou pas) la statistique,<br/>
 * puis enfin faire appelle à une des méthodes assert.<br/>
 * 
 * 
 * @author ginguene
 * 
 */
public class MeasureTestCase {

    private final float initialMeasureValue;
    private final DateOfMeasure dateOfMeasure;
    private final Statistic statistic;
    private final String userLogin;

    /** le user appelant l'objet */
    protected final UserId userIdCaller;
    /** le tld sur lequel on appelle l'objet */
    protected final TldServiceFacade tldCaller;

    /**
     * Constructeur initialisé avec tous les parametres permettant de consulter la measure d'une statistiques.
     * 
     * @param dateOfMeasure
     *            Date de la mesure.
     * 
     * @param statistic
     *            Statistique mesurée.
     * 
     * @param userLogin
     *            User concerné par la mesure (si null, tous les users).
     * @throws ServiceException
     * 
     * 
     */
    public MeasureTestCase(DateOfMeasure dateOfMeasure, Statistic statistic, String userLogin, UserId userId, TldServiceFacade tld) throws ServiceException {

        this.dateOfMeasure = dateOfMeasure;
        this.statistic = statistic;
        this.userLogin = userLogin;
        this.userIdCaller = userId;
        this.tldCaller = tld;

        this.initialMeasureValue = this.getMeasureValue();

    }

    /**
     * Vérifie que la statistique a toujours la même valeur.
     * 
     * @throws ServiceException
     * 
     */
    public void assertUnchanged() throws ServiceException {
        float newValue = this.getMeasureValue();
        TestCase.assertEquals("Measure should not has changed", this.initialMeasureValue, newValue);
    }

    /**
     * Vérifie que la statistique vaut la valeur passée en parametre.
     * 
     * @param expectedValue
     *            Valeur attendue.
     * @throws ServiceException
     * 
     */
    public void assertEquals(float expectedValue) throws ServiceException {
        float newValue = this.getMeasureValue();
        TestCase.assertEquals("Unexpected measure value", expectedValue, newValue);
    }

    /**
     * Vérifie que la statistique a été incrémentée de 1.
     * 
     * @throws ServiceException
     * 
     */
    public void assertIncremented() throws ServiceException {
        float newValue = this.getMeasureValue();
        TestCase.assertEquals("Measure value was not incremented", this.initialMeasureValue + 1, newValue);
    }

    /**
     * Retourne la valeur de la mesures correspondant aux parametres passés au constructeur.
     * 
     * @return La valeur actuelle de la mesure.
     * @throws ServiceException
     */
    private float getMeasureValue() throws ServiceException {
        float measureValue;
        try {
            measureValue = AppServiceFacade.getStatisticService().getMeasure(this.dateOfMeasure,
                                                                             this.statistic,
                                                                             this.userLogin, this.userIdCaller, this.tldCaller).getValue();
        } catch (NotFoundException e) {
            measureValue = 0;
        }

        return measureValue;
    }
}
