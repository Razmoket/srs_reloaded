package fr.afnic.commons.test;

import java.lang.reflect.Method;

/**
 * Interface définissant une règle pour controler ou non une méthode de setter via le BeanTestCase.
 * 
 * @author ginguene
 * 
 */
public interface IBeanTestCaseMethodRule {

    public boolean hasToAssertSetter(Method setter);

}
