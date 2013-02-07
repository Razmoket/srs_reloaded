/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */
package fr.afnic.commons.beans.validatable;

import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author alaphilippe
 * 
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(TYPE)
public @interface ResetValidate {

}
