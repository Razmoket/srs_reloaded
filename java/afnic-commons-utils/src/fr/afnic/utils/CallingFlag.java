/*
 * $Id: $ $Revision: $ $Author: $
 */

package fr.afnic.utils;

/**
 * Objet contenant un booleen.<br/>
 * Utilisé en test dans des méthodes de type.
 * 
 * <code>
 * final CallingFlag toString = new CallingFlag(); 
 * Object o = new Object() { 
 *     protected void toString() {
 *         toString.call(); 
 *         return ""; 
 *     } 
 * };
 * 
 * [Test]
 * TestCase.assertTrue(toString.hasBeenCalled()); 
 * </code>
 * 
 * @author ginguene
 * 
 */
public class CallingFlag {

    private boolean flag = false;

    public void call() {
        this.flag = true;
    }

    public boolean hasBeenCalled() {
        return this.flag;
    }

}
