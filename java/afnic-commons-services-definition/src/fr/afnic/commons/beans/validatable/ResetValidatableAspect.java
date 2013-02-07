/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */
package fr.afnic.commons.beans.validatable;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

/**
 * @author alaphilippe
 * 
 */
@Aspect
public class ResetValidatableAspect {

    @Pointcut("call( void fr.afnic.commons.beans.*.set*(..))")
    void reset() {
    }

    @Before("reset()")
    public void callReset(JoinPoint thisJoinPoint) {
        Object target = thisJoinPoint.getTarget();
        if (target instanceof AbstractValidatable) {
            AbstractValidatable obj = (AbstractValidatable) target;
            if (obj.isAutoResetActivated()) {
                obj.resetValidate();
            }
        }
    }
}
