package fr.afnic.commons.test;

import java.lang.reflect.Method;

/**
 * Rule refusant les méthode concernant certains attributs
 * 
 * @author ginguene
 * 
 */
public class BeanTestCaseNameMethodRule implements IBeanTestCaseMethodRule {

    public String[] acceptedAttributeNames = null;

    public BeanTestCaseNameMethodRule(String... acceptedAttributeNames) {
        if (acceptedAttributeNames == null) {
            throw new IllegalArgumentException("acceptedAttributeNames cannot be null");
        }
        this.acceptedAttributeNames = acceptedAttributeNames;

    }

    public boolean hasToAssertSetter(Method setter) {
        for (String name : acceptedAttributeNames) {
            if (setter.getName().equalsIgnoreCase("set" + name)) {
                return false;
            }
        }
        return true;
    }

}
