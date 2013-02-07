/*
 * $Id: $ $Revision: $ $Author: $
 */

package fr.afnic.utils;

/**
 * Objet contenant un entier incrémenté à chaque appel.<br/>
 * Utilisé en test dans des méthodes de type.
 * 
 * <code>
 * final CallingCount toString = new CallingCount(); 
 * Object o = new Object() { 
 *     protected void toString() {
 *         toString.call(); 
 *         return ""; 
 *     } 
 * };
 * 
 * o.toString();
 * o.toString();
 * TestCase.assertEquals(2,toString.getCallCount()); 
 * </code>
 * 
 * @author ginguene
 * 
 */
public class CallingCount {

    private int count = 0;

    public void call() {
        this.count++;
    }

    public int getCallCount() {
        return this.count;
    }
}
